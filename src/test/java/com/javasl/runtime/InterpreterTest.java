package com.javasl.runtime;

import java.util.ArrayList;
import java.util.Stack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.javasl.compiler.Compiler;
import com.javasl.compiler.Parser;
import com.javasl.compiler.Token;
import com.javasl.compiler.Tokenizer;
import com.javasl.runtime.types.*;

public class InterpreterTest {
    @Test public void testAssignment() {
        {
            String test = "int64 x = 5;";
            Stack<Variable> expected = new Stack<Variable>(){{
                push(new Variable("0_COMPILER_TEMP", new Int64(5)));
                push(new Variable("x", new Int64(5)));
            }};
            
            ArrayList<Statement> instr = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(test)));
            instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
            Interpreter interpreter = new Interpreter();
            interpreter.execute(instr);

            compareStack(expected, interpreter.getStack());
        }
        {
            String test = "int32 x = 2 * 8;";
            test += "int16 y = x + 2 * -7;";
            test += "y = y / 2 + 2;";
            test += "int32 z = (x + 3) % 3;";
            Stack<Variable> expected = new Stack<Variable>(){{
                push(new Variable("0_COMPILER_TEMP", new Int64(2)));
                push(new Variable("1_COMPILER_TEMP", new Int64(8)));
                push(new Variable("2_COMPILER_TEMP", new Int64(16)));
                push(new Variable("x", new Int32(16)));
                push(new Variable("3_COMPILER_TEMP", new Int64(2)));
                push(new Variable("4_COMPILER_TEMP", new Int64(-7)));
                push(new Variable("5_COMPILER_TEMP", new Int64(-14)));
                push(new Variable("6_COMPILER_TEMP", new Int64(2)));
                push(new Variable("y", new Int16(3)));
                push(new Variable("7_COMPILER_TEMP", new Int64(2)));
                push(new Variable("8_COMPILER_TEMP", new Int64(1)));
                push(new Variable("9_COMPILER_TEMP", new Int64(2)));
                push(new Variable("10_COMPILER_TEMP", new Int64(3)));
                push(new Variable("11_COMPILER_TEMP", new Int64(3)));
                push(new Variable("12_COMPILER_TEMP", new Int64(19)));
                push(new Variable("13_COMPILER_TEMP", new Int64(3)));
                push(new Variable("14_COMPILER_TEMP", new Int64(1)));
                push(new Variable("z", new Int32(1)));
            }};
            
            ArrayList<Statement> instr = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(test)));
            instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
            Interpreter interpreter = new Interpreter();
            interpreter.execute(instr);

            compareStack(expected, interpreter.getStack());
        }
        {
            String test = "uint32 x = 2 * 8;";
            test += "uint8 t = x * 20;";
            test += "uint16 a = -5 - -10;";
            test += "uint8 underflow = -1;";
            Stack<Variable> expected = new Stack<Variable>(){{
                push(new Variable("0_COMPILER_TEMP", new Int64(2)));
                push(new Variable("1_COMPILER_TEMP", new Int64(8)));
                push(new Variable("2_COMPILER_TEMP", new Int64(16)));
                push(new Variable("x", new Uint32(16)));
                push(new Variable("3_COMPILER_TEMP", new Int64(20)));
                push(new Variable("4_COMPILER_TEMP", new Uint64(320)));
                push(new Variable("t", new Uint8(64)));
                push(new Variable("5_COMPILER_TEMP", new Int64(-5)));
                push(new Variable("6_COMPILER_TEMP", new Int64(-10)));
                push(new Variable("7_COMPILER_TEMP", new Int64(5)));
                push(new Variable("a", new Uint16(5)));
                push(new Variable("8_COMPILER_TEMP", new Int64(-1)));
                push(new Variable("underflow", new Uint8(255)));
            }};
            
            ArrayList<Statement> instr = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(test)));
            instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
            Interpreter interpreter = new Interpreter();
            interpreter.execute(instr);

            compareStack(expected, interpreter.getStack());
        }
        {
            String test = "bool x = true; bool y = 10; int32 z = x + y;";
            
            ArrayList<Statement> instr = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(test)));
            instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
            Interpreter interpreter = new Interpreter();
            interpreter.execute(instr);

            Stack<Variable> expected = new Stack<Variable>(){{
                push(new Variable("0_COMPILER_TEMP", new Bool(true)));
                push(new Variable("x", new Bool(true)));
                push(new Variable("1_COMPILER_TEMP", new Int64(10)));
                push(new Variable("y", new Bool(true)));
                push(new Variable("2_COMPILER_TEMP", new Int64(2)));
                push(new Variable("z", new Int32(2)));
            }};

            compareStack(expected, interpreter.getStack());
        }
        {
            String test = "int32 x = -9; int32 y = 99; bool z = x < y;";
                        
            ArrayList<Statement> instr = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(test)));
            instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
            Interpreter interpreter = new Interpreter();
            interpreter.execute(instr);

            Stack<Variable> expected = new Stack<Variable>(){{
                push(new Variable("0_COMPILER_TEMP", new Int64(-9)));
                push(new Variable("x", new Int32(-9)));
                push(new Variable("1_COMPILER_TEMP", new Int64(99)));
                push(new Variable("y", new Int32(99)));
                push(new Variable("2_COMPILER_TEMP", new Bool(true)));
                push(new Variable("z", new Bool(true)));
            }};

            compareStack(expected, interpreter.getStack());
        }
    }
    @Test public void testFunctions() {
        {

            String testStr = "int32 foo(int32 PARAM1) { int32 x = PARAM1 + 5; return x; } int32 result = foo(5);";
            ArrayList<Variable> fooParams = new ArrayList<Variable>() {{
                add(new Variable("PARAM1", new Int32()));
            }};
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // define function
                add(Statement.declareVariable(new FunctionVariable("foo", new Int32(), fooParams, 1)));
                add(Statement.jumpRel(8));
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64(5))));
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int32())));
                add(Statement.binaryOp(Token.Type.OP_PLUS, true, 0, true, -2, true, -1));
                add(Statement.declareVariable(new Variable("x", new Int32())));
                add(Statement.assignVariable(true, 0, true, -1));
                // return x
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int32())));
                add(Statement.assignVariable(true, 0, true, -1));
                add(Statement.functionReturn(true, 4 + 1 - 1)); // local + params - return

                // function call
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int64(5))));
                add(Statement.functionCall(1));
                add(Statement.declareVariable(new Variable("result", new Int32())));
                add(Statement.assignVariable(true, 0, true, -1));

                add(Statement.clearStack(3));
            }};

            ArrayList<Stack<Variable>> results = new ArrayList<>();
            // hardcoded
            {
                Interpreter interpreter = new Interpreter();
                instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
                interpreter.execute(instr);
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.execute(instr2);
                results.add(interpreter.getStack()); 

            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("result", result.name);
                Assertions.assertEquals(new Int32(5+5).getValue(), result.value.getValue());
            }
        }
        {
            String testStr = "int64 value = 5;";
            testStr += "int32 foo(int32 PARAM1, int32 PARAM2) { int32 x = PARAM1 + value; return x + PARAM2; }";
            testStr += "int32 result = foo(2, 3);";
            ArrayList<Variable> fooParams = new ArrayList<Variable>() {{
                add(new Variable("PARAM1", new Int32()));
                add(new Variable("PARAM2", new Int32()));
            }};
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // define var
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64(5))));
                add(Statement.declareVariable(new Variable("value", new Int64())));
                add(Statement.assignVariable(true, 0, true, -1));
                // define function
                add(Statement.declareVariable(new FunctionVariable("foo", new Int32(), fooParams, 4)));
                add(Statement.jumpRel(7));

                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int32())));
                add(Statement.binaryOp(Token.Type.OP_PLUS, true, 0, true, -2, false, 1));
                add(Statement.declareVariable(new Variable("x", new Int32())));
                add(Statement.assignVariable(true, 0, true, -1));
                // return x
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int32())));
                add(Statement.binaryOp(Token.Type.OP_PLUS, true, 0, true, -1, true, -3));
                add(Statement.functionReturn(true, 3 + 2 - 1)); // local + params - return

                // function call
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int64(2))));
                add(Statement.declareVariable(new Variable("4_COMPILER_TEMP", new Int64(3))));
                add(Statement.functionCall(4));
                add(Statement.declareVariable(new Variable("result", new Int32())));
                add(Statement.assignVariable(true, 0, true, -1));

                add(Statement.clearStack(5));
            }};

            ArrayList<Stack<Variable>> results = new ArrayList<>();
            // hardcoded
            {
                Interpreter interpreter = new Interpreter();
                instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
                interpreter.execute(instr);
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.execute(instr2);
                results.add(interpreter.getStack()); 

            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("result", result.name);
                Assertions.assertEquals(new Int32(2+5+3).getValue(), result.value.getValue());
            }
        }
    }

    @Test public void testConditionals() {
        {
            String testStr = "int32 x = 1; if (x > 0) x = -1; else x = 1;";
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // declare x
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64(1))));
                add(Statement.declareVariable(new Variable("x", new Int32())));
                add(Statement.assignVariable(true, 0, true, -1));
                // if condition
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64(0))));
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int64())));
                add(Statement.binaryOp(Token.Type.OP_GREATER, true, 0, false, 1, true, -1));
                add(Statement.jumpRelConditional(false, 4, true, 0));
                // true body
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int64(-1))));
                add(Statement.assignVariable(false, 1, true, 0));
                add(Statement.clearStack(3));
                add(Statement.jumpRel(3));
                // else body
                add(Statement.declareVariable(new Variable("4_COMPILER_TEMP", new Int64(1))));
                add(Statement.assignVariable(false, 1, true, 0));
                add(Statement.clearStack(3));

                // clear stack
                add(Statement.clearStack(2));
            }};

            ArrayList<Stack<Variable>> results = new ArrayList<>();
            // hardcoded
            {
                Interpreter interpreter = new Interpreter();
                instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
                interpreter.execute(instr);
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.execute(instr2);
                results.add(interpreter.getStack()); 
            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("x", result.name);
                Assertions.assertEquals(new Int32(-1).getValue(), result.value.getValue());
            }
        }
        {
            String testStr = "int32 x = 1; if (x) x = -1; else x = 1;";
 
            ArrayList<Statement> instr = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
            Interpreter interpreter = new Interpreter();
            instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
            interpreter.execute(instr);

            Variable result = interpreter.getStack().peek();
            Assertions.assertEquals("x", result.name);
            Assertions.assertEquals(new Int32(-1).getValue(), result.value.getValue());
        }
        {
            String testStr = "int32 x = 0; if (x) x = -1; else x = 1;";
 
            ArrayList<Statement> instr = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
            Interpreter interpreter = new Interpreter();
            instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
            interpreter.execute(instr);

            Variable result = interpreter.getStack().peek();
            Assertions.assertEquals("x", result.name);
            Assertions.assertEquals(new Int32(1).getValue(), result.value.getValue());
        }
    }
    @Test public void testLoops() {
        {
            String testStr = "int32 x = 10; int32 y = 0; while (x > 0) {x = x - 1; y = y + 1;}";
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // declare x
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64(10))));
                add(Statement.declareVariable(new Variable("x", new Int32())));
                add(Statement.assignVariable(true, 0, true, -1));
                // declare y
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64(0))));
                add(Statement.declareVariable(new Variable("y", new Int32())));
                add(Statement.assignVariable(true, 0, true, -1));
                // while condition
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int64(0))));
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int32())));
                add(Statement.binaryOp(Token.Type.OP_GREATER, true, 0, false, 1, true, -1));
                add(Statement.jumpRelConditional(true, 2, true, 0));
                add(Statement.clearStack(2)); // condition
                add(Statement.jumpRel(11));
                // body
                add(Statement.declareVariable(new Variable("4_COMPILER_TEMP", new Int64(1))));
                add(Statement.declareVariable(new Variable("5_COMPILER_TEMP", new Int32())));
                add(Statement.binaryOp(Token.Type.OP_MINUS, true, 0, false, 1, true, -1));
                add(Statement.assignVariable(false, 1, true, 0));
                add(Statement.declareVariable(new Variable("6_COMPILER_TEMP", new Int64(1))));
                add(Statement.declareVariable(new Variable("7_COMPILER_TEMP", new Int32())));
                add(Statement.binaryOp(Token.Type.OP_PLUS, true, 0, false, 3, true, -1));
                add(Statement.assignVariable(false, 3, true, 0));
                add(Statement.clearStack(4)); // scope
                // loop to while
                add(Statement.clearStack(2)); // body + condition
                add(Statement.jumpRel(-(10+6+1))); // -(body + condition + 1)

                // clear stack
                add(Statement.clearStack(4));
            }};

            ArrayList<Stack<Variable>> results = new ArrayList<>();
            // hardcoded
            {
                Interpreter interpreter = new Interpreter();
                instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
                interpreter.execute(instr);
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.execute(instr2);
                results.add(interpreter.getStack()); 
            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("y", result.name);
                Assertions.assertEquals(new Int32(10).getValue(), result.value.getValue());
            }
        }
        {
            String testStr = "int32 y = 0; for (int32 x = 10; x > 0; x = x - 1) {y = y + 1; continue;}";
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // declare y
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64(0))));
                add(Statement.declareVariable(new Variable("y", new Int32())));
                add(Statement.assignVariable(true, 0, true, -1));

                // for init
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64(10))));
                add(Statement.declareVariable(new Variable("x", new Int32())));
                add(Statement.assignVariable(true, 0, true, -1));
                // for condition
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int64(0))));
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int32())));
                add(Statement.binaryOp(Token.Type.OP_GREATER, true, 0, false, 3, true, -1));
                add(Statement.jumpRelConditional(false, 6+5+1, true, 0)); // body + increment + 1
                // body
                add(Statement.declareVariable(new Variable("4_COMPILER_TEMP", new Int64(1))));
                add(Statement.declareVariable(new Variable("5_COMPILER_TEMP", new Int32())));
                add(Statement.binaryOp(Token.Type.OP_PLUS, true, 0, false, 1, true, -1));
                add(Statement.assignVariable(false, 1, true, 0));
                add(Statement.continueLoop(2, 1));
                add(Statement.clearStack(2)); // scope
                // for increment
                add(Statement.declareVariable(new Variable("6_COMPILER_TEMP", new Int64(1))));
                add(Statement.declareVariable(new Variable("7_COMPILER_TEMP", new Int32())));
                add(Statement.binaryOp(Token.Type.OP_MINUS, true, 0, false, 3, true, -1));
                add(Statement.assignVariable(false, 3, true, 0));
                add(Statement.clearStack(2 + 0 + 2)); // increment + body + condition
                // loop to condition
                add(Statement.jumpRel(-(5+6+4+1))); // -(increment + body + condition + 1)
                // clear for loop stack
                add(Statement.clearStack(2 + 2)); // init + condition

                // clear stack
                add(Statement.clearStack(2));
            }};

            ArrayList<Stack<Variable>> results = new ArrayList<>();
            // hardcoded
            {
                Interpreter interpreter = new Interpreter();
                instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
                interpreter.execute(instr);
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.execute(instr2);
                results.add(interpreter.getStack()); 
            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("y", result.name);
                Assertions.assertEquals(new Int32(10).getValue(), result.value.getValue());
            }
        }
        {
            String testStr = "int32 y = 0; for (;;) {y = y + 1; if (y == 10) break; continue; }";
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // declare y
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64(0))));
                add(Statement.declareVariable(new Variable("y", new Int32())));
                add(Statement.assignVariable(true, 0, true, -1));

                // for init
                // for condition
                // body
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64(1))));
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int32())));
                add(Statement.binaryOp(Token.Type.OP_PLUS, true, 0, false, 1, true, -1));
                add(Statement.assignVariable(false, 1, true, 0));
                    // if condition
                    add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int64(10))));
                    add(Statement.declareVariable(new Variable("4_COMPILER_TEMP", new Int32())));
                    add(Statement.binaryOp(Token.Type.OP_EQUAL, true, 0, false, 1, true, -1));
                    add(Statement.jumpRelConditional(false, 3, true, 0));
                    // true body
                    add(Statement.breakLoop(4, 6));
                    add(Statement.clearStack(2)); // skip else
                    add(Statement.jumpRel(1)); // skip else
                    // else body
                    add(Statement.clearStack(2));
                add(Statement.continueLoop(2, 1));
                add(Statement.clearStack(2)); // scope
                // for increment
                // loop to condition
                add(Statement.jumpRel(-(0+14+0+1))); // -(increment + body + condition + 1)
                // clear for loop stack
                //add(Statement.clearStack(0 + 0)); // init + condition

                // clear stack
                add(Statement.clearStack(2));
            }};
            ArrayList<Stack<Variable>> results = new ArrayList<>();
            // hardcoded
            {
                Interpreter interpreter = new Interpreter();
                instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
                interpreter.execute(instr);
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.execute(instr2);
                results.add(interpreter.getStack()); 
            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("y", result.name);
                Assertions.assertEquals(new Int32(10).getValue(), result.value.getValue());
            }
        }
        {
            String testStr = "int32 y = 0; for (;true;) {y = y + 1; if (y == 10) break; continue; }";
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // declare y
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64(0))));
                add(Statement.declareVariable(new Variable("y", new Int32())));
                add(Statement.assignVariable(true, 0, true, -1));

                // for init
                // for condition
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Bool(true))));
                add(Statement.jumpRelConditional(false, 14 + 1 + 1, true, 0)); // body + increment + 1
                // body
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int64(1))));
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int32())));
                add(Statement.binaryOp(Token.Type.OP_PLUS, true, 0, false, 1, true, -1));
                add(Statement.assignVariable(false, 1, true, 0));
                    // if condition
                    add(Statement.declareVariable(new Variable("4_COMPILER_TEMP", new Int64(10))));
                    add(Statement.declareVariable(new Variable("5_COMPILER_TEMP", new Int32())));
                    add(Statement.binaryOp(Token.Type.OP_EQUAL, true, 0, false, 1, true, -1));
                    add(Statement.jumpRelConditional(false, 3, true, 0));
                    // true body
                    add(Statement.breakLoop(4 + 1, 8));
                    add(Statement.clearStack(2)); // skip else
                    add(Statement.jumpRel(1)); // skip else
                    // else body
                    add(Statement.clearStack(2));
                add(Statement.continueLoop(2, 1));
                add(Statement.clearStack(2)); // scope
                // for increment
                add(Statement.clearStack(0 + 1)); // increment + condition
                // loop to condition
                add(Statement.jumpRel(-(1+14+2+1))); // -(increment + body + condition + 1)
                // clear for loop stack
                add(Statement.clearStack(0 + 1)); // init + condition

                // clear stack
                add(Statement.clearStack(2));
            }};

            ArrayList<Stack<Variable>> results = new ArrayList<>();
            // hardcoded
            {
                Interpreter interpreter = new Interpreter();
                instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
                interpreter.execute(instr);
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.execute(instr2);
                results.add(interpreter.getStack()); 
            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("y", result.name);
                Assertions.assertEquals(new Int32(10).getValue(), result.value.getValue());
            }
        }
        {
            String testStr = "int32 x = 10; int32 y = 0; while (x > 0) {x = x - 1; continue; y = y + 1;}";
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // declare x
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64(10))));
                add(Statement.declareVariable(new Variable("x", new Int32())));
                add(Statement.assignVariable(true, 0, true, -1));
                // declare y
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64(0))));
                add(Statement.declareVariable(new Variable("y", new Int32())));
                add(Statement.assignVariable(true, 0, true, -1));
                // while condition
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int64(0))));
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int32())));
                add(Statement.binaryOp(Token.Type.OP_GREATER, true, 0, false, 1, true, -1));
                add(Statement.jumpRelConditional(true, 2, true, 0));
                add(Statement.clearStack(2)); // condition
                add(Statement.jumpRel(12));
                // body
                add(Statement.declareVariable(new Variable("4_COMPILER_TEMP", new Int64(1))));
                add(Statement.declareVariable(new Variable("5_COMPILER_TEMP", new Int32())));
                add(Statement.binaryOp(Token.Type.OP_MINUS, true, 0, false, 1, true, -1));
                add(Statement.assignVariable(false, 1, true, 0));
                add(Statement.continueLoop(2+2, 6));
                add(Statement.declareVariable(new Variable("6_COMPILER_TEMP", new Int64(1))));
                add(Statement.declareVariable(new Variable("7_COMPILER_TEMP", new Int32())));
                add(Statement.binaryOp(Token.Type.OP_PLUS, true, 0, false, 3, true, -1));
                add(Statement.assignVariable(false, 3, true, 0));
                add(Statement.clearStack(4)); // scope
                // loop to while
                add(Statement.clearStack(2)); // body + condition
                add(Statement.jumpRel(-(11+6+1))); // -(body + condition + 1)

                // clear stack
                add(Statement.clearStack(4));
            }};

            ArrayList<Stack<Variable>> results = new ArrayList<>();
            // hardcoded
            {
                Interpreter interpreter = new Interpreter();
                instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
                interpreter.execute(instr);
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.execute(instr2);
                results.add(interpreter.getStack()); 
            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("y", result.name);
                Assertions.assertEquals(new Int32(0).getValue(), result.value.getValue());
            }
        }
        {
            String testStr = "int32 x = 10; int32 y = 0; while (x > 0) {x = x - 1; break; y = y + 1;}";
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // declare x
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64(10))));
                add(Statement.declareVariable(new Variable("x", new Int32())));
                add(Statement.assignVariable(true, 0, true, -1));
                // declare y
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64(0))));
                add(Statement.declareVariable(new Variable("y", new Int32())));
                add(Statement.assignVariable(true, 0, true, -1));
                // while condition
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int64(0))));
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int32())));
                add(Statement.binaryOp(Token.Type.OP_GREATER, true, 0, false, 1, true, -1));
                add(Statement.jumpRelConditional(true, 2, true, 0));
                add(Statement.clearStack(2)); // condition
                add(Statement.jumpRel(12));
                // body
                add(Statement.declareVariable(new Variable("4_COMPILER_TEMP", new Int64(1))));
                add(Statement.declareVariable(new Variable("5_COMPILER_TEMP", new Int32())));
                add(Statement.binaryOp(Token.Type.OP_MINUS, true, 0, false, 1, true, -1));
                add(Statement.assignVariable(false, 1, true, 0));
                add(Statement.breakLoop(2+2, 7));
                add(Statement.declareVariable(new Variable("6_COMPILER_TEMP", new Int64(1))));
                add(Statement.declareVariable(new Variable("7_COMPILER_TEMP", new Int32())));
                add(Statement.binaryOp(Token.Type.OP_PLUS, true, 0, false, 3, true, -1));
                add(Statement.assignVariable(false, 3, true, 0));
                add(Statement.clearStack(4)); // scope
                // loop to while
                add(Statement.clearStack(2)); // body + condition
                add(Statement.jumpRel(-(11+6+1))); // -(body + condition + 1)

                // clear stack
                add(Statement.clearStack(4));
            }};

            ArrayList<Stack<Variable>> results = new ArrayList<>();
            // hardcoded
            {
                Interpreter interpreter = new Interpreter();
                instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
                interpreter.execute(instr);
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.execute(instr2);
                results.add(interpreter.getStack()); 
            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.get(1);
                Assertions.assertEquals("x", result.name);
                Assertions.assertEquals(new Int32(9).getValue(), result.value.getValue());
            }
        }
    }

    private void compareStack(Stack<Variable> expected, Stack<Variable> real) {
        Assertions.assertEquals(expected.size(), real.size());
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i).toString(), real.get(i).toString());
        }
    }
}