cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"




# tester l'erreur de definition de la meme classe 2 fois

if ! test_context src/test/deca/context/invalid/provided/class-deja-definie.deca 2>&1  | \
    grep -q "Class A is already defined";
then
    echo "FAILED"
    exit 1
fi

# tester l'erreur sur l'heritage d'une classe non definie
if ! test_context src/test/deca/context/invalid/provided/class-is-not-defined.deca 2>&1  | \
    grep -q "Super Class B is not defined !";
then
    echo "FAILED"
    exit 1
fi



# tester l'erreur sur l'accces sur une method qui n'existe pas dans la classe
if ! test_context src/test/deca/context/invalid/provided/method-is-not-defined.deca 2>&1  | \
    grep -q " not defined in the local environment";
then
    echo "FAILED"
    exit 1
fi

if ! test_context src/test/deca/context/invalid/provided/access-invalid-field.deca 2>&1  | \
    grep -q " 'y' is not defined in the local environment";
then
    echo "FAILED"
    exit 1
fi

# tester une method qui retourne void avec un void
if ! test_context src/test/deca/context/invalid/provided/void-return.deca 2>&1  | \
    grep -q "Return Type should not be void !";
then
    echo "FAILED"
    exit 1
fi

# tester l'acces a un field protected en dehors de la classe fille
if ! test_context src/test/deca/context/invalid/provided/access-protected-field.deca 2>&1  | \
    grep -q "Cannot get access to field x from current class";
then
    echo "FAILED"
    exit 1
fi



 


echo "\033[32mPASSED\033[0m"