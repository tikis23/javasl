package com.javasl.runtime.types;

public class Int16 extends SignedInt<Short> {
    public Int16() {
        super((short) 0);
    }
    public Int16(long value) {
        super((short)value);
    }
    
    // value
    public void setValue(Object value) {
        m_value = ((Number)value).shortValue();
    }

    // misc
    public Type getTypeInstance() {
        return new Int16();
    }
    public String toString() {
        return "int16(" + getValue() + ")";
    }
}
