#! /bin/sh

# Auteur : gl22
# Version initiale : 01/01/2024

# Test minimaliste de la vérification contextuelle.
# Le principe et les limitations sont les mêmes que pour basic-synt.sh
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"





# One class Testing


if ! test_context src/test/deca/context/valid/provided/test_New_class.deca | \
    grep -q -F -f  src/test/deca/context/valid/provided/AST/New_class_AST.txt
then
    echo "FAILED"
    exit 1
fi


if ! test_context src/test/deca/context/valid/provided/test_New_class_extended.deca | \
    grep -q -F -f  src/test/deca/context/valid/provided/AST/New_class_extended_AST.txt
then
    echo "FAILED"
    exit 1
fi




if ! test_context src/test/deca/context/invalid/provided/Child_class_New_Mother.deca 2>&1 | \
    grep -q   "Trying to assign B to A"
then
    echo "FAILED"
    exit 1
fi


echo -e "\033[32mPASSED\033[0m"