/*
TASK: location.c
LANG: C
COMPILER: WCB
AUTHOR: Supachawal Kullanansiri
CENTER: ?????
*/

#include <stdio.h>
#include <stdlib.h>

long maxPopulation(int *matrix, int m, int n, int k);

int main()
{
    int M, N, K, i, j;
    int *matrix;

    scanf("%d%d%d", &M, &N, &K);
    matrix = (int*)malloc(M * N * sizeof(int));

    for (i = 0; i < M; i++)
        for (j = 0; j < N; j++)
            scanf("%d", matrix + i * N + j);

    long result = maxPopulation(matrix, M, N, K);
    printf("%ld\n", result);

    free(matrix);
    return 0;
}

long maxPopulation(int *matrix, int m, int n, int k)
{
    long result = 0;
    long sum = 0;
    int i, j, a = 0, b = 0;
    int i2, j2;

    for (i = 0; i < k; i++)
        for (j = 0; j < k; j++)
            sum += matrix[i * n + j];

    for (i = 0; i <= m - k; i++)
    {
        if (i == a + 1)
        {
            for (j2 = b; j2 < b + k; j2++)
                sum += matrix[(a + k) * n + j2] - matrix[a * n + j2];
            a = i;
        }

        if ((i & 1) == 0)
        {
            for (j = 0; j <= n - k; j++)
            {
                if (j == b + 1)
                {
                    for (i2 = a; i2 < a + k; i2++)
                        sum += matrix[i2 * n + b + k] - matrix[i2 * n + b];
                    b = j;
                }

                if (sum > result)
                    result = sum;
            }
        }
        else
        {
            for (j = b; j >= 0; j--)
            {
                if (j == b - 1)
                {
                    for (i2 = a; i2 < a + k; i2++)
                        sum += matrix[i2 * n + j] - matrix[i2 * n + j + k];
                    b = j;
                }

                if (sum > result)
                    result = sum;
            }
        }
    }

    return result;
}

