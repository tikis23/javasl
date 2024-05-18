package com.javasl.compiler;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class TokenizerTest {
    @Test public void testTypes() {
        {
            Tokenizer tokenizer = new Tokenizer();
            ArrayList<Token> tokens = tokenizer.tokenize("void int8 int16 int32 int64 uint8 uint16 uint32 uint64 float double bool string char");
            ArrayList<Token> expectedTokens = new ArrayList<Token>() {{
                add(new Token(Token.Type.T_VOID, "void"));
                add(new Token(Token.Type.T_INT8, "int8"));
                add(new Token(Token.Type.T_INT16, "int16"));
                add(new Token(Token.Type.T_INT32, "int32"));
                add(new Token(Token.Type.T_INT64, "int64"));
                add(new Token(Token.Type.T_UINT8, "uint8"));
                add(new Token(Token.Type.T_UINT16, "uint16"));
                add(new Token(Token.Type.T_UINT32, "uint32"));
                add(new Token(Token.Type.T_UINT64, "uint64"));
                add(new Token(Token.Type.T_FLOAT, "float"));
                add(new Token(Token.Type.T_DOUBLE, "double"));
                add(new Token(Token.Type.T_BOOL, "bool"));
                add(new Token(Token.Type.T_STRING, "string"));
                add(new Token(Token.Type.T_CHAR, "char"));
            }};
            Assertions.assertEquals(expectedTokens.size(), tokens.size());
            for (int i = 0; i < tokens.size(); i++) {
                Token expectedToken = expectedTokens.get(i);
                Token token = tokens.get(i);
                Assertions.assertEquals(expectedToken.type, token.type);
                Assertions.assertEquals(expectedToken.textRepresentation, token.textRepresentation);
            }
        }
        {
            Tokenizer tokenizer = new Tokenizer();
            ArrayList<Token> tokens = tokenizer.tokenize("void5 int32 String CHAR int161");
            Assertions.assertEquals(5, tokens.size());
            Assertions.assertNotEquals(tokens.get(0).type, Token.Type.T_VOID);
            Assertions.assertNotEquals(tokens.get(0).textRepresentation, "void");
            Assertions.assertEquals(tokens.get(1).type, Token.Type.T_INT32);
            Assertions.assertEquals(tokens.get(1).textRepresentation, "int32");
            Assertions.assertNotEquals(tokens.get(2).type, Token.Type.T_STRING);
            Assertions.assertNotEquals(tokens.get(2).textRepresentation, "string");
            Assertions.assertNotEquals(tokens.get(3).type, Token.Type.T_CHAR);
            Assertions.assertNotEquals(tokens.get(3).textRepresentation, "char");
            Assertions.assertNotEquals(tokens.get(4).type, Token.Type.T_INT16);
            Assertions.assertNotEquals(tokens.get(4).textRepresentation, "int16");
        }
    }
    @Test public void testKeywords() {
        {
            Tokenizer tokenizer = new Tokenizer();
            ArrayList<Token> tokens = tokenizer.tokenize("if else do while for break continue return true false null");
            ArrayList<Token> expectedTokens = new ArrayList<Token>() {{
                add(new Token(Token.Type.KW_IF, "if"));
                add(new Token(Token.Type.KW_ELSE, "else"));
                add(new Token(Token.Type.KW_DO, "do"));
                add(new Token(Token.Type.KW_WHILE, "while"));
                add(new Token(Token.Type.KW_FOR, "for"));
                add(new Token(Token.Type.KW_BREAK, "break"));
                add(new Token(Token.Type.KW_CONTINUE, "continue"));
                add(new Token(Token.Type.KW_RETURN, "return"));
                add(new Token(Token.Type.KW_TRUE, "true"));
                add(new Token(Token.Type.KW_FALSE, "false"));
                add(new Token(Token.Type.KW_NULL, "null"));
            }};
            Assertions.assertEquals(expectedTokens.size(), tokens.size());
            for (int i = 0; i < tokens.size(); i++) {
                Token expectedToken = expectedTokens.get(i);
                Token token = tokens.get(i);
                Assertions.assertEquals(expectedToken.type, token.type);
                Assertions.assertEquals(expectedToken.textRepresentation, token.textRepresentation);
            }
        }
        {
            Tokenizer tokenizer = new Tokenizer();
            ArrayList<Token> tokens = tokenizer.tokenize("if5 elsew while dodo _Do NULL");
            Assertions.assertEquals(6, tokens.size());
            Assertions.assertNotEquals(tokens.get(0).type, Token.Type.KW_IF);
            Assertions.assertNotEquals(tokens.get(0).textRepresentation, "if");
            Assertions.assertNotEquals(tokens.get(1).type, Token.Type.KW_ELSE);
            Assertions.assertNotEquals(tokens.get(1).textRepresentation, "else");
            Assertions.assertEquals(tokens.get(2).type, Token.Type.KW_WHILE);
            Assertions.assertEquals(tokens.get(2).textRepresentation, "while");
            Assertions.assertNotEquals(tokens.get(3).type, Token.Type.KW_DO);
            Assertions.assertNotEquals(tokens.get(3).textRepresentation, "do");
            Assertions.assertNotEquals(tokens.get(4).type, Token.Type.KW_DO);
            Assertions.assertNotEquals(tokens.get(4).textRepresentation, "do");
            Assertions.assertNotEquals(tokens.get(5).type, Token.Type.KW_NULL);
            Assertions.assertNotEquals(tokens.get(5).textRepresentation, "null");
        }
    }
    @Test public void testIdentifiers() {
        {
            Tokenizer tokenizer = new Tokenizer();
            ArrayList<Token> tokens = tokenizer.tokenize("bool test a_15 while _0if");
            ArrayList<Token> expectedTokens = new ArrayList<Token>() {{
                add(new Token(Token.Type.T_BOOL, "bool"));
                add(new Token(Token.Type.IDENTIFIER, "test"));
                add(new Token(Token.Type.IDENTIFIER, "a_15"));
                add(new Token(Token.Type.KW_WHILE, "while"));
                add(new Token(Token.Type.IDENTIFIER, "_0if"));
            }};
            Assertions.assertEquals(expectedTokens.size(), tokens.size());
            for (int i = 0; i < tokens.size(); i++) {
                Token expectedToken = expectedTokens.get(i);
                Token token = tokens.get(i);
                Assertions.assertEquals(expectedToken.type, token.type);
                Assertions.assertEquals(expectedToken.textRepresentation, token.textRepresentation);
            }
        }
        {
            Tokenizer tokenizer = new Tokenizer();
            ArrayList<Token> tokens = tokenizer.tokenize("void5 int32 String CHAR int161");
            Assertions.assertEquals(5, tokens.size());
            Assertions.assertNotEquals(tokens.get(0).type, Token.Type.T_VOID);
            Assertions.assertNotEquals(tokens.get(0).textRepresentation, "void");
            Assertions.assertEquals(tokens.get(1).type, Token.Type.T_INT32);
            Assertions.assertEquals(tokens.get(1).textRepresentation, "int32");
            Assertions.assertNotEquals(tokens.get(2).type, Token.Type.T_STRING);
            Assertions.assertNotEquals(tokens.get(2).textRepresentation, "string");
            Assertions.assertNotEquals(tokens.get(3).type, Token.Type.T_CHAR);
            Assertions.assertNotEquals(tokens.get(3).textRepresentation, "char");
            Assertions.assertNotEquals(tokens.get(4).type, Token.Type.T_INT16);
            Assertions.assertNotEquals(tokens.get(4).textRepresentation, "int16");
        }
    }
    @Test public void testOperators() {
        {
            Tokenizer tokenizer = new Tokenizer();
            ArrayList<Token> tokens = tokenizer.tokenize("! ~ + - * / % && || << >> & | ^ = == != < <= > >=");
            ArrayList<Token> expectedTokens = new ArrayList<Token>() {{
                add(new Token(Token.Type.OP_LOGIC_NOT, "!"));
                add(new Token(Token.Type.OP_BITWISE_NOT, "~"));
                add(new Token(Token.Type.OP_PLUS, "+"));
                add(new Token(Token.Type.OP_MINUS, "-"));
                add(new Token(Token.Type.OP_MULTIPLY, "*"));
                add(new Token(Token.Type.OP_DIVIDE, "/"));
                add(new Token(Token.Type.OP_MODULO, "%"));
                add(new Token(Token.Type.OP_LOGIC_AND, "&&"));
                add(new Token(Token.Type.OP_LOGIC_OR, "||"));
                add(new Token(Token.Type.OP_SHIFT_LEFT, "<<"));
                add(new Token(Token.Type.OP_SHIFT_RIGHT, ">>"));
                add(new Token(Token.Type.OP_BITWISE_AND, "&"));
                add(new Token(Token.Type.OP_BITWISE_OR, "|"));
                add(new Token(Token.Type.OP_BITWISE_XOR, "^"));
                add(new Token(Token.Type.OP_ASSIGN, "="));
                add(new Token(Token.Type.OP_EQUAL, "=="));
                add(new Token(Token.Type.OP_NOT_EQUAL, "!="));
                add(new Token(Token.Type.OP_LESS, "<"));
                add(new Token(Token.Type.OP_LESS_EQUAL, "<="));
                add(new Token(Token.Type.OP_GREATER, ">"));
                add(new Token(Token.Type.OP_GREATER_EQUAL, ">="));
            }};
            Assertions.assertEquals(expectedTokens.size(), tokens.size());
            for (int i = 0; i < tokens.size(); i++) {
                Token expectedToken = expectedTokens.get(i);
                Token token = tokens.get(i);
                Assertions.assertEquals(expectedToken.type, token.type);
                Assertions.assertEquals(expectedToken.textRepresentation, token.textRepresentation);
            }
        }
        {
            Tokenizer tokenizer = new Tokenizer();
            ArrayList<Token> tokens = tokenizer.tokenize("!! <> |&");
            ArrayList<Token> expectedTokens = new ArrayList<Token>() {{
                add(new Token(Token.Type.OP_LOGIC_NOT, "!"));
                add(new Token(Token.Type.OP_LOGIC_NOT, "!"));
                add(new Token(Token.Type.OP_LESS, "<"));
                add(new Token(Token.Type.OP_GREATER, ">"));
                add(new Token(Token.Type.OP_BITWISE_OR, "|"));
                add(new Token(Token.Type.OP_BITWISE_AND, "&"));
            }};
            Assertions.assertEquals(expectedTokens.size(), tokens.size());
            for (int i = 0; i < tokens.size(); i++) {
                Token expectedToken = expectedTokens.get(i);
                Token token = tokens.get(i);
                Assertions.assertEquals(expectedToken.type, token.type);
                Assertions.assertEquals(expectedToken.textRepresentation, token.textRepresentation);
            }
        }
    }
    @Test public void testCombined() {
        {
            Tokenizer tokenizer = new Tokenizer();
            ArrayList<Token> tokens = tokenizer.tokenize("void main() \n { int32 a = 5 << 9 * 8;\n     print(\"Hello, World!\\n\", a);\n}\n");
            ArrayList<Token> expectedTokens = new ArrayList<Token>() {{
                add(new Token(Token.Type.T_VOID, "void"));
                add(new Token(Token.Type.IDENTIFIER, "main"));
                add(new Token(Token.Type.OPEN_PAREN, "("));
                add(new Token(Token.Type.CLOSE_PAREN, ")"));
                add(new Token(Token.Type.OPEN_CURLY_BRACKET, "{"));
                add(new Token(Token.Type.T_INT32, "int32"));
                add(new Token(Token.Type.IDENTIFIER, "a"));
                add(new Token(Token.Type.OP_ASSIGN, "="));
                add(new Token(Token.Type.LIT_NUMBER, "5"));
                add(new Token(Token.Type.OP_SHIFT_LEFT, "<<"));
                add(new Token(Token.Type.LIT_NUMBER, "9"));
                add(new Token(Token.Type.OP_MULTIPLY, "*"));
                add(new Token(Token.Type.LIT_NUMBER, "8"));
                add(new Token(Token.Type.SEMICOLON, ";"));
                add(new Token(Token.Type.IDENTIFIER, "print"));
                add(new Token(Token.Type.OPEN_PAREN, "("));
                add(new Token(Token.Type.LIT_STRING, "Hello, World!\n"));
                add(new Token(Token.Type.COMMA, ","));
                add(new Token(Token.Type.IDENTIFIER, "a"));
                add(new Token(Token.Type.CLOSE_PAREN, ")"));
                add(new Token(Token.Type.SEMICOLON, ";"));
                add(new Token(Token.Type.CLOSE_CURLY_BRACKET, "}"));
            }};
            Assertions.assertEquals(expectedTokens.size(), tokens.size());
            for (int i = 0; i < tokens.size(); i++) {
                Token expectedToken = expectedTokens.get(i);
                Token token = tokens.get(i);
                Assertions.assertEquals(expectedToken.type, token.type);
                Assertions.assertEquals(expectedToken.textRepresentation, token.textRepresentation);
            }
        }
    }
}