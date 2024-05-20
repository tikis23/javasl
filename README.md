# JavaSL - Java scripting language

A simple scripting language written in java.

# Types
```
Misc:
    void
Scalar:
    int8, int16, int32, int64
    uint8, uint16, uint32, uint64
```

# Operators
```
Arithmetic:
    + - * / %
```

# Grammar
TODO: move IDENTIFIER IDENTIFIER from LHS to VAR_DECL
```
LHS => IDENTIFIER | VAR_DECL | IDENTIFIER IDENTIFIER
VAR_DECL => TYPE IDENTIFIER
RHS => EXPR
EXPR => TERM {"+" TERM | "-" TERM}
TERM => FACTOR {"*" FACTOR | "/" FACTOR | "%" FACTOR}
FACTOR => FUNC_CALL | LITERAL | IDENTIFIER | "(" EXPR ")"
LITERAL => LITERAL_NUMBER | LITERAL_STRING | LITERAL_CHAR
LITERAL_NUMBER => digit {digit} | "-" digit {digit}
LITERAL_STRING => """ {character} """
LITERAL_CHAR => "'" character "'"
ASSIGNMENT => LHS "=" RHS
FUNC_DEF => VAR_DECL "(" PARAM_DECL ")" BLOCK
BLOCK => "{" STATEMENT ... STATEMENT "}"
STATEMENT => BLOCK | FUNC_DEF | FUNC_CALL ";" | ASSIGNMENT ";" | RRET ";"
PARAM_DECL => VAR_DECL { "," VAR_DECL } | NOTHING
PARAMS => EXPR { "," EXPR } | NOTHING
RET => "return" | "return" RHS
FUNC_CALL => IDENTIFIER "(" PARAMS ")"
```