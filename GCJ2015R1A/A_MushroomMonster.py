# -*- coding: utf-8 -*-
"""
Created on Sun Apr 26 04:35:42 2015

@author: supachawal

Problem A. Mushroom Monster

Kaylin loves mushrooms. Put them on her plate and she'll eat them up! In this problem she's eating a plate of mushrooms, and Bartholomew is putting more pieces on her plate.

In this problem, we'll look at how many pieces of mushroom are on her plate at 10-second intervals. Bartholomew could put any non-negative integer number of mushroom pieces down at any time, and the only way they can leave the plate is by being eaten.

Figure out the minimum number of mushrooms that Kaylin could have eaten using two different methods of computation:

    Assume Kaylin could eat any number of mushroom pieces at any time.
    Assume that, starting with the first time we look at the plate, Kaylin eats mushrooms at a constant rate 
        whenever there are mushrooms on her plate. 

For example, if the input is 10 5 15 5:

With the first method, Kaylin must have eaten at least 15 mushroom pieces: 
    first she eats 5, then 10 more are put on her plate, then she eats another 10.
    There's no way she could have eaten fewer pieces.

With the second method, Kaylin must have eaten at least 25 mushroom pieces. 
    We can determine that she must eat mushrooms at a rate of at least 1 piece per second. 
    She starts with 10 pieces on her plate. In the first 10 seconds, she eats 10 pieces, 
    and 5 more are put on her plate. In the next 5 seconds, she eats 5 pieces, then her plate 
    stays empty for 5 seconds, and then Bartholomew puts 15 more pieces on her plate. 
    Then she eats 10 pieces in the last 10 seconds.
    
Input: The first line of the input gives the number of test cases, T. T test cases follow. 
    Each will consist of one line containing a single integer N, followed by a line containing N 
    space-separated integers mi; the number of mushrooms on Kaylin's plate at the start, and at 10-second intervals.
Output: For each test case, output one line containing "Case #x: y z", where x is the test case number (starting from 1), y is the minimum number of mushrooms Kaylin could have eaten using the first method of computation, and z is the minimum number of mushrooms Kaylin could have eaten using the second method of computation.

Limits
1 ≤ T ≤ 100.
Small dataset: 2 ≤ N ≤ 10.    0 ≤ mi ≤ 100.
Large dataset: 2 ≤ N ≤ 1000.  0 ≤ mi ≤ 10000.

Sample

Input

4
4
10 5 15 5
2
100 100
8
81 81 81 81 81 81 81 0
6
23 90 40 0 100 9
	
Output

Case #1: 15 25
Case #2: 0 0
Case #3: 81 567
Case #4: 181 244
"""

import os
import re
import sys
import time

from psutil import virtual_memory


def eatAnyQuantityAnyTime(N, mi):
    return sum([max(0, mi[i - 1] - mi[i]) for i in range(1, N)])

def eatConstantly(N, mi):
    c = max([mi[i - 1] - mi[i] for i in range(1, N)])
    return sum([min(c, mi[i]) for i in range(N - 1)])

def showString(s, limit):
    if len(s) > limit:
        s = s[0:limit - 3] + '...'
    return s
    
def main(argv):
    mem = virtual_memory()
    starttime = time.clock()
    print('=============== start program (FREEMEM=%.0fm)===============' % (mem.total/1024/1024))
#    inputFileName = '-sample1.in'
#    inputFileName = '-sample2.in'
#    inputFileName = '-small-practice.in'
    inputFileName = '-large-practice.in'
    inputFileName = os.path.basename(__file__)[0] + inputFileName
    if len(argv) > 1:
        inputFileName = argv[1]
        
    outputFileName = inputFileName.split('.in', 1)[0] + '.out'
    print('%s --> %s' % (inputFileName, outputFileName))
    inputFile = open(inputFileName, 'r')
    outputFile = open(outputFileName, 'w')
    textLine = inputFile.readline().rstrip()
    testCaseCount = int(textLine) 
    testCaseNumber = 1
    textLine = inputFile.readline().rstrip()
    
    while testCaseNumber <= testCaseCount:
        N = int(textLine) 
        textLine = inputFile.readline().rstrip()
        mi = [int(s) for s in re.split('\\s+', textLine)]
        N = min(N, len(mi))
        print('Case #%d: N=%d, data={%s},' % (testCaseNumber, N, showString(textLine, 60)), end=' ')
        answer1 = eatAnyQuantityAnyTime(N, mi)
        answer2 = eatConstantly(N, mi)
        print('answer=%d %d' % (answer1, answer2))
        print('Case #%d: %d %d' % (testCaseNumber, answer1, answer2), file=outputFile, flush=True)

        testCaseNumber += 1
        textLine = inputFile.readline().rstrip()

    print('=============== end program (FREEMEM=%.0fm ELAPSED=%f)===============' % (mem.total/1024/1024, time.clock() - starttime))

if __name__ == '__main__':
    main(sys.argv)
