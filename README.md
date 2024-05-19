# JavaSL - Java scripting language

A simple scripting language written in java.

# Types
```
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
```
LHS => IDENTIFIER | TYPE IDENTIFIER | IDENTIFIER IDENTIFIER
RHS => EXPR
EXPR => TERM {"+" TERM | "-" TERM}
TERM => FACTOR {"*" FACTOR | "/" FACTOR | "%" FACTOR}
FACTOR => LITERAL | IDENTIFIER | "(" EXPR ")"
LITERAL => LITERAL_NUMBER | LITERAL_STRING | LITERAL_CHAR
LITERAL_NUMBER => digit {digit} | "-" digit {digit}
LITERAL_STRING => """ {character} """
LITERAL_CHAR => "'" character "'"
ASSIGNMENT => LHS "=" RHS
BLOCK => "{" STATEMENT ... STATEMENT "}"
STATEMENT => ASSIGNMENT ";"

```