/*
 * MinimumSpanningTreeContainingAnEdge.cpp
 *
 *  Created on: Jan 20, 2016
 *      Author: chutima
 */

#include <iostream>
#include <vector>
#include <stdio.h>
#include <algorithm>

const int N = 200100;
const int MAX_RELATIVE_ANCESTOR = 16;  // = floor(log2(N)) - 1

int n, m;
std::vector<std::pair<int, int> > adjList[N];
std::vector<std::pair<int, std::pair<int, int> > > edges, edgesOriginal;
int root[N];
long long mstCost;
int timer;
int upLine[N][MAX_RELATIVE_ANCESTOR + 1];
int maxEdgeWeight[N][MAX_RELATIVE_ANCESTOR + 1];
int timeIn[N];
int timeOut[N];
int layer[N];
int ccSize[N];

int _find(int a) {
    while (a != root[a]) {
        a = root[a] = root[root[a]];
    }
    return a;
}

bool merge(int a, int b) {
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

void dfsForTree(int v, int parent, int pCost) {
    layer[v] = layer[parent] + 1;
    timeIn[v] = ++timer;
    upLine[v][0] = parent;
    maxEdgeWeight[v][0] = pCost;
    int i;
    for (i = 1; i <= MAX_RELATIVE_ANCESTOR; i++) {
        upLine[v][i] = upLine[upLine[v][i - 1]][i - 1];
        maxEdgeWeight[v][i] = std::max(maxEdgeWeight[v][i - 1], maxEdgeWeight[upLine[v][i - 1]][i - 1]);
    }
    for (i = adjList[v].size() - 1; i >= 0; i--) {
        int to = adjList[v][i].first;
        if (to != parent) {
            dfsForTree(to, v, adjList[v][i].second);
        }
    }
    timeOut[v] = ++timer;
}

inline bool isAncestor(int a, int b) {
    return (timeIn[a] <= timeIn[b] && timeOut[a] >= timeOut[b]);
}

int lca(int u, int v) {
    if (isAncestor(u, v))
        return u;
    if (isAncestor(v, u))
        return v;

    for (int j = MAX_RELATIVE_ANCESTOR; j >= 0; --j) {
        if (!isAncestor(upLine[u][j], v)) {
            u = upLine[u][j];
        }
    }
    return upLine[u][0];
}

int maxEdgeWeightAlongUpwardPath(int v, int span) {
    int ret = 0;
    for (int i = 0; i <= MAX_RELATIVE_ANCESTOR; ++i, span >>= 1) {
        if ((span & 1) != 0) {
            ret = std::max(ret, maxEdgeWeight[v][i]);
            v = upLine[v][i];
        }
    }
    return ret;
}

int main() {
    int u, v, weight;
    scanf("%d%d", &n, &m);
    for (int i = 1; i <= m; i++) {
        scanf("%d%d%d", &u, &v, &weight);
        edges.push_back(std::make_pair(weight, std::make_pair(u, v)));
    }
    edgesOriginal = edges;
    // ----- Kruskal's algorithm
    std::sort(edges.begin(), edges.end());
    for (int i = 1; i <= n; i++) {
        root[i] = i;
        ccSize[i] = 1;
    }

    for (int i = 0; i < m; i++) {
        u = edges[i].second.first;
        v = edges[i].second.second;
        weight = edges[i].first;
        if (merge(u, v)) {
            mstCost += weight;
            adjList[u].push_back(std::make_pair(v, weight));
            adjList[v].push_back(std::make_pair(u, weight));
        }
    }
    // ----------------------------------------------

    layer[1] = -1;
    dfsForTree(1, 1, 0);

    for (int i = 0; i < m; i++) {
        u = edgesOriginal[i].second.first;
        v = edgesOriginal[i].second.second;
        int theLCA = lca(u, v);
        weight = std::max(maxEdgeWeightAlongUpwardPath(u, layer[u] - layer[theLCA]),
                        maxEdgeWeightAlongUpwardPath(v, layer[v] - layer[theLCA]));
        std::cout << mstCost + edgesOriginal[i].first - weight << '\n';
    }

    return 0;
}

