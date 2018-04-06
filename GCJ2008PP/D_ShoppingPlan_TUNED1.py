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
import re
import sys
import time
import math

inline_compute_distance = lambda a, b: ((a[0]-b[0])**2 + (a[1]-b[1])**2)**0.5
INFINITY = 1e+16

def possible_subsets_descending(iterable, minSubsetSize, maxSubsetSize):
    for r in range(maxSubsetSize, minSubsetSize-1, -1):
        yield from itertools.combinations(iterable, r)

def cartesian_product(setList):
    lastIndex = len(setList) - 1
    r = []

    def __cartesian_product_recursive(index):
        for y in setList[index]:
            r.append(y)
            if index == lastIndex:
                yield r
            else:
                yield from __cartesian_product_recursive(index + 1)
            r.pop()
            
    if lastIndex >= 0:
        yield from __cartesian_product_recursive(0)

class ItemInfo:
    def __init__(self, initName):
        if initName[-1] == '!':
            self.name = initName[0:-1]
            self.is_perishable = True
        else:
            self.name = initName
            self.is_perishable = False

#NO NEED:         self.store_list = []

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
        self.__items_original = {}
        self.__items = {}
        self.perishable_item_bought = False
    
    def __hash__(self):
        return hash(self.store_id) + hash(self.perishable_item_bought)

    def __lt__(self, another):
        return self.store_id < another.store_id \
            or self.store_id == another.store_id and not self.perishable_item_bought and another.perishable_item_bought

    def __eq__(self, another):
        return self.store_id == another.store_id and self.perishable_item_bought == another.perishable_item_bought

    def compute_transportation_cost_to_origin(self, gasPrice):
        return gasPrice * (self.position_xy[0]**2 + self.position_xy[1]**2)**0.5 
       
    def add_item_original(self, itemInfo, price):
        self.__items_original[itemInfo] = price

    def copy_items_from_original(self, itemInfo=None):
        if itemInfo is None:
            for item, price in self.__items_original.items():
                self.__items[item] = price
        else:
            self.__items[itemInfo] = self.__items_original[itemInfo]
             
    def get_items(self):
        return self.__items
    
def circuit_transportation_cost(store_route, gasPrice, storesWithPerishableItems):
    pos = (0, 0)
    ret = 0.0

#     print('(0,0)', end='')
    for st in store_route:
#         print('->(%d,%d)' % (st.position_xy[0], st.position_xy[1]), end='')
        ret += inline_compute_distance(pos, st.position_xy) * gasPrice
        if st in storesWithPerishableItems:
#         for item, price in st.get_items().items():
#             print(':%s@%g' % (item.name, price), end='')
#             print('->(0,0)', end='')
            ret += st.compute_transportation_cost_to_origin(gasPrice)
            pos = (0, 0)
        else:
            pos = st.position_xy

    if pos != (0, 0):
#         print('->(0,0)', end='')
        ret += inline_compute_distance(pos, (0, 0)) * gasPrice
#     print()
    return ret

def minimum_shopping_cost(stores, items, gasPrice):
    minCost = INFINITY
    itemSet = set(items)
    nS = len(stores)
    nR = min(len(itemSet), nS)
    totalLoops = sum([(math.factorial(nS) / math.factorial(r) / math.factorial(nS-r)) for r in range(1, nR+1)])
#     print('totalLoops = %g, loopCount = ...' % totalLoops, end='', flush=True)
    print('totalLoops=%g,' % totalLoops, end=' ', flush=True)
    loopCount = 0
    linePrintCount = 0
    perishableItems = []
    perishableItemsStores = []
    storesWithPerishableItems = set()
#     notEnoughCombiSet = set()
    
    for st_combi in possible_subsets_descending(stores, 1, nR):
        loopCount += 1
#         if loopCount % 10 == 0:
#             if linePrintCount % 20 == 0:
#                 print()
#             linePrintCount += 1
#             print(loopCount, end=' ', flush=True)

        # check for covering
#         relax = False
#         for notEnoughCombi in notEnoughCombiSet:
#             if notEnoughCombi.issuperset(st_combi):
#                 relax = True
#                 break
#         if relax:
#             continue
          
        itemSetCopy = itemSet.copy()
        for st in st_combi:
            st.copy_items_from_original()
            itemSetCopy -= st.get_items().keys()
        
        if len(itemSetCopy) > 0:    # if not covered all items
#             notEnoughCombiSet.add(st_combi)
            continue
             
        relax = False
        totalNonperishableItemCost = 0.0
        for item in itemSet:
            if not item.is_perishable or gasPrice == 0: #prune the farther pricier
                choiceStore = None
                choicePrice = INFINITY
                for st in st_combi:
                    checkedPrice = st.get_items().get(item)
                    if checkedPrice is not None:
                        if choiceStore is None \
                            or checkedPrice < choicePrice \
                            or (checkedPrice == choicePrice 
                                and st.compute_transportation_cost_to_origin(gasPrice)
                                    < choiceStore.compute_transportation_cost_to_origin(gasPrice)
                               ) \
                            : 
                            choiceStore = st
                            choicePrice = checkedPrice
                # prune
                for st in st_combi:
                    st_items = st.get_items()
                    if item in st_items and st is not choiceStore:
                        st_items.pop(item)
                        if len(st_items) == 0:
                            relax = True
                            break
                
                if relax:
                    break

                totalNonperishableItemCost += choicePrice       
                
        if not relax:
            if gasPrice == 0.0:
                minCost = min(minCost, totalNonperishableItemCost)
            else:
                perishableItems.clear()
                perishableItemsStores.clear()
                
                for item in itemSet:
                    if item.is_perishable:
                        perishableItems.append(item)
                        perishableItemsStores.append([st for st in st_combi if item in st.get_items()])
                
                nPerishable = len(perishableItems)
                if nPerishable == 0: 
                    storesWithPerishableItems.clear()
                    minTransportationCost1 = INFINITY
                    for store_route in itertools.permutations(st_combi):
                        minTransportationCost1 = min(minTransportationCost1 
                                                    ,circuit_transportation_cost(store_route, gasPrice, storesWithPerishableItems)
                                                    )
                    minCost = min(minCost, totalNonperishableItemCost + minTransportationCost1)
                else:
                    for perishableItemsChoiceStore in cartesian_product(perishableItemsStores):
                        relax = False
                        totalPerishableItemCost = 0.0
                        storesWithPerishableItems.clear()
                        # prune
                        for i in range(nPerishable):
                            item = perishableItems[i]
                            itemStore = perishableItemsChoiceStore[i]
                            storesWithPerishableItems.add(itemStore) 
                            for st in st_combi:
                                st_items = st.get_items()
                                if st is itemStore:
                                    if item not in st_items:
                                        st.copy_items_from_original(item)
                                    totalPerishableItemCost += st_items[item]
                                else:
                                    if item in st_items: 
                                        st_items.pop(item)
                                        if len(st_items) == 0:
                                            relax = True
                                            break
                            if relax:
                                break
                
                        if not relax:
                            minTransportationCost2 = INFINITY
                            print(st_combi)
                            for store_route in itertools.permutations(st_combi):
                                minTransportationCost2 = min(minTransportationCost2 
                                                            ,circuit_transportation_cost(store_route, gasPrice, storesWithPerishableItems)
                                                            )
                            minCost = min(minCost, totalNonperishableItemCost + totalPerishableItemCost + minTransportationCost2)
 
#     print(loopCount, end=' ', flush=True)
    assert(minCost < INFINITY)
    return minCost

def main(argv):
    mem = virtual_memory()
    starttime = time.clock()
    print('=============== start program (FREEMEM=%.0fm)===============' % (mem.total/1024/1024))
#     inputFileName = '-sample1.in'
#     inputFileName = '-sample2.in'
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
        splitted = re.split('\\s+', textLine)
        num_items = int(splitted[0])
        num_stores = int(splitted[1])
        price_of_gas = float(splitted[2])
        
        textLine = inputFile.readline().rstrip()
        splitted = re.split('\\s+', textLine)
        items = [ItemInfo(item_name) for item_name in splitted]
        assert(num_items == len(items))
        nPerishable = len([1 for item in items if item.is_perishable]) 
        itemMapIndex = {items[i].name: i for i in range(num_items)} 
        stores = []
        for i in range(num_stores):
            textLine = inputFile.readline().rstrip()
            splitted = re.split('\\s+', textLine)
            storePos = (float(splitted[0]), float(splitted[1]))
            store = StoreInfo(i, storePos)
            for item_with_price in splitted[2:]:
                subsplitted = re.split(':', item_with_price)
                item = items[itemMapIndex[subsplitted[0]]]
                store.add_item_original(item, float(subsplitted[1]))
#NO NEED:                 item.store_list.append(store)
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
