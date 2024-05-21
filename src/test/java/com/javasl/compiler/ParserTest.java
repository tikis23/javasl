package com.javasl.compiler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.javasl.compiler.ast.*;

import java.util.ArrayList;

public class ParserTest {
    @Test public void testAssignment() {
        {
            BlockNode expectedAst = new BlockNode();
            expectedAst.statements.add(new AssignmentNode() {{
                lhs = new DeclarationNode() {{
                    type = new Token(Token.Type.T_INT64, "int64");
                    identifier = new Token(Token.Type.IDENTIFIER, "x");
                }};
                rhs = new LiteralNode() {{
                    literal = new Token(Token.Type.LIT_NUMBER, "1");
                }};
            }});

            // harcoded
            {
                ArrayList<Token> tokens = new ArrayList<Token>(){{
                    add(new Token(Token.Type.T_INT64, "int64"));
                    add(new Token(Token.Type.IDENTIFIER, "x"));
                    add(new Token(Token.Type.OP_ASSIGN, "="));
                    add(new Token(Token.Type.LIT_NUMBER, "1"));
                    add(new Token(Token.Type.SEMICOLON, ";"));
                }};
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);
                CompareAST(expectedAst, ast);
            }
            // using tokenizer
            {
                Tokenizer tokenizer = new Tokenizer();
                ArrayList<Token> tokens = tokenizer.tokenize("int64 x = 1;");
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);
                CompareAST(expectedAst, ast);
            }
        }
        {
            BlockNode expectedAst = new BlockNode();
            expectedAst.statements.add(new AssignmentNode() {{
                lhs = new DeclarationNode() {{
                    type = new Token(Token.Type.T_INT64, "int64");
                    identifier = new Token(Token.Type.IDENTIFIER, "x");
                }};
                rhs = new BinaryOpNode() {{
                    operator = new Token(Token.Type.OP_MULTIPLY, "*");
                    lhs = new LiteralNode() {{
                        literal = new Token(Token.Type.LIT_NUMBER, "-99");
                    }};
                    rhs = new LiteralNode() {{
                        literal = new Token(Token.Type.LIT_NUMBER, "-1");
                    }};
                }};
            }});

            // harcoded
            {
                ArrayList<Token> tokens = new ArrayList<Token>(){{
                    add(new Token(Token.Type.T_INT64, "int64"));
                    add(new Token(Token.Type.IDENTIFIER, "x"));
                    add(new Token(Token.Type.OP_ASSIGN, "="));
                    add(new Token(Token.Type.LIT_NUMBER, "-99"));
                    add(new Token(Token.Type.OP_MULTIPLY, "*"));
                    add(new Token(Token.Type.LIT_NUMBER, "-1"));
                    add(new Token(Token.Type.SEMICOLON, ";"));
                }};
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);
                CompareAST(expectedAst, ast);
            }
            // using tokenizer
            {
                Tokenizer tokenizer = new Tokenizer();
                ArrayList<Token> tokens = tokenizer.tokenize("int64 x = -99 * -1;");
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);
                CompareAST(expectedAst, ast);
            }
        }
        {
            BlockNode expectedAst = new BlockNode();
            expectedAst.statements.add(new AssignmentNode() {{
                lhs = new DeclarationNode() {{
                    type = new Token(Token.Type.T_FLOAT, "float");
                    identifier = new Token(Token.Type.IDENTIFIER, "x");
                }};
                rhs = new BinaryOpNode() {{
                    operator = new Token(Token.Type.OP_MULTIPLY, "*");
                    lhs = new LiteralNode() {{
                        literal = new Token(Token.Type.LIT_NUMBER, "-99.0");
                    }};
                    rhs = new LiteralNode() {{
                        literal = new Token(Token.Type.LIT_NUMBER, "-1.9875");
                    }};
                }};
            }});

            // harcoded
            {
                ArrayList<Token> tokens = new ArrayList<Token>(){{
                    add(new Token(Token.Type.T_FLOAT, "float"));
                    add(new Token(Token.Type.IDENTIFIER, "x"));
                    add(new Token(Token.Type.OP_ASSIGN, "="));
                    add(new Token(Token.Type.OP_MINUS, "-"));
                    add(new Token(Token.Type.LIT_NUMBER, "99"));
                    add(new Token(Token.Type.DOT, "."));
                    add(new Token(Token.Type.OP_MULTIPLY, "*"));
                    add(new Token(Token.Type.OP_MINUS, "-"));
                    add(new Token(Token.Type.LIT_NUMBER, "1"));
                    add(new Token(Token.Type.DOT, "."));
                    add(new Token(Token.Type.LIT_NUMBER, "9875"));
                    add(new Token(Token.Type.SEMICOLON, ";"));
                }};
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);
                CompareAST(expectedAst, ast);
            }
            // using tokenizer
            {
                Tokenizer tokenizer = new Tokenizer();
                ArrayList<Token> tokens = tokenizer.tokenize("float x = -99. * -1.9875;");
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);
                CompareAST(expectedAst, ast);
            }
        }
        {
            BlockNode expectedAst = new BlockNode();
            expectedAst.statements.add(new AssignmentNode() {{
                lhs = new DeclarationNode() {{
                    type = new Token(Token.Type.IDENTIFIER, "customType");
                    identifier = new Token(Token.Type.IDENTIFIER, "customName89");
                }};
                rhs = new BinaryOpNode() {{
                    operator = new Token(Token.Type.OP_PLUS, "+");
                    lhs = new LiteralNode() {{
                        literal = new Token(Token.Type.LIT_NUMBER, "1");
                    }};
                    rhs = new LiteralNode() {{
                        literal = new Token(Token.Type.LIT_NUMBER, "998");
                    }};
                }};
            }});

            // hardcoded
            {
                ArrayList<Token> tokens = new ArrayList<Token>(){{
                    add(new Token(Token.Type.IDENTIFIER, "customType"));
                    add(new Token(Token.Type.IDENTIFIER, "customName89"));
                    add(new Token(Token.Type.OP_ASSIGN, "="));
                    add(new Token(Token.Type.LIT_NUMBER, "1"));
                    add(new Token(Token.Type.OP_PLUS, "+"));
                    add(new Token(Token.Type.LIT_NUMBER, "998"));
                    add(new Token(Token.Type.SEMICOLON, ";"));
                }};
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);
                CompareAST(expectedAst, ast);
            }
            // using tokenizer
            {
                Tokenizer tokenizer = new Tokenizer();
                ArrayList<Token> tokens = tokenizer.tokenize("customType customName89 = 1 + 998;");
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);
                CompareAST(expectedAst, ast);
            }
        }
        {
            BlockNode expectedAst = new BlockNode();
            expectedAst.statements.add(new AssignmentNode() {{
                lhs = new IdentifierNode() {{
                    identifier = new Token(Token.Type.IDENTIFIER, "great_name99");
                }};
                rhs = new BinaryOpNode() {{
                    operator = new Token(Token.Type.OP_PLUS, "+");
                    lhs = new LiteralNode() {{
                        literal = new Token(Token.Type.LIT_NUMBER, "1");
                    }};
                    rhs = new BinaryOpNode() {{
                        operator = new Token(Token.Type.OP_DIVIDE, "/");
                        lhs = new LiteralNode() {{
                            literal = new Token(Token.Type.LIT_NUMBER, "998");
                        }};
                        rhs = new LiteralNode() {{
                            literal = new Token(Token.Type.LIT_NUMBER, "8");
                        }};
                    }};
                }};
            }});

            // hardcoded
            {
                ArrayList<Token> tokens = new ArrayList<Token>(){{
                    add(new Token(Token.Type.IDENTIFIER, "great_name99"));
                    add(new Token(Token.Type.OP_ASSIGN, "="));
                    add(new Token(Token.Type.LIT_NUMBER, "1"));
                    add(new Token(Token.Type.OP_PLUS, "+"));
                    add(new Token(Token.Type.OPEN_PAREN, "("));
                    add(new Token(Token.Type.LIT_NUMBER, "998"));
                    add(new Token(Token.Type.OP_DIVIDE, "/"));
                    add(new Token(Token.Type.LIT_NUMBER, "8"));
                    add(new Token(Token.Type.CLOSE_PAREN, ")"));
                    add(new Token(Token.Type.SEMICOLON, ";"));
                }};
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);
                CompareAST(expectedAst, ast);
            }
            // using tokenizer
            {
                Tokenizer tokenizer = new Tokenizer();
                ArrayList<Token> tokens = tokenizer.tokenize("great_name99 = 1 + (998 / 8);");
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);
                CompareAST(expectedAst, ast);
            }
        }
        {
            BlockNode expectedAst = new BlockNode();
            expectedAst.statements.add(new AssignmentNode() {{
                lhs = new IdentifierNode() {{
                    identifier = new Token(Token.Type.IDENTIFIER, "great_name99");
                }};
                rhs = new BinaryOpNode() {{
                    operator = new Token(Token.Type.OP_PLUS, "+");
                    lhs = new IdentifierNode() {{
                        identifier = new Token(Token.Type.IDENTIFIER, "test_identifier");
                    }};
                    rhs = new BinaryOpNode() {{
                        operator = new Token(Token.Type.OP_DIVIDE, "/");
                        lhs = new LiteralNode() {{
                            literal = new Token(Token.Type.LIT_NUMBER, "998");
                        }};
                        rhs = new LiteralNode() {{
                            literal = new Token(Token.Type.LIT_NUMBER, "8");
                        }};
                    }};
                }};
            }});

            // hardcoded
            {
                ArrayList<Token> tokens = new ArrayList<Token>(){{
                    add(new Token(Token.Type.IDENTIFIER, "great_name99"));
                    add(new Token(Token.Type.OP_ASSIGN, "="));
                    add(new Token(Token.Type.IDENTIFIER, "test_identifier"));
                    add(new Token(Token.Type.OP_PLUS, "+"));
                    add(new Token(Token.Type.LIT_NUMBER, "998"));
                    add(new Token(Token.Type.OP_DIVIDE, "/"));
                    add(new Token(Token.Type.LIT_NUMBER, "8"));
                    add(new Token(Token.Type.SEMICOLON, ";"));
                }};
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);
                CompareAST(expectedAst, ast);
            }
            // using tokenizer
            {
                Tokenizer tokenizer = new Tokenizer();
                ArrayList<Token> tokens = tokenizer.tokenize("great_name99 = test_identifier + 998 / 8;");
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);
                CompareAST(expectedAst, ast);
            }
        }
        {
            BlockNode expectedAst = new BlockNode();
            expectedAst.statements.add(new AssignmentNode() {{
                lhs = new DeclarationNode() {{
                    type = new Token(Token.Type.IDENTIFIER, "testType");
                    identifier = new Token(Token.Type.IDENTIFIER, "test");
                }};
                rhs = new BinaryOpNode() {{
                    operator = new Token(Token.Type.OP_PLUS, "+");
                    lhs = new BinaryOpNode() {{
                        operator = new Token(Token.Type.OP_PLUS, "+");
                        lhs = new IdentifierNode() {{
                            identifier = new Token(Token.Type.IDENTIFIER, "x");
                        }};
                        rhs = new BinaryOpNode() {{
                            operator = new Token(Token.Type.OP_DIVIDE, "/");
                            lhs = new LiteralNode() {{
                                literal = new Token(Token.Type.LIT_NUMBER, "998");
                            }};
                            rhs = new BinaryOpNode() {{
                                operator = new Token(Token.Type.OP_MODULO, "%");
                                lhs = new LiteralNode() {{
                                    literal = new Token(Token.Type.LIT_NUMBER, "8");
                                }};
                                rhs = new LiteralNode() {{
                                    literal = new Token(Token.Type.LIT_NUMBER, "2");
                                }};
                            }};
                        }};
                    }};
                    rhs = new IdentifierNode() {{
                        identifier = new Token(Token.Type.IDENTIFIER, "y");
                    }};
                }};
            }});

            // hardcoded
            {
                ArrayList<Token> tokens = new ArrayList<Token>(){{
                    add(new Token(Token.Type.IDENTIFIER, "testType"));
                    add(new Token(Token.Type.IDENTIFIER, "test"));
                    add(new Token(Token.Type.OP_ASSIGN, "="));
                    add(new Token(Token.Type.IDENTIFIER, "x"));
                    add(new Token(Token.Type.OP_PLUS, "+"));
                    add(new Token(Token.Type.LIT_NUMBER, "998"));
                    add(new Token(Token.Type.OP_DIVIDE, "/"));
                    add(new Token(Token.Type.OPEN_PAREN, "("));
                    add(new Token(Token.Type.LIT_NUMBER, "8"));
                    add(new Token(Token.Type.OP_MODULO, "%"));
                    add(new Token(Token.Type.LIT_NUMBER, "2"));
                    add(new Token(Token.Type.CLOSE_PAREN, ")"));
                    add(new Token(Token.Type.OP_PLUS, "+"));
                    add(new Token(Token.Type.IDENTIFIER, "y"));
                    add(new Token(Token.Type.SEMICOLON, ";"));
                }};
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);
                CompareAST(expectedAst, ast);
            }
            // using tokenizer
            {
                Tokenizer tokenizer = new Tokenizer();
                ArrayList<Token> tokens = tokenizer.tokenize("testType test = x + 998 / (8 % 2) + y;");
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);
                CompareAST(expectedAst, ast);
            }
        }
        {
            BlockNode expectedAst = new BlockNode();
            expectedAst.statements.add(new AssignmentNode() {{
                lhs = new DeclarationNode() {{
                    type = new Token(Token.Type.T_BOOL, "bool");
                    identifier = new Token(Token.Type.IDENTIFIER, "test");
                }};
                rhs = new LiteralNode() {{
                    literal = new Token(Token.Type.KW_TRUE, "true");
                }};
            }});
            expectedAst.statements.add(new AssignmentNode() {{
                lhs = new DeclarationNode() {{
                    type = new Token(Token.Type.T_BOOL, "bool");
                    identifier = new Token(Token.Type.IDENTIFIER, "test2");
                }};
                rhs = new LiteralNode() {{
                    literal = new Token(Token.Type.KW_FALSE, "false");
                }};
            }});

            // hardcoded
            {
                ArrayList<Token> tokens = new ArrayList<Token>(){{
                    add(new Token(Token.Type.T_BOOL, "bool"));
                    add(new Token(Token.Type.IDENTIFIER, "test"));
                    add(new Token(Token.Type.OP_ASSIGN, "="));
                    add(new Token(Token.Type.KW_TRUE, "true"));
                    add(new Token(Token.Type.SEMICOLON, ";"));
                    add(new Token(Token.Type.T_BOOL, "bool"));
                    add(new Token(Token.Type.IDENTIFIER, "test2"));
                    add(new Token(Token.Type.OP_ASSIGN, "="));
                    add(new Token(Token.Type.KW_FALSE, "false"));
                    add(new Token(Token.Type.SEMICOLON, ";"));
                }};
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);
                CompareAST(expectedAst, ast);
            }
            // using tokenizer
            {
                Tokenizer tokenizer = new Tokenizer();
                ArrayList<Token> tokens = tokenizer.tokenize("bool test = true; bool test2 = false;");
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);
                CompareAST(expectedAst, ast);
            }
        }
    }

    @Test public void testFunctions() {
        {
            BlockNode expectedAst = new BlockNode();
            expectedAst.statements.add(new FunctionDefNode() {{
                declaration = new DeclarationNode() {{
                    type = new Token(Token.Type.T_INT64, "int64");
                    identifier = new Token(Token.Type.IDENTIFIER, "x");
                }};
                paramDeclaration = new ParamDeclarationNode() {{}};
                block = new BlockNode() {{
                    statements.add(new AssignmentNode() {{
                        lhs = new DeclarationNode() {{
                            type = new Token(Token.Type.T_INT32, "int32");
                            identifier = new Token(Token.Type.IDENTIFIER, "b");
                        }};
                        rhs = new LiteralNode() {{
                            literal = new Token(Token.Type.LIT_NUMBER, "1");
                        }};
                    }});
                    statements.add(new ReturnNode() {{
                        retVal = new IdentifierNode() {{
                            identifier = new Token(Token.Type.IDENTIFIER, "b");
                        }};
                    }});
                }};
            }});
            expectedAst.statements.add(new FunctionCallNode() {{
                funcName = new IdentifierNode() {{
                    identifier = new Token(Token.Type.IDENTIFIER, "x");
                }};
                params = new CallParamsNode() {{}};
            }});

            // hardcoded
            {
                ArrayList<Token> tokens = new ArrayList<Token>(){{
                    // func def
                    add(new Token(Token.Type.T_INT64, "int64"));
                    add(new Token(Token.Type.IDENTIFIER, "x"));
                    add(new Token(Token.Type.OPEN_PAREN, "("));
                    add(new Token(Token.Type.CLOSE_PAREN, ")"));
                    add(new Token(Token.Type.OPEN_CURLY_BRACKET, "{"));
                    add(new Token(Token.Type.T_INT32, "int32"));
                    add(new Token(Token.Type.IDENTIFIER, "b"));
                    add(new Token(Token.Type.OP_ASSIGN, "="));
                    add(new Token(Token.Type.LIT_NUMBER, "1"));
                    add(new Token(Token.Type.SEMICOLON, ";"));
                    add(new Token(Token.Type.KW_RETURN, "return"));
                    add(new Token(Token.Type.IDENTIFIER, "b"));
                    add(new Token(Token.Type.SEMICOLON, ";"));
                    add(new Token(Token.Type.CLOSE_CURLY_BRACKET, "}"));
                    // func call
                    add(new Token(Token.Type.IDENTIFIER, "x"));
                    add(new Token(Token.Type.OPEN_PAREN, "("));
                    add(new Token(Token.Type.CLOSE_PAREN, ")"));
                    add(new Token(Token.Type.SEMICOLON, ";"));
                }};
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);

                CompareAST(expectedAst, ast);
            }
            // using tokenizer
            {
                Tokenizer tokenizer = new Tokenizer();
                ArrayList<Token> tokens = tokenizer.tokenize("int64 x() { int32 b = 1; return b; } x();");
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);
                CompareAST(expectedAst, ast);
            }
        }
        {
            BlockNode expectedAst = new BlockNode();
            expectedAst.statements.add(new FunctionDefNode() {{
                declaration = new DeclarationNode() {{
                    type = new Token(Token.Type.T_INT64, "int64");
                    identifier = new Token(Token.Type.IDENTIFIER, "x");
                }};
                paramDeclaration = new ParamDeclarationNode() {{
                    declarations.add(new DeclarationNode() {{
                        type = new Token(Token.Type.T_INT16, "int16");
                        identifier = new Token(Token.Type.IDENTIFIER, "param1");
                    }});
                    declarations.add(new DeclarationNode() {{
                        type = new Token(Token.Type.T_INT32, "int32");
                        identifier = new Token(Token.Type.IDENTIFIER, "param2");
                    }});
                }};
                block = new BlockNode() {{
                    statements.add(new AssignmentNode() {{
                        lhs = new DeclarationNode() {{
                            type = new Token(Token.Type.T_INT32, "int32");
                            identifier = new Token(Token.Type.IDENTIFIER, "b");
                        }};
                        rhs = new BinaryOpNode() {{
                            operator = new Token(Token.Type.OP_PLUS, "+");
                            lhs = new IdentifierNode() {{
                                identifier = new Token(Token.Type.IDENTIFIER, "param1");
                            }};
                            rhs = new IdentifierNode() {{
                                identifier = new Token(Token.Type.IDENTIFIER, "param2");
                            }};
                        }};
                    }});
                    statements.add(new ReturnNode() {{
                        retVal = new BinaryOpNode() {{
                            operator = new Token(Token.Type.OP_PLUS, "+");
                            lhs = new IdentifierNode() {{
                                identifier = new Token(Token.Type.IDENTIFIER, "b");
                            }};
                            rhs = new IdentifierNode() {{
                                identifier = new Token(Token.Type.IDENTIFIER, "param2");
                            }};
                        }};
                    }});
                }};
            }});
            expectedAst.statements.add(new AssignmentNode() {{
                lhs = new DeclarationNode() {{
                    type = new Token(Token.Type.T_INT64, "int64");
                    identifier = new Token(Token.Type.IDENTIFIER, "result");
                }};
                rhs = new FunctionCallNode() {{
                    funcName = new IdentifierNode() {{
                        identifier = new Token(Token.Type.IDENTIFIER, "x");
                    }};
                    params = new CallParamsNode() {{
                        params.add(new LiteralNode() {{
                            literal = new Token(Token.Type.LIT_NUMBER, "1");
                        }});
                        params.add(new BinaryOpNode() {{
                            operator = new Token(Token.Type.OP_PLUS, "+");
                            lhs = new LiteralNode() {{
                                literal = new Token(Token.Type.LIT_NUMBER, "2");
                            }};
                            rhs = new LiteralNode() {{
                                literal = new Token(Token.Type.LIT_NUMBER, "5");
                            }};
                        }});
                    }};
                }};
            }});

            // hardcoded
            {
                ArrayList<Token> tokens = new ArrayList<Token>(){{
                    // func def
                    add(new Token(Token.Type.T_INT64, "int64"));
                    add(new Token(Token.Type.IDENTIFIER, "x"));
                    add(new Token(Token.Type.OPEN_PAREN, "("));
                    add(new Token(Token.Type.T_INT16, "int16"));
                    add(new Token(Token.Type.IDENTIFIER, "param1"));
                    add(new Token(Token.Type.COMMA, ","));
                    add(new Token(Token.Type.T_INT32, "int32"));
                    add(new Token(Token.Type.IDENTIFIER, "param2"));
                    add(new Token(Token.Type.CLOSE_PAREN, ")"));
                    add(new Token(Token.Type.OPEN_CURLY_BRACKET, "{"));
                    add(new Token(Token.Type.T_INT32, "int32"));
                    add(new Token(Token.Type.IDENTIFIER, "b"));
                    add(new Token(Token.Type.OP_ASSIGN, "="));
                    add(new Token(Token.Type.IDENTIFIER, "param1"));
                    add(new Token(Token.Type.OP_PLUS, "+"));
                    add(new Token(Token.Type.IDENTIFIER, "param2"));
                    add(new Token(Token.Type.SEMICOLON, ";"));
                    add(new Token(Token.Type.KW_RETURN, "return"));
                    add(new Token(Token.Type.IDENTIFIER, "b"));
                    add(new Token(Token.Type.OP_PLUS, "+"));
                    add(new Token(Token.Type.IDENTIFIER, "param2"));
                    add(new Token(Token.Type.SEMICOLON, ";"));
                    add(new Token(Token.Type.CLOSE_CURLY_BRACKET, "}"));
                    // func call
                    add(new Token(Token.Type.T_INT64, "int64"));
                    add(new Token(Token.Type.IDENTIFIER, "result"));
                    add(new Token(Token.Type.OP_ASSIGN, "="));
                    add(new Token(Token.Type.IDENTIFIER, "x"));
                    add(new Token(Token.Type.OPEN_PAREN, "("));
                    add(new Token(Token.Type.LIT_NUMBER, "1"));
                    add(new Token(Token.Type.COMMA, ","));
                    add(new Token(Token.Type.LIT_NUMBER, "2"));
                    add(new Token(Token.Type.OP_PLUS, "+"));
                    add(new Token(Token.Type.LIT_NUMBER, "5"));
                    add(new Token(Token.Type.CLOSE_PAREN, ")"));
                    add(new Token(Token.Type.SEMICOLON, ";"));
                }};
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);
                CompareAST(expectedAst, ast);
            }
            // using tokenizer
            {
                Tokenizer tokenizer = new Tokenizer();
                ArrayList<Token> tokens = tokenizer.tokenize("int64 x(int16 param1, int32 param2)" + 
                                                                " { int32 b = param1 + param2; return b + param2; }" +
                                                                "int64 result = x(1, 2 + 5);");
                Parser parser = new Parser();
                AST ast = parser.parse(tokens);
                Assertions.assertNotNull(ast);
                CompareAST(expectedAst, ast);
            }
        }
    }

    @Test public void testBranching() {
        {
            String testStr = "if (15 < 76) { int64 x = 1; }";
            testStr += "if (false) print(5); else { print(6);}";
            BlockNode expectedAst = new BlockNode();
            expectedAst.statements.add(new ConditionalNode() {{
                condition = new BinaryOpNode() {{
                    operator = new Token(Token.Type.OP_LESS, "<");
                    lhs = new LiteralNode() {{
                        literal = new Token(Token.Type.LIT_NUMBER, "15");
                    }};
                    rhs = new LiteralNode() {{
                        literal = new Token(Token.Type.LIT_NUMBER, "76");
                    }};
                }};
                trueBody = new BlockNode() {{
                    statements.add(new AssignmentNode() {{
                        lhs = new DeclarationNode() {{
                            type = new Token(Token.Type.T_INT64, "int64");
                            identifier = new Token(Token.Type.IDENTIFIER, "x");
                        }};
                        rhs = new LiteralNode() {{
                            literal = new Token(Token.Type.LIT_NUMBER, "1");
                        }};
                    }});
                }};
            }});
            expectedAst.statements.add(new ConditionalNode() {{
                condition = new LiteralNode() {{
                    literal = new Token(Token.Type.KW_FALSE, "false");
                }};
                trueBody = new FunctionCallNode() {{
                    funcName = new IdentifierNode() {{
                        identifier = new Token(Token.Type.IDENTIFIER, "print");
                    }};
                    params = new CallParamsNode() {{
                        params.add(new LiteralNode() {{
                            literal = new Token(Token.Type.LIT_NUMBER, "5");
                        }});
                    }};
                }};
                falseBody = new BlockNode() {{
                    statements.add(new FunctionCallNode() {{
                        funcName = new IdentifierNode() {{
                            identifier = new Token(Token.Type.IDENTIFIER, "print");
                        }};
                        params = new CallParamsNode() {{
                            params.add(new LiteralNode() {{
                                literal = new Token(Token.Type.LIT_NUMBER, "6");
                            }});
                        }};
                    }});
                }};
            }});

            Tokenizer tokenizer = new Tokenizer();
            ArrayList<Token> tokens = tokenizer.tokenize(testStr);
            Parser parser = new Parser();
            AST ast = parser.parse(tokens);
            Assertions.assertNotNull(ast);
            CompareAST(expectedAst, ast);
        }
    }
    @Test public void testLoops() {
        {
            String testStr = "while (15 < 76) { int64 x = 1; break; continue; }";
            BlockNode expectedAst = new BlockNode();
            expectedAst.statements.add(new WhileLoopNode() {{
                condition = new BinaryOpNode() {{
                    operator = new Token(Token.Type.OP_LESS, "<");
                    lhs = new LiteralNode() {{
                        literal = new Token(Token.Type.LIT_NUMBER, "15");
                    }};
                    rhs = new LiteralNode() {{
                        literal = new Token(Token.Type.LIT_NUMBER, "76");
                    }};
                }};
                body = new BlockNode() {{
                    statements.add(new AssignmentNode() {{
                        lhs = new DeclarationNode() {{
                            type = new Token(Token.Type.T_INT64, "int64");
                            identifier = new Token(Token.Type.IDENTIFIER, "x");
                        }};
                        rhs = new LiteralNode() {{
                            literal = new Token(Token.Type.LIT_NUMBER, "1");
                        }};
                    }});
                    statements.add(new BreakNode());
                    statements.add(new ContinueNode());
                }};
            }});

            Tokenizer tokenizer = new Tokenizer();
            ArrayList<Token> tokens = tokenizer.tokenize(testStr);
            Parser parser = new Parser();
            AST ast = parser.parse(tokens);
            Assertions.assertNotNull(ast);
            CompareAST(expectedAst, ast);
        }
        {
            String testStr = "for (int32 x = 0; x < 5; x = x + 1) {}";
            BlockNode expectedAst = new BlockNode();
            expectedAst.statements.add(new ForLoopNode() {{
                init = new AssignmentNode() {{
                    lhs = new DeclarationNode() {{
                        type = new Token(Token.Type.T_INT32, "int32");
                        identifier = new Token(Token.Type.IDENTIFIER, "x");
                    }};
                    rhs = new LiteralNode() {{
                        literal = new Token(Token.Type.LIT_NUMBER, "0");
                    }};
                }};
                condition = new BinaryOpNode() {{
                    operator = new Token(Token.Type.OP_LESS, "<");
                    lhs = new IdentifierNode() {{
                        identifier = new Token(Token.Type.IDENTIFIER, "x");
                    }};
                    rhs = new LiteralNode() {{
                        literal = new Token(Token.Type.LIT_NUMBER, "5");
                    }};
                }};
                increment = new AssignmentNode() {{
                    lhs = new IdentifierNode() {{
                        identifier = new Token(Token.Type.IDENTIFIER, "x");
                    }};
                    rhs = new BinaryOpNode() {{
                        operator = new Token(Token.Type.OP_PLUS, "+");
                        lhs = new IdentifierNode() {{
                            identifier = new Token(Token.Type.IDENTIFIER, "x");
                        }};
                        rhs = new LiteralNode() {{
                            literal = new Token(Token.Type.LIT_NUMBER, "1");
                        }};
                    }};
                }};
                body = new BlockNode() {{}};
            }});

            Tokenizer tokenizer = new Tokenizer();
            ArrayList<Token> tokens = tokenizer.tokenize(testStr);
            Parser parser = new Parser();
            AST ast = parser.parse(tokens);
            Assertions.assertNotNull(ast);
            CompareAST(expectedAst, ast);
        }
        {
            String testStr = "int32 x = 5; for (x = 0; 5 > 2;) {}";
            BlockNode expectedAst = new BlockNode();
            expectedAst.statements.add(new AssignmentNode() {{
                lhs = new DeclarationNode() {{
                    type = new Token(Token.Type.T_INT32, "int32");
                    identifier = new Token(Token.Type.IDENTIFIER, "x");
                }};
                rhs = new LiteralNode() {{
                    literal = new Token(Token.Type.LIT_NUMBER, "5");
                }};
            }});
            expectedAst.statements.add(new ForLoopNode() {{
                init = new AssignmentNode() {{
                    lhs = new IdentifierNode() {{
                        identifier = new Token(Token.Type.IDENTIFIER, "x");
                    }};
                    rhs = new LiteralNode() {{
                        literal = new Token(Token.Type.LIT_NUMBER, "0");
                    }};
                }};
                condition = new BinaryOpNode() {{
                    operator = new Token(Token.Type.OP_GREATER, ">");
                    lhs = new LiteralNode() {{
                        literal = new Token(Token.Type.LIT_NUMBER, "5");
                    }};
                    rhs = new LiteralNode() {{
                        literal = new Token(Token.Type.LIT_NUMBER, "2");
                    }};
                }};
                increment = null;
                body = new BlockNode() {{}};
            }});

            Tokenizer tokenizer = new Tokenizer();
            ArrayList<Token> tokens = tokenizer.tokenize(testStr);
            Parser parser = new Parser();
            AST ast = parser.parse(tokens);
            Assertions.assertNotNull(ast);
            CompareAST(expectedAst, ast);
        }
        {
            String testStr = "for (;;) {}";
            BlockNode expectedAst = new BlockNode();
            expectedAst.statements.add(new ForLoopNode() {{
                init = null;
                condition = null;
                increment = null;
                body = new BlockNode() {{}};
            }});

            Tokenizer tokenizer = new Tokenizer();
            ArrayList<Token> tokens = tokenizer.tokenize(testStr);
            Parser parser = new Parser();
            AST ast = parser.parse(tokens);
            Assertions.assertNotNull(ast);
            CompareAST(expectedAst, ast);
        }
    }

    private void CompareAST(AST expected, AST actual) {
        if (expected instanceof BlockNode) {
            Assertions.assertEquals(BlockNode.class, actual.getClass());
            BlockNode expectedBlock = (BlockNode) expected;
            BlockNode actualBlock = (BlockNode) actual;
            Assertions.assertEquals(expectedBlock.statements.size(), actualBlock.statements.size());
            for (int i = 0; i < expectedBlock.statements.size(); i++) {
                CompareAST(expectedBlock.statements.get(i), actualBlock.statements.get(i));
            }
        } else if (expected instanceof AssignmentNode) {
            Assertions.assertEquals(AssignmentNode.class, actual.getClass());
            AssignmentNode expectedAssignment = (AssignmentNode) expected;
            AssignmentNode actualAssignment = (AssignmentNode) actual;
            CompareAST(expectedAssignment.lhs, actualAssignment.lhs);
            CompareAST(expectedAssignment.rhs, actualAssignment.rhs);
        } else if (expected instanceof DeclarationNode) {
            Assertions.assertEquals(DeclarationNode.class, actual.getClass());
            DeclarationNode expectedDeclaration = (DeclarationNode) expected;
            DeclarationNode actualDeclaration = (DeclarationNode) actual;
            Assertions.assertEquals(expectedDeclaration.type.type, actualDeclaration.type.type);
            Assertions.assertEquals(expectedDeclaration.type.textRepresentation, actualDeclaration.type.textRepresentation);
            Assertions.assertEquals(expectedDeclaration.identifier.type, actualDeclaration.identifier.type);
            Assertions.assertEquals(expectedDeclaration.identifier.textRepresentation, actualDeclaration.identifier.textRepresentation);
        } else if (expected instanceof LiteralNode) {
            Assertions.assertEquals(LiteralNode.class, actual.getClass());
            LiteralNode expectedLiteral = (LiteralNode) expected;
            LiteralNode actualLiteral = (LiteralNode) actual;
            Assertions.assertEquals(expectedLiteral.literal.type, actualLiteral.literal.type);
            Assertions.assertEquals(expectedLiteral.literal.textRepresentation, actualLiteral.literal.textRepresentation);
        } else if (expected instanceof IdentifierNode) {
            Assertions.assertEquals(IdentifierNode.class, actual.getClass());
            IdentifierNode expectedIdentifier = (IdentifierNode) expected;
            IdentifierNode actualIdentifier = (IdentifierNode) actual;
            Assertions.assertEquals(expectedIdentifier.identifier.type, actualIdentifier.identifier.type);
            Assertions.assertEquals(expectedIdentifier.identifier.textRepresentation, actualIdentifier.identifier.textRepresentation);
        } else if (expected instanceof BinaryOpNode) {
            Assertions.assertEquals(BinaryOpNode.class, actual.getClass());
            BinaryOpNode expectedBinaryOp = (BinaryOpNode) expected;
            BinaryOpNode actualBinaryOp = (BinaryOpNode) actual;
            Assertions.assertEquals(expectedBinaryOp.operator.type, actualBinaryOp.operator.type);
            Assertions.assertEquals(expectedBinaryOp.operator.textRepresentation, actualBinaryOp.operator.textRepresentation);
            CompareAST(expectedBinaryOp.lhs, actualBinaryOp.lhs);
            CompareAST(expectedBinaryOp.rhs, actualBinaryOp.rhs);
        } else if (expected instanceof ParamDeclarationNode) {
            Assertions.assertEquals(ParamDeclarationNode.class, actual.getClass());
            ParamDeclarationNode expectedParamDeclaration = (ParamDeclarationNode) expected;
            ParamDeclarationNode actualParamDeclaration = (ParamDeclarationNode) actual;
            Assertions.assertEquals(expectedParamDeclaration.declarations.size(), actualParamDeclaration.declarations.size());
            for (int i = 0; i < expectedParamDeclaration.declarations.size(); i++) {
                CompareAST(expectedParamDeclaration.declarations.get(i), actualParamDeclaration.declarations.get(i));
            }
        } else if (expected instanceof FunctionDefNode) {
            Assertions.assertEquals(FunctionDefNode.class, actual.getClass());
            FunctionDefNode expectedFunctionDef = (FunctionDefNode) expected;
            FunctionDefNode actualFunctionDef = (FunctionDefNode) actual;
            CompareAST(expectedFunctionDef.declaration, actualFunctionDef.declaration);
            CompareAST(expectedFunctionDef.paramDeclaration, actualFunctionDef.paramDeclaration);
            CompareAST(expectedFunctionDef.block, actualFunctionDef.block);
        } else if (expected instanceof ReturnNode) {
            Assertions.assertEquals(ReturnNode.class, actual.getClass());
            ReturnNode expectedReturn = (ReturnNode) expected;
            ReturnNode actualReturn = (ReturnNode) actual;
            CompareAST(expectedReturn.retVal, actualReturn.retVal);
        } else if (expected instanceof CallParamsNode) {
            Assertions.assertEquals(CallParamsNode.class, actual.getClass());
            CallParamsNode expectedCallParams = (CallParamsNode) expected;
            CallParamsNode actualCallParams = (CallParamsNode) actual;
            Assertions.assertEquals(expectedCallParams.params.size(), actualCallParams.params.size());
            for (int i = 0; i < expectedCallParams.params.size(); i++) {
                CompareAST(expectedCallParams.params.get(i), actualCallParams.params.get(i));
            }
        } else if (expected instanceof FunctionCallNode) {
            Assertions.assertEquals(FunctionCallNode.class, actual.getClass());
            FunctionCallNode expectedFunctionCall = (FunctionCallNode) expected;
            FunctionCallNode actualFunctionCall = (FunctionCallNode) actual;
            CompareAST(expectedFunctionCall.funcName, actualFunctionCall.funcName);
            CompareAST(expectedFunctionCall.params, actualFunctionCall.params);
        } else if (expected instanceof ConditionalNode) {
            Assertions.assertEquals(ConditionalNode.class, actual.getClass());
            ConditionalNode expectedConditional = (ConditionalNode) expected;
            ConditionalNode actualConditional = (ConditionalNode) actual;
            CompareAST(expectedConditional.condition, actualConditional.condition);
            CompareAST(expectedConditional.trueBody, actualConditional.trueBody);
            Assertions.assertEquals(expectedConditional.falseBody == null, actualConditional.falseBody == null);
            if (expectedConditional.falseBody != null) CompareAST(expectedConditional.falseBody, actualConditional.falseBody);
        } else if (expected instanceof WhileLoopNode) {
            Assertions.assertEquals(WhileLoopNode.class, actual.getClass());
            WhileLoopNode expectedWhileLoop = (WhileLoopNode) expected;
            WhileLoopNode actualWhileLoop = (WhileLoopNode) actual;
            CompareAST(expectedWhileLoop.condition, actualWhileLoop.condition);
            CompareAST(expectedWhileLoop.body, actualWhileLoop.body);
        } else if (expected instanceof ForLoopNode) {
            Assertions.assertEquals(ForLoopNode.class, actual.getClass());
            ForLoopNode expectedForLoop = (ForLoopNode) expected;
            ForLoopNode actualForLoop = (ForLoopNode) actual;
            Assertions.assertEquals(expectedForLoop.init == null, actualForLoop.init == null);
            Assertions.assertEquals(expectedForLoop.condition == null, actualForLoop.condition == null);
            Assertions.assertEquals(expectedForLoop.increment == null, actualForLoop.increment == null);
            if (expectedForLoop.init != null) CompareAST(expectedForLoop.init, actualForLoop.init);
            if (expectedForLoop.condition != null) CompareAST(expectedForLoop.condition, actualForLoop.condition);
            if (expectedForLoop.increment != null) CompareAST(expectedForLoop.increment, actualForLoop.increment);
            CompareAST(expectedForLoop.body, actualForLoop.body);
        } else if (expected instanceof BreakNode) {
            Assertions.assertEquals(BreakNode.class, actual.getClass());
        } else if (expected instanceof BreakNode) {
            Assertions.assertEquals(BreakNode.class, actual.getClass());
        } else if (expected instanceof ContinueNode) {
            Assertions.assertEquals(ContinueNode.class, actual.getClass());
        } else {
            Assertions.fail("Unknown AST type " + expected.getClass().getName());
        }
    }
}