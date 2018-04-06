#include <algorithm>
using namespace std;

typedef long long ll;
typedef unsigned long long ull;
typedef pair<int, int> pii;
#define mod 1000000007
#define x first
#define y second
#define pi acos(-1)
#define N 100010
const ll base = 13331;
ll h[N], b[N];
int l[N], r[N], in[N], ages[N];
int read() {
    int i, j, n;
    scanf("%d", &n);
    for (i = 1; i <= n; i++) {
        scanf("%d", &ages[i]);
        l[i] = -1;
        r[i] = -1;
        in[i] = -1;
    }
    for (i = 1; i < n; i++) {
        char c[2];
        int p;
        scanf("%d%d%s", &p, &j, &c);
        in[j] = 1;
        if (c[0] == 'M')
            l[p] = j;
        else
            r[p] = j;
    }
    return n;
}
ll traverseFromRoot(int p) {
    if (p == -1)
        return 0;
    return h[p] = ages[p] + base * traverseFromRoot(l[p]) + base * base * traverseFromRoot(r[p]);
}
int main() {
    int i, j, ca = 0, m, k, T, n;
    n = read();
    for (i = 1; i <= n; i++)
        if (in[i] == -1) { //root
            traverseFromRoot(i);
            break;
        }
    for (i = 1; i <= n; i++)
        b[i] = h[i];
    sort(b + 1, b + n + 1);
    int q;
    scanf("%d", &q);
    while (q--) {
        m = read();
        for (i = 1; i <= m; i++)
            if (in[i] == -1) {
                traverseFromRoot(i);
                if (binary_search(b + 1, b + n + 1, h[i]))
                    puts("YES");
                else
                    puts("NO");
                break;
            }
    }
    return 0;
}
