#!/bin/bash

cd "$(dirname "$0")"/../../.. || exit 1

source_directory=./src/test/deca/codegen/valid/unprovided/optimTests
destination_directory=./src/test/deca/codegen/valid/unprovided/generated
perf_directory=./src/test/deca/codegen/valid/unprovided/

timestamp=$(date +"%Y-%m-%d %HH%Mmin%Ss")
perf_file="$perf_directory/perf.log"

# Create the destination directory if it doesn't exist
mkdir -p "$destination_directory"

# Create the timestamped performance log file
echo "Timestamp: $timestamp" >> "$perf_file"

# Iterate through all .deca files and compile them
for decaFile in "$source_directory"/*.deca; do
    # Get the filename without extension
    filename=$(basename "$decaFile" .deca)

    # Compile the .deca file and move the .ass file to the destination directory
    decac "$decaFile"
    mv "$source_directory"/"$filename".ass "$destination_directory"

    decac -op "$decaFile"
    mv "$source_directory"/"$filename".ass "$destination_directory"/"$filename"op.ass
    echo "" >> "$perf_file"
    # Execute 'time' on 'ima' and append the user time to the performance log file
    echo "$filename performances are :" >> "$perf_file"
    { time ima "$destination_directory"/"$filename".ass; } 2>&1 | grep real | cut -d' ' -f2 >> "$perf_file"

    echo "$filename optimized performances are :" >> "$perf_file"
    { time ima "$destination_directory"/"$filename"op.ass; } 2>&1 | grep real | cut -d' ' -f2 >> "$perf_file"

    echo "" >> "$perf_file"

done
