#!/bin/bash
gradle jar
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
java -jar build/libs/prog-2020-virt-mem-IDKWNTCMF.jar $inputFile output.txt
diff -q $outputFile output.txt
if [ $? -eq 1 ] 
then 
echo "test $testNumber failed"
exit $testNumber
else
echo "test $testNumber passed"
fi
done
