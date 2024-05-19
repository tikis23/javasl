package com.javasl.runtime.types;

public class Int32 extends SignedInt<Integer> {
    public Int32() {
        super((int) 0);
    }
    public Int32(long value) {
        super((int)value);
    }
    
    // value
    public void setValue(Object value) {
        m_value = ((Number)value).intValue();
    }

    // misc
    public Type getTypeInstance() {
        return new Int32();
    }
    public String toString() {
        return "int32(" + getValue() + ")";
    }
}