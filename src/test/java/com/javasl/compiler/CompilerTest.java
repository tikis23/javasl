package com.javasl.compiler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
                add(Statement.assignVariable(1, 0));
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
                add(Statement.binaryOp(Token.Type.OP_MULTIPLY, 2, 0, 1));
                add(Statement.declareVariable(new Variable("x", new Int16()))); // s3
                add(Statement.assignVariable(3, 2));
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int64(2)))); // s4
                add(Statement.declareVariable(new Variable("4_COMPILER_TEMP", new Int16()))); // s5
                add(Statement.binaryOp(Token.Type.OP_PLUS, 5, 3, 4));
                add(Statement.declareVariable(new Variable("y", new Int32()))); // s6
                add(Statement.assignVariable(6, 5));
                add(Statement.declareVariable(new Variable("5_COMPILER_TEMP", new Int64(2)))); // s7
                add(Statement.assignVariable(3, 7));
                add(Statement.clearStack(8));
            }};
            
            ArrayList<Statement> real = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));         
            CompareStatementLists(expected, real);
        }
    }

    private void CompareStatementLists(ArrayList<Statement> expected, ArrayList<Statement> real) {
        Assertions.assertEquals(expected.size(), real.size());
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertEquals(expected.get(i).toString(), real.get(i).toString());
        }
    }
}