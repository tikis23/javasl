package com.javasl.runtime;

import java.util.Stack;

import com.javasl.compiler.Token;

public interface Statement {
    public void execute(Stack<Variable> variables);
    public String toString();

    public static Statement declareVariable(Variable variable) {
        return new Statement() {
            public void execute(Stack<Variable> variables) {
                variables.push(variable);
            }
            public String toString() {
                return "declareVariable " + variable.toString();
            }
        };
    }
    public static Statement binaryOp(Token.Type op, int result_sp, int left_sp, int right_sp) {
        abstract class OperationOverload implements Statement {
            public void execute(Stack<Variable> variables) {
                Variable result = variables.get(result_sp);
                Variable left = variables.get(left_sp);
                Variable right = variables.get(right_sp);
                operation(result, left, right);
            }
            public String toString() {
                return "binaryOp " + op + " " + result_sp + " " + left_sp + " " + right_sp;
            }
            public abstract void operation(Variable result, Variable left, Variable right);
        }

        if (op == Token.Type.OP_PLUS) {
            return new OperationOverload() {
                public void operation(Variable result, Variable left, Variable right) {
                    result.value = left.value.op_plus(right.value);
                }
            };
        } else if (op == Token.Type.OP_MINUS) {
            return new OperationOverload() {
                public void operation(Variable result, Variable left, Variable right) {
                    result.value = left.value.op_minus(right.value);
                }
            };
        } else if (op == Token.Type.OP_MULTIPLY) {
            return new OperationOverload() {
                public void operation(Variable result, Variable left, Variable right) {
                    result.value = left.value.op_multiply(right.value);
                }
            };
        } else if (op == Token.Type.OP_DIVIDE) {
            return new OperationOverload() {
                public void operation(Variable result, Variable left, Variable right) {
                    result.value = left.value.op_divide(right.value);
                }
            };
        } else if (op == Token.Type.OP_MODULO) {
            return new OperationOverload() {
                public void operation(Variable result, Variable left, Variable right) {
                    result.value = left.value.op_modulo(right.value);
                }
            };
        } else {
            throw new RuntimeException("Unsupported binary operation: " + op);
        }
    }
    public static Statement assignVariable(int target_sp, int source_sp) {
        return new Statement() {
            public void execute(Stack<Variable> variables) {
                Variable var1 = variables.get(target_sp);
                Variable var2 = variables.get(source_sp);
                var1.value.setValue(var2.value.getValue());
            }
            public String toString() {
                return "assignVariable " + target_sp + " " + source_sp;
            }
        };
    }
    public static Statement clearStack(int count) {
        return new Statement() {
            public void execute(Stack<Variable> variables) {
                for (int i = 0; i < count; i++) {
                    variables.pop();
                }
            }
            public String toString() {
                return "clearStack " + count;
            }
        };
    }
}
