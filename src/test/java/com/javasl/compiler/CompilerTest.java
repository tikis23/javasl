package com.javasl.compiler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.javasl.Script;
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
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64_T(1)))); //s0
                add(Statement.declareVariable(new Variable("x", new Int32_T()))); // s1
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
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64_T(1)))); // s0
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64_T(5)))); // s1
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int64_T()))); // s2
                add(Statement.binaryOp(Token.Type.OP_MULTIPLY, true, 0, true, -2, true, -1));
                add(Statement.declareVariable(new Variable("x", new Int16_T()))); // s3
                add(Statement.assignVariable(true, 0, true, -1));
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int64_T(2)))); // s4
                add(Statement.declareVariable(new Variable("4_COMPILER_TEMP", new Int16_T()))); // s5
                add(Statement.binaryOp(Token.Type.OP_PLUS, true, 0, false, 3, true, -1));
                add(Statement.declareVariable(new Variable("y", new Int32_T()))); // s6
                add(Statement.assignVariable(true, 0, true, -1));
                add(Statement.declareVariable(new Variable("5_COMPILER_TEMP", new Int64_T(2)))); // s7
                add(Statement.assignVariable(false, 3, true, 0));
                add(Statement.clearStack(8));
            }};
            
            ArrayList<Statement> real = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));         
            CompareStatementLists(expected, real);
        }
        {
            String testStr = "bool x = true; bool y = 1; int32 z = x;";
            ArrayList<Statement> expected = new ArrayList<Statement>(){{
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Bool_T(true))));
                add(Statement.declareVariable(new Variable("x", new Bool_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64_T(1))));
                add(Statement.declareVariable(new Variable("y", new Bool_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                add(Statement.declareVariable(new Variable("z", new Int32_T())));
                add(Statement.assignVariable(true, 0, false, 1));
                add(Statement.clearStack(5));
            }};
            
            ArrayList<Statement> real = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));  
            CompareStatementLists(expected, real);
        }
        {
            String testStr = "int32 x = -9; int32 y = 99; bool z = x < y;";
            ArrayList<Statement> expected = new ArrayList<Statement>(){{
                add(Statement.declareVariable(new Variable("0_COMPILER_TEMP", new Int64_T(-9))));
                add(Statement.declareVariable(new Variable("x", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                add(Statement.declareVariable(new Variable("1_COMPILER_TEMP", new Int64_T(99))));
                add(Statement.declareVariable(new Variable("y", new Int32_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                add(Statement.declareVariable(new Variable("2_COMPILER_TEMP", new Int32_T())));
                add(Statement.binaryOp(Token.Type.OP_LESS, true, 0, false, 1, false, 3));
                add(Statement.declareVariable(new Variable("z", new Bool_T())));
                add(Statement.assignVariable(true, 0, true, -1));
                add(Statement.clearStack(6));
            }};
            
            ArrayList<Statement> real = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));  
            CompareStatementLists(expected, real);
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
            
            ArrayList<Statement> result = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));

            CompareStatementLists(instr, result);
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
            
            ArrayList<Statement> result = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));

            CompareStatementLists(instr, result);
        }
    }

    @Test public void testConditionals() {

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

            ArrayList<Statement> result = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
            CompareStatementLists(instr, result);
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

            ArrayList<Statement> result = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));

            CompareStatementLists(instr, result);
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

            ArrayList<Statement> result = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));

            CompareStatementLists(instr, result);
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

            ArrayList<Statement> result = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));

            CompareStatementLists(instr, result);
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

            ArrayList<Statement> result = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));

            CompareStatementLists(instr, result);
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

            ArrayList<Statement> result = new Compiler().compile(new Parser().parse(new Tokenizer().tokenize(testStr)));

            CompareStatementLists(instr, result);
        }
    }

  @Test public void testExternalFunctions() {
        {
            Compiler compiler = new Compiler();
            compiler.addExternalFunction("testFunc", false, new Int32_T(), new Type_T[]{}, (Type_T[] p) -> {
                return new Int32_T(999);
            });
            String testStr = "int32 x = 5; x = testFunc();";
            ArrayList<Statement> instr = new ArrayList<Statement>() {{
                // declare external function
                add(Statement.declareVariable(new FunctionVariable("testFunc", new Int32_T(), new ArrayList<Variable>(), 1)));
                add(Statement.jumpRel(1));
                add(Statement.externalFunctionCall(new Compiler.ExtFuncDecl() {{
                    name = "testFunc";
                    retType = new Int32_T();
                    params = new ArrayList<>();
                    function = (Type_T[] p) -> {
                        return new Int32_T(999);
                    };
                }}, true));

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

            ArrayList<Statement> result = compiler.compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
            CompareStatementLists(instr, result);
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

            ArrayList<Statement> result = compiler.compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
            CompareStatementLists(instr, result);
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
                add(Statement.declareVariable(new Variable("3_COMPILER_TEMP", new Int64_T(6))));
                add(Statement.functionCall(4));
                add(Statement.assignVariable(false, 3, true, 0));
                // clear stack
                add(Statement.clearStack(6));
            }};

            ArrayList<Statement> result = compiler.compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
            CompareStatementLists(instr, result);
        }
        {
            Compiler compiler = new Compiler();
            Compiler.ExtFuncDecl funcDecl = compiler.addExternalFunction("externalFunc", false, new Int32_T(), new Type_T[]{new Int32_T()}, (p) -> {
                int p0 = Script.intParam(p[0]);
                return new Int32_T(p0 * 2);
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

            ArrayList<Statement> result = compiler.compile(new Parser().parse(new Tokenizer().tokenize(testStr)));
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