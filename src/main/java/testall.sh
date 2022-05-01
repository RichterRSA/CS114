#!/bin/bash

# v2.0
# Guaranteed to work in FHARGA (Windows) and NARGA (Ubuntu).
# No other environments will be supported.

# Change your program name here
JAVA_PROGRAM_NAME="SUxxxxxxxx"

echo ""
echo "CS113/114 Testing Script testall.sh"

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
    echo "Test #$2"
    cat files/$1_$2_params.txt | java $JAVA_PROGRAM_NAME `xargs` < files/$1_$2_moves.txt | diff -Z -s - files/$1_$2_out.txt
}

# Category 1, 2
for category in {1..2}
do
    report_category_number $category
    for number in {1..10}
    do
        execute_test $category $number
    done
done

# Category 3, 4
for category in {3..5}
do
    report_category_number $category
    for number in {1..5}
    do
        execute_test $category $number
    done
done


report_category_number 6
for number in {1..10}
do
    execute_test 6 $number
done

report_category_number 7
for number in {1..3}
do
    execute_test 7 $number
done
