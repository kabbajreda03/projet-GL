#! /bin/sh

# Auteur : gl22
# Version initiale : 01/01/2024

# Encore un test simpliste. On compile un fichier (cond0.deca), on
# lance ima dessus, et on compare le résultat avec la valeur attendue.

# Ce genre d'approche est bien sûr généralisable, en conservant le
# résultat attendu dans un fichier pour chaque fichier source.
cd "$(dirname "$0")"/../../..  

PATH=./src/test/script/launchers:./src/main/bin:"$PATH"








# Tester l'implementation de print et println

# Tester la declaration et initialisation d'un int et float et boolean
rm -f ./src/test/deca/codegen/valid/provided/test_print_println.ass 2>/dev/null
decac ./src/test/deca/codegen/valid/provided/test_print_println.deca  
if [ ! -f ./src/test/deca/codegen/valid/provided/test_print_println.ass ]; then
    echo "Fichier test_print_println.ass non généré."
    exit 1
fi

resultat=$(ima ./src/test/deca/codegen/valid/provided/test_print_println.ass)  
rm -f ./src/test/deca/codegen/valid/provided/test_print_println.ass

# On code en dur la valeur attendue.
attendu="This Deca needs more sugar"

if [ "$resultat" != "$attendu" ]; then
    echo "FAILED"
    echo "expected $attendu but got instead $resultat"
    exit 1
fi



# Tester la declaration et initialisation d'un int et float et boolean
rm -f ./src/test/deca/codegen/valid/provided/test_declaration_init.ass 2>/dev/null
decac ./src/test/deca/codegen/valid/provided/test_declaration_init.deca  
if [ ! -f ./src/test/deca/codegen/valid/provided/test_declaration_init.ass ]; then
    echo "Fichier test_declaration_init.ass non généré."
    exit 1
fi

resultat=$(ima ./src/test/deca/codegen/valid/provided/test_declaration_init.ass)  
rm -f ./src/test/deca/codegen/valid/provided/test_declaration_init.ass

# On code en dur la valeur attendue.
attendu="54.50000e+00"

if [ "$resultat" != "$attendu" ]; then
    echo "FAILED"
    echo "expected $attendu but got instead $resultat"
    exit 1
fi



# Tester la conversion de int en float
rm -f ./src/test/deca/codegen/valid/provided/test_conv_float.ass 2>/dev/null
decac ./src/test/deca/codegen/valid/provided/test_conv_float.deca  
if [ ! -f ./src/test/deca/codegen/valid/provided/test_conv_float.ass ]; then
    echo "Fichier test_conv_float.ass non généré."
    exit 1
fi

resultat=$(ima ./src/test/deca/codegen/valid/provided/test_conv_float.ass)  
rm -f ./src/test/deca/codegen/valid/provided/test_conv_float.ass

# On code en dur la valeur attendue.
attendu="7.50000e+00"

if [ "$resultat" != "$attendu" ]; then
    echo "FAILED"
    echo "expected $attendu but got instead $resultat"
    exit 1
fi



# Tester la boucle while

rm -f ./src/test/deca/codegen/valid/provided/test_while.ass 2>/dev/null
decac ./src/test/deca/codegen/valid/provided/test_while.deca  
if [ ! -f ./src/test/deca/codegen/valid/provided/test_while.ass ]; then
    echo "Fichier test_while.ass non généré."
    exit 1
fi

resultat=$(ima ./src/test/deca/codegen/valid/provided/test_while.ass)  
rm -f ./src/test/deca/codegen/valid/provided/test_while.ass

# On code en dur la valeur attendue.
attendu="10"

if [ "$resultat" != "$attendu" ]; then
    echo "FAILED"
    echo "expected $attendu but got instead $resultat"
    exit 1
fi



# Tester la condition if else if

rm -f ./src/test/deca/codegen/valid/provided/test_ifelse.ass 2>/dev/null
decac ./src/test/deca/codegen/valid/provided/test_ifelse.deca  
if [ ! -f ./src/test/deca/codegen/valid/provided/test_ifelse.ass ]; then
    echo "Fichier test_ifelse.ass non généré."
    exit 1
fi

resultat=$(ima ./src/test/deca/codegen/valid/provided/test_ifelse.ass)  
rm -f ./src/test/deca/codegen/valid/provided/test_ifelse.ass

# On code en dur la valeur attendue.
attendu="Here is your coffee Sir / Here is your decaf coffee Sir as you like it"

if [ "$resultat" != "$attendu" ]; then
    echo "FAILED"
    echo "expected $attendu but got instead $resultat"
    exit 1
fi








# Tester les operations arithmetiques

rm -f ./src/test/deca/codegen/valid/provided/test_op_arithmetique.ass 2>/dev/null
decac ./src/test/deca/codegen/valid/provided/test_op_arithmetique.deca  
if [ ! -f ./src/test/deca/codegen/valid/provided/test_op_arithmetique.ass ]; then
    echo "Fichier test_op_arithmetique.ass non généré."
    exit 1
fi

resultat=$(ima ./src/test/deca/codegen/valid/provided/test_op_arithmetique.ass)
rm -f ./src/test/deca/codegen/valid/provided/test_op_arithmetique.ass

# On code en dur la valeur attendue.
attendu="128520"

if [ "$resultat" != "$attendu" ]; then
    echo "FAILED"
    echo "expected $attendu but got instead $resultat"
    exit 1
fi



# Tester AND et OR

rm -f src/test/deca/context/valid/provided/test_AND.ass 2>/dev/null
decac src/test/deca/context/valid/provided/test_AND.deca 
if [ ! -f src/test/deca/context/valid/provided/test_AND.deca ]; then
    echo "Fichier test_op_arithmettest_AND.ass non généré."
    exit 1
fi

resultat=$(ima src/test/deca/context/valid/provided/test_AND.ass)
rm -f src/test/deca/context/valid/provided/test_AND.ass

# On code en dur la valeur attendue.
attendu="hey let's go"

if [ "$resultat" != "$attendu" ]; then
    echo "FAILED"
    echo "expected $attendu but got instead $resultat"
    exit 1
fi

# Tester le declenchement d'erreure d'une division par 0

rm -f src/test/deca/codegen/invalid/zeroDivision.ass 2>/dev/null
decac src/test/deca/codegen/invalid/zeroDivision.deca  
if [ ! -f src/test/deca/context/valid/provided/test_AND.deca ]; then
    echo "Fichier src/test/deca/codegen/invalid/zeroDivision.ass non généré."
    exit 1
fi

resultat=$(ima src/test/deca/codegen/invalid/zeroDivision.ass) 
rm -f src/test/deca/codegen/invalid/zeroDivision.ass

# On code en dur la valeur attendue.
attendu="Error: Division by Zero"

if [ "$resultat" != "$attendu" ]; then
    echo "FAILED"
    echo "expected $attendu but got instead $resultat"
    exit 1
fi
# Tester le declenchement d'erreure d'un debordement de flaot

decac src/test/deca/codegen/invalid/floatOverflow.deca


if ! decac src/test/deca/codegen/invalid/floatOverflow.deca  2>&1 | grep -q  "Invalid float" ; then
    echo "FAILED"
    exit 1
fi

echo "\033[32mPASSED\033[0m"




