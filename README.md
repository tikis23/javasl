# JavaSL - Java scripting language

A simple scripting language written in java.

# Grammar
```
LHS => IDENTIFIER | TYPE IDENTIFIER | IDENTIFIER IDENTIFIER
RHS => EXPR
EXPR => TERM {"+" TERM | "-" TERM}
TERM => FACTOR {"*" FACTOR | "/" FACTOR | "%" FACTOR}
FACTOR => LITERAL | IDENTIFIER | "(" EXPR ")"
ASSIGNMENT => LHS "=" RHS
BLOCK => "{" STATEMENT ... STATEMENT "}"
STATEMENT => ASSIGNMENT ";"

```