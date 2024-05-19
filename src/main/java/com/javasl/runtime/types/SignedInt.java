package com.javasl.runtime.types;

public abstract class SignedInt<T> extends Type {
    public SignedInt(T value) {
        m_value = value;
    }

    // value
    public Object getValue() {
        return m_value;
    }
    public void assignValueFromString(String value) {
        setValue(Long.parseLong(value));
    }

    // ops 
    public Type op_plus(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Int64();
        val.setValue(lhs + rhs);
        return val;
    }
    public Type op_minus(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Int64();
        val.setValue(lhs - rhs);
        return val;
    }
    public Type op_multiply(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Int64();
        val.setValue(lhs * rhs);
        return val;
    }
    public Type op_divide(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Int64();
        val.setValue(lhs / rhs);
        return val;
    }
    public Type op_modulo(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Int64();
        val.setValue(lhs % rhs);
        return val;
    }

    protected T m_value;
}
