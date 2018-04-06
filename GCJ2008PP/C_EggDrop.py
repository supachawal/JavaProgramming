"""
Created on Tue May 19 03:22:42 2015

@author: supachawal

Problem C. Egg Drop

Imagine that you are in a building with F floors (starting at floor 1, the lowest floor), and you have a large number of identical eggs, 
each in its own identical protective container. For each floor in the building, you want to know whether or not an egg dropped from that floor will break. 
If an egg breaks when dropped from floor i, then all eggs are guaranteed to break when dropped from any floor j ≥ i. 
Likewise, if an egg doesn't break when dropped from floor i, then all eggs are guaranteed to never break when dropped from any floor j ≤ i.

We can define Solvable(F, D, B) to be true if and only if there exists an algorithm to determine 
whether or not an egg will break when dropped from any floor of a building with F floors, with the following restrictions: 
you may drop a maximum of D eggs (one at a time, from any floors of your choosing), and you may break a maximum of B eggs. 
You can assume you have at least D eggs in your possession.

Input

The first line of input gives the number of cases, N. N test cases follow. Each case is a line formatted as:
F D B

Solvable(F, D, B) is guaranteed to be true for all input cases.

Output

For each test case, output one line containing "Case #x: " followed by three space-separated integers: Fmax, Dmin, and Bmin. 
The definitions are as follows:
    •Fmax is defined as the largest value of F' such that Solvable(F', D, B) is true, 
        or -1 if this value would be greater than or equal to 2^32 (4294967296).
    (In other words, Fmax = -1 if and only if Solvable(2^32, D, B) is true.)
    •Dmin is defined as the smallest value of D' such that Solvable(F, D', B) is true.
    •Bmin is defined as the smallest value of B' such that Solvable(F, D, B') is true.

Limits: 1 ≤ N ≤ 100. 

Small dataset: 1 ≤ F ≤ 100,  1 ≤ D ≤ 100,  1 ≤ B ≤ 100. 

Large dataset: 1 ≤ F ≤ 2000000000,  1 ≤ D ≤ 2000000000,  1 ≤ B ≤ 2000000000. 

Sample
Input 
 2
 3 3 3
 7 5 3

Output 
 Case #1: 7 2 1
 Case #2: 25 3 2
 
"""

import os
import re
import sys
import time

from psutil import virtual_memory

def canSolve(F, D, B):
    for startingFloorIndexToBreakEgg in range(F):
        eggBreakingFloors = [(i >= startingFloorIndexToBreakEgg) for i in range(F)]
        solvable = False
        minEggBreakingFloor = F
        maxEggSavingFloor = -1
        step = 1
        pivot = 0
        countD = D
        countB = B
        alreadyTried = set([])
        while (countD > 0):
            broken = eggBreakingFloors[pivot]
                
            if broken:   #broken egg
                if not (pivot in alreadyTried):
                    alreadyTried.add(pivot)
                    countD -= 1
                    if countB <= 0:
                        break
                    countB -= 1
                if pivot < minEggBreakingFloor:
                    minEggBreakingFloor = pivot
                pivot = maxEggSavingFloor + 1
                step = 1
            else: #saving egg
                if not (pivot in alreadyTried):
                    alreadyTried.add(pivot)
                    countD -= 1
                if pivot > maxEggSavingFloor:
                    maxEggSavingFloor = pivot
                pivot += step
                if pivot < minEggBreakingFloor:
                    step *= 2
                else:
                    pivot = maxEggSavingFloor + 1
                    step = 1
            
            if minEggBreakingFloor - maxEggSavingFloor == 1:
                solvable = True
                break     
    
        if not solvable:
            return False

    return True

def canSolveUsingDivideAndConquer(F, D, B):
    for startingFloorIndexToBreakEgg in range(F):
        eggBreakingFloors = [(i >= startingFloorIndexToBreakEgg) for i in range(F)]
        solvable = False
        low = 0
        high = F - 1
        minEggBreakingFloor = F
        maxEggSavingFloor = -1
        
        pivot = (low + high) // 2
        countD = D
        countB = B
        while (countD > 0):
            broken = eggBreakingFloors[pivot]
            if broken:   #broken egg
                if pivot < minEggBreakingFloor:
                    minEggBreakingFloor = pivot
                    if countB <= 0:
                        break
                    countB -= 1
                high = minEggBreakingFloor - 1
            else: #saving egg
                if pivot > maxEggSavingFloor:
                    maxEggSavingFloor = pivot
                low = maxEggSavingFloor + 1
            
            if minEggBreakingFloor - maxEggSavingFloor == 1:
                solvable = True
                break     
            pivot = (low + high) // 2
            countD -= 1
    
        if not solvable:
            return False

    return True

def solvableFmax(D, B):
    f = 0
    while canSolve(f + 1, D, B):
        f += 1
    return f

# ฺ555 By conjecture
# def solvableFmax(D, B):
#     # case of the last floor breaks egg
#     f = 0
#     if (B > 0):
#         countD = D
#         step = 1
#         while (countD > 0):
#             countD -= 1
#             f += step
#             step *= 2
#     countCaseLastFloor = f
# 
#     # case of the last floor breaks egg
#     f = 0
#     if (D > 0):
#         countB = B
#         step = 1
#         while (countB > 0):
#             countB -= 1
#             f += step
#             step *= 2
#     countCaseFirstFloor = f
#   
#     return countCaseLastFloor + countCaseFirstFloor
# #     return countCaseLastFloor
            
def showString(s, limit):
    if len(s) > limit:
        s = s[0:limit - 3] + '...'
    return s
    
def main(argv):
    mem = virtual_memory()
    starttime = time.clock()
    print('=============== start program (FREEMEM=%.0fm)===============' % (mem.total/1024/1024))
    inputFileName = '-sample1.in'
#    inputFileName = '-sample2.in'
#     inputFileName = '-small-practice.in'
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
        F, D, B = (int(s) for s in re.split('\\s+', textLine))
        print('Case #%d: F=%d, D=%d, B=%d' % (testCaseNumber, F, D, B), end=' ', flush=True)
        answer1_Fmax = solvableFmax(D, B)
        answer2_Dmin = 0#solvableDmin(F, B)
        answer3_Bmin = 0#solvableBmin(F, D)
        print('answer=%d, %d, %d' % (answer1_Fmax, answer2_Dmin, answer3_Bmin), sep='', flush=True)
        print('Case #%d: %d %d %d' % (testCaseNumber, answer1_Fmax, answer2_Dmin, answer3_Bmin), file=outputFile, flush=True)

        testCaseNumber += 1
        textLine = inputFile.readline().rstrip()

    print('=============== end program (FREEMEM=%.0fm ELAPSED=%f)===============' % (mem.total/1024/1024, time.clock() - starttime))

if __name__ == '__main__':
    main(sys.argv)
