# -*- coding: utf-8 -*-
"""
Created on Sat Apr 25 11:22:16 2015

@author: supachawal

Problem B. Picking Up Chicks

A flock of chickens are running east along a straight, narrow road. Each one is running with its own constant
speed. Whenever a chick catches up to the one in front of it, it has to slow down and follow at the speed of 
the other chick. You are in a mobile crane behind the flock, chasing the chicks towards the barn at the end of 
the road. The arm of the crane allows you to pick up any chick momentarily, let the chick behind it pass 
underneath and place the picked up chick back down. This operation takes no time and can only be performed on 
a pair of chicks that are immediately next to each other, even if 3 or more chicks are in a row, one after the 
other.

Given the initial locations (Xi) at time 0 and natural speeds (Vi) of the chicks, as well as the location of 
the barn (B), what is the minimum number of swaps you need to perform with your crane in order to have 
at least K of the N chicks arrive at the barn no later than time T?

You may think of the chicks as points moving along a line. Even if 3 or more chicks are at the same location, 
next to each other, picking up one of them will only let one of the other two pass through. Any swap is 
instantaneous, which means that you may perform multiple swaps at the same time, but each one will count as a 
separate swap.

Input

The first line of the input gives the number of test cases, C. C test cases follow. Each test case starts 
with 4 integers on a line -- N, K, B and T. The next line contains the N different integers Xi, in 
increasing order. The line after that contains the N integers Vi. All distances are in meters; all speeds are 
in meters per second; all times are in seconds.
Output

For each test case, output one line containing "Case #x: S", where x is the case number (starting from 1) and 
S is the smallest number of required swaps, or the word "IMPOSSIBLE".
Limits

1 ≤ C ≤ 100;
1 ≤ B ≤ 1,000,000,000;
1 ≤ T ≤ 1,000;
0 ≤ Xi < B;
1 ≤ Vi ≤ 100;
All the Xi's will be distinct and in increasing order.
Small dataset

1 ≤ N ≤ 10;
0 ≤ K ≤ min(3, N);
Large dataset

1 ≤ N ≤ 50;
0 ≤ K ≤ N; 
"""
import sys
import os
import re
from psutil import virtual_memory
import time

def swapChicks(Xi, Vi, N, K, B, T):
    ret = 0
    i = N - 1
    delayerCount = 0

    while i >= 0 and K > 0:
        if Xi[i] + Vi[i] * T >= B:
            K -= 1
            ret += delayerCount
        else:
            delayerCount += 1

        i -= 1;

    return -1 if K > 0 else ret
    
def main(argv):
    mem = virtual_memory()
    starttime = time.clock()
    print('=============== start program (FREEMEM=%.0fm)===============' % (mem.total/1024/1024))
#    inputFileName = '-sample1.in'
#    inputFileName = '-sample2.in'
#     inputFileName = '-small-practice.in'
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
        """Given the initial locations (Xi) at time 0 and natural speeds (Vi) of the chicks, 
        as well as the location of the barn (B), what is the minimum number of swaps you need to perform
        with your crane in order to have at least K of the N chicks arrive at the barn no later than time T? """
        N, K, B, T = (int(s) for s in re.split('\\s+', textLine))
        textLine = inputFile.readline().rstrip()
        Xi = [int(s) for s in re.split('\\s+', textLine)]
        textLine = inputFile.readline().rstrip()
        Vi = [int(s) for s in re.split('\\s+', textLine)]
            
        print('Case #%d: N=%d, K=%d, B=%d, T=%d,' % (testCaseNumber, N, K, B, T), end=' ')
        answer = swapChicks(Xi, Vi, N, K, B, T)
        
        print('answer=%d' % (answer))
        print('Case #%d: %s' % (testCaseNumber, answer if answer >= 0 else 'IMPOSSIBLE'), file=outputFile, flush=True)

        testCaseNumber += 1
        textLine = inputFile.readline().rstrip()
    print('=============== end program (FREEMEM=%.0fm ELAPSED=%f)===============' % (mem.total/1024/1024, time.clock() - starttime))

if __name__ == '__main__':
    main(sys.argv)
