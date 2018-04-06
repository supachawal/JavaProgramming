"""
Created on Tue May 19 03:22:42 2015

@author: supachawal

Problem B. Always Turn Left

You find yourself standing outside of a perfect maze. A maze is defined as "perfect" if it meets the following conditions: 
1.It is a rectangular grid of rooms, R rows by C columns.
2.There are exactly two openings on the outside of the maze: the entrance and the exit. The entrance is always on the north wall, while the exit could be on any wall.
3.There is exactly one path between any two rooms in the maze (that is, exactly one path that does not involve backtracking).

You decide to solve the perfect maze using the "always turn left" algorithm, which states that you take the leftmost fork at every opportunity. If you hit a dead end, you turn right twice (180 degrees clockwise) and continue. (If you were to stick out your left arm and touch the wall while following this algorithm, you'd solve the maze without ever breaking contact with the wall.) Once you finish the maze, you decide to go the extra step and solve it again (still always turning left), but starting at the exit and finishing at the entrance.

The path you take through the maze can be described with three characters: 'W' means to walk forward into the next room, 'L' means to turn left (or counterclockwise) 90 degrees, and 'R' means to turn right (or clockwise) 90 degrees. You begin outside the maze, immediately adjacent to the entrance, facing the maze. You finish when you have stepped outside the maze through the exit. For example, if the entrance is on the north and the exit is on the west, your path through the following maze would be WRWWLWWLWWLWLWRRWRWWWRWWRWLW:
 
If the entrance and exit were reversed such that you began outside the west wall and finished out the north wall, your path would be WWRRWLWLWWLWWLWWRWWRWWLW. Given your two paths through the maze (entrance to exit and exit to entrance), your code should return a description of the maze.

Input

The first line of input gives the number of cases, N. N test cases follow. Each case is a line formatted as
entrance_to_exit exit_to_entrance

All paths will be at least two characters long, consist only of the characters 'W', 'L', and 'R', and begin and end with 'W'.

Output

For each test case, output one line containing "Case #x:" by itself. The next R lines give a description of the R by C maze. There should be C characters in each line, representing which directions it is possible to walk from that room. Refer to the following legend:

Character   

Can walk north?   

Can walk south?   

Can walk west?   

Can walk east?   

1 Yes No No No 
2 No Yes No No 
3 Yes Yes No No 
4 No No Yes No 
5 Yes No Yes No 
6 No Yes Yes No 
7 Yes Yes Yes No 
8 No No No Yes 
9 Yes No No Yes 
a No Yes No Yes 
b Yes Yes No Yes 
c No No Yes Yes 
d Yes No Yes Yes 
e No Yes Yes Yes 
f Yes Yes Yes Yes 

Limits

1 ≤ N ≤ 100. 

Small dataset

2 ≤ len(entrance_to_exit) ≤ 100,
 2 ≤ len(exit_to_entrance) ≤ 100. 

Large dataset

2 ≤ len(entrance_to_exit) ≤ 10000,
 2 ≤ len(exit_to_entrance) ≤ 10000. 

Sample

Input 
 
 2
 WRWWLWWLWWLWLWRRWRWWWRWWRWLW WWRRWLWLWWLWWLWWRWWRWWLW
 WW WW
  
Output 
 
 Case #1:
 ac5
 386
 9c7
 e43
 9c5
 Case #2:
 3
 
"""

import os
import re
import sys
import time

from psutil import virtual_memory

def mazeCellCode(canWalkNorth=False, canWalkSouth=False, canWalkWest=False, canWalkEast=False):
    return (1 if canWalkNorth else 0) | (2 if canWalkSouth else 0) | (4 if canWalkWest else 0) | (8 if canWalkEast else 0)

def surveyMeshDimension(forwardPath, backwardPath):
    twoWayPath = forwardPath[1:-1] + 'RR' + backwardPath[1:-1]
    i, j = 0, 0
    di, dj = 1, 0
    left, right, bottom = 0, 0, 0

    for c in twoWayPath:
        if c == 'W':
            i, j = i + di, j + dj
            
            left = min(left, j)
            right = max(right, j)
            bottom = max(bottom, i)
        else:
            if c == 'L':
                di, dj = -dj, di
            elif c == 'R':
                di, dj = dj, -di

    return (bottom + 1, right - left + 1, j - left)       

def discoverMeshCodes(forwardPath, backwardPath, m, n, startColIndex):
    twoWayPath = forwardPath + 'RR' + backwardPath
    i, j = -1, startColIndex
    di, dj = 1, 0
    mesh = [[0] * n for i in range(m)]
    
    for c in twoWayPath:
        if c == 'W':
            if i >= 0 and i < m and j >= 0 and j < n:
                mesh[i][j] |= mazeCellCode((di < 0), (di > 0), (dj < 0), (dj > 0)) 

            i, j = i + di, j + dj

# REMOVED: No need            
#             if i >= 0 and i < m and j >= 0 and j < n:
#                 mesh[i][j] |= mazeCellCode((di > 0), (di < 0), (dj > 0), (dj < 0)) 
        else:
            if c == 'L':
                di, dj = -dj, di
            elif c == 'R':
                di, dj = dj, -di

    ret = ''
    for i in range(m):
        ret += '\n'
        ret += ''.join([hex(x)[2:] for x in mesh[i]])
    return ret       
                
def showString(s, limit):
    if len(s) > limit:
        s = s[0:limit - 3] + '...'
    return s
    
def main(argv):
    mem = virtual_memory()
    starttime = time.clock()
    print('=============== start program (FREEMEM=%.0fm)===============' % (mem.total/1024/1024))
#     inputFileName = '-sample1.in'
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
        forwardPath, backwardPath = re.split('\\s+', textLine)
        print('Case #%d: data={%s},' % (testCaseNumber, showString(textLine, 70)), end=' ', flush=True)
        twoWayPath = ''
        if not (forwardPath[0] == 'W' and forwardPath[-1] == 'W' and backwardPath[0] == 'W' and backwardPath[-1] == 'W'):
            print('Error: unexpected input', file=sys.stderr)
            sys.exit(-1)

        m, n, startColIndex = surveyMeshDimension(forwardPath, backwardPath)
        answer = discoverMeshCodes(forwardPath, backwardPath, m, n, startColIndex)
        print('answer=', answer, sep='')
        print('Case #%d: %s' % (testCaseNumber, answer), file=outputFile, flush=True)

        testCaseNumber += 1
        textLine = inputFile.readline().rstrip()

    print('=============== end program (FREEMEM=%.0fm ELAPSED=%f)===============' % (mem.total/1024/1024, time.clock() - starttime))

if __name__ == '__main__':
    main(sys.argv)
