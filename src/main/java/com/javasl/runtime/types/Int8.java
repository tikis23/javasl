package com.javasl.runtime.types;

public class Int8 extends SignedInt<Byte> {
    public Int8() {
        super((byte) 0);
    }
    public Int8(long value) {
        super((byte)value);
    }
    
    // value
    public void setValue(Object value) {
        m_value = ((Number)value).byteValue();
    }

    // misc
    public Type getTypeInstance() {
        return new Int8();
    }
    public String toString() {
        return "int8(" + getValue() + ")";
    }
}
