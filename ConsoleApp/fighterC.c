/*
TASK: fighterC
LANG: C
AUTHOR: Sukit seripanitkarn
CENTER: Kvis
*/
#include<stdio.h>
#include<string.h>
int main()
{
    int n,n1,n2,i,k,tmp,cnt=1,a,d=1;
    scanf("%d",&n);
    n1=n2=n;
    n1-=1;
    scanf("%d",&a);
    for(i=0;i<2*n-1;i++)
    {
        tmp=a;
        scanf("%d",&a);
        if((tmp-a)%2==0)
        {

           cnt++;
           if(cnt<3)n1-=1;
           else n1-=3;
           if(n1<=0&&d)
           {k=a;d=0;}
        }
        else {
            cnt=1;
            tmp=n1;
            n1=n2;
            n2=tmp;
            n1-=1;

        }
    }
    printf("%d\n%d",k%2,k);
}
