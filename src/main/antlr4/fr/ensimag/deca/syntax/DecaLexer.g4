lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}



// Reserved words
ASM          : 'asm';
INSTANCEOF   : 'instanceof';
PRINTLN      : 'println';
TRUE         : 'true';
CLASS        : 'class';
NEW          : 'new';
PRINTLNX     : 'printlnx';
WHILE        : 'while';
EXTENDS      : 'extends';
NULL         : 'null';
PRINTX       : 'printx';
ELSE         : 'else';
READINT      : 'readInt';
PROTECTED    : 'protected';
FALSE        : 'false';
READFLOAT    : 'readFloat';
RETURN       : 'return';
IF           : 'if';
PRINT        : 'print';
THIS         : 'this';


// Tokens
EQUALS : '=' ;
PLUS : '+' ;
EXCLAM : '!' ;
SEMI : ';' ;
MINUS : '-' ;
TIMES : '*' ;
GEQ : '>=' ;
SLASH : '/' ;
PERCENT : '%' ;
DOT : '.' ;
LEQ : '<=' ;
AND : '&&' ;
OR : '||' ;
COMMA : ',' ;
OPARENT : '(' ;
CPARENT : ')' ;
OBRACE : '{' ;
CBRACE : '}' ;

//Token rules
LT : '<' ;
GT : '>' ;
EQEQ : '==' ;
NEQ : '!=' ;

// Integers
fragment POSITIVE_DIGIT : '1' .. '9';
INT : '0' | POSITIVE_DIGIT DIGIT*;

// Floats
fragment NUM : DIGIT+;
fragment SIGN : ('+' | '-')?; // This means that + or minus are optionat and that the fragment can take an empty string
fragment EXP: ('E' | 'e') SIGN NUM;
fragment DEC: NUM '.' NUM;
fragment FLOATDEC : (DEC | DEC EXP)(('F' | 'f')?);
fragment DIGITHEX: '0' .. '9' | 'A' .. 'F' | 'a' .. 'f';
fragment NUMHEX : DIGITHEX+;
fragment FLOATHEX : ('0x' | '0X') NUMHEX '.' NUMHEX ('P' + 'p') SIGN NUM (('F' | 'f')?);
FLOAT : FLOATDEC | FLOATHEX;


// Strings
fragment STRING_CAR : ~('"'|'\\'|'\n') ;
STRING: '"' (STRING_CAR | '\\"' | '\\\\')* '"';
MULTI_LINE_STRING: '"' (STRING_CAR | '\n' | '\\"' | '\\\\')* '"';

// Comments

fragment MULTI_LINE_COMMENT: '/*' .*? '*/' ;
fragment MONO_LINE_COMMENT: '//' ~('\n')* '\n';

// File Name

fragment FILENAME : (LETTER | DIGIT | '.' | '_' | '-')+;
INCLUDE : '#include' (' ')* '"' FILENAME '"'{ 
      String filename = getText();
      doInclude(filename);
};

//Identifiors
fragment LETTER: 'a' .. 'z' | 'A' .. 'Z';
fragment DIGIT: '0' .. '9';
IDENT: (LETTER | '$' | '_')(LETTER | DIGIT | '$' | '_')*;

// Ignore spaces, tabs, newlines and whitespaces
WS  :   ( ' '
        | '\t'
        | '\r'
        | '\n'
        | MULTI_LINE_COMMENT
        | MONO_LINE_COMMENT
        ) ->
              skip; // avoid producing a token



