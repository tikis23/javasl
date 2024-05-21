package com.javasl.runtime.types;

public class Bool extends Type {
    public Bool() {
        m_value = false;
    }
    public Bool(boolean value) {
        m_value = value;
    }

    // value
    public void setValue(Object value) {
        if (value instanceof Number)
            m_value = ((Number)value).longValue() != 0;
        else if (value instanceof Boolean)
            m_value = (Boolean)value;
        else
            throw new RuntimeException("Invalid boolean value type");
    }
    public Object getValue() {
        return m_value ? 1L : 0L;
    }
    public void assignValueFromString(String value) {
        setValue(Boolean.parseBoolean(value));
    }

    // ops
    public Type op_multiply(Type other) {
        Long lhs = m_value ? 1L : 0L;
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Int64();
        val.setValue(lhs * rhs);
        return val;
    }
    public Type op_divide(Type other) {
        Long lhs = m_value ? 1L : 0L;
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Int64();
        val.setValue(lhs / rhs);
        return val;
    }
    public Type op_modulo(Type other) {
        Long lhs = m_value ? 1L : 0L;
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Int64();
        val.setValue(lhs % rhs);
        return val;
    }
    public Type op_plus(Type other) {
        Long lhs = m_value ? 1L : 0L;
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Int64();
        val.setValue(lhs + rhs);
        return val;
    }
    public Type op_minus(Type other) {
        Long lhs = m_value ? 1L : 0L;
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Int64();
        val.setValue(lhs - rhs);
        return val;
    }
    public Type op_shift_left(Type other) {
        Long lhs = m_value ? 1L : 0L;
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Int64();
        val.setValue(lhs << rhs);
        return val;
    }
    public Type op_shift_right(Type other) {
        Long lhs = m_value ? 1L : 0L;
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Int64();
        val.setValue(lhs >> rhs);
        return val;
    }
    public Type op_less(Type other) {
        Long lhs = m_value ? 1L : 0L;
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Bool();
        val.setValue(lhs < rhs);
        return val;
    }
    public Type op_less_equal(Type other) {
        Long lhs = m_value ? 1L : 0L;
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Bool();
        val.setValue(lhs <= rhs);
        return val;
    }
    public Type op_greater(Type other) {
        Long lhs = m_value ? 1L : 0L;
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Bool();
        val.setValue(lhs > rhs);
        return val;
    }
    public Type op_greater_equal(Type other) {
        Long lhs = m_value ? 1L : 0L;
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Bool();
        val.setValue(lhs >= rhs);
        return val;
    }
    public Type op_equal(Type other) {
        Long lhs = m_value ? 1L : 0L;
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Bool();
        val.setValue(lhs == rhs);
        return val;
    }
    public Type op_not_equal(Type other) {
        Long lhs = m_value ? 1L : 0L;
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Bool();
        val.setValue(lhs != rhs);
        return val;
    }
    public Type op_bitwise_and(Type other) {
        Long lhs = m_value ? 1L : 0L;
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Int64();
        val.setValue(lhs & rhs);
        return val;
    }
    public Type op_bitwise_xor(Type other) {
        Long lhs = m_value ? 1L : 0L;
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Int64();
        val.setValue(lhs ^ rhs);
        return val;
    }
    public Type op_bitwise_or(Type other) {
        Long lhs = m_value ? 1L : 0L;
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Int64();
        val.setValue(lhs | rhs);
        return val;
    }
    public Type op_logical_and(Type other) {
        Long lhs = m_value ? 1L : 0L;
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Bool();
        val.setValue((lhs != 0) && (rhs != 0));
        return val;
    }
    public Type op_logical_or(Type other) {
        Long lhs = m_value ? 1L : 0L;
        Long rhs = ((Number)other.getValue()).longValue();
        Type val = new Bool();
        val.setValue((lhs != 0) || (rhs != 0));
        return val;
    }

    // misc
    public Type getTypeInstance() {
        return new Bool();
    }
    public String toString() {
        return "bool(" + Boolean.toString(m_value) + ")";
    }

    protected boolean m_value;
}
