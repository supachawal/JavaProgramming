# -*- coding: utf-8 -*-
"""
Created on Sun Apr 26 00:23:58 2015

@author: supachawal

Problem C. Your Rank is Pure

    Pontius: You know, I like this number 127, I don't know why.
    Woland: Well, that is an object so pure. You know the prime numbers.
    Pontius: Surely I do. Those are the objects possessed by our ancient masters hundreds of years ago. Oh, yes,
        why then? 127 is indeed a prime number as I was told.
    Woland: Not... only... that. 127 is the 31st prime number; then, 31 is itself a prime, it is the 11th; 
        and 11 is the 5th; 5 is the 3rd; 3, you know, is the second; and finally 2 is the 1st.
    Pontius: Heh, that is indeed... purely prime.

The game can be played on any subset S of positive integers. A number in S is considered pure with respect to S if,
starting from it, you can continue taking its rank in S, and get a number that is also in S, 
until in finite steps you hit the number 1, which is not in S.

When n is given, in how many ways you can pick S, a subset of {2, 3, ..., n}, so that n is pure, 
with respect to S? The answer might be a big number, you need to output it modulo 100003.

Input: The first line of the input gives the number of test cases, T. T lines follow.
    Each contains a single integer n.
Output: For each test case, output one line containing "Case #x: y", where x is the case number (starting from 1) 
    and y is the answer as described above.

Limits
T ≤ 100.
Small dataset
2 ≤ n ≤ 25.
Large dataset
2 ≤ n ≤ 500.

Sample

Input
2
5
6

Output
Case #1: 5
Case #2: 8

Initial approach
Let's study the process described in the problem statement. Suppose the rank of number N with respect to
 set S is K. Since N is the largest number in S, that just means the number of elements in S is K.

Then let's consider the set S' = S ∩ {1, 2, ..., K}. From the definition of a pure number, 
K is now pure with respect to S'. Have we got a Dynamic Programming solution yet?

Does that mean that we've managed to reduce the problem for N to a smaller problem for K? 
Not yet: suppose we know the number of possible sets S' for which K is pure. 
How do we find the number of sets S that contain this set (and for which N is pure and that have K elements)?

In order to do that, we need to know how many numbers are there in S'. 
Suppose there are K' numbers in S'. 
Then the number of ways to extend this set S' back to S is the number of ways to
choose K-K'-1 numbers from the set {K+1, K+2, ..., N-1}.

"""

import sys
import os
import re
from psutil import virtual_memory

def main(argv):
    mem = virtual_memory()
    print '=============== start program (FREEMEM=%.0fm)===============' % (mem.total/1024/1024)
#    inputFileName = '-sample1.in'
#    inputFileName = '-sample2.in'
#    inputFileName = '-small-practice.in'
    inputFileName = '-large-practice.in'
    inputFileName = os.path.basename(argv[0])[0:1] + inputFileName
    if len(argv) > 1:
        inputFileName = argv[1]
        
    outputFileName = inputFileName.split('.in', 1)[0] + '.out'
    print '%s --> %s' % (inputFileName, outputFileName)
    inputFile = open(inputFileName, 'r')
    outputFile = open(outputFileName, 'w')
    textLine = inputFile.readline().rstrip()
    testCaseCount = int(textLine) 
    testCaseNumber = 1
    textLine = inputFile.readline().rstrip()
    
    while testCaseNumber <= testCaseCount:
        N, K, B, T = (int(s) for s in re.split('\\s+', textLine))
        textLine = inputFile.readline().rstrip()
        Xi = [int(s) for s in re.split('\\s+', textLine)]
        textLine = inputFile.readline().rstrip()
        Vi = [int(s) for s in re.split('\\s+', textLine)]
            
        print 'Case #%d: N=%d, K=%d, B=%d, T=%d,' % (testCaseNumber, N, K, B, T),
#        answer = swapChicks(Xi, Vi, N, K, B, T)
        
        print 'answer=%d' % (answer)
        print >> outputFile, 'Case #%d: %s' % (testCaseNumber, answer if answer >= 0 else 'IMPOSSIBLE')

        testCaseNumber += 1
        textLine = inputFile.readline().rstrip()
    print '=============== end program (FREEMEM=%.0fm)===============' % (mem.total/1024/1024)

if __name__ == '__main__':
    main(sys.argv)
