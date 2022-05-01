#!/bin/bash

# v2.0
# Guaranteed to work in FHARGA (Windows) and NARGA (Ubuntu).
# No other environments will be supported.

# Change your program name here
JAVA_PROGRAM_NAME="SUxxxxxxxx"

echo ""
echo "CS113/114 Testing Script testone.sh"

echo "Deleting old files..."
rm Std*.class "${JAVA_PROGRAM_NAME}.class"
echo "Compiling..."
javac Std*.java "${JAVA_PROGRAM_NAME}.java"


report_category_number () {
    echo ""
    echo "========================================================================"
    echo "Test category $1"
    echo "========================================================================"
}

execute_test () {
    echo "Arguments:"; echo -n "  "
    cat files/$1_$2_params.txt
    echo "Moves:"; echo -n "  "
    cat files/$1_$2_moves.txt
    echo "Comparison:"
	echo "---------------------------------------"
    cat files/$1_$2_params.txt | java $JAVA_PROGRAM_NAME `xargs` < files/$1_$2_moves.txt | diff -Z -s - files/$1_$2_out.txt
}

echo "Category $1, case $2"
execute_test $1 $2
