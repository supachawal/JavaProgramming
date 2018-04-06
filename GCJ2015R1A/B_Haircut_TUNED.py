# -*- coding: utf-8 -*-
"""
Created on Sun Apr 26 06:18:03 2015

@author: supachawal

Problem B. Haircut

You are waiting in a long line to get a haircut at a trendy barber shop. The shop has B barbers on duty, and they are numbered 1 through B. It always takes the kth barber exactly Mk minutes to cut a customer's hair, and a barber can only cut one customer's hair at a time. Once a barber finishes cutting hair, he is immediately free to help another customer.

While the shop is open, the customer at the head of the queue always goes to the lowest-numbered barber who is available. When no barber is available, that customer waits until at least one becomes available.

You are the Nth person in line, and the shop has just opened. Which barber will cut your hair?

Input: The first line of the input gives the number of test cases, T. T test cases follow; each consists of two lines. The first contains two space-separated integers B and N -- the number of barbers and your place in line. The customer at the head of the line is number 1, the next one is number 2, and so on. The second line contains M1, M2, ..., MB.
Output: For each test case, output one line containing "Case #x: y", where x is the test case number (starting from 1) and y is the number of the barber who will cut your hair.

Limits
1 ≤ T ≤ 100.
1 ≤ N ≤ 109.

Small dataset
1 ≤ B ≤ 5.
1 ≤ Mk ≤ 25.

Large dataset
1 ≤ B ≤ 1000.
1 ≤ Mk ≤ 100000.

Sample

Input
3
2 4
10 5
3 12
7 7 7
3 8
4 2 1

Output
Case #1: 1
Case #2: 3
Case #3: 1

In Case #1, you are the fourth person in line, and barbers 1 and 2 take 10 and 5 minutes, respectively, to cut hair. When the shop opens, the first customer immediately has the choice of barbers 1 and 2, and she will choose the lowest-numbered barber, 1. The second customer will immediately be served by barber 2. The third customer will wait since there are no more free barbers. After 5 minutes, barber 2 will finish cutting the second customer's hair, and will serve the third customer. After 10 minutes, both barbers 1 and 2 will finish; you are next in line, and you will have the choice of barbers 1 and 2, and will choose 1. 
"""

from functools import reduce
import os
import re
import sys
import time

from psutil import virtual_memory

def GCD(a, b):
    """ The Euclidean Algorithm """
    a = abs(a)
    b = abs(b)
    while a != 0:
        a, b = b%a, a
    return b
        
def getBarber(B     # barber count
             , customerIndex    # customer index (zero-based)
             , Mk   # barber timings
             ):
    ret = 0
    t = 0
    b = -1
    
    gcd = reduce(GCD, Mk)
    lcm = reduce(lambda a,b: a*b/GCD(a,b), Mk)
    repeat = sum([lcm // mki for mki in Mk])
    if customerIndex > repeat:
        customerIndex %= repeat
        
    while b < customerIndex:
        i = 0
        for i in range(B):     # for every barber
            if t % Mk[i] == 0:
                b += 1
                if b == customerIndex:
                    ret = i
                    break
        t += gcd

    return ret

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
    inputFileName = '-small-practice.in'
#     inputFileName = '-large-practice.in'
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
        B, N = (int(s) for s in re.split('\\s+', textLine))
        textLine = inputFile.readline().rstrip()
        Mk = [int(s) for s in re.split('\\s+', textLine)]
        print('Case #%d: B=%d, N=%d, Mk=[%s],' % (testCaseNumber, B, N, showString(textLine, 70))),
        answer = getBarber(B, N - 1, Mk) + 1
        print('answer=%d' % (answer))
        print('Case #%d: %d' % (testCaseNumber, answer), file=outputFile, flush=True)

        testCaseNumber += 1
        textLine = inputFile.readline().rstrip()

    print('=============== end program (FREEMEM=%.0fm ELAPSED=%f)===============' % (mem.total/1024/1024, time.clock() - starttime))

if __name__ == '__main__':
    main(sys.argv)

