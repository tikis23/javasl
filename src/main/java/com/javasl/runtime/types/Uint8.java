package com.javasl.runtime.types;

public class Uint8 extends UnsignedInt<Byte> {
    public Uint8() {
        super((byte) 0);
    }
    public Uint8(long value) {
        super((byte)value);
    }
    
    // value
    public void setValue(Object value) {
        m_value = ((Number)value).byteValue();
    }

    // misc
    public Type getTypeInstance() {
        return new Uint8();
    }
    public String toString() {
        return "uint8(" + Integer.toUnsignedString(Byte.toUnsignedInt((Byte)getValue())) + ")";
    }
}
