package com.javasl.runtime.types;

public class Uint64 extends UnsignedInt<Long> {
    public Uint64() {
        super((long) 0);
    }
    public Uint64(long value) {
        super((long)value);
    }
    
    // value
    public void setValue(Object value) {
        m_value = ((Number)value).longValue();
    }

    // misc
    public Type getTypeInstance() {
        return new Uint64();
    }
    public String toString() {
        return "uint64(" + Long.toUnsignedString((Long)getValue()) + ")";
    }
}
