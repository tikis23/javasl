package com.javasl.compiler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.javasl.runtime.FunctionVariable;
import com.javasl.runtime.Statement;
import com.javasl.runtime.types.*;
import com.javasl.runtime.Variable;

import java.util.ArrayList;

public class CompilerTest {
    @Test public void testAssignment() {
        {
            String testStr = "int32 x = 1;";
            ArrayList<Statement> expected = new ArrayList<Statement>(){{
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64(1)))); //s0
                add(Statement.declareVariable(new Variable("x", new Int32()))); // s1
                add(Statement.assignVariable(true, 0, true, -1));
                add(Statement.clearStack(2));
            }};
            
            ArrayList<Statement> real = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));  
            CompareStatementLists(expected, real);
        }
        {
            String testStr = "int16 x = 1 * 5;";
            testStr += "int32 y = x + 2;";
            testStr += "x = 2;";
            ArrayList<Statement> expected = new ArrayList<Statement>(){{
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64(1)))); // s0
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64(5)))); // s1
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int64()))); // s2
                add(Statement.binaryOp(Token.Type.OP_MULTIPLY, true, 0, true, -2, true, -1));
                add(Statement.declareVariable(new Variable("x", new Int16()))); // s3
                add(Statement.assignVariable(true, 0, true, -1));
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int64(2)))); // s4
                add(Statement.declareVariable(new Variable("4_COMPILER_TEMP", new Int16()))); // s5
                add(Statement.binaryOp(Token.Type.OP_PLUS, true, 0, false, 3, true, -1));
                add(Statement.declareVariable(new Variable("y", new Int32()))); // s6
                add(Statement.assignVariable(true, 0, true, -1));
                add(Statement.declareVariable(new Variable("5_COMPILER_TEMP", new Int64(2)))); // s7
                add(Statement.assignVariable(false, 3, true, 0));
                add(Statement.clearStack(8));
            }};
            
            ArrayList<Statement> real = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));         
            CompareStatementLists(expected, real);
        }
        {
            String testStr = "bool x = true; bool y = 1; int32 z = x;";
            ArrayList<Statement> expected = new ArrayList<Statement>(){{
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Bool(true))));
                add(Statement.declareVariable(new Variable("x", new Bool())));
                add(Statement.assignVariable(true, 0, true, -1));
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64(1))));
                add(Statement.declareVariable(new Variable("y", new Bool())));
                add(Statement.assignVariable(true, 0, true, -1));
                add(Statement.declareVariable(new Variable("z", new Int32())));
                add(Statement.assignVariable(true, 0, false, 1));
                add(Statement.clearStack(5));
            }};
            
            ArrayList<Statement> real = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));  
            CompareStatementLists(expected, real);
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
            
            ArrayList<Statement> result = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));

            CompareStatementLists(instr, result);
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
            
            ArrayList<Statement> result = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));

            CompareStatementLists(instr, result);
        }
    }

    private void CompareStatementLists(ArrayList<Statement> expected, ArrayList<Statement> real) {
        Assertions.assertEquals(expected.size(), real.size());
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i).toString(), real.get(i).toString());
        }
    }
}