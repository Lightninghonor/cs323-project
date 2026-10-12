lexer grammar SplLexer;

INT    : 'int';
STRUCT : 'struct';
RETURN : 'return';
IF     : 'if';
ELSE   : 'else';
WHILE  : 'while';

ASSIGN : '=';
PLUS   : '+';
MINUS  : '-';
MUL    : '*';
DIV    : '/';
MOD    : '%';
POW    : '^';
LT     : '<';
LE     : '<=';
GT     : '>';
GE     : '>=';
EQ     : '==';
NEQ    : '!=';
AND    : '&&';
OR     : '||';
NOT    : '!';
DOT    : '.';

SEMI   : ';';
COMMA  : ',';
LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
LBRACK : '[';
RBRACK : ']';

Identifier : [a-zA-Z_] [a-zA-Z_0-9]*;
Number     : '0' | [1-9] [0-9]*;

WS : [ \t\r\n]+ -> skip;
LINE_COMMENT : '//' ~[\r\n]* -> skip;
// Non-greedy matching stops at the first closing delimiter (no nesting).
BLOCK_COMMENT : '/*' .*? '*/' -> skip;
