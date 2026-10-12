grammar Calc1;

root : expr* EOF;

expr
    : LPAREN expr RPAREN       # aren
    | expr (MUL | DIV) expr    # mulDiv
    | expr (ADD | SUB) expr    # addSub
    | factor                   # goFactor
    ;

factor : INT;

INT : '0' | [1-9][0-9]*;
ADD : '+';
SUB : '-';
MUL : '*';
DIV : '/';
LPAREN : '(';
RPAREN : ')';
WS : [ \t\r\n]+ -> skip;
