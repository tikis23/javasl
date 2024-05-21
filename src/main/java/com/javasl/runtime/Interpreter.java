package com.javasl.runtime;

import java.util.ArrayList;
import java.util.Stack;

public class Interpreter {
    public void execute(ArrayList<Statement> statements) {
        for (;m_state.ip < statements.size(); m_state.ip++) {
            statements.get(m_state.ip).execute(m_state);
        }
    }
    public void execute(ArrayList<Statement> statements, int cycles) {
        for (int i = 0; i < cycles; i++) {
            if (m_state.ip >= statements.size()) {
                break;
            }
            statements.get(m_state.ip).execute(m_state);
            m_state.ip++;
        }
    }
    public void reset() {
        m_state = new State();
    }
    public Stack<Variable> getStack() {
        return m_state.variables;
    }
    protected static class State {
        public int ip = 0;
        public Stack<Variable> variables = new Stack<>();
        public Stack<Integer> funcCalls = new Stack<>();
    }
    private State m_state = new State();
}
