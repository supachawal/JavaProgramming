/*
 * SMPLSUM_SimpleSum_SUCCESS1.cpp
 *
 *  Created on: Jan 18, 2016
 *      Author: chutima
 */

#include <iostream>
#include <cstdio>
#include <string.h>

using namespace std;

#define  gc  getchar()
#define PROBLEM_SIZE 10000001

int primeFactor[PROBLEM_SIZE];

void fr(int &X)  { register char CC=gc;  while(CC<33)CC=gc;  X=0;  while(CC>47 && CC<58) { X=(X<<1)+(X<<3)+CC-48; CC=gc; }  }

int main() {
    int t, n, i, j, p;
    long long ans, val;

    // Modified Sieve of Eratosthenes
    bool *isPrime = new bool[PROBLEM_SIZE];
    isPrime[0] = false;
    isPrime[1] = false;
    memset(isPrime + 2, true, PROBLEM_SIZE * sizeof(bool));
    primeFactor[2] = 2;
    for (i = 1; i < PROBLEM_SIZE; i += 2) {
        primeFactor[i] = i;
    }
    for (i = 4; i < PROBLEM_SIZE; i += 2) {
        isPrime[i] = false;
        primeFactor[i] = 2;
    }
    for (i = 3; i * i < PROBLEM_SIZE; i += 2) {
        if (isPrime[i]) {
            // sieve the multiple of i
            int step = i * 2;
            for (j = i * i; j < PROBLEM_SIZE; j += step) {
                if (isPrime[j]) {
                    isPrime[j] = false;
                    primeFactor[j] = i;
                }
            }
        }
    }
    delete isPrime;

    fr(t);
    while (t-- > 0) {
        fr(n);
        ans = 1;
        while (n > 1) {
            p = primeFactor[n];
            val = 1;
            while (n % p == 0) {
                n /= p;
                val *= p;
            }
            if (val == p)
                ans *= (val*val - p + 1);
            else
                ans *= (val*val * p + 1) / (p + 1);
        }
        printf("%lld\n", ans);
    }

    return 0;
}

