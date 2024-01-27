cd "$(dirname "$0")"/../../.. || exit 1
PATH=./src/test/script/launchers:./src/main/bin:"$PATH"



# tester le programme de test_class.deca qui doit rentre 1995 ( Anniversaire de Java :) )
rm -f ./src/test/deca/codegen/valid/provided/test_class.ass 2>/dev/null
decac ./src/test/deca/codegen/valid/provided/test_class.deca  
if [ ! -f ./src/test/deca/codegen/valid/provided/test_class.ass ]; then
    echo "Fichier test_class.ass non généré."
    exit 1
fi

resultat=$(ima ./src/test/deca/codegen/valid/provided/test_class.ass)  
rm -f ./src/test/deca/codegen/valid/provided/test_class.ass

# On code en dur la valeur attendue.
attendu="1995"

if [ "$resultat" != "$attendu" ]; then
    echo "FAILED"
    echo "expected $attendu but got instead $resultat"
    exit 1
fi



rm -f src/test/deca/codegen/invalid/test_class.ass 2>/dev/null
 

attendu2="Vous avez déclaré la méthode \[Square, setY\] plusieurs fois dans la classe !"

if ! decac ./src/test/deca/codegen/invalid/test_class.deca 2>&1 | \
    grep -q "$attendu2"; 
then
    echo "FAILED"
    exit 1
fi

echo -e  "\033[32mPASSED\033[0m"