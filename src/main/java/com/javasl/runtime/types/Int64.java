package com.javasl.runtime.types;

public class Int64 extends SignedInt<Long> {
    public Int64() {
        super((long) 0);
    }
    public Int64(long value) {
        super(value);
    }
    
    // value
    public void setValue(Object value) {
        m_value = ((Number)value).longValue();
    }

    // misc
    public Type getTypeInstance() {
        return new Int64();
    }
    public String toString() {
        return "int64(" + getValue() + ")";
    }
}