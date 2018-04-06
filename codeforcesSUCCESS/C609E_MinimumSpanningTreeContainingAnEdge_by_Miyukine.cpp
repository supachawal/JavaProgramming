#include <bits/stdc++.h>
using namespace std;
#define e1 first
#define e2 second
#define MAX_N 200100
typedef pair<int, int> EDGE;
typedef pair<EDGE, EDGE> EDGE_PAIR;
const int MAX_RELATIVE_ANCESTOR = 16;
int root[MAX_N];
int ccSize[MAX_N];

vector<EDGE> adjList[MAX_N];
bool isMstEdge[MAX_N];
int n, m, ages, b, c, dl, depth;
EDGE up[MAX_N][MAX_RELATIVE_ANCESTOR + 1];
int pre[MAX_N], post[MAX_N], layer[MAX_N];
EDGE_PAIR x;
EDGE_PAIR edgeList[MAX_N];
long long answer[MAX_N];

int _find(int a) {
    while (a != root[a]) {
        a = root[a] = root[root[a]];
    }
    return a;
}

bool _union(int a, int b) {
    int fa = _find(a);
    int fb = _find(b);
    if (fa != fb) {
        if (ccSize[fa] > ccSize[fb]) {
            root[fb] = fa;
            ccSize[fa] += ccSize[fb];
        } else {
            root[fa] = fb;
            ccSize[fb] += ccSize[fa];
        }

        return true;
    }
    return false;
}

void dfsForTree(int x, int parent) {
    layer[x] = layer[parent] + 1;
    pre[x] = ++dl;
    for (int i = adjList[x].size() - 1; i >= 0; i--) {
        if (adjList[x][i].e1 != parent) {
            dfsForTree(adjList[x][i].e1, x);
            up[adjList[x][i].e1][0] = make_pair(x, adjList[x][i].e2);
        }
    }
    post[x] = ++dl;
}

inline bool isParent(int x, int y) {
    return pre[x] <= pre[y] && post[x] >= post[y];
}

int lca(int u, int v) {
    if (isParent(u, v))
        return u;
    if (isParent(v, u))
        return v;
    for (int j = MAX_RELATIVE_ANCESTOR; j >= 0; --j)
        if (!isParent(up[u][j].e1, v))
            u = up[u][j].e1;
    return up[u][0].e1;
}

int maxEdgeWeightAlongUpwardPath(int v, int span) {
    int ret = 0;
    for (int i = 0; i <= MAX_RELATIVE_ANCESTOR; ++i, span >>= 1) {
        if ((span & 1) != 0) {
            ret = max(ret, up[v][i].second);
            v = up[v][i].e1;
        }
    }
    return ret;
}

int main() {
    scanf("%d%d", &n, &m);

    for (int i = 1; i <= m; ++i) {
        scanf("%d%d%d", &x.e1.e2, &x.e2.e1, &x.e1.e1);
        edgeList[i] = x;
        edgeList[i].e2.e2 = i;
    }
    // Kruskal's algorithm
    sort(edgeList + 1, edgeList + m + 1);
    long long mstCost = 0;
    for (int i = 1; i <= n; ++i) {
        root[i] = i;
        ccSize[i] = 1;
    }
    for (int i = 1; i <= m; ++i) {
        int u = edgeList[i].e1.e2;
        int v = edgeList[i].e2.e1;
        int weight = edgeList[i].e1.e1;
        if (_union(u, v)) {
            isMstEdge[i] = true;
            mstCost += edgeList[i].e1.e1;
            // build graph structure of MST
            adjList[v].push_back(make_pair(u, weight));
            adjList[u].push_back(make_pair(v, weight));
        }
    }

    //----------- Preparations for LCA
    layer[1] = -1;
    up[1][0] = make_pair(1, 0);
    dfsForTree(1, 1);
    for (int j = 1; j <= MAX_RELATIVE_ANCESTOR; ++j) {
        for (int x = 1; x <= n; x++) {
            up[x][j] = make_pair(up[up[x][j - 1].e1][j - 1].e1, max(up[x][j - 1].e2, up[up[x][j - 1].e1][j - 1].e2));
        }
    }
    //--------------------------------------------

    for (int i = 1; i <= m; ++i) {
        if (isMstEdge[i]) {
            answer[edgeList[i].e2.e2] = mstCost;
        } else {
            int v1 = edgeList[i].e1.e2;
            int v2 = edgeList[i].e2.e1;
            int theLCA = lca(v1, v2);
            int co = max(maxEdgeWeightAlongUpwardPath(v2, layer[v2] - layer[theLCA]),
                         maxEdgeWeightAlongUpwardPath(v1, layer[v1] - layer[theLCA]));
            answer[edgeList[i].e2.e2] = mstCost + edgeList[i].e1.e1 - co;
        }
    }

    for (int i = 1; i <= m; ++i)
        cout << answer[i] << '\n';
}
