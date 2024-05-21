# JavaSL - Java scripting language

A simple scripting language written in java.

# Types
```
Misc:
    void, bool
Scalar:
    int8, int16, int32, int64
    uint8, uint16, uint32, uint64
```

# Operators
| Precedence | Operator |
|:----------:|:--------:|
|1|* / %|
|2|+ -|
|3|<< >>|
|4|< <= > >=|
|5|== !=|
|6|&|
|7|^|
|8|\||
|9|&&|
|10|\|\||

# Grammar
TODO: move IDENTIFIER IDENTIFIER from LHS to VAR_DECL  
- TERM[x] means for every operator precedence apply this grammar 
```
LHS => IDENTIFIER | VAR_DECL | IDENTIFIER IDENTIFIER
VAR_DECL => TYPE IDENTIFIER
RHS => EXPR
EXPR => TERM[x]
TERM[x] => TERM[x-1] {OP_PREC[x][0] TERM[x-1] | OP_PREC[x][1] TERM[x-1] | ...}
TERM[0] => FACTOR {OP_PREC[0][0] FACTOR | OP_PREC[0][1] FACTOR | ...}
FACTOR => FUNC_CALL | LITERAL | IDENTIFIER | "(" EXPR ")"
LITERAL => LITERAL_NUMBER | LITERAL_STRING | LITERAL_CHAR
LITERAL_NUMBER => digit {digit} | "-" digit {digit}
LITERAL_STRING => """ {character} """
LITERAL_CHAR => "'" character "'"
ASSIGNMENT => LHS "=" RHS
FUNC_DEF => VAR_DECL "(" PARAM_DECL ")" BLOCK
BLOCK => "{" STATEMENT ... STATEMENT "}"
STATEMENT => BLOCK | FUNC_DEF | FUNC_CALL ";" | ASSIGNMENT ";" | RRET ";" | IF | WHILE | FOR | BREAK ";" | CONTINUE ";"
PARAM_DECL => VAR_DECL { "," VAR_DECL } | NOTHING
PARAMS => EXPR { "," EXPR } | NOTHING
RET => "return" | "return" RHS
FUNC_CALL => IDENTIFIER "(" PARAMS ")"
IF => "if" "(" EXPR ")" STATEMENT | "if" "(" EXPR ")" STATEMENT "else" STATEMENT
WHILE => "while" "(" EXPR ")" STATEMENT
FOR => "for" "(" ASSIGNMENT | EXPR ";" EXPR ";" ASSIGNMENT | EXPR ")" STATEMENT
BREAK => "break"
CONTINUE => "continue"
```