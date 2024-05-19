package com.javasl.compiler;

import java.util.ArrayList;

import com.javasl.compiler.ast.*;

public class Parser {
    public AST parse(ArrayList<Token> tokens) throws IllegalArgumentException {
        if (tokens == null) {
            throw new IllegalArgumentException("Parser error: tokens is null.");
        }

        // make all tokens in a block
        Token open = new Token(Token.Type.OPEN_CURLY_BRACKET, "{");
        Token close = new Token(Token.Type.CLOSE_CURLY_BRACKET, "}");
        tokens.add(0, open);
        tokens.add(close);

        m_tokens = tokens;
        m_index = 0;

        try {
            return parseBlock();
        } catch (Exception e) {
            throw new IllegalArgumentException("Parser error: " + e.getMessage());
        }
    }

    private Token peek(int amount) {
        if (m_index + amount >= m_tokens.size()) {
            return null;
        }
        return m_tokens.get(m_index + amount);
    }
    private Token next() {
        if (m_index >= m_tokens.size()) {
            return null;
        }
        return m_tokens.get(m_index++);
    }
    private void restoreBackup(int backup) {
        m_index = backup;
    }
    private int backup() {
        return m_index;
    }
    private void consumeSemicolon() {
        Token next = next();
        if (next == null || next.type != Token.Type.SEMICOLON) {
            throw new RuntimeException("expected semicolon.");
        }
    }

    private AST parseBlock() {
        int backup = backup();
        Token open = next();
        if (open == null || open.type != Token.Type.OPEN_CURLY_BRACKET) {
            restoreBackup(backup);
            return null;
        }
        BlockNode node = new BlockNode();
        while (true) {
            AST statement = parseStatement();
            if (statement == null) {
                break;
            }
            node.statements.add(statement);
        }
        Token close = next();
        if (close == null || close.type != Token.Type.CLOSE_CURLY_BRACKET) {
            throw new RuntimeException("expected '}' after block.");
        }
        return node;
    }
    private AST parseStatement() {
        int backup = backup();
        // comment
        Token comment = peek(0);
        while (comment != null && comment.type == Token.Type.COMMENT) {
            comment = next();
            backup = backup();
        }

        // block
        restoreBackup(backup);
        AST node = parseBlock();
        if (node != null) {
            return node;
        }
        // assignment
        restoreBackup(backup);
        node = parseAssignment();
        if (node != null) {
            consumeSemicolon();
            return node;
        }
        restoreBackup(backup);
        return null;
    }
    private AST parseAssignment() {
        int backup = backup();
        AST lhs = parseLHS();
        if (lhs == null) {
            restoreBackup(backup);
            return null;
        }
        Token op = next();
        if (op == null || op.type != Token.Type.OP_ASSIGN) {
            restoreBackup(backup);
            throw new RuntimeException("expected assignment operator.");
        }
        AST rhs = parseRHS();
        if (rhs == null) {
            restoreBackup(backup);
            throw new RuntimeException("expected rhs of assignment.");
        }
        AssignmentNode node = new AssignmentNode();
        node.lhs = lhs;
        node.rhs = rhs;
        return node;
    }
    private AST parseLHS() {
        int backup = backup();
        Token token = next();
        if (token == null) {
            restoreBackup(backup);
            return null;
        }
        Token token2 = peek(0);
        if (token.type == Token.Type.IDENTIFIER) { // token could be a name or user type at this point
            if (token2 != null && token2.type == Token.Type.IDENTIFIER) { // token is type, token2 is name
                DeclarationNode node = new DeclarationNode();
                node.type = token;
                node.identifier = token2;
                next(); // consume token2
                return node;
            } else { // token is name
                IdentifierNode node = new IdentifierNode();
                node.identifier = token;
                return node;
            }
        } else if (token.getTypeGroup() == Token.TypeGroup.TYPE) { // token is a type
            if (token2 == null || token2.type != Token.Type.IDENTIFIER) {
                throw new RuntimeException("expected identifier after type.");
            } else { // token2 is a name
                DeclarationNode node = new DeclarationNode();
                node.type = token;
                node.identifier = token2;
                next(); // consume token2
                return node;
            }
        }
        restoreBackup(backup);
        return null;
    }
    private AST parseRHS() {
        int backup = backup();
        AST expression = parseExpression();
        if (expression == null) {
            restoreBackup(backup);
            return null;
        }
        return expression;
    }
    private AST parseExpression() {
        int backup = backup();
        AST term = parseTerm();
        if (term == null) {
            restoreBackup(backup);
            return null;
        }
        while (true) {
            Token op = peek(0);
            if (op != null && (op.type == Token.Type.OP_PLUS || op.type == Token.Type.OP_MINUS)) {
                next(); // consume operator
                AST term2 = parseTerm();
                if (term2 == null) {
                    throw new RuntimeException("expected term after operator.");
                }
                BinaryOpNode node = new BinaryOpNode();
                node.lhs = term;
                node.rhs = term2;
                node.operator = op;
                term = node;
            } else {
                break;
            }
        }

        return term;
    }
    private AST parseTerm() {
        int backup = backup();
        AST factor = parseFactor();
        if (factor == null) {
            restoreBackup(backup);
            return null;
        }
        while (true) {
            Token op = peek(0);
            if (op != null && (op.type == Token.Type.OP_MULTIPLY || op.type == Token.Type.OP_DIVIDE ||
                op.type == Token.Type.OP_MODULO)) {
                next(); // consume operator
                AST factor2 = parseFactor();
                if (factor2 == null) {
                    throw new RuntimeException("expected factor after operator.");
                }
                BinaryOpNode node = new BinaryOpNode();
                node.lhs = factor;
                node.rhs = factor2;
                node.operator = op;
                factor = node;
            } else {
                break;
            }
        }
        return factor;
    }
    private AST parseFactor() {
        int backup = backup();
        Token token = next();
        if (token == null) {
            throw new RuntimeException("Unexpected EOF.");
        }

        // TODO: function calls

        // literal number with prefix -
        if (token.type == Token.Type.OP_MINUS) {
            Token next = peek(0);
            if (next != null && next.type == Token.Type.LIT_NUMBER) {
                next(); // consume next
                next.textRepresentation = "-" + next.textRepresentation;
                LiteralNode node = new LiteralNode();
                node.literal = next;
                return node;
            }
            throw new RuntimeException("Unexpected '-'.");
        }

        // literal
        if (token.getTypeGroup() == Token.TypeGroup.LITERAL) {
            LiteralNode node = new LiteralNode();
            node.literal = token;
            return node;
        }
        // identifier
        if (token.type == Token.Type.IDENTIFIER) {
            IdentifierNode node = new IdentifierNode();
            node.identifier = token;
            return node;
        }

        // "(" expression ")"
        if (token.type == Token.Type.OPEN_PAREN) {
            AST expression = parseExpression();
            if (expression == null) {
                throw new RuntimeException("expected expression after '('.");
            }
            Token closeParen = next();
            if (closeParen == null || closeParen.type != Token.Type.CLOSE_PAREN) {
                throw new RuntimeException("expected ')' after expression.");
            }
            return expression;
        }

        restoreBackup(backup);
        return null;
    }

    private ArrayList<Token> m_tokens;
    private int m_index;
}
