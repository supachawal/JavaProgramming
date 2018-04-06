#include <stdio.h>
#include <stdlib.h>

static long iterationCounter;

void towerOfHanoiMove(int n, char a, char b, char c) {
    if (n > 0) {
        iterationCounter++;
        towerOfHanoiMove(n - 1, a, c, b);
        //printf("Move disc from %c to %c\n", a, c);
        towerOfHanoiMove(n - 1, b, a, c);
    }
}

int main() {
    int i;
    for (i = 0; i < 50; i++) {
        iterationCounter = 0;
        towerOfHanoiMove(i, 'A', 'B', 'C');
        printf("i = %d, #iterations = %ld\n", i, iterationCounter);
    }
    return 0;
}
