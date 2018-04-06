/*
TASK: maze.c
LANG: C
COMPILER: WCB
AUTHOR: Supachawal Kullanansiri
CENTER: ?????
*/
#include <stdio.h>
#include <stdlib.h>

typedef struct MazeCell
{
    int row;
    int col;
    int cellFlag;   // 0=wall, 1=passable, 2=start
    struct MazeCell *parent;
    int targetableWall;
} MazeCellType;

int shortestDistance(MazeCellType *maze, int m, int n, MazeCellType *currentCellPtr
                    , MazeCellType *exitCellPtr, int bombsAvailable);

static int targetableWallCount;

int main()
{
    int M, N, i, j;
    int row, col;
    MazeCellType *maze, *cellPtr, *startCellPtr, *exitCellPtr;
    int shortestDistanceResult;

    scanf("%d %d", &M, &N);
    maze = (MazeCellType*)malloc(M * N * sizeof(MazeCellType));
    scanf("%d %d", &row, &col);
    startCellPtr = &maze[(row - 1) * N + col - 1];
    scanf("%d %d", &row, &col);
    exitCellPtr = &maze[(row - 1) * N + col - 1];

    for (i = 0; i < M; i++)
        for (j = 0; j < N; j++)
        {
            cellPtr = &maze[i * N + j];
            cellPtr->row = i;
            cellPtr->col = j;
            scanf("%d", &cellPtr->cellFlag);
            cellPtr->parent = NULL;
            cellPtr->targetableWall = 0;
        }

    startCellPtr->cellFlag = 2;

    targetableWallCount = 0;
    shortestDistanceResult = shortestDistance(maze, M, N, startCellPtr, exitCellPtr, 1);
    printf("%d\n%d", targetableWallCount, shortestDistanceResult + 1);

    free(maze);
    return 0;
}

static int traverse_dir[4][2] = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

int shortestDistance(MazeCellType *maze, int m, int n, MazeCellType *currentCellPtr
                     , MazeCellType *exitCellPtr, int bombsAvailable)
{
    int i, j, k, result = m * n, branchResult;
    MazeCellType *p, *neighbor;

    if (currentCellPtr == exitCellPtr)
    {
        p = currentCellPtr;
        while (p != NULL)
        {
            if (p->cellFlag == 0 && !p->targetableWall)
            {
                targetableWallCount++;
                p->targetableWall = 1;
            }
            p = p->parent;
        }
        return 0;
    }

    for (k = 0; k < 4; k++)
    {
        i = currentCellPtr->row + traverse_dir[k][0];
        j = currentCellPtr->col + traverse_dir[k][1];

        if (i >= 0 && i < m && j >= 0 && j < n)
        {
            neighbor = &maze[i * n + j];

            if (neighbor->parent == NULL
                && neighbor->cellFlag != 2
                && (neighbor->cellFlag != 0 || bombsAvailable > 0))
            {
                neighbor->parent = currentCellPtr;
                branchResult = 1 + shortestDistance(maze, m, n, neighbor, exitCellPtr, bombsAvailable - (neighbor->cellFlag ^ 1));
                neighbor->parent = NULL;

                if (branchResult < result)
                    result = branchResult;
            }

        }
    }

    return result;
}
