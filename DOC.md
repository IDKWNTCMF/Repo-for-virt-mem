# Documentation of Virtual Memory Management Algorithms

## How to use the program

To run this program:

Write "gradle run --args="*\<path to an input file\>* *\<path to an output file\>*" in terminal.
If you don't write your preferences of input and output files then the program will take default values(input file: "input.txt", output file: "output.txt").

## Where are the tests

Some tests for external testing are in the directory named **data**.

## Speaking of input data format

The first string of the clause will be considered as a string of 2 Ints divided by ' ' (m, n).
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
Then there will be a string for each appeal in the clause.

String: "Add *\<Number of the page\>* in *\<Number of the frame\>* frame" 
means that the page with this number has been added in an empty frame with this number.\
String: "Replace *\<Number of the replaced page\>* with *\<Number of the current page\>* in *\<Number of the frame\>* frame" 
means that current page replaced a page in the frame with this number.\
String: "*\<Number of the page\>*: The needed page is in memory."
means that the needed page is in memory at the moment.

### Example of output file for a clause

Clause 1:

FIFO: 8 replacements

Add 1 in 1 frame\
Add 2 in 2 frame\
Add 3 in 3 frame\
Replace 1 with 4 in 1 frame\
Replace 2 with 5 in 2 frame\
Replace 3 with 1 in 3 frame\
Replace 4 with 3 in 1 frame\
5: The needed page is in memory\
Replace 5 with 2 in 2 frame\
1: The needed page is in memory

LRU: 9 replacements

Add 1 in 1 frame\
Add 2 in 2 frame\
Add 3 in 3 frame\
Replace 1 with 4 in 1 frame\
Replace 2 with 5 in 2 frame\
Replace 3 with 1 in 3 frame\
Replace 4 with 3 in 1 frame\
5: The needed page is in memory\
Replace 1 with 2 in 3 frame\
Replace 3 with 1 in 1 frame

OPT: 6 replacements

Add 1 in 1 frame\
Add 2 in 2 frame\
Add 3 in 3 frame\
Replace 2 with 4 in 2 frame\
Replace 4 with 5 in 2 frame\
1: The needed page is in memory\
3: The needed page is in memory\
5: The needed page is in memory\
Replace 5 with 2 in 2 frame\
1: The needed page is in memory