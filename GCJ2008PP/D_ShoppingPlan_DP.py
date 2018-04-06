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

1 โ�ค N โ�ค 100,
0 โ�ค price_of_gas โ�ค 1000,
-1000 โ�ค x_pos โ�ค 1000,
-1000 โ�ค y_pos โ�ค 1000,
1 โ�ค price of each item โ�ค 1000.

Small dataset

1 โ�ค num_items โ�ค 5,
1 โ�ค num_stores โ�ค 10.

Large dataset

1 โ�ค num_items โ�ค 15,
1 โ�ค num_stores โ�ค 50.

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
import re
import sys
import time
import math

inline_compute_distance = lambda a, b: ((a[0]-b[0])**2 + (a[1]-b[1])**2)**0.5
INFINITY = 1e+16

class ItemInfo:
    def __init__(self, itemId, initName):
        if initName[-1] == '!':
            self.item_id = itemId
            self.name = initName[0:-1]
            self.is_perishable = True
        else:
            self.name = initName
            self.is_perishable = False

        self.store_list = []

    def __hash__(self):
        return hash(self.name)

    def __lt__(self, another):
        return self.name < another.name

    def __eq__(self, another):
        return self.name == another.name

class StoreInfo:
    def __init__(self, storeId, initPositionXY):
        self.store_id = storeId
        self.position_xy = initPositionXY
        self.__items = {}
    
    def __hash__(self):
        return hash(self.store_id)

    def __lt__(self, another):
        return self.store_id < another.store_id

    def __eq__(self, another):
        return self.store_id == another.store_id

    def compute_transportation_cost_to_origin(self, gasPrice):
        return gasPrice * (self.position_xy[0]**2 + self.position_xy[1]**2)**0.5 
       
    def add_item(self, itemInfo, price):
        self.__items[itemInfo] = price

    def get_items(self):
        return self.__items
    
def minimum_shopping_cost(stores, items, gasPrice):
    # dynamic programming algorithm with worst case running time of O(m^2 * n)
    m = len(stores)
    n = len(items) + 1
    R = [[INFINITY] * n for i in range(m)]  # accumulated item amount (element i = 0 means home, j = 0 means nothing on hand)
    F = [[-1] * n for i in range(m)]    #buy from F
#Case #1: #items=3, #stores=2, gas_price=1, answer=5454.0824588
# gk cr! qq!
# -883 -292 cr:953 gk:967
# 526 -734 qq:794 gk:41

    for i in range(m):
        R[i][0] = stores[i].compute_transportation_cost_to_origin(gasPrice)

    for j in range(1, n):
        item = items[j - 1]
        
        # minimize cost of the new item (wherever we buy it from, we update as if we go to every stores[i])
        for i in range(m):
            minCost = R[i][j]
            bestStore = i
            for store, price in item.store_list:
                k = store.store_id
                if R[k][j] == INFINITY:  
                    R[k][j] = R[k][j - 1] + price     # initial price without moving

                cost = R[k][j]
                if gasPrice > 0.0 and k != i:   # if move from other store that never visited
                    visited = False
                    for v in range(j):
                        if k == F[i][v]:
                            visited = True
                            break
                        
                    if not visited:
                        if item.is_perishable:
                            cost += stores[k].compute_transportation_cost_to_origin(gasPrice) \
                                    + stores[i].compute_transportation_cost_to_origin(gasPrice)
                        else:
                            cost += gasPrice * inline_compute_distance(stores[k].position_xy
                                                                       , stores[i].position_xy) 
    
                if cost < minCost:
                    minCost = cost
                    bestStore = k
            
            R[i][j] = minCost
            F[i][j] = bestStore
            
    return R[0][n - 1]

def main(argv):
    mem = virtual_memory()
    starttime = time.clock()
    print('=============== start program (FREEMEM=%.0fm)===============' % (mem.total/1024/1024))
#     inputFileName = '-sample1.in'
#     inputFileName = '-sample2.in'
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
        splitted = re.split('\\s+', textLine)
        num_items = int(splitted[0])
        num_stores = int(splitted[1])
        price_of_gas = float(splitted[2])
        
        textLine = inputFile.readline().rstrip()
        splitted = re.split('\\s+', textLine)
        items = []
        itemId = 0
        for item_name in splitted:
            items.append(ItemInfo(itemId, item_name))
            itemId += 1
        assert(num_items == len(items))
        nPerishable = len([1 for item in items if item.is_perishable]) 
        itemMapIndex = {items[i].name: i for i in range(num_items)} 
        stores = []
        stores.append(StoreInfo(0, (0, 0)))
        for i in range(num_stores):
            textLine = inputFile.readline().rstrip()
            splitted = re.split('\\s+', textLine)
            storePos = (float(splitted[0]), float(splitted[1]))
            store = StoreInfo(i + 1, storePos)
            for item_with_price in splitted[2:]:
                subsplitted = re.split(':', item_with_price)
                item = items[itemMapIndex[subsplitted[0]]]
                price = float(subsplitted[1])
                store.add_item(item, price)
                item.store_list.append((store, price))
            stores.append(store)
        print('Case #%d: #npitems=%d, #pItems=%d, #stores=%d, gas_price=%g,' % (testCaseNumber, num_items - nPerishable, nPerishable, num_stores, price_of_gas), end=' ', flush=True)
        
        answer = minimum_shopping_cost(stores, items, price_of_gas)
        print('answer=%.7f' % (answer), sep='', flush=True)
        print('Case #%d: %.7f' % (testCaseNumber, answer), file=outputFile, flush=True)

        testCaseNumber += 1
        textLine = inputFile.readline().rstrip()

    print('=============== end program (FREEMEM=%.0fm ELAPSED=%f)===============' % (mem.total/1024/1024, time.clock() - starttime))

if __name__ == '__main__':
    main(sys.argv)
