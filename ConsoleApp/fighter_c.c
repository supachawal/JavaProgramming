/*
TASK: fighter.c
LANG: C++
COMPILER: WCB
AUTHOR: Supachawal Kullanansiri
CENTER: ?????
*/

#include <stdio.h>

int main()
{
    int P;
    int i, n, attackNumber, attacker, victim, attackPower;
    int fighterP[2];
    int comboCharge[2] = {0, 0};

    scanf("%d", &P);
    fighterP[0] = P;
    fighterP[1] = P;
    n = 2 * P;

    for (i = 0; i < n; i++)
    {
        scanf("%d", &attackNumber);
        attacker = attackNumber & 1;   // even => 0, odd => 1
        victim = attacker ^ 1;

        comboCharge[victim] = 0;
        if (++comboCharge[attacker] >= 3)
            attackPower = 3;
        else
            attackPower = 1;

        fighterP[victim] -= attackPower;

        if (fighterP[victim] <= 0)
            break;
    }

    printf("%d\n%d\n", attacker, attackNumber);
    return 0;
}
