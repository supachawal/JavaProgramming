/*
TASK: electricity.c
LANG: C
COMPILER: WCB
AUTHOR: Supachawal Kullanansiri
CENTER: ?????
*/

#include <stdio.h>
#include <stdlib.h>

const long INF = 500000L * 2000L;

long minimizedSumCostInBetween(int *P, int k, int i, int prev);

int main()
{
    int N, k, i;
    int *P;

    scanf("%d%d", &N, &k);
    P = (int*)malloc(N * sizeof(int));

    for (i = 0; i < N; i++)
        scanf("%d", P + i);

    long minSum = minimizedSumCostInBetween(P, k, N - 2, N - 1)
                    +  P[0] + P[N - 1];

    printf("%ld\n", minSum);

    free(P);
    return 0;
}

long minimizedSumCostInBetween(int *P, int k, int i, int prev)
{
    if (i < prev - k)
        return INF;

    if (i <= 0)
        return 0;

    long choice1 = P[i] + minimizedSumCostInBetween(P, k, i - 1, i);
    long choice2 = minimizedSumCostInBetween(P, k, i - 1, prev);
    return choice1 <= choice2 ? choice1 : choice2;
}
