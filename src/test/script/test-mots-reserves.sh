#!/bin/bash
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"



file_path="src/test/deca/syntax/valid/provided/lexical-objects.deca"

# Define the list of words and their corresponding line numbers
words=("GT" "LT" "EQEQ" "LEQ" "GEQ" "NEQ" "IF" "ELSE" "WHILE" "PLUS" "TIMES" "PERCENT" "SLASH" "COMMA" "SEMI" "STRING" "ASM" "CLASS" "EXTENDS" "FALSE" "NEW" "NULL" "READINT" "READFLOAT" "PRINT" "PRINTLN" "PRINTLNX" "PRINTX" "PROTECTED" "RETURN" "THIS" "TRUE" "WHILE" "OR" "AND" "MINUS")
line_numbers=(2 3 4 5 6 7 8 9 10  11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 36 37)

RED='\033[0;31m'
RESET='\033[0m'

for ((i=0; i<${#words[@]}; i++)); do
    word="${words[i]}"
    line="${line_numbers[i]}"
    
    if ! test_lex "$file_path" | grep -q "${word}: \[@.*${line}:4]$"; then
        echo $word
        echo $line
        echo "${RED}FAILED${RESET}"
        exit 1
    fi
done


echo -e "\033[32mPASSED\033[0m"