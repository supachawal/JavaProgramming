"""
Created on Tue May 19 02:01:42 2015

@author: supachawal


Problem A. Alien Numbers

The decimal numeral system is composed of ten digits, which we represent as "0123456789" (the digits in a system are written from lowest to highest). 
Imagine you have discovered an alien numeral system composed of some number of digits, which may or may not be the same as those used in decimal. 
For example, if the alien numeral system were represented as "oF8", then the numbers one through ten would be (F, 8, Fo, FF, F8, 8o, 8F, 88, Foo, FoF). 
We would like to be able to work with numbers in arbitrary alien systems. More generally, we want to be able to convert an arbitrary number that's written 
in one alien system into a second alien system.

Input

The first line of input gives the number of cases, N. N test cases follow. Each case is a line formatted as
alien_number source_language target_language

Each language will be represented by a list of its digits, ordered from lowest to highest value. No digit will be repeated in any representation, 
all digits in the alien number will be present in the source language, and the first digit of the alien number will not be the lowest valued digit of the 
source language (in other words, the alien numbers have no leading zeroes). Each digit will either be a number 0-9, an uppercase or lowercase letter, 
or one of the following symbols !"#$%&'()*+,-./:;<=>?@[\]^_`{|}~ 

Output

For each test case, output one line containing "Case #x: " followed by the alien number translated from the source language to the target language.

Limits

1 ≤ N ≤ 100. 

Small dataset

1 ≤ num digits in alien_number ≤ 4,
 2 ≤ num digits in source_language ≤ 16,
 2 ≤ num digits in target_language ≤ 16. 

Large dataset

1 ≤ alien_number (in decimal) ≤ 1000000000,
 2 ≤ num digits in source_language ≤ 94,
 2 ≤ num digits in target_language ≤ 94. 

Sample

Input 
 4
 9 0123456789 oF8
 Foo oF8 0123456789
 13 0123456789abcdef 01
 CODE O!CDE? A?JM!.

Output 
 Case #1: Foo
 Case #2: 9
 Case #3: 10011
 Case #4: JAM!
 
"""

import os
import re
import sys
import time

from psutil import virtual_memory


def composeAlienNumber(digits, n):
    radix = len(digits)
    digit = n % radix
    ret = digits[digit]
    n //= radix
    while n > 0:
        digit = n % radix
        ret += digits[digit]
        n //= radix
    return ret[::-1]    # tricky to reverse string

def extractAlienNumber(digits, numberString):
    ret = 0
    radix = len(digits)
    digitMap = {digits[x]: x for x in range(radix)}
    posVal = 1
    for i in range(len(numberString) - 1, -1, -1):
        ret += digitMap[numberString[i]] * posVal
        posVal *= radix
        
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
        numberString, sourceDigits, destDigits = re.split('\\s+', textLine)
        print('Case #%d: data={%s},' % (testCaseNumber, showString(textLine, 70)), end=' ', flush=True)
        answer = composeAlienNumber(destDigits, extractAlienNumber(sourceDigits, numberString))
        print('answer=', answer, sep='')
        print('Case #%d: %s' % (testCaseNumber, answer), file=outputFile, flush=True)

        testCaseNumber += 1
        textLine = inputFile.readline().rstrip()

    print('=============== end program (FREEMEM=%.0fm ELAPSED=%f)===============' % (mem.total/1024/1024, time.clock() - starttime))

if __name__ == '__main__':
    main(sys.argv)
