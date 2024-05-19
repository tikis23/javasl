package com.javasl.compiler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import com.javasl.compiler.ast.*;
import com.javasl.runtime.Statement;
import com.javasl.runtime.Variable;
import com.javasl.runtime.types.SignedInt;
import com.javasl.runtime.types.Type;
import com.javasl.runtime.types.UnsignedInt;

public class Compiler {
    public ArrayList<Statement> compile(AST ast) throws IllegalArgumentException {

        try {
            return dispatchToCompile(ast);
        } catch (Exception e) {
            throw new IllegalArgumentException("Compiler error: " + e.getMessage());
        }
    }
    private ArrayList<Statement> dispatchToCompile(AST ast) throws IllegalArgumentException {
        if      (ast instanceof BlockNode) return compileBlockNode((BlockNode) ast);
        else if (ast instanceof BinaryOpNode) return compileBinaryOpNode((BinaryOpNode) ast);
        else if (ast instanceof LiteralNode) return compileLiteralNode((LiteralNode) ast);
        else if (ast instanceof AssignmentNode) return compileAssignmentNode((AssignmentNode) ast);
        else if (ast instanceof DeclarationNode) return compileDeclarationNode((DeclarationNode) ast);

        else throw new IllegalArgumentException("Unknown AST type");
    }

    private ArrayList<Statement> compileBlockNode(BlockNode ast) {
        startScope();
        ArrayList<Statement> ret = new ArrayList<>();
        for (AST statement : ast.statements) {
            ret.addAll(dispatchToCompile(statement));
        }
        ret.addAll(endScope());
        return ret;
    }
    private ArrayList<Statement> compileAssignmentNode(AssignmentNode ast) {
        ArrayList<Statement> ret = new ArrayList<>();


        // compile rhs of assignment
        ret.addAll(dispatchToCompile(ast.rhs));
        int rhsIndex = m_variableStack.size() - 1;
        Type rhsType = m_variableStack.get(rhsIndex).value.getTypeInstance();

        // lhs
        int lhsIndex = -1;
        if (ast.lhs instanceof IdentifierNode) {
            IdentifierNode node = (IdentifierNode) ast.lhs;
            // check if variable exists
            Variable var = new Variable(node.identifier.textRepresentation, null);
            if (!doesVariableExist(var)) {
                throw new IllegalArgumentException("Variable does not exist in scope: " + var.name);
            }
            lhsIndex = getVariableStackLocation(var);
        } else {
            ret.addAll(dispatchToCompile(ast.lhs));
            lhsIndex = m_variableStack.size() - 1;
        }
        if (lhsIndex == -1) {
            throw new IllegalArgumentException("Failed to compile lhs of assignment.");
        }
        Type lhsType = m_variableStack.get(lhsIndex).value.getTypeInstance();
        
        checkIfTypesAreCompatible(lhsType, rhsType, Token.Type.OP_ASSIGN);

        // assign value to variable
        ret.add(Statement.assignVariable(lhsIndex, rhsIndex));

        return ret;
    }
    private ArrayList<Statement> compileDeclarationNode(DeclarationNode ast) {
        ArrayList<Statement> ret = new ArrayList<>();

        // create variable
        Variable var = new Variable(ast.identifier.textRepresentation, Type.fromToken(ast.type));

        // declare variable
        if (doesVariableExist(var)) {
            throw new IllegalArgumentException("Variable already exists in scope: " + var.name);
        }
        ret.add(Statement.declareVariable(var));

        // add variable to scope
        addVariableToScope(var);

        return ret;
    }
    private ArrayList<Statement> compileBinaryOpNode(BinaryOpNode ast) {
        ArrayList<Statement> ret = new ArrayList<>();

        // lhs
        int lhsIndex = -1;
        if (ast.lhs instanceof IdentifierNode) {
            IdentifierNode node = (IdentifierNode) ast.lhs;
            // check if variable exists
            Variable var = new Variable(node.identifier.textRepresentation, null);
            if (!doesVariableExist(var)) {
                throw new IllegalArgumentException("Variable does not exist in scope: " + var.name);
            }
            lhsIndex = getVariableStackLocation(var);
        } else {
            ret.addAll(dispatchToCompile(ast.lhs));
            lhsIndex = m_variableStack.size() - 1;
        }
        if (lhsIndex == -1) {
            throw new IllegalArgumentException("Failed to compile lhs of binary operation.");
        }
        Type lhsType = m_variableStack.get(lhsIndex).value.getTypeInstance();

        // rhs
        int rhsIndex = -1;
        if (ast.rhs instanceof IdentifierNode) {
            IdentifierNode node = (IdentifierNode) ast.rhs;
            // check if variable exists
            Variable var = new Variable(node.identifier.textRepresentation, null);
            if (!doesVariableExist(var)) {
                throw new IllegalArgumentException("Variable does not exist in scope: " + var.name);
            }
            rhsIndex = getVariableStackLocation(var);
        } else {
            ret.addAll(dispatchToCompile(ast.rhs));
            rhsIndex = m_variableStack.size() - 1;
        }
        if (rhsIndex == -1) {
            throw new IllegalArgumentException("Failed to compile rhs of binary operation.");
        }
        Type rhsType = m_variableStack.get(rhsIndex).value.getTypeInstance();

        // check if lhs and rhs are compatible types
        checkIfTypesAreCompatible(lhsType, rhsType, ast.operator.type);

        // create temp variable on the stack to store result
        Variable var = new TempVariable(lhsType);
        addVariableToScope(var);
        ret.add(Statement.declareVariable(var));
        int resultIndex = m_variableStack.size() - 1;
        
        // perform binary operation
        ret.add(Statement.binaryOp(ast.operator.type, resultIndex, lhsIndex, rhsIndex));

        return ret;
    }
    private ArrayList<Statement> compileLiteralNode(LiteralNode ast) {
        ArrayList<Statement> ret = new ArrayList<>();
        // create temp variable on the stack to store literal
        Variable var = new TempVariable(Type.fromToken(ast.literal));
        var.value.assignValueFromString(ast.literal.textRepresentation);
        addVariableToScope(var);
        // declare variable with value
        ret.add(Statement.declareVariable(var));
        return ret;
    }

    private void checkIfTypesAreCompatible(Type lhs, Type rhs, Token.Type op) {
        if ((lhs instanceof UnsignedInt || lhs instanceof SignedInt) &&
            (rhs instanceof UnsignedInt || rhs instanceof SignedInt)) {
            return;
        }

        throw new IllegalArgumentException("Incompatible types for operation: " + lhs + " " + op + " " + rhs);
    }

    private boolean doesVariableExist(Variable var) {
        for (Variable v : m_variableStack) {
            if (v.equals(var)) {
                return true;
            }
        }
        return false;
    }
    private int getVariableStackLocation(Variable var) {
        for (int i = 0; i < m_variableStack.size(); i++) {
            if (m_variableStack.get(i).equals(var)) {
                return i;
            }
        }
        return -1;
    }
    private void addVariableToScope(Variable variable) {
        m_scopeStack.peek().variables.add(variable);
        m_variableStack.push(variable);
    }
    private void startScope() {
        m_scopeStack.push(new Scope());
    }
    private ArrayList<Statement> endScope() {
        ArrayList<Statement> ret = new ArrayList<>();
        Scope scope = m_scopeStack.pop();
        for (int i = 0; i < scope.variables.size(); i++) {
            m_variableStack.pop();
        }
        ret.add(Statement.clearStack(scope.variables.size()));
        return ret;
    }

    private class TempVariable extends Variable {
        public TempVariable(Type type) {
            super(m_tempVariableCounter++ + "_COMPILER_TEMP", type);
        }
    }
    private long m_tempVariableCounter = 0;
    private static class Scope {
        public HashSet<Variable> variables = new HashSet<>();
    }
    private Stack<Scope> m_scopeStack = new Stack<>();
    private Stack<Variable> m_variableStack = new Stack<>();
}
