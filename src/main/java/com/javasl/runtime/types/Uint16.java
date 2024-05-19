package com.javasl.runtime.types;

public class Uint16 extends UnsignedInt<Short> {
    public Uint16() {
        super((short) 0);
    }
    public Uint16(long value) {
        super((short)value);
    }
    
    // value
    public void setValue(Object value) {
        m_value = ((Number)value).shortValue();
    }

    // misc
    public Type getTypeInstance() {
        return new Uint16();
    }
    public String toString() {
        return "uint16(" + Integer.toUnsignedString(Short.toUnsignedInt((Short)getValue())) + ")";
    }
}
