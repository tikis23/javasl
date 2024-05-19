package com.javasl.runtime.types;

public class Uint32 extends UnsignedInt<Integer> {
    public Uint32() {
        super((int) 0);
    }
    public Uint32(long value) {
        super((int)value);
    }
    
    // value
    public void setValue(Object value) {
        m_value = ((Number)value).intValue();
    }

    // misc
    public Type getTypeInstance() {
        return new Uint32();
    }
    public String toString() {
        return "uint32(" + Integer.toUnsignedString((Integer)getValue()) + ")";
    }
}
