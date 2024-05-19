package com.javasl.runtime;

import java.util.ArrayList;
import java.util.Stack;

public class Interpreter {
    public void execute(ArrayList<Statement> statements) {
        for (Statement statement : statements) {
            statement.execute(m_variables);
        }
    }
    public Stack<Variable> getStack() {
        return m_variables;
    }
    private Stack<Variable> m_variables = new Stack<>();
}
