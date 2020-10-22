#!/bin/bash
jar src/main/kotlin/VirtualMemory.kt VirtualMemory.jar
for (( testNumber = 1; testNumber <= 10; testNumber++ ))
do
if [ $testNumber -eq 10 ]
then
inputFile="data/Input${testNumber}.txt"
outputFile="data/Output${testNumber}.txt"
else
inputFile="data/Input0${testNumber}.txt"
outputFile="data/Output0${testNumber}.txt"
fi
java -jar VirtualMemory.jar $inputFile output.txt
diff -q $outputFile output.txt
if [ $? -eq 1 ] 
then 
echo "test $testNumber failed"
else
echo "test $testNumber passed"
fi
done
