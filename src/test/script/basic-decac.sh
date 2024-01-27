cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"



# decac -b


echo "Testing decac -b "

resultat=$(decac -b  2>&1 )

# On code en dur la valeur attendue.
attendu="GL g22"

if [ "$resultat" != "$attendu" ]; then
    echo "FAILED"
    echo "expected $attendu but got instead $resultat"
    exit 1
fi


echo "Testing decac -v"

resultat=$(decac -v ./src/test/deca/syntax/valid/provided/helloWorld.deca )
attendu=""
if [ "$resultat" != "$attendu" ]; then
    echo "FAILED"
    echo "expected $attendu but got instead $resultat"
    exit 1
fi




echo "Testing decac -n"
resultat=$(decac -n ./src/test/deca/syntax/valid/provided/test_noCheck_option.deca )
resultat=$(ima ./src/test/deca/syntax/valid/provided/test_noCheck_option.ass) 
rm -f ./src/test/deca/syntax/valid/provided/test_noCheck_option.ass

# On code en dur la valeur attendue.
attendu="no check"

if [ "$resultat" != "$attendu" ]; then
    echo "FAILED"
    echo "expected $attendu but got instead $resultat"
    exit 1
fi
rm -f ./src/test/deca/syntax/valid/provided/test_noCheck_option.ass


echo "Testing decac -r X"
resultat1=$(decac -r 9 ./src/test/deca/syntax/valid/provided/helloWorld.deca  2>&1  )
resultat2=$(decac -r 17 ./src/test/deca/syntax/valid/provided/helloWorld.deca  2>&1  )
resultat3=$(decac -r 2 ./src/test/deca/syntax/valid/provided/helloWorld.deca  2>&1  ) 
attendu1=""
attendu2="Number of registers needs to be between 4 and 16 "
attendu3="Number of registers needs to be between 4 and 16 "

if [ "$resultat1" != "$attendu1" ] || [ "$resultat2" != "$attendu2" ] || [ "$resultat3" != "$attendu3" ]; then
    echo "FAILED"
    echo "expected $attendu but got instead $resultat"
    exit 1
fi
rm -f ./src/test/deca/syntax/valid/provided/helloWorld.ass

echo "Testing decac -d"
expected_text="Writing assembler file ..."


if ! decac -d ./src/test/deca/syntax/valid/provided/helloWorld.deca 2>&1 | \
    grep -q "$expected_text"; 
then
    echo "FAILED"
    exit 1
fi

echo "Testing decac -P"
rm -f ./src/test/deca/syntax/valid/provided/helloWorld.ass 2>/dev/null
rm -f ./src/test/deca/syntax/valid/provided/MyFILE.ass 2>/dev/null
file1=./src/test/deca/syntax/valid/provided/helloWorld.deca 
file2=./src/test/deca/syntax/valid/provided/MyFILE.deca
decac -P $file1 $file2

if [ ! -f ./src/test/deca/syntax/valid/provided/helloWorld.ass ] && [ ! -f ./src/test/deca/syntax/valid/provided/myFILE.ass ]; then
    echo "FAILED"
    exit 1
fi
rm -f ./src/test/deca/syntax/valid/provided/helloWorld.ass 2>/dev/null
rm -f ./src/test/deca/syntax/valid/provided/MyFILE.ass 2>/dev/null


echo "Testing decac -p -v"

# On code en dur la valeur attendue.
attendu="option -p and -v cannot be taken together"
if ! decac -p -v ./src/test/deca/syntax/valid/provided/test_noCheck_option.deca 2>&1 | \
    grep -q "$attendu"; 
then
    echo "FAILED"
    exit 1
fi