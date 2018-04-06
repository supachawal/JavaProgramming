#include<cstdio>
#include<cstring>
#include<cmath>
#include<algorithm>

const int maxn = 100000 + 10;
struct Edge {
	int u, v;
	int w;
	int mstFlag;
} e[maxn];
int n, m;
bool impossible;
int last[maxn];
int K;
int now;

bool cmp(const Edge&a, const Edge&b) {
	return a.w < b.w;
}

void init() {
	now = 1;
	impossible = false;
	K = 2;
	for (int i = 1; i <= n; i++)
		last[i] = i;
}

int main() {
	scanf("%d%d", &n, &m);
	init();
	for (int i = 1; i <= m; i++)
		scanf("%d%d", &e[i].w, &e[i].mstFlag);
	std::sort(e + 1, e + m + 1, cmp);

	for (int i = 1; i <= m; i++) {
		if (e[i].mstFlag == 1) {
			e[i].u = 1;
			e[i].v = ++now;

			K = 2;
		} else {
			while (1) {
				if (K > n) {
					impossible = true;
					break;
				}
				if (last[K] < n) {
					e[i].u = K;
					e[i].v = 1 + last[K]++;
					break;
				}

				K++;
			}
		}

		if (impossible)
			break;
	}

	if (!impossible) {
		for (int i = 1; i <= m; i++)
			printf("%d %d\n", e[i].u, e[i].v);
	} else {
		printf("-1\n");
	}

	return 0;
}
