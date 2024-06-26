package com.javasl.runtime;

import java.util.ArrayList;
import java.util.Stack;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.javasl.Script;
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
                push(new Variable("0_COMPILER_TEMP", new Int64_T(5)));
                push(new Variable("x", new Int64_T(5)));
            }};
            
            ArrayList<Statement> instr = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(test)));
            instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
            Interpreter interpreter = new Interpreter();
            interpreter.setStatements(instr);
            interpreter.execute();

            compareStack(expected, interpreter.getStack());
        }
        {
            String test = "double x = 10 / 2.5;";
            ArrayList<Statement> instr = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(test)));
            instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
            Interpreter interpreter = new Interpreter();
            interpreter.setStatements(instr);
            interpreter.execute();

            double resultX = (Double)interpreter.getStack().peek().value.getValue();
            double expectedX = 10 / 2.5;
            Assertions.assertEquals(expectedX, resultX);
        }
        {
            String test = "int32 x = 2 * 8;";
            test += "int16 y = x + 2 * -7;";
            test += "y = y / 2 + 2;";
            test += "int32 z = (x + 3) % 3;";
            Stack<Variable> expected = new Stack<Variable>(){{
                push(new Variable("0_COMPILER_TEMP", new Int64_T(2)));
                push(new Variable("1_COMPILER_TEMP", new Int64_T(8)));
                push(new Variable("2_COMPILER_TEMP", new Int64_T(16)));
                push(new Variable("x", new Int32_T(16)));
                push(new Variable("3_COMPILER_TEMP", new Int64_T(2)));
                push(new Variable("4_COMPILER_TEMP", new Int64_T(-7)));
                push(new Variable("5_COMPILER_TEMP", new Int64_T(-14)));
                push(new Variable("6_COMPILER_TEMP", new Int64_T(2)));
                push(new Variable("y", new Int16_T(3)));
                push(new Variable("7_COMPILER_TEMP", new Int64_T(2)));
                push(new Variable("8_COMPILER_TEMP", new Int64_T(1)));
                push(new Variable("9_COMPILER_TEMP", new Int64_T(2)));
                push(new Variable("10_COMPILER_TEMP", new Int64_T(3)));
                push(new Variable("11_COMPILER_TEMP", new Int64_T(3)));
                push(new Variable("12_COMPILER_TEMP", new Int64_T(19)));
                push(new Variable("13_COMPILER_TEMP", new Int64_T(3)));
                push(new Variable("14_COMPILER_TEMP", new Int64_T(1)));
                push(new Variable("z", new Int32_T(1)));
            }};
            
            ArrayList<Statement> instr = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(test)));
            instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
            Interpreter interpreter = new Interpreter();
            interpreter.setStatements(instr);
            interpreter.execute();

            compareStack(expected, interpreter.getStack());
        }
        {
            String test = "uint32 x = 2 * 8;";
            test += "uint8 t = x * 20;";
            test += "uint16 a = -5 - -10;";
            test += "uint8 underflow = -1;";
            Stack<Variable> expected = new Stack<Variable>(){{
                push(new Variable("0_COMPILER_TEMP", new Int64_T(2)));
                push(new Variable("1_COMPILER_TEMP", new Int64_T(8)));
                push(new Variable("2_COMPILER_TEMP", new Int64_T(16)));
                push(new Variable("x", new Uint32_T(16)));
                push(new Variable("3_COMPILER_TEMP", new Int64_T(20)));
                push(new Variable("4_COMPILER_TEMP", new Uint64_T(320)));
                push(new Variable("t", new Uint8_T(64)));
                push(new Variable("5_COMPILER_TEMP", new Int64_T(-5)));
                push(new Variable("6_COMPILER_TEMP", new Int64_T(-10)));
                push(new Variable("7_COMPILER_TEMP", new Int64_T(5)));
                push(new Variable("a", new Uint16_T(5)));
                push(new Variable("8_COMPILER_TEMP", new Int64_T(-1)));
                push(new Variable("underflow", new Uint8_T(255)));
            }};
            
            ArrayList<Statement> instr = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(test)));
            instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
            Interpreter interpreter = new Interpreter();
            interpreter.setStatements(instr);
            interpreter.execute();

            compareStack(expected, interpreter.getStack());
        }
        {
            String test = "bool x = true; bool y = 10; int32 z = x + y;";
            
            ArrayList<Statement> instr = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(test)));
            instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
            Interpreter interpreter = new Interpreter();
            interpreter.setStatements(instr);
            interpreter.execute();

            Stack<Variable> expected = new Stack<Variable>(){{
                push(new Variable("0_COMPILER_TEMP", new Bool_T(true)));
                push(new Variable("x", new Bool_T(true)));
                push(new Variable("1_COMPILER_TEMP", new Int64_T(10)));
                push(new Variable("y", new Bool_T(true)));
                push(new Variable("2_COMPILER_TEMP", new Int64_T(2)));
                push(new Variable("z", new Int32_T(2)));
            }};

            compareStack(expected, interpreter.getStack());
        }
        {
            String test = "int32 x = -9; int32 y = 99; bool z = x < y;";
                        
            ArrayList<Statement> instr = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(test)));
            instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
            Interpreter interpreter = new Interpreter();
            interpreter.setStatements(instr);
            interpreter.execute();

            Stack<Variable> expected = new Stack<Variable>(){{
                push(new Variable("0_COMPILER_TEMP", new Int64_T(-9)));
                push(new Variable("x", new Int32_T(-9)));
                push(new Variable("1_COMPILER_TEMP", new Int64_T(99)));
                push(new Variable("y", new Int32_T(99)));
                push(new Variable("2_COMPILER_TEMP", new Bool_T(true)));
                push(new Variable("z", new Bool_T(true)));
            }};

            compareStack(expected, interpreter.getStack());
        }
    }
    @Test public void testFunctions() {
        {

            String testStr = "int32 foo(int32 PARAM1) { int32 x = PARAM1 + 5; return x; } int32 result = foo(5);";
            ArrayList<Variable> fooParams = new ArrayList<Variable>() {{
                add(new Variable("PARAM1", new Int32_T()));
            }};
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // define function
                add(Statement.declareVariable(new FunctionVariable("foo", new Int32_T(), fooParams, 1)));
                add(Statement.jumpRel(8));
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64_T(5))));
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int32_T())));
                add(Statement.binaryOp(Token.Type.OP_PLUS, true, 0, true, -2, true, -1));
                add(Statement.declareVariable(new Variable("x", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                // return x
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                add(Statement.functionReturn(true, 4 + 1 - 1)); // local + params - return

                // function call
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int64_T(5))));
                add(Statement.functionCall(1));
                add(Statement.declareVariable(new Variable("result", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));

                add(Statement.clearStack(3));
            }};

            ArrayList<Stack<Variable>> results = new ArrayList<>();
            // hardcoded
            {
                Interpreter interpreter = new Interpreter();
                instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
                interpreter.setStatements(instr);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.setStatements(instr2);
                interpreter.execute();
                results.add(interpreter.getStack()); 

            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("result", result.name);
                Assertions.assertEquals(new Int32_T(5+5).getValue(), result.value.getValue());
            }
        }
        {
            String testStr = "int64 value = 5;";
            testStr += "int32 foo(int32 PARAM1, int32 PARAM2) { int32 x = PARAM1 + value; return x + PARAM2; }";
            testStr += "int32 result = foo(2, 3);";
            ArrayList<Variable> fooParams = new ArrayList<Variable>() {{
                add(new Variable("PARAM1", new Int32_T()));
                add(new Variable("PARAM2", new Int32_T()));
            }};
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // define var
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64_T(5))));
                add(Statement.declareVariable(new Variable("value", new Int64_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                // define function
                add(Statement.declareVariable(new FunctionVariable("foo", new Int32_T(), fooParams, 4)));
                add(Statement.jumpRel(7));

                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int32_T())));
                add(Statement.binaryOp(Token.Type.OP_PLUS, true, 0, true, -2, false, 1));
                add(Statement.declareVariable(new Variable("x", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                // return x
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int32_T())));
                add(Statement.binaryOp(Token.Type.OP_PLUS, true, 0, true, -1, true, -3));
                add(Statement.functionReturn(true, 3 + 2 - 1)); // local + params - return

                // function call
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int64_T(2))));
                add(Statement.declareVariable(new Variable("4_COMPILER_TEMP", new Int64_T(3))));
                add(Statement.functionCall(4));
                add(Statement.declareVariable(new Variable("result", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));

                add(Statement.clearStack(5));
            }};

            ArrayList<Stack<Variable>> results = new ArrayList<>();
            // hardcoded
            {
                Interpreter interpreter = new Interpreter();
                instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
                interpreter.setStatements(instr);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.setStatements(instr2);
                interpreter.execute();
                results.add(interpreter.getStack()); 

            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("result", result.name);
                Assertions.assertEquals(new Int32_T(2+5+3).getValue(), result.value.getValue());
            }
        }
        {
            String testStr = "int64 value = 5;";
            testStr += "void test(int32 amount) { return; }";
            testStr += "int32 foo(int32 PARAM1, int32 PARAM2) { int32 x = PARAM1 + value; return x + PARAM2; }";
            testStr += "int32 bar(bool PARAM1, uint64 PARAM2) { if (PARAM1 == true) return PARAM2; return 0; }";
            testStr += "int32 result = foo(2, 3);";
            ArrayList<Stack<Variable>> results = new ArrayList<>();

            ArrayList<Statement> instr = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
            Interpreter interpreter = new Interpreter();
            instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
            interpreter.setStatements(instr);
            interpreter.execute();
            results.add(interpreter.getStack()); 

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("result", result.name);
                Assertions.assertEquals(new Int32_T(2+5+3).getValue(), result.value.getValue());
            }
        }
        {
            String testStr = "int64 getVal(int64 val) { val = val - 1; return val; }";
            testStr += "int64 result = getVal(5);";
            ArrayList<Stack<Variable>> results = new ArrayList<>();

            ArrayList<Statement> instr = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
            Interpreter interpreter = new Interpreter();
            instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
            interpreter.setStatements(instr);
            interpreter.execute();
            results.add(interpreter.getStack()); 

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("result", result.name);
                Assertions.assertEquals(new Int64_T(4).getValue(), result.value.getValue());
            }
        }
        {
            String testStr = "int64 getVal(int64 val, int64 dummy) { while (val > 0) { val = val - 1; dummy = dummy + 1; }";
            testStr += "return dummy;}";
            testStr += "int64 result = getVal(5, 0);";
            ArrayList<Stack<Variable>> results = new ArrayList<>();

            ArrayList<Statement> instr = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
            Interpreter interpreter = new Interpreter();
            instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
            interpreter.setStatements(instr);
            interpreter.execute();
            results.add(interpreter.getStack()); 

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("result", result.name);
                Assertions.assertEquals(new Int64_T(5).getValue(), result.value.getValue());
            }
        }
        {
            String testStr = "int64 y = 0;";
            testStr += "int64 random() { return 42; }";
            testStr += "void addToY(int32 amount) { y = y + amount; return; }";
            testStr += "void removeFromY(int32 amount) { y = y - amount; return; }";
            testStr += "void randMove(int32 steps, int32 amount) {";
            testStr += "    int64 rand = random();";
            testStr += "    while (steps > 0) {";
            testStr += "        if (rand < 50) addToY(amount);";
            testStr += "        else removeFromY(amount);";
            testStr += "        steps = steps - 1;";
            testStr += "    }";
            testStr += "    return;";
            testStr += "}";
            testStr += "int64 getY() { randMove(10, 10); return y; }";
            testStr += "getY();";
            testStr += "int64 result = getY();";
            ArrayList<Stack<Variable>> results = new ArrayList<>();

            ArrayList<Statement> instr = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
            Interpreter interpreter = new Interpreter();
            instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
            interpreter.setStatements(instr);
            interpreter.execute();
            results.add(interpreter.getStack()); 

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("result", result.name);
                Assertions.assertEquals(new Int64_T(200).getValue(), result.value.getValue());
            }
        }
    }

    @Test public void testConditionals() {
        {
            String testStr = "int32 x = 1; if (x > 0) x = -1; else x = 1;";
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // declare x
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64_T(1))));
                add(Statement.declareVariable(new Variable("x", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                // if condition
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64_T(0))));
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int64_T())));
                add(Statement.binaryOp(Token.Type.OP_GREATER, true, 0, false, 1, true, -1));
                add(Statement.jumpRelConditional(false, 4, true, 0));
                // true body
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int64_T(-1))));
                add(Statement.assignVariable(false, 1, true, 0));
                add(Statement.clearStack(3));
                add(Statement.jumpRel(3));
                // else body
                add(Statement.declareVariable(new Variable("4_COMPILER_TEMP", new Int64_T(1))));
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
                interpreter.setStatements(instr);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.setStatements(instr2);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("x", result.name);
                Assertions.assertEquals(new Int32_T(-1).getValue(), result.value.getValue());
            }
        }
        {
            String testStr = "int32 x = 1; if (x) x = -1; else x = 1;";
 
            ArrayList<Statement> instr = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
            Interpreter interpreter = new Interpreter();
            instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
            interpreter.setStatements(instr);
            interpreter.execute();

            Variable result = interpreter.getStack().peek();
            Assertions.assertEquals("x", result.name);
            Assertions.assertEquals(new Int32_T(-1).getValue(), result.value.getValue());
        }
        {
            String testStr = "int32 x = 0; if (x) x = -1; else x = 1;";
 
            ArrayList<Statement> instr = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
            Interpreter interpreter = new Interpreter();
            instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
            interpreter.setStatements(instr);
            interpreter.execute();

            Variable result = interpreter.getStack().peek();
            Assertions.assertEquals("x", result.name);
            Assertions.assertEquals(new Int32_T(1).getValue(), result.value.getValue());
        }
    }
    @Test public void testLoops() {
        {
            String testStr = "int32 x = 10; int32 y = 0; while (x > 0) {x = x - 1; y = y + 1;}";
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // declare x
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64_T(10))));
                add(Statement.declareVariable(new Variable("x", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                // declare y
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64_T(0))));
                add(Statement.declareVariable(new Variable("y", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                // while condition
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int64_T(0))));
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int32_T())));
                add(Statement.binaryOp(Token.Type.OP_GREATER, true, 0, false, 1, true, -1));
                add(Statement.jumpRelConditional(true, 2, true, 0));
                add(Statement.clearStack(2)); // condition
                add(Statement.jumpRel(11));
                // body
                add(Statement.declareVariable(new Variable("4_COMPILER_TEMP", new Int64_T(1))));
                add(Statement.declareVariable(new Variable("5_COMPILER_TEMP", new Int32_T())));
                add(Statement.binaryOp(Token.Type.OP_MINUS, true, 0, false, 1, true, -1));
                add(Statement.assignVariable(false, 1, true, 0));
                add(Statement.declareVariable(new Variable("6_COMPILER_TEMP", new Int64_T(1))));
                add(Statement.declareVariable(new Variable("7_COMPILER_TEMP", new Int32_T())));
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
                interpreter.setStatements(instr);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.setStatements(instr2);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("y", result.name);
                Assertions.assertEquals(new Int32_T(10).getValue(), result.value.getValue());
            }
        }
        {
            String testStr = "int32 y = 0; for (int32 x = 10; x > 0; x = x - 1) {y = y + 1; continue;}";
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // declare y
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64_T(0))));
                add(Statement.declareVariable(new Variable("y", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));

                // for init
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64_T(10))));
                add(Statement.declareVariable(new Variable("x", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                // for condition
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int64_T(0))));
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int32_T())));
                add(Statement.binaryOp(Token.Type.OP_GREATER, true, 0, false, 3, true, -1));
                add(Statement.jumpRelConditional(false, 6+5+1, true, 0)); // body + increment + 1
                // body
                add(Statement.declareVariable(new Variable("4_COMPILER_TEMP", new Int64_T(1))));
                add(Statement.declareVariable(new Variable("5_COMPILER_TEMP", new Int32_T())));
                add(Statement.binaryOp(Token.Type.OP_PLUS, true, 0, false, 1, true, -1));
                add(Statement.assignVariable(false, 1, true, 0));
                add(Statement.continueLoop(2, 1));
                add(Statement.clearStack(2)); // scope
                // for increment
                add(Statement.declareVariable(new Variable("6_COMPILER_TEMP", new Int64_T(1))));
                add(Statement.declareVariable(new Variable("7_COMPILER_TEMP", new Int32_T())));
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
                interpreter.setStatements(instr);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.setStatements(instr2);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("y", result.name);
                Assertions.assertEquals(new Int32_T(10).getValue(), result.value.getValue());
            }
        }
        {
            String testStr = "int32 y = 0; for (;;) {y = y + 1; if (y == 10) break; continue; }";
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // declare y
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64_T(0))));
                add(Statement.declareVariable(new Variable("y", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));

                // for init
                // for condition
                // body
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64_T(1))));
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int32_T())));
                add(Statement.binaryOp(Token.Type.OP_PLUS, true, 0, false, 1, true, -1));
                add(Statement.assignVariable(false, 1, true, 0));
                    // if condition
                    add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int64_T(10))));
                    add(Statement.declareVariable(new Variable("4_COMPILER_TEMP", new Int32_T())));
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
                interpreter.setStatements(instr);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.setStatements(instr2);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("y", result.name);
                Assertions.assertEquals(new Int32_T(10).getValue(), result.value.getValue());
            }
        }
        {
            String testStr = "int32 y = 0; for (;true;) {y = y + 1; if (y == 10) break; continue; }";
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // declare y
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64_T(0))));
                add(Statement.declareVariable(new Variable("y", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));

                // for init
                // for condition
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Bool_T(true))));
                add(Statement.jumpRelConditional(false, 14 + 1 + 1, true, 0)); // body + increment + 1
                // body
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int64_T(1))));
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int32_T())));
                add(Statement.binaryOp(Token.Type.OP_PLUS, true, 0, false, 1, true, -1));
                add(Statement.assignVariable(false, 1, true, 0));
                    // if condition
                    add(Statement.declareVariable(new Variable("4_COMPILER_TEMP", new Int64_T(10))));
                    add(Statement.declareVariable(new Variable("5_COMPILER_TEMP", new Int32_T())));
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
                interpreter.setStatements(instr);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.setStatements(instr2);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("y", result.name);
                Assertions.assertEquals(new Int32_T(10).getValue(), result.value.getValue());
            }
        }
        {
            String testStr = "int32 x = 10; int32 y = 0; while (x > 0) {x = x - 1; continue; y = y + 1;}";
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // declare x
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64_T(10))));
                add(Statement.declareVariable(new Variable("x", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                // declare y
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64_T(0))));
                add(Statement.declareVariable(new Variable("y", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                // while condition
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int64_T(0))));
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int32_T())));
                add(Statement.binaryOp(Token.Type.OP_GREATER, true, 0, false, 1, true, -1));
                add(Statement.jumpRelConditional(true, 2, true, 0));
                add(Statement.clearStack(2)); // condition
                add(Statement.jumpRel(12));
                // body
                add(Statement.declareVariable(new Variable("4_COMPILER_TEMP", new Int64_T(1))));
                add(Statement.declareVariable(new Variable("5_COMPILER_TEMP", new Int32_T())));
                add(Statement.binaryOp(Token.Type.OP_MINUS, true, 0, false, 1, true, -1));
                add(Statement.assignVariable(false, 1, true, 0));
                add(Statement.continueLoop(2+2, 6));
                add(Statement.declareVariable(new Variable("6_COMPILER_TEMP", new Int64_T(1))));
                add(Statement.declareVariable(new Variable("7_COMPILER_TEMP", new Int32_T())));
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
                interpreter.setStatements(instr);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.setStatements(instr2);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.peek();
                Assertions.assertEquals("y", result.name);
                Assertions.assertEquals(new Int32_T(0).getValue(), result.value.getValue());
            }
        }
        {
            String testStr = "int32 x = 10; int32 y = 0; while (x > 0) {x = x - 1; break; y = y + 1;}";
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // declare x
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64_T(10))));
                add(Statement.declareVariable(new Variable("x", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                // declare y
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64_T(0))));
                add(Statement.declareVariable(new Variable("y", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                // while condition
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int64_T(0))));
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int32_T())));
                add(Statement.binaryOp(Token.Type.OP_GREATER, true, 0, false, 1, true, -1));
                add(Statement.jumpRelConditional(true, 2, true, 0));
                add(Statement.clearStack(2)); // condition
                add(Statement.jumpRel(12));
                // body
                add(Statement.declareVariable(new Variable("4_COMPILER_TEMP", new Int64_T(1))));
                add(Statement.declareVariable(new Variable("5_COMPILER_TEMP", new Int32_T())));
                add(Statement.binaryOp(Token.Type.OP_MINUS, true, 0, false, 1, true, -1));
                add(Statement.assignVariable(false, 1, true, 0));
                add(Statement.breakLoop(2+2, 7));
                add(Statement.declareVariable(new Variable("6_COMPILER_TEMP", new Int64_T(1))));
                add(Statement.declareVariable(new Variable("7_COMPILER_TEMP", new Int32_T())));
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
                interpreter.setStatements(instr);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.setStatements(instr2);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.get(1);
                Assertions.assertEquals("x", result.name);
                Assertions.assertEquals(new Int32_T(9).getValue(), result.value.getValue());
            }
        }
    }

    @Test public void testExternalFunctions() {
        {
            Compiler compiler = new Compiler();
            Compiler.ExtFuncDecl funcDecl = compiler.addExternalFunction("testFunc", false, new Int32_T(), new Type_T[]{}, (Type_T[] p) -> {
                return new Int32_T(999);
            });
            String testStr = "int32 x = 5; x = testFunc();";
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // declare external function
                add(Statement.declareVariable(new FunctionVariable("testFunc", funcDecl.retType, funcDecl.params, 1)));
                add(Statement.jumpRel(1));
                add(Statement.externalFunctionCall(funcDecl, true));

                // declare x
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64_T(5))));
                add(Statement.declareVariable(new Variable("x", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                // call external function
                add(Statement.functionCall(1));
                add(Statement.assignVariable(false, 2, true, 0));
                // clear stack
                add(Statement.clearStack(4));
            }};

            ArrayList<Stack<Variable>> results = new ArrayList<>();
            // hardcoded
            {
                Interpreter interpreter = new Interpreter();
                instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
                interpreter.setStatements(instr);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = compiler.compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.setStatements(instr2);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.get(2);
                Assertions.assertEquals("x", result.name);
                Assertions.assertEquals(new Int32_T(999).getValue(), result.value.getValue());
            }
        }
        {
            Compiler compiler = new Compiler();
            Compiler.ExtFuncDecl funcDecl = compiler.addExternalFunction("testFunc", false, new Int32_T(), new Type_T[]{new Int32_T()}, (Type_T[] p) -> {
                int p0 = ((Number)(p[0].getValue())).intValue();
                return new Int32_T(p0 + 5);
            });
            String testStr = "int32 x = 5; x = testFunc(5);";
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // declare external function
                add(Statement.declareVariable(new FunctionVariable("testFunc", funcDecl.retType, funcDecl.params, 1)));
                add(Statement.jumpRel(1));
                add(Statement.externalFunctionCall(funcDecl, true));

                // declare x
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64_T(5))));
                add(Statement.declareVariable(new Variable("x", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                // call external function
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64_T(5))));
                add(Statement.functionCall(1));
                add(Statement.assignVariable(false, 2, true, 0));
                // clear stack
                add(Statement.clearStack(4));
            }};

            ArrayList<Stack<Variable>> results = new ArrayList<>();
            // hardcoded
            {
                Interpreter interpreter = new Interpreter();
                instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
                interpreter.setStatements(instr);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = compiler.compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.setStatements(instr2);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.get(2);
                Assertions.assertEquals("x", result.name);
                Assertions.assertEquals(new Int32_T(10).getValue(), result.value.getValue());
            }
        }
        {
            Compiler compiler = new Compiler();
            Compiler.ExtFuncDecl funcDecl = compiler.addExternalFunction("testFunc", false, new Int32_T(), new Type_T[]{new Int32_T()}, (Type_T[] p) -> {
                int p0 = ((Number)(p[0].getValue())).intValue();
                return new Int32_T(p0 + 5);
            });
            Compiler.ExtFuncDecl funcDecl2 = compiler.addExternalFunction("testFunc2", false, new Int32_T(), new Type_T[]{new Int32_T()}, (Type_T[] p) -> {
                int p0 = ((Number)(p[0].getValue())).intValue();
                return new Int32_T(p0 + 10);
            });
            String testStr = "int32 x = 5; x = testFunc(5); x = testFunc2(6);";
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // declare external functions
                add(Statement.declareVariable(new FunctionVariable("testFunc", funcDecl.retType, funcDecl.params, 1)));
                add(Statement.jumpRel(1));
                add(Statement.externalFunctionCall(funcDecl, true));
                add(Statement.declareVariable(new FunctionVariable("testFunc2", funcDecl2.retType, funcDecl2.params, 4)));
                add(Statement.jumpRel(1));
                add(Statement.externalFunctionCall(funcDecl2, true));

                // declare x
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64_T(5))));
                add(Statement.declareVariable(new Variable("x", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                // call external function
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64_T(5))));
                add(Statement.functionCall(1));
                add(Statement.assignVariable(false, 3, true, 0));
                // call external function
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int64_T(6))));
                add(Statement.functionCall(4));
                add(Statement.assignVariable(false, 3, true, 0));
                // clear stack
                add(Statement.clearStack(6));
            }};

            ArrayList<Stack<Variable>> results = new ArrayList<>();
            // hardcoded
            {
                Interpreter interpreter = new Interpreter();
                instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
                interpreter.setStatements(instr);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = compiler.compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.setStatements(instr2);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.get(3);
                Assertions.assertEquals("x", result.name);
                Assertions.assertEquals(new Int32_T(16).getValue(), result.value.getValue());
            }
        }
        {
          Compiler compiler = new Compiler();
            Compiler.ExtFuncDecl funcDecl = compiler.addExternalFunction("externalFunc", false, new Int32_T(), new Type_T[]{new Int32_T()}, (p) -> {
                int p0 = Script.intParam(p[0]);
                return new Int32_T(p0 + 2);
            });
            String testStr = "int32 x = 0; while (x < 10) { x = x + externalFunc(x); }";
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // declare external functions
                add(Statement.declareVariable(new FunctionVariable(funcDecl.name, funcDecl.retType, funcDecl.params, 1)));
                add(Statement.jumpRel(1));
                add(Statement.externalFunctionCall(funcDecl, true));

                // declare x
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64_T(0))));
                add(Statement.declareVariable(new Variable("x", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));

                // while
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64_T(10))));
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int32_T())));
                add(Statement.binaryOp(Token.Type.OP_LESS, true, 0, false, 2, true, -1));
                add(Statement.jumpRelConditional(true, 2, true, 0));
                add(Statement.clearStack(2)); // condition
                add(Statement.jumpRel(8));
                // body
                add(Statement.createTempCopy(false, 2));
                add(Statement.functionCall(1));
                add(Statement.declareVariable(new Variable("5_COMPILER_TEMP", new Int32_T())));
                add(Statement.binaryOp(Token.Type.OP_PLUS, true, 0, false, 2, true, -1));
                add(Statement.assignVariable(false, 2, true, 0));
                add(Statement.clearStack(2)); // scope
                // while
                add(Statement.clearStack(2)); // body + condition
                add(Statement.jumpRel(-(7+6+1))); // -(body + condition + 1)

                // clear stack
                add(Statement.clearStack(3));
            }};

            ArrayList<Stack<Variable>> results = new ArrayList<>();
            // hardcoded
            {
                Interpreter interpreter = new Interpreter();
                instr.remove(instr.size() - 1); // remove stack clear instruction to see variables
                interpreter.setStatements(instr);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }
            // from compiler
            {
                ArrayList<Statement> instr2 = compiler.compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
                Interpreter interpreter = new Interpreter();
                instr2.remove(instr2.size() - 1); // remove stack clear instruction to see variables
                interpreter.setStatements(instr2);
                interpreter.execute();
                results.add(interpreter.getStack()); 
            }

            for (Stack<Variable> stack : results) {
                Variable result = stack.get(2);
                Assertions.assertEquals("x", result.name);
                Assertions.assertEquals(new Int32_T(14).getValue(), result.value.getValue());
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