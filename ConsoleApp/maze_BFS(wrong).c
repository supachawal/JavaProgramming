/*
TASK: maze.c
LANG: C
COMPILER: WCB
AUTHOR: Supachawal Kullanansiri
CENTER: ?????
*/
#include <stdio.h>
#include <stdlib.h>
#include <limits.h>
#include <sys/queue.h>

typedef struct MazeCell
{
    int row;
    int col;
    int cellFlag;   // 0=wall, 1=passable
    int distance;
    int bombsUsed;
    struct MazeCell *parent;
} MazeCellType;

int shortestDistance(MazeCellType *maze, int m, int n, MazeCellType *startCellPtr, MazeCellType *exitCellPtr, int *targetableWallCountPtr);

#define BOMB_USE_MAX 1
int main()
{
    int M, N, i, j;
    int row, col;
    MazeCellType *maze, *cellPtr, *startCellPtr, *exitCellPtr;
    int shortestDistanceResult, targetableWallCount;

    scanf("%d %d", &M, &N);
    maze = (MazeCellType*)malloc(M * N * sizeof(MazeCellType));
    scanf("%d %d", &row, &col);
    startCellPtr = &maze[row * N + col];
    scanf("%d %d", &row, &col);
    exitCellPtr = &maze[row * N + col];

    for (i = 0; i < M; i++)
        for (j = 0; j < N; j++)
        {
            cellPtr = &maze[i * N + j];
            cellPtr->row = i;
            cellPtr->col = j;
            scanf("%d", &cellPtr->cellFlag);
        }

    shortestDistanceResult = shortestDistance(maze, M, N, startCellPtr, exitCellPtr, &targetableWallCount);
    printf("%d\n%d", targetableWallCount, shortestDistanceResult);

    free(maze);
    return 0;
}

typedef struct Q_Entry
{
    MazeCellType *cellPtr;
    SIMPLEQ_ENTRY(Q_Entry) q_entries;
} Q_EntryType;

typedef SIMPLEQ_HEAD(SimpleQHead, Q_Entry) Q_HeadType;

void appendQueue(Q_HeadType *qHeadPtr, MazeCellType *cellPtr)
{
    Q_EntryType *elem = malloc(sizeof(Q_EntryType));
    elem->cellPtr = cellPtr;
    SIMPLEQ_INSERT_TAIL(qHeadPtr, elem, q_entries);
}

int shortestDistance(MazeCellType *maze, int m, int n, MazeCellType *startCellPtr, MazeCellType *exitCellPtr, int *targetableWallCountPtr)
{
    static int dir[][2] = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    int i, j, k, size = m * n;
    MazeCellType *p, *neighbor;
    Q_EntryType *elem;
    Q_HeadType qHead;

    SIMPLEQ_INIT(&qHead);

    for (k = 0; k < size; k++)
    {
        p = &maze[k];
        p->distance = INT_MAX;
        p->parent = NULL;
        p->bombsUsed = 0;
    }

    appendQueue(&qHead, startCellPtr);
    startCellPtr->distance = 1;

    while (!SIMPLEQ_EMPTY(&qHead))
    {
        elem = SIMPLEQ_FIRST(&qHead);
        SIMPLEQ_REMOVE_HEAD(&qHead, q_entries);

        p = elem->cellPtr;
        p->distance = (p->parent == NULL ? 1 : p->parent->distance + 1);

        for (k = 0; k < 4; k++)
        {
            i = p->row + dir[k][0];
            j = p->col + dir[k][1];

            if (i >= 0 && i < m && j >= 0 && j < n)
            {
                neighbor = &maze[i * n + j];

                if (neighbor->parent == NULL && (neighbor->cellFlag == 1 || p->bombsUsed < BOMB_USE_MAX))
                {
                    if (neighbor->cellFlag == 0)

                } && !p->bombsUsed)

            }
        elem->cellPtr->distance
    }
        .
    return 0;
}
