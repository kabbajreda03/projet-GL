#!/bin/bash


cd "$(dirname "$0")"/../../.. || exit 1


source_directory=./src/test/deca/codegen/valid/unprovided/general_tests
destination_directory=./src/test/deca/codegen/valid/unprovided/generated
expected_directory=./src/test/deca/codegen/valid/unprovided/expected
log_directory=./src/test/deca/codegen/valid/unprovided/

# Initialize counters
compile_error_count=0
match_count=0
mismatch_count=0


GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
NC='\033[0m' # No Color, to reset the color back to the default

# Create a log file
log_file="$log_directory/compile.log"
touch "$log_file"

# Create the destination directory if it doesn't exist
mkdir -p "$destination_directory"

# Function to log information about each file
log_info() {
    echo  "$1" >> "$log_file"
    echo  "Generated Output:" >> "$log_file"
    echo  "$2" >> "$log_file"
    echo  "Expected Output:" >> "$log_file"
    echo  "$3" >> "$log_file"
    echo  "-----------------------------------" >> "$log_file"
}


# Iterate through all .deca files and compile them
for decaFile in "$source_directory"/*.deca; do
    # Get the filename without extension
    filename=$(basename "$decaFile" .deca)

    # Compile the .deca file and move the .ass file to the destination directory
    decac "$decaFile"
    mv "$source_directory"/"$filename".ass "$destination_directory"


    if [ $? -eq 0 ]; then
      output=$(ima "$destination_directory"/"$filename".ass)
      expected_file="$expected_directory/$filename.expected"

      if [ -e "$expected_file" ]; then
            if diff -q <(echo "$output") "$expected_file" > /dev/null; then
              echo -e "${GREEN} Match: Output for $filename matches the expected content.${NC}"
              ((match_count++))

              # Log information about the file
              log_info "$filename" "Compiled correctly" "$(cat "$expected_file")"

            else
              echo -e "${RED} Mismatch: Output for $filename does not match the expected content.${NC}"
              ((mismatch_count++))

              log_info "$filename" "$output" "$(cat "$expected_file")"
            fi
      else
            echo -e "${RED} Warning: No expected file found for $filename ${NC}"
      fi

    else
        echo -e "${RED}Compilation failed for $filename. Please check for errors.${NC}"
        ((compile_error_count++))
        log_info "$filename" "Compilation failed" ""
    fi

done

# Display summary
echo -e "${YELLOW}Compilation errors: $compile_error_count${NC}"
echo -e "${GREEN}Matches: $match_count${NC}"
echo -e "${RED}Mismatches: $mismatch_count${NC}"

# Set exit code based on compilation errors or mismatches
if [ "$compile_error_count" -gt 0 ] || [ "$mismatch_count" -gt 0 ]; then
    exit 1
else
    exit 0
fi

