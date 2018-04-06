# -*- coding: utf-8 -*-
"""
Created on Tue Apr 28 03:47:53 2015

@author: supachawal

Problem C. Logging

A certain forest consists of N trees, each of which is inhabited by a squirrel.

The boundary of the forest is the convex polygon of smallest area which contains every tree, 
as if a giant rubber band had been stretched around the outside of the forest.

Formally, every tree is a single point in two-dimensional space with unique coordinates (Xi, Yi), 
and the boundary is the convex hull of those points.

Some trees are on the boundary of the forest, which means they are on an edge or a corner of the polygon. 
The squirrels wonder how close their trees are to being on the boundary of the forest.

One at a time, each squirrel climbs down from its tree, examines the forest, and determines the minimum number of trees 
that would need to be cut down for its own tree to be on the boundary. It then writes that number down on a log.

Determine the list of numbers written on the log.

Input
The first line of the input gives the number of test cases, T. T test cases follow; each consists of a single line with an integer N, the number of trees, followed by N lines with two space-separated integers Xi and Yi, the coordinates of each tree. No two trees will have the same coordinates.

Output
For each test case, output one line containing "Case #x:", followed by N lines with one integer each, where line i contains the number of trees that the squirrel living in tree i would need to cut down.

Limits: -106 ≤ Xi, Yi ≤ 106.
Small dataset: 1 ≤ T ≤ 100. 1 ≤ N ≤ 15.
Large dataset: 1 ≤ T ≤ 14. 1 ≤ N ≤ 3000.

Sample

Input
2
5
0 0
10 0
10 10
0 10
5 5
9
0 0
5 0
10 0
0 5
5 5
10 5
0 10
5 10
10 10

Output
Case #1:
0
0
0
0
1
Case #2:
0
0
0
0
3
0
0
0
0

In the first sample case, there are four trees forming a square, and a fifth tree inside the square. 
Since the first four trees are already on the boundary, the squirrels for those trees each write down 0.
Since one tree needs to be cut down for the fifth tree to be on the boundary, the fifth squirrel writes down 1.
"""

import sys
import os
import re
from psutil import virtual_memory
from scipy import spatial
import time
 
def nearestConvexHullPoint(points, xy):
    if len(points) < 4:
        hullPoints = points
    else:
        hull = spatial.ConvexHull(points)
        hullPoints = [points[v] for v in hull.vertices]
    kdtree = spatial.KDTree(hullPoints)
    ret = kdtree.query(xy)
    nearest = hullPoints[ret[1]]

    for i in range(len(points)):
        if points[i] == nearest:
            ret = (ret[0], i)
            break
        
    return ret

def concave_hull(points):
    if len(points) < 4:
        # When you have a triangle, there is no sense
        # in computing an alpha shape.
        return points

    tri = spatial.Delaunay(points)
    edges = set()
    shared_edges = set()
    tris = tri.simplices
    #print 'tris =', tris

    for ia, ib, ic in tris:
        for e in [(min(ia, ib), max(ia, ib)), (min(ia, ic), max(ia, ic)), (min(ib, ic), max(ib, ic))]:
            if e not in shared_edges:
                if e not in edges:
                    edges.add(e)
                else:
                    shared_edges.add(e)
    edges -= shared_edges
    edge_points = set([e[i] for i in range(2) for e in edges])
#    print 'edge_points =', edge_points
    return [points[p] for p in edge_points]

def nearestConcaveHullPoint(points, xy):
    hull = concave_hull(points)
    kdtree = spatial.KDTree(hull)
    ret = kdtree.query(xy)
    nearest = hull[ret[1]]

    for i in range(len(points)):
        if points[i] == nearest:
            ret = (ret[0], i)
            break
        
    return ret


def findMinTreesToCut(XiYi, i):
    xy = XiYi[i]
    kdtreeHull = nearestConvexHullPoint(XiYi, xy)
    nhi = kdtreeHull[1]
    distance = kdtreeHull[0]
    
    if kdtreeHull[1] == i:
        return 0
        
    kdtree = spatial.KDTree(XiYi)
    pointSet1 = set(kdtree.query_ball_point(xy, distance - 0.01))
    if i in pointSet1:
        pointSet1.remove(i)

    pointSet2 = set(kdtree.query_ball_point(XiYi[nhi], distance - 0.01))
    if i in pointSet2:
        pointSet2.remove(i)
    
    return len(pointSet1.intersection(pointSet2)) + 1

def showString(s, limit):
    sc = s
    if len(sc) > limit:
        sc = sc[0:limit - 3] + '...'
    return sc
    
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
        N = int(textLine)
        XiYi = []
        for i in range(N):
            textLine = inputFile.readline().rstrip()
            splitted = re.split('\\s+', textLine)
            XiYi.append( (int(splitted[0]), int(splitted[1])) )
        
        print('Case #%d: N=%d, XiYi=%s,' % (testCaseNumber, N, showString(str(XiYi), 70)), end=' ')
        answer = [findMinTreesToCut(XiYi, i) for i in range(N)]

        print('answer=', end=' ')
        for i in range(N):
            print(answer[i], end=' ')
        print
        
        print('Case #%d:' % (testCaseNumber), file=outputFile, flush=True)
        for i in range(N):
            print(answer[i], file=outputFile, flush=True)

        testCaseNumber += 1
        textLine = inputFile.readline().rstrip()

    print('=============== end program (FREEMEM=%.0fm ELAPSED=%f)===============' % (mem.total/1024/1024, time.clock() - starttime))

if __name__ == '__main__':
    main(sys.argv)
