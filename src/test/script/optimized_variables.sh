#!/bin/bash

cd "$(dirname "$0")"/../../.. || exit 1

GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color, to reset the color back to the default
PASSED="PASSED"
FAILED="FAILED"

TESTNAME="optimized_variables"

UNPROVIDED=src/test/deca/codegen/valid/unprovided

rm -f "$UNPROVIDED"/"$TESTNAME".ass  2>/dev/null

decac "$UNPROVIDED"/"$TESTNAME".deca 2>/dev/null

# Check the exit status of the command
if [ $? -ne 0 ]; then
    echo "Command failed. The file prolly doesn't exist..."
    exit 1
fi

RESULT="$UNPROVIDED"/"$TESTNAME".ass

# You can also check for multiple strings

if cat "$RESULT" | grep -qE "LOAD #42, R2|STORE R2, 1(GB)|LOAD #0x1.5p5, R2|STORE R2, 2(GB)"; then
    echo -e "${GREEN}${PASSED}${NC}"
    exit 0
else
    echo -e "${RED}${FAILED}${NC}"
fi



