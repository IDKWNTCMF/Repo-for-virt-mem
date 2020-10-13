# Documentation of Virtual Memory Management Algorithms

## How to use the program

To run this program:

Write "gradle run --args="*\<path to an input file\>* *\<path to an output file\>*" in terminal.
If you don't write your preferences of input and output files then the program will take default values(input file: "input.txt", output file: "output.txt").

Example: gradle run --args="input.txt output.txt"

## Where are the tests

Some tests for external testing are in the directory named **data**.

## Speaking of input data format

The first string of the clause will be considered as a string of 2 Ints divided by ' ' (RAM size and maximum number of a possible appeal).
The second string of the clause will be considered as a list of Ints divided by ' ' (appeals).
There shouldn't be any empty strings between the clauses.

### Example of input file with one clause

3 5\
1 2 3 4 5 1 3 5 2 1

### Example of input file with several clauses

3 5\
1 2 3 4 5 1 3 5 2 1\
3 5\
1 2 3 4 5 5 4 3 2 1

## How you should understand the output

Output data for a clause will begin with string: "Clause *\<Number of the clause in input\>*:".\
Output data for an algorithm will begin with string: "*\<Name of the algorithm (FIFO, LRU or OPT)\>*: *\<Number of replacements\>* replacements"\
Then there will be a list of Ints divided by ' '.

If the i-th appealed page was in RAM when the i-th appeal happened then the i-th number would be 0
else the i-th number would be a number of the replaced frame.

### Example of output file for a clause

Clause 1:

FIFO: 8 replacements\
1 2 3 1 2 3 1 0 2 0 

LRU: 9 replacements\
1 2 3 1 2 3 1 0 3 1 

OPT: 6 replacements\
1 2 3 2 2 0 0 0 2 0 