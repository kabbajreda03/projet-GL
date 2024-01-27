#!/bin/bash
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"




if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "CLASS: \[@.*1:0]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*1:6]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "OBRACE: \[@.*1:7]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi


if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "PROTECTED: \[@.*2:4]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi


if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*2:14]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi


if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*2:18]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "SEMI: \[@.*2:19]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*3:4]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*3:10]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "SEMI: \[@.*3:11]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*5:4]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*5:8]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "OPARENT: \[@.*5:12]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi


if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "CPARENT: \[@.*5:13]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "OBRACE: \[@.*5:14]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "RETURN: \[@.*6:8]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*6:15]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi


if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "SEMI: \[@.*6:16]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "CBRACE: \[@.*7:4]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi


if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*9:4]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*9:9]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "OPARENT: \[@.*9:13]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*9:14]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi


if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*9:18]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "CPARENT: \[@.*9:19]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "OBRACE: \[@.*9:20]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*10:8]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "EQUALS: \[@.*10:9]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*10:10]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "SEMI: \[@.*10:11]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "CBRACE: \[@.*11:4]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*13:4]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*13:10]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "OPARENT: \[@.*13:23]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "CPARENT: \[@.*13:24]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "OBRACE: \[@.*13:25]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi


if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "RETURN: \[@.*14:8]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi


if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*14:15]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi


if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "TIMES: \[@.*14:16]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*14:17]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi


if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "SEMI: \[@.*14:18]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi


if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "CBRACE: \[@.*15:4]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "CBRACE: \[@.*16:0]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi


if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "CLASS: \[@.*18:0]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi


if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*18:6]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi


if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "EXTENDS: \[@.*18:8]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi


if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*18:16]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi


if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "OBRACE: \[@.*18:17]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "PROTECTED: \[@.*19:4]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "PROTECTED: \[@.*19:4]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*19:14]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*19:18]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi


if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "SEMI: \[@.*19:19]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "CBRACE: \[@.*20:0]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "OBRACE: \[@.*23:0]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*24:4]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*24:6]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "EQUALS: \[@.*24:8]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "NEW: \[@.*24:10]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*24:14]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "OPARENT: \[@.*24:15]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "CPARENT: \[@.*24:16]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "SEMI: \[@.*24:17]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*25:4]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*25:6]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "EQUALS: \[@.*25:8]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "NEW: \[@.*25:10]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "IDENT: \[@.*25:14]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "OPARENT: \[@.*25:15]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "CPARENT: \[@.*25:16]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi

if ! test_lex "src/test/deca/syntax/valid/provided/lexer-test.deca"  | \
             grep -q "SEMI: \[@.*25:17]$"
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi



echo -e "\033[32mPASSED\033[0m"