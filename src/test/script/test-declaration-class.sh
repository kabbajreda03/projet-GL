cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"


if ! test_synt src/test/deca/context/valid/provided/test_class_decl.deca 2>&1  | \
    grep -q "\[2, 17\] \[visibility = PROTECTED\] DeclField";
then
    echo "FAILED"
    exit 1
fi
if ! test_synt src/test/deca/context/valid/provided/test_class_decl.deca  | \
    grep -q "\[3, 7\] \[visibility = PUBLIC\] DeclField";
then
    echo "FAILED"
    exit 1
fi 

if ! test_synt src/test/deca/context/valid/provided/test_class_decl.deca  | \
    grep -q "ListDeclMethod \[List with 1 elements\]";
then
    echo "FAILED"
    exit 1

fi 




echo  -e "\033[32mPASSED\033[0m"

