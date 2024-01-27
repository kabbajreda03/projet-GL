cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

echo  -e "\033[33mTesting class declaration with methods and fields\033[0m"

AST_Generated=src/test/deca/context/valid/provided/AST/AST-Result.txt
result_file2=src/test/deca/context/valid/provided/AST/Full-class-AST.txt

test_context src/test/deca/context/valid/provided/test_class_AST.deca > $AST_Generated


if  diff -b -w "$AST_Generated" "$result_file2" ; then
echo  -e "\033[32mPASSED\033[0m"
else
  echo -e "\e[31mTest Failed\e[0m"
  exit 1
fi





echo  -e "\033[32mPASSED\033[0m"

