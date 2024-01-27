#! /bin/sh

# Auteur : gl22
# Version initiale : 01/01/2024

# Base pour un script de test de la lexicographie.
# On teste un fichier valide et un fichier invalide.
# Il est conseillé de garder ce fichier tel quel, et de créer de
# nouveaux scripts (en s'inspirant si besoin de ceux fournis).

# Il faudrait améliorer ce script pour qu'il puisse lancer test_lex
# sur un grand nombre de fichiers à la suite.

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

RED='\e[0;31m'
GREEN='\e[0;32m'
RESET='\e[0m'


# Testing  synthax
if ! test_lex "./src/test/deca/syntax/valid/provided/Synthax_test.deca" 2>&1 | \
             grep -q  -e 'while' 
then

    echo "${RED}FAILED${RESET}"
    exit 1
fi


# Testing  include

if ! test_lex "./src/test/deca/syntax/valid/provided/test_includeMyFILE.deca" 2>&1 \
            | grep -q -e 'println'
then
    echo "${RED}FAILED${RESET}"
    exit 1
fi




# Testing Circular include

if ! test_lex "./src/test/deca/syntax/invalid/provided/circularInclude_test.deca" 2>&1 \
            | grep -q -e ' Circular include for file'
then
    echo "${RED}FAILED${RESET}"
    exit 1
fi


# Testing not found include

if ! test_lex "./src/test/deca/syntax/invalid/provided/Include_not_found_test.deca" 2>&1 \
            | grep -q -e ' include file not found'
then
    echo "${RED}FAILED${RESET}"
    exit 1
fi


# Testing Wrong braces
if ! test_lex ./src/test/deca/syntax/invalid/provided/wrong_braces_lex.deca 2>&1 \
    | grep -q -e 'wrong_braces_lex.deca:[0-9]:'
then
echo "${RED}FAILED${RESET}"
exit 1
fi

# Testing comments 
if  test_lex ./src/test/deca/syntax/invalid/provided/Comments_text.deca 2>&1 \
    | grep -q -e 'comment'
then
   echo "${RED}FAILED${RESET}"
    exit 1
fi


# Testing chaine incomplete
if ! test_lex ./src/test/deca/syntax/invalid/provided/chaine_incomplete_test.deca 2>&1 \
    | grep -q -e 'chaine_incomplete_test.deca:[0-9]'
then
   echo "FAILED"
    exit 1
fi



# Testing Wrong braces
if ! test_lex ./src/test/deca/syntax/invalid/provided/wrong_braces_lex.deca 2>&1 \
    | grep -q -e 'wrong_braces_lex.deca:[0-9]:'
then
echo "${RED}FAILED${RESET}"
exit 1
fi

# Testing comments 
if test_lex ./src/test/deca/syntax/invalid/provided/Comments_text.deca 2>&1 \
    | grep -q -e 'comment'
then
   echo "${RED}FAILED${RESET}"
    exit 1
fi


# Testing chaine incomplete

if !test_lex ./src/test/deca/syntax/invalid/provided/chaine_incomplete_test.deca 2>&1 \
    | grep -q -e 'chaine_incomplete_test.deca:[0-9]'
then
echo "${RED}FAILED${RESET}"
exit 1
fi






echo "\033[32mPASSED\033[0m"