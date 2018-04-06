/*
TASK: sport.c
LANG: C
COMPILER: WCB
AUTHOR: Supachawal Kullanansiri
CENTER: ?????
*/

#include <stdio.h>
#include <stdlib.h>

void printPossibleResult(char *buffer, int seq, int remainW, int remainL);

int main()
{
    int k, nW, nL;
    char *buffer;

    scanf("%d%d%d", &k, &nW, &nL);
    buffer = (char*)malloc((2 * k - 1 - nW - nL) * sizeof(char));
    printPossibleResult(buffer, 0, k - nW, k - nL);
    free(buffer);
    return 0;
}

void printPossibleResult(char *buffer, int seq, int remainW, int remainL)
{
    int i;
    if (remainW <= 0 || remainL <= 0)
    {
        if (seq > 0)
        {
            putchar(buffer[0]);
            for (i = 1; i < seq; i++)
                printf(" %c", buffer[i]);
        }
        putchar('\n');
    }
    else
    {
        if (remainW > 0)
        {
            buffer[seq] = 'W';
            printPossibleResult(buffer, seq + 1, remainW - 1, remainL);
        }

        if (remainL > 0)
        {
            buffer[seq] = 'L';
            printPossibleResult(buffer, seq + 1, remainW, remainL - 1);
        }
    }
}
