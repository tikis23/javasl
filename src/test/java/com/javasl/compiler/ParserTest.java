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
        } else {
            Assertions.fail("Unknown AST type");
        }
    }
}