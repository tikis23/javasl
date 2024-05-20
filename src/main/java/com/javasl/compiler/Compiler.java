package com.javasl.compiler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import com.javasl.compiler.ast.*;
import com.javasl.runtime.FunctionVariable;
import com.javasl.runtime.Statement;
import com.javasl.runtime.Variable;
import com.javasl.runtime.types.*;
import com.javasl.runtime.types.Void;

public class Compiler {
    public ArrayList<Statement> compile(AST ast) throws IllegalArgumentException {

        try {
            return dispatchToCompile(ast);
        } catch (Exception e) {
            throw new IllegalArgumentException("Compiler error: " + e.getMessage(), e);
        }
    }
    private ArrayList<Statement> dispatchToCompile(AST ast) throws IllegalArgumentException {
        if      (ast instanceof BlockNode) return compileBlockNode((BlockNode) ast);
        else if (ast instanceof BinaryOpNode) return compileBinaryOpNode((BinaryOpNode) ast);
        else if (ast instanceof LiteralNode) return compileLiteralNode((LiteralNode) ast);
        else if (ast instanceof AssignmentNode) return compileAssignmentNode((AssignmentNode) ast);
        else if (ast instanceof DeclarationNode) return compileDeclarationNode((DeclarationNode) ast);
        else if (ast instanceof FunctionDefNode) return compileFunctionDefinitionNode((FunctionDefNode) ast);
        else if (ast instanceof ReturnNode) return compileReturnNode((ReturnNode) ast);
        else if (ast instanceof FunctionCallNode) return compileFunctionCallNode((FunctionCallNode) ast);

        else throw new IllegalArgumentException("Unknown AST type `" + ast.getClass().getName() + "`.");
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
    private ArrayList<Statement> compileFunctionCallNode(FunctionCallNode ast) {
        ArrayList<Statement> ret = new ArrayList<>();

        // check if function exists
        FunctionVariable funcVar = new FunctionVariable(ast.funcName.identifier.textRepresentation, null, null, 0);
        if (!doesVariableExist(funcVar)) {
            throw new IllegalArgumentException("Function does not exist in scope: " + funcVar.name);
        }
        int funcStackLoc = getVariableStackLocation(funcVar);
        Variable var = m_variableStack.get(funcStackLoc);
        if (!(var instanceof FunctionVariable)) {
            throw new IllegalArgumentException("Variable is not a function: " + var.name);
        }
        funcVar = (FunctionVariable) var;

        // TODO: add checks to make sure function params match
        // TODO: add checks to make sure return type matches

        // compile param list
        ret.addAll(compileCallParamsNode(ast.params));

        // call function
        ret.add(Statement.functionCall(funcVar.ip));
        m_functionIp++;

        // remove params from compiler stack and push return value, because they are cleared by the function
        for (int i = 0; i < funcVar.params.size(); i++) {
            Variable delVar = m_variableStack.peek();
            removeVariableFromScope(delVar);
        }
        if (!(funcVar.value instanceof Void)) {
            Variable tempVar = new TempVariable(funcVar.value.getTypeInstance());
            addVariableToScope(tempVar);
        }

        return ret;
    }
    private ArrayList<Statement> compileCallParamsNode(CallParamsNode ast) {
        ArrayList<Statement> ret = new ArrayList<>();
        
        ArrayList<Integer> exprResults = new ArrayList<>();
        for (AST param : ast.params) {
            if (!(param instanceof IdentifierNode) && !(param instanceof LiteralNode)) {
                ret.addAll(dispatchToCompile(param));
                exprResults.add(m_variableStack.size() - 1);
            }
        }

        for (AST param : ast.params) {
            if (param instanceof IdentifierNode) {
                IdentifierNode node = (IdentifierNode) param;
                Variable var = new Variable(node.identifier.textRepresentation, null);
                // check if variable exists in function scope (params + locals)(relative)
                boolean rel = false;
                if (m_inFunction) {
                    for (Variable v : m_functionVariables) {
                        if (v.equals(var)) {
                            rel = true;
                            break;
                        }
                    }
                }
                
                // check if identifier exists
                if (!doesVariableExist(var)) {
                    throw new IllegalArgumentException("Variable does not exist in scope: " + var.name);
                }
                
                // push copy to stack
                int index = getVariableStackLocation(var);
                TempVariable tempVar = new TempVariable(m_variableStack.get(index).value.getTypeInstance());
                if (rel) {
                    index = index - m_variableStack.size() + 1;
                }
                addVariableToScope(tempVar);
                ret.add(Statement.createTempCopy(rel, index));
                m_functionIp++;
            } else if (param instanceof LiteralNode) {
                LiteralNode node = (LiteralNode) param;
                TempVariable tempVar = new TempVariable(Type.fromToken(node.literal));
                tempVar.value.assignValueFromString(node.literal.textRepresentation);
                addVariableToScope(tempVar);
                ret.add(Statement.declareVariable(tempVar));
                m_functionIp++;
            } else {
                if (exprResults.size() == 0) {
                    throw new IllegalArgumentException("Expression result missing.");
                }
                // copy result of expression to stack
                int index = exprResults.get(0);
                exprResults.remove(0);
                TempVariable tempVar = new TempVariable(m_variableStack.get(index).value.getTypeInstance());
                addVariableToScope(tempVar);
                ret.add(Statement.createTempCopy(true, index - m_variableStack.size() + 1));
                m_functionIp++;
            }
        }
        return ret;
    }
    private ArrayList<Statement> compileReturnNode(ReturnNode ast) {
        ArrayList<Statement> ret = new ArrayList<>();
        if (!m_inFunction) {
            throw new IllegalArgumentException("Cannot return outside of function.");
        }
        // TODO: check if return types matches function
        // TODO: use createTempCopy when applicable to avoid unnecessary temp variables like in compileCallParamsNode

        // compile rhs
        if (ast.retVal != null) {
            if (ast.retVal instanceof IdentifierNode) {
                int retIndex = 0;
                boolean rel = false;
                IdentifierNode node = (IdentifierNode) ast.retVal;
                Variable var = new Variable(node.identifier.textRepresentation, null);
                // check if variable exists in function scope (params + locals)(relative)
                for (Variable v : m_functionVariables) {
                    if (v.equals(var)) {
                        rel = true;
                    }
                }
                // else check global scope (absolute)
                // check if variable exists
                if (!doesVariableExist(var)) {
                    throw new IllegalArgumentException("Variable does not exist in scope: " + var.name);
                }
                retIndex = getVariableStackLocation(var);

                Type retType = m_variableStack.get(retIndex).value.getTypeInstance();
                Variable tempVar = new TempVariable(retType);
                addVariableToScope(tempVar);
                ret.add(Statement.declareVariable(tempVar));
                m_functionIp++;
                if (rel) {
                    retIndex = retIndex - m_variableStack.size() + 1;
                }
                ret.add(Statement.assignVariable(true, 0, rel, retIndex));
                m_functionIp++;
            } else {
                ret.addAll(dispatchToCompile(ast.retVal));
            }
            ret.add(Statement.functionReturn(true, m_functionVariables.size() - 1));
            m_functionIp++;
        } else {
            ret.add(Statement.functionReturn(false, m_functionVariables.size()));
            m_functionIp++;
        }
        return ret;
    }
    private ArrayList<Statement> compileFunctionDefinitionNode(FunctionDefNode ast) {
        ArrayList<Statement> ret = new ArrayList<>();

        // TODO: somehow force return statement

        if (m_inFunction) {
            throw new IllegalArgumentException("Cannot define function inside another function.");
        }

        // compile function params
        ArrayList<Variable> params = compileParamDeclarationNode(ast.paramDeclaration);

        // check if name is taken already
        FunctionVariable funcVar = new FunctionVariable(ast.declaration.identifier.textRepresentation, Type.fromToken(ast.declaration.type), params, m_functionIp + 1);
        if (doesVariableExist(funcVar)) {
            throw new IllegalArgumentException("Function name taken, variable already exists in scope: " + funcVar.name);
        }
        addVariableToScope(funcVar);
        m_inFunction = true;

        // add params to scope
        for (Variable param : params) {
            addVariableToScope(param);
        }

        // compile function block
        ArrayList<Statement> block = compileBlockNode(ast.block);
        block.remove(block.size() - 1); // remove stack clear instruction because return clears stack

        // remove params from scope
        for (Variable param : params) {
            removeVariableFromScope(param);
        }

        ret.add(Statement.declareVariable(funcVar));
        m_functionIp++;
        ret.add(Statement.jumpRel(block.size()));
        m_functionIp++;
        ret.addAll(block);
        m_inFunction = false;
        return ret;
    }
    private ArrayList<Variable> compileParamDeclarationNode(ParamDeclarationNode ast) {
        ArrayList<Variable> ret = new ArrayList<>();
        for (DeclarationNode declaration : ast.declarations) {
            Variable var = new Variable(declaration.identifier.textRepresentation, Type.fromToken(declaration.type));
            for (Variable v : ret) {
                if (v.equals(var)) {
                    throw new IllegalArgumentException("Duplicate parameter name: " + var.name);
                }
            }
            ret.add(var);
        }
        return ret;
    }
    private ArrayList<Statement> compileAssignmentNode(AssignmentNode ast) {
        ArrayList<Statement> ret = new ArrayList<>();

        // TODO: if RHS is Literal, then we can optimize by not creating a temp variable

        // compile rhs of assignment
        int rhsIndex = 0;
        boolean rhsRel = false;
        if (ast.rhs instanceof IdentifierNode) {
            IdentifierNode node = (IdentifierNode) ast.rhs;
            Variable var = new Variable(node.identifier.textRepresentation, null);
            // check if variable exists in function scope (params + locals)(relative)
            if (m_inFunction) {
                for (Variable v : m_functionVariables) {
                    if (v.equals(var)) {
                        rhsRel = true;
                    }
                }
            }
            // else check global scope (absolute)
            // check if variable exists
            if (!doesVariableExist(var)) {
                throw new IllegalArgumentException("Variable does not exist in scope: " + var.name);
            }
            rhsIndex = getVariableStackLocation(var);
        } else {
            ret.addAll(dispatchToCompile(ast.rhs));
            rhsIndex = m_variableStack.size() - 1;
            rhsRel = true;
        }
        Type rhsType = m_variableStack.get(rhsIndex).value.getTypeInstance();


        // lhs
        int lhsIndex = 0;
        boolean lhsRel = false;
        if (ast.lhs instanceof IdentifierNode) {
            IdentifierNode node = (IdentifierNode) ast.lhs;
            Variable var = new Variable(node.identifier.textRepresentation, null);
            // check if variable exists in function scope (params + locals)(relative)
            if (m_inFunction) {
                for (Variable v : m_functionVariables) {
                    if (v.equals(var)) {
                        lhsRel = true;
                    }
                }
            }
            // else check global scope (absolute)
            // check if variable exists
            if (!doesVariableExist(var)) {
                throw new IllegalArgumentException("Variable does not exist in scope: " + var.name);
            }
            lhsIndex = getVariableStackLocation(var);
        } else {
            ret.addAll(dispatchToCompile(ast.lhs));
            lhsIndex = m_variableStack.size() - 1;
            lhsRel = true;
        }
        Type lhsType = m_variableStack.get(lhsIndex).value.getTypeInstance();
        
        checkIfTypesAreCompatible(lhsType, rhsType, Token.Type.OP_ASSIGN);

        // assign value to variable
        if (lhsRel) {
            lhsIndex = lhsIndex - m_variableStack.size() + 1;
        }
        if (rhsRel) {
            rhsIndex = rhsIndex - m_variableStack.size() + 1;
        }
        ret.add(Statement.assignVariable(lhsRel, lhsIndex, rhsRel, rhsIndex));
        m_functionIp++;

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
        addVariableToScope(var);
        ret.add(Statement.declareVariable(var));
        m_functionIp++;

        return ret;
    }
    private ArrayList<Statement> compileBinaryOpNode(BinaryOpNode ast) {
        ArrayList<Statement> ret = new ArrayList<>();

        // lhs
        int lhsIndex = -1;
        boolean lhsRel = false;
        if (ast.lhs instanceof IdentifierNode) {
            IdentifierNode node = (IdentifierNode) ast.lhs;
            Variable var = new Variable(node.identifier.textRepresentation, null);
            // check if variable exists in function scope (params + locals)(relative)
            if (m_inFunction) {
                for (Variable v : m_functionVariables) {
                    if (v.equals(var)) {
                        lhsRel = true;
                    }
                }
            }
            // else check global scope (absolute)
            // check if variable exists
            if (!doesVariableExist(var)) {
                throw new IllegalArgumentException("Variable does not exist in scope: " + var.name);
            }
            lhsIndex = getVariableStackLocation(var);
        } else {
            ret.addAll(dispatchToCompile(ast.lhs));
            lhsIndex = m_variableStack.size() - 1;
            lhsRel = true;
        }
        Type lhsType = m_variableStack.get(lhsIndex).value.getTypeInstance();

        // rhs
        int rhsIndex = -1;
        boolean rhsRel = false;
        if (ast.rhs instanceof IdentifierNode) {
            IdentifierNode node = (IdentifierNode) ast.rhs;
            Variable var = new Variable(node.identifier.textRepresentation, null);
            // check if variable exists in function scope (params + locals)(relative)
            if (m_inFunction) {
                for (Variable v : m_functionVariables) {
                    if (v.equals(var)) {
                        rhsRel = true;
                    }
                }
            }
            // else check global scope (absolute)
            // check if variable exists
            if (!doesVariableExist(var)) {
                throw new IllegalArgumentException("Variable does not exist in scope: " + var.name);
            }
            rhsIndex = getVariableStackLocation(var);
        } else {
            ret.addAll(dispatchToCompile(ast.rhs));
            rhsIndex = m_variableStack.size() - 1;
            rhsRel = true;
        }
        Type rhsType = m_variableStack.get(rhsIndex).value.getTypeInstance();

        // check if lhs and rhs are compatible types
        checkIfTypesAreCompatible(lhsType, rhsType, ast.operator.type);

        // create temp variable on the stack to store result
        Variable var = new TempVariable(lhsType);
        addVariableToScope(var);
        ret.add(Statement.declareVariable(var));
        m_functionIp++;
        int resultIndex = m_variableStack.size() - 1;
        boolean resultRel = true;
        
        // perform binary operation
        if (resultRel) {
            resultIndex = resultIndex - m_variableStack.size() + 1;
        }
        if (lhsRel) {
            lhsIndex = lhsIndex - m_variableStack.size() + 1;
        }
        if (rhsRel) {
            rhsIndex = rhsIndex - m_variableStack.size() + 1;
        }
        ret.add(Statement.binaryOp(ast.operator.type, resultRel, resultIndex, lhsRel, lhsIndex, rhsRel, rhsIndex));
        m_functionIp++;

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
        m_functionIp++;
        return ret;
    }

    private void checkIfTypesAreCompatible(Type lhs, Type rhs, Token.Type op) {
        // TODO: add more checks
        if ((lhs instanceof UnsignedInt || lhs instanceof SignedInt || lhs instanceof Bool) &&
            (rhs instanceof UnsignedInt || rhs instanceof SignedInt || rhs instanceof Bool)) {
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
        for (int i = m_variableStack.size() - 1; i >= 0; i--) {
            if (m_variableStack.get(i).equals(var)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Variable not found in stack: " + var.name);
    }
    private void addVariableToScope(Variable variable) {
        m_scopeStack.peek().variables.add(variable);
        m_variableStack.push(variable);
        if (m_inFunction) {
            m_functionVariables.add(variable);
        }
    }
    private void removeVariableFromScope(Variable variable) {
        m_scopeStack.peek().variables.remove(variable);
        m_variableStack.pop();
        if (m_inFunction) {
            m_functionVariables.remove(variable);
        }
    }
    private void startScope() {
        m_scopeStack.push(new Scope());
    }
    private ArrayList<Statement> endScope() {
        ArrayList<Statement> ret = new ArrayList<>();
        Scope scope = m_scopeStack.pop();
        if (m_inFunction) {
            for (Variable var : scope.variables) {
                m_functionVariables.remove(var);
            }
        }
        for (int i = 0; i < scope.variables.size(); i++) {
            m_variableStack.pop();
        }
        ret.add(Statement.clearStack(scope.variables.size()));
        m_functionIp++;
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
    private HashSet<Variable> m_functionVariables = new HashSet<>();
    private int m_functionIp = 0;
    private boolean m_inFunction = false;
}
