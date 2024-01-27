#! /bin/sh

# Auteur : gl22
# Version initiale : 01/01/2024

# Test minimaliste de la vérification contextuelle.
# Le principe et les limitations sont les mêmes que pour basic-synt.sh
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"




# Test variable declarations

if ! test_context src/test/deca/context/valid/provided/test_declaration_int.deca 2>&1 | \
    grep -q 'definition: variable defined at \[5, 8\], type=int'
then
    echo "FAILED"
    exit 1
fi

if ! test_context src/test/deca/context/valid/provided/test_declaration_int.deca 2>&1 | \
    grep -q 'definition: variable defined at \[7, 8\], type=int'
then
    echo "FAILED"
    exit 1
fi


if ! test_context src/test/deca/context/valid/provided/test_declaration_float.deca 2>&1 | \
    grep -q 'definition: variable defined at \[5, 10\], type=float'
then
    echo "FAILED"
    exit 1
fi

if ! test_context src/test/deca/context/valid/provided/test_declaration_int.deca 2>&1 | \
    grep -q 'type: int'
then
    echo "FAILED"
    exit 1
fi

if ! test_context src/test/deca/context/valid/provided/test_conv_Float.deca 2>&1 | \
    grep -q 'ConvFloat'
then
    echo "FAILED"
    exit 1
fi




# # Testing Variable Declaration Feature

if ! test_context src/test/deca/context/invalid/provided/variable-deja-declare.deca 2>&1 | \
    grep -q -e 'Name a is already defined in localEnv !'
then
    echo "FAILED"
    exit 1
fi


if ! test_context src/test/deca/context/invalid/provided/not-defined-variable.deca 2>&1 | \
    grep -q -e  "Expression 'a' is not defined in the local environment"
then
    echo "FAILED"
    exit 1
fi




if ! test_context src/test/deca/context/valid/provided/test_AND.deca 2>&1 | \
    grep -q -e "type: boolean"
then
    echo "FAILED"
    exit 1
fi


if ! test_context src/test/deca/context/valid/provided/test_AND.deca 2>&1 | \
    grep -q -e "type: boolean"
then
    echo "FAILED"
    exit 1
fi


if ! test_context src/test/deca/context/valid/provided/test_error_identifier.deca 2>&1 | \
    grep -q -e "Type: String is undefined !"
then
    echo "FAILED"
    exit 1
fi



echo "\033[32mPASSED\033[0m"
