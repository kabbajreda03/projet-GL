cd "$(dirname "$0")"/../../.. || exit 1
PATH=./src/test/script/launchers:"$PATH"


result_file="./src/test/deca/syntax/valid/provided/decompile_results/decompile_result.deca"
result_file2="./src/test/deca/syntax/valid/provided/decompile_results/decompile_result2.deca"



# tester la decompilation de print,println
input_file="./src/test/deca/syntax/valid/provided/test_class_decompile.deca"
decac -p "$input_file" > "$result_file"
decac -p "$result_file" > "$result_file2"

if diff "$result_file" "$result_file2" &> /dev/null; then
echo  -e "\033[32m Decompile test PASSED\033[0m"
else
  echo -e "\e[31mTest Failed\e[0m"
  exit 1
fi









