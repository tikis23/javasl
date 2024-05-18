package com.javasl.compiler.ast;

public abstract class AST {
    // for testing purposes
    public String toString(int depth) {
        String res = "";
        for (int i = 0; i < depth; i++) {
            res += '-';
        }
        if (this instanceof BlockNode) {
            BlockNode block = (BlockNode)this;
            res += "Block:\n";
            for (int i = 0; i < block.statements.size(); i++) {
                res += block.statements.get(i).toString(depth + 1);
            }
        } else if (this instanceof AssignmentNode) {
            AssignmentNode assignment = (AssignmentNode)this;
            res += "Assignment:\n";
            res += assignment.lhs.toString(depth + 1);
            res += assignment.rhs.toString(depth + 1);
        } else if (this instanceof DeclarationNode) {
            DeclarationNode declaration = (DeclarationNode) this;
            res += "Declaration: " + declaration.type.textRepresentation + " " + declaration.identifier.textRepresentation + "\n";
        } else if (this instanceof LiteralNode) {
            LiteralNode literal = (LiteralNode) this;
            res += "Literal: " + literal.literal.type.toString() + " " + literal.literal.textRepresentation + "\n";
        } else if (this instanceof IdentifierNode) {
            IdentifierNode identifier = (IdentifierNode) this;
            res += "Identifier: " + identifier.identifier.textRepresentation + "\n";
        } else if (this instanceof BinaryOpNode) {
            BinaryOpNode binaryOp = (BinaryOpNode) this;
            res += "BinaryOp: " + binaryOp.operator.textRepresentation + "\n";
            res += binaryOp.lhs.toString(depth + 1);
            res += binaryOp.rhs.toString(depth + 1);
        } else {
            res += ("Unknown");
        }
        return res;
    }
}