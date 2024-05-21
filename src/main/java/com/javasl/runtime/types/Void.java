package com.javasl.runtime.types;

public class Void extends Type {
    public void assignValueFromString(String value) {
        throw new UnsupportedOperationException("Unsupported method 'assignValueFromString'");
    }
    public Object getValue() {
        throw new UnsupportedOperationException("Unsupported method 'getValue'");
    }
    public void setValue(Object value) {
        throw new UnsupportedOperationException("Unsupported method 'setValue'");
    }

    public Type op_multiply(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_multiply'");
    }
    public Type op_divide(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_divide'");
    }
    public Type op_modulo(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_modulo'");
    }
    public Type op_plus(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_plus'");
    }
    public Type op_minus(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_minus'");
    }
    public Type op_shift_left(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_shift_left'");
    }
    public Type op_shift_right(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_shift_right'");
    }
    public Type op_less(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_less'");
    }
    public Type op_less_equal(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_less_equal'");
    }
    public Type op_greater(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_greater'");
    }
    public Type op_greater_equal(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_greater_equal'");
    }
    public Type op_equal(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_equal'");
    }
    public Type op_not_equal(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_not_equal'");
    }
    public Type op_bitwise_and(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_bitwise_and'");
    }
    public Type op_bitwise_xor(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_bitwise_xor'");
    }
    public Type op_bitwise_or(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_bitwise_or'");
    }
    public Type op_logical_and(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_logical_and'");
    }
    public Type op_logical_or(Type other) {
        throw new UnsupportedOperationException("Unsupported method 'op_logical_or'");
    }


    public String toString() {
        return "void";
    }
    public Type getTypeInstance() {
        return new Void();
    }
}
