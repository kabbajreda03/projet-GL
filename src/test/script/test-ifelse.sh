# Tester la condition sur Equals
# Tester la condition sur NotEquals
# Tester la condition sur >
# Tester la condition sur <
# Tester la condition sur >=
# Tester la condition sur <=



cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"


expected_text="\[3, 4\] IfThenElse"

# Check if the output contains the expected text
if !test_context src/test/deca/context/valid/provided/ifelse/test_ifelse.deca 2>&1 | \
    grep -q "$expected_text"; 
then
    echo "FAILED"
    exit 1

fi




expected_text="\[3, 7\] Equals"

# Check if the output contains the expected text
if test_context ./src/test/deca/context/valid/provided/ifelse/test_ifelse_assign.deca 2>&1 | \
    grep -q "$expected_text"; 
then
    echo   "\033[32mPASSED\033[0m"
else
    echo "FAILED"
    exit 1
fi


expected_text="\[3, 7\] NotEquals"

# Check if the output contains the expected text
if test_context src/test/deca/context/valid/provided/ifelse/test_ifelse_notEqual.deca 2>&1 | \
    grep -q "$expected_text"; 
then
    echo   "\033[32mPASSED\033[0m"
else
    echo "FAILED"
    exit 1
fi


expected_text="\[3, 7\] Greater"

# Check if the output contains the expected text
if test_context src/test/deca/context/valid/provided/ifelse/test_ifelse_greater.deca 2>&1 | \
    grep -q "$expected_text"; 
then
    echo   "\033[32mPASSED\033[0m"
else
    echo "FAILED"
    exit 1
fi



expected_text="\[3, 7\] Lower"

# Check if the output contains the expected text
if test_context src/test/deca/context/valid/provided/ifelse/test_ifelse_lower.deca 2>&1 | \
    grep -q "$expected_text"; 
then
    echo   "\033[32mPASSED\033[0m"
else
    echo "FAILED"
    exit 1
fi

expected_text="\[3, 7\] GreaterOrEqual"

# Check if the output contains the expected text
if test_context src/test/deca/context/valid/provided/ifelse/test_ifelse_greaterEqual.deca 2>&1 | \
    grep -q "$expected_text"; 
then
    echo   "\033[32mPASSED\033[0m"
else
    echo "FAILED"
    exit 1
fi


expected_text="\[3, 7\] LowerOrEqual"

# Check if the output contains the expected text
if test_context src/test/deca/context/valid/provided/ifelse/test_ifelse_lowerEqual.deca 2>&1 | \
    grep -q "$expected_text"; 
then
    echo   "\033[32mPASSED\033[0m"
else
    echo "FAILED"
    exit 1
fi


expected_text="\[8, 8\] IfThenElse"

# Check if the output contains the expected text
if test_context src/test/deca/context/valid/provided/ifelse/test_ifelseif.deca 2>&1 | \
    grep -q "$expected_text"; 
then
    echo   "\033[32mPASSED\033[0m"
else
    echo "FAILED"
    exit 1
fi
expected_text="\[4, 4\] IfThenElse"

# Check if the output contains the expected text
if test_context src/test/deca/context/valid/provided/ifelse/test_ifelseif.deca 2>&1 | \
    grep -q "$expected_text"; 
then
    echo   "\033[32mPASSED\033[0m"
else
    echo "FAILED"
    exit 1
fi

expected_text="Condition in ifThenElse loop must be of type boolean: int was given !"

# Check if the output contains the expected text
if test_context src/test/deca/context/invalid/provided/ifelse_not_working.deca 2>&1 | \
    grep -q "$expected_text"; 
then
    echo   "\033[32mPASSED\033[0m"
else
    echo "FAILED"
    exit 1
fi



