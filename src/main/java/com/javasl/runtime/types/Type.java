package com.javasl.runtime.types;

import java.util.HashMap;

import com.javasl.compiler.Token;

public abstract class Type {
    // value
    public abstract void assignValueFromString(String value);
    public abstract Object getValue();
    public abstract void setValue(Object value);

    // ops
    public abstract Type op_multiply(Type other);
    public abstract Type op_divide(Type other);
    public abstract Type op_modulo(Type other);
    public abstract Type op_plus(Type other);
    public abstract Type op_minus(Type other);
    public abstract Type op_shift_left(Type other);
    public abstract Type op_shift_right(Type other);
    public abstract Type op_less(Type other);
    public abstract Type op_less_equal(Type other);
    public abstract Type op_greater(Type other);
    public abstract Type op_greater_equal(Type other);
    public abstract Type op_equal(Type other);
    public abstract Type op_not_equal(Type other);
    public abstract Type op_bitwise_and(Type other);
    public abstract Type op_bitwise_xor(Type other);
    public abstract Type op_bitwise_or(Type other);
    public abstract Type op_logical_and(Type other);
    public abstract Type op_logical_or(Type other);

    // misc
    public Type copy() {
        Type val = getTypeInstance();
        val.setValue(getValue());
        return val;
    }
    public abstract String toString();
    public abstract Type getTypeInstance();
    public static Type fromToken(Token token) {
        return types.get(token.type).getTypeInstance();
    }
    private static final HashMap<Token.Type, Type> types = new HashMap<Token.Type, Type>() {{
        put(Token.Type.T_VOID,   new Void());
        put(Token.Type.T_INT8,   new Int8());
        put(Token.Type.T_INT16,  new Int16());
        put(Token.Type.T_INT32,  new Int32());
        put(Token.Type.T_INT64,  new Int64());
        put(Token.Type.T_UINT8,  new Uint8());
        put(Token.Type.T_UINT16, new Uint16());
        put(Token.Type.T_UINT32, new Uint32());
        put(Token.Type.T_UINT64, new Uint64());
        put(Token.Type.T_FLOAT,  null);
        put(Token.Type.T_DOUBLE, null);
        put(Token.Type.T_BOOL,   new Bool());
        put(Token.Type.T_STRING, null);
        put(Token.Type.T_CHAR,   null);
        put(Token.Type.LIT_NUMBER, new Int64());
        put(Token.Type.LIT_STRING, null);
        put(Token.Type.LIT_CHAR, null);
        put(Token.Type.KW_TRUE, new Bool());
        put(Token.Type.KW_FALSE, new Bool());
    }};
}
