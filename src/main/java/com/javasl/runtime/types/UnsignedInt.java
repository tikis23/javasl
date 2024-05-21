package com.javasl.runtime.types;

public abstract class UnsignedInt<T> extends Type {
    public UnsignedInt(T value) {
        m_value = value;
    }

    // value
    public Object getValue() {
        return m_value;
    }
    public void assignValueFromString(String value) {
        setValue(Long.parseUnsignedLong(value));
    }

    // ops 
    public Type op_multiply(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Uint64();
        val.setValue(lhs * rhs);
        return val;
    }
    public Type op_divide(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Uint64();
        val.setValue(Long.divideUnsigned(lhs, rhs));
        return val;
    }
    public Type op_modulo(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Uint64();
        val.setValue(Long.remainderUnsigned(lhs, rhs));
        return val;
    }
    public Type op_plus(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Uint64();
        val.setValue(lhs + rhs);
        return val;
    }
    public Type op_minus(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Uint64();
        val.setValue(lhs - rhs);
        return val;
    }
    public Type op_shift_left(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Uint64();
        val.setValue(lhs << rhs);
        return val;
    }
    public Type op_shift_right(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Uint64();
        val.setValue(lhs >>> rhs);
        return val;
    }
    public Type op_less(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Bool();
        val.setValue(Long.compareUnsigned(lhs, rhs) < 0);
        return val;
    }
    public Type op_less_equal(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Bool();
        val.setValue(Long.compareUnsigned(lhs, rhs) <= 0);
        return val;
    }
    public Type op_greater(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Bool();
        val.setValue(Long.compareUnsigned(lhs, rhs) > 0);
        return val;
    }
    public Type op_greater_equal(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Bool();
        val.setValue(Long.compareUnsigned(lhs, rhs) >= 0);
        return val;
    }
    public Type op_equal(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Bool();
        val.setValue(lhs == rhs);
        return val;
    }
    public Type op_not_equal(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Bool();
        val.setValue(lhs != rhs);
        return val;
    }
    public Type op_bitwise_and(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Uint64();
        val.setValue(lhs & rhs);
        return val;
    }
    public Type op_bitwise_xor(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Uint64();
        val.setValue(lhs ^ rhs);
        return val;
    }
    public Type op_bitwise_or(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Uint64();
        val.setValue(lhs | rhs);
        return val;
    }
    public Type op_logical_and(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Bool();
        val.setValue((lhs != 0) && (rhs != 0));
        return val;
    }
    public Type op_logical_or(Type other) {
        Long lhs = ((Number)m_value).longValue();
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Bool();
        val.setValue((lhs != 0) || (rhs != 0));
        return val;
    }

    protected T m_value;
}
