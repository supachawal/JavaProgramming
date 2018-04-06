"""
Created on Fri May 22 01:07 2015

@author: supachawal

Problem D. Shopping Plan

You have a list of items you need to buy today, and you know the locations (represented as points on a cartesian grid) 
of a few stores in the area. You also know which of these stores are selling each item on your list, and at what price 
each store sells it. Given the price of gas, what is the minimum amount you need to spend in order to buy all the items 
on your shopping list and then drive back home? You start and end the journey at your house, which is located at (0,0).

To make matters interesting, some of the items on your list may be perishable. Whenever you make a purchase that includes 
one or more perishable items, you cannot drive to another store without first stopping back at your house. Every item on 
your shopping list is guaranteed to be sold by at least one store, so the trip will always be possible.

Input

The first line of input gives the number of cases, N. N test cases follow. Each case starts with a line formatted as

num_items num_stores price_of_gas

The next line contains the num_items items on your shopping list. The items will be space separated, and each item will 
consist of only lowercase letters. If an item is perishable, its name will be followed by a single exclamation point. 
There will be no duplicate items on your list. The next num_stores lines will each be formatted as

x_pos y_pos item1:price1 item2:price2 ...

Each of these lines gives the location of one store, along with the items available at that store and their corresponding 
prices. Only items which are on your shopping list will appear in these lists. Perishable items will not end with exclamation 
points on these lists. No item will be repeated in a store's list. Each store will offer at least one item for sale. 
No two stores will be at the same location, and no store will be located at (0,0).

Output

For each test case, output one line containing "Case #x: " followed by the minimum possible cost of the trip, 
rounded to seven decimal places. Don't forget about price_of_gas, which is the amount of money you must spend 
per unit distance that you drive.

Limits

1 ≤ N ≤ 100,
0 ≤ price_of_gas ≤ 1000,
-1000 ≤ x_pos ≤ 1000,
-1000 ≤ y_pos ≤ 1000,
1 ≤ price of each item ≤ 1000.

Small dataset

1 ≤ num_items ≤ 5,
1 ≤ num_stores ≤ 10.

Large dataset

1 ≤ num_items ≤ 15,
1 ≤ num_stores ≤ 50.

Sample

Input 

2
1 2 10
cookies
0 2 cookies:400
4 0 cookies:320
3 3 5
cookies milk! cereal
0 2 cookies:360 cereal:110
4 0 cereal:90 milk:150
-3 -3 milk:200 cookies:200

Output 
Case #1: 400.0000000
Case #2: 519.2920690
"""

import itertools
import os
from psutil import virtual_memory
from psutil._common import memoize
import re
import sys
import time


INFINITY = 1e16

inline_compute_distance = lambda a, b: ((a[0]-b[0])**2 + (a[1]-b[1])**2)**0.5
# @memoize
# def inline_compute_distance(a, b):
#     return ((a[0]-b[0])**2 + (a[1]-b[1])**2)**0.5

class ItemStoreInfo:
    def __init__(self, storeId, initPositionXY, initPrice):
        self.store_id = storeId
        self.position_xy = initPositionXY
        self.price = initPrice
    def compute_shopping_cost(self, gasPrice, fromPos):
        return inline_compute_distance(self.position_xy, fromPos) * gasPrice + self.price   
        
class ItemInfo:
    
    def __init__(self, initName):
        if initName[-1] == '!':
            self.name = initName[0:-1]
            self.is_perishable = True
        else:
            self.name = initName
            self.is_perishable = False
        self.__item_store_info_list = []

    def __hash__(self):
        return hash(self.name)

    def __eq__(self, another):
        return self.name == another.name

    def add_store_info(self, itemStoreInfo):
        self.__item_store_info_list.append(itemStoreInfo)

    def get_store_info_list(self):
        return self.__item_store_info_list

def show_string(s, limit):
    if len(s) > limit:
        s = s[0:limit - 3] + '...'
    return s

visitedStores = []
origin_pos = (0, 0)

def minimum_shopping_cost(items, gasPrice, currentPos, itemIndex, perishableItemBought=False):
    costOfGoingHome = inline_compute_distance(currentPos, origin_pos) * gasPrice
    if itemIndex < 0:
        return costOfGoingHome

    minCost = INFINITY
    for itemStoreInfo in items[itemIndex].get_store_info_list():
        if itemStoreInfo.store_id in visitedStores and itemStoreInfo.position_xy != currentPos:
            continue
        
        cost = 0.0
        if perishableItemBought and itemStoreInfo.position_xy != currentPos:
            cost += costOfGoingHome
            cost += itemStoreInfo.compute_shopping_cost(gasPrice, origin_pos)

            visitedStores.append(itemStoreInfo.store_id)
            cost += minimum_shopping_cost(items, gasPrice, itemStoreInfo.position_xy, itemIndex - 1
                                          , items[itemIndex].is_perishable)
            visitedStores.pop()
        else:
            cost += itemStoreInfo.compute_shopping_cost(gasPrice, currentPos)

            visitedStores.append(itemStoreInfo.store_id)
            cost += minimum_shopping_cost(items, gasPrice, itemStoreInfo.position_xy, itemIndex - 1
                                          , perishableItemBought or items[itemIndex].is_perishable)
            visitedStores.pop()
        
        if cost < minCost:
            minCost = cost

    return minCost
    
def main(argv):
    mem = virtual_memory()
    starttime = time.clock()
    print('=============== start program (FREEMEM=%.0fm)===============' % (mem.total/1024/1024))
#     inputFileName = '-sample1.in'
    inputFileName = '-sample2.in'
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
        splitted = re.split('\\s+', textLine)
        num_items = int(splitted[0])
        num_stores = int(splitted[1])
        price_of_gas = float(splitted[2])
        
        textLine = inputFile.readline().rstrip()
        splitted = re.split('\\s+', textLine)
        items = [ItemInfo(item_name) for item_name in splitted]
        assert(num_items == len(items))
        itemMapIndex = {items[i].name: i for i in range(num_items)} 
        for i in range(num_stores):
            textLine = inputFile.readline().rstrip()
            splitted = re.split('\\s+', textLine)
            storePos = (float(splitted[0]), float(splitted[1]))
            for item_with_price in splitted[2:]:
                subsplitted = re.split(':', item_with_price)
                items[itemMapIndex[subsplitted[0]]].add_store_info(ItemStoreInfo(i, storePos, float(subsplitted[1])))

        print('Case #%d: #items=%d, #stores=%d, gas_price=%g,' % (testCaseNumber, num_items, num_stores, price_of_gas), end=' ', flush=True)
        
        answer = INFINITY
        for permuted_items in itertools.permutations(items, num_items):
            visitedStores.clear()
            answer = min(answer, minimum_shopping_cost(permuted_items, price_of_gas, origin_pos, num_items - 1))
        print('answer=%.7f' % (answer), flush=True)
        print('Case #%d: %.7f' % (testCaseNumber, answer), file=outputFile, flush=True)

        testCaseNumber += 1
        textLine = inputFile.readline().rstrip()

    print('=============== end program (FREEMEM=%.0fm ELAPSED=%f)===============' % (mem.total/1024/1024, time.clock() - starttime))

if __name__ == '__main__':
    main(sys.argv)
