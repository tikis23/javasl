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

    private void compareStack(Stack<Variable> expected, Stack<Variable> real) {
        Assertions.assertEquals(expected.size(), real.size());
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i).toString(), real.get(i).toString());
        }
    }
}