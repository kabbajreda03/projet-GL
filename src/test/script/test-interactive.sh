#! /bin/sh

# Auteur : gl22
# Version initiale : 01/01/2024

# Test minimaliste de la vérification contextuelle.
# Le principe et les limitations sont les mêmes que pour basic-synt.sh
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"


echo "\033[33mTesting interactive-testing\033[0m"

rm -f ./src/test/deca/codegen/interactive/readInt_test.ass 2>/dev/null
decac ./src/test/deca/codegen/interactive/readInt_test.deca
if [ ! -f ./src/test/deca/codegen/interactive/readInt_test.ass ]; then
    echo "Fichier readInt_test.ass non généré."
    exit 1
fi

resultat=$(echo 23 | ima ./src/test/deca/codegen/interactive/readInt_test.ass)  
rm -f ./src/test/deca/codegen/interactive/readInt_test.ass

# On code en dur la valeur attendue.
attendu=23

if [ "$resultat" != "$attendu" ]; then
    echo "FAILED"
    echo "expected $attendu but got instead $resultat"
    exit 1
fi

rm -f ./src/test/deca/codegen/interactive/readFloat_test.ass 2>/dev/null
decac ./src/test/deca/codegen/interactive/readFloat_test.deca
if [ ! -f ./src/test/deca/codegen/interactive/readFloat_test.ass ]; then
    echo "Fichier readFloat_test.ass non généré."
    exit 1
fi

resultat=$(echo 3.5 | ima ./src/test/deca/codegen/interactive/readFloat_test.ass)  
rm -f ./src/test/deca/codegen/interactive/readFloat_test.ass

# On code en dur la valeur attendue.
attendu=3.50000e+00

if [ "$resultat" != "$attendu" ]; then
    echo "FAILED"
    echo "expected $attendu but got instead $resultat"
    exit 1
fi


echo -e "\033[32mPASSED\033[0m"