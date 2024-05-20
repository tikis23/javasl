package com.javasl.runtime.types;

public class Void extends Type {
    @Override
    public void assignValueFromString(String value) {
        throw new UnsupportedOperationException("Unsupported method 'assignValueFromString'");
    }

    @Override
    public Object getValue() {
        throw new UnsupportedOperationException("Unsupported method 'getValue'");
    }

    @Override
    public void setValue(Object value) {
        throw new UnsupportedOperationException("Unsupported method 'setValue'");
    }

    @Override
    public Type op_plus(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_plus'");
    }

    @Override
    public Type op_minus(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_minus'");
    }

    @Override
    public Type op_multiply(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_multiply'");
    }

    @Override
    public Type op_divide(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_divide'");
    }

    @Override
    public Type op_modulo(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_modulo'");
    }

    @Override
    public String toString() {
        return "void";
    }

    @Override
    public Type getTypeInstance() {
        return new Void();
    }
    
}
