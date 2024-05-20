package com.javasl.runtime;

import com.javasl.runtime.types.Type;

public class Variable {
    public String name;
    public Type value;

    public Variable(String name, Type value) {
        this.name = name;
        this.value = value;
    }

    public Variable copy() {
        return new Variable(name, value.copy());
    }

    public String toString() {
        return name + "-" + value.toString();
    }

    public int hashCode() {
        return name.hashCode();
    }
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Variable) {
            Variable other = (Variable) obj;
            return name.equals(other.name);
        }
        return false;
    }
}
