#include <cstdio>
#include <cstdlib>
#include <cassert>
#include <iostream>
#include <set>
#include <vector>
#include <cstring>
#include <string>
#include <algorithm>
#include <cmath>
#include <map>
using namespace std;
typedef long long ll;
typedef unsigned long long ull;
typedef vector<int> vi;
typedef vector<unsigned int> vu;
typedef vector<ll> vl;
typedef vector<vl> vvl;
typedef vector<vi> vvi;
typedef vector<double> vd;
typedef vector<vd> vvd;
typedef pair<int, int> pii;
typedef pair<double, double> pdd;
typedef vector<pii> vii;
typedef vector<string> vs;
const int DEBUG = 0;
const double time_limit = 4.86;
time_t BEGIN;
 
unsigned long long myrand() {
  return (((ull)rand() << 30) + rand());
}
 
double remaining_time() {
  return (clock() - BEGIN) / (double)CLOCKS_PER_SEC;
}
 
void check(vi v) {
  assert(v.size() % 3 == 0);
  set<int> was;
  for (int i = 0; i + 5 < v.size(); i += 3) {
    assert(v[i]+(ll)v[i+1]+v[i+2] == v[i+3]+(ll)v[i+4]+v[i+5]);
  }
}
 
pii filter(vu & a, vu & was, int itb, int ite) {
  vu na, nwas;
  int b = a.size(), e = 0;
  if (DEBUG) assert(a.size() == was.size());
  for (int i = 0; i < a.size(); ++i) if (was[i]) {
    if (b == a.size() && i >= itb) {
      b = na.size();
    }
    if (i <= ite) {
      e = na.size();
    }
    na.push_back(a[i]);
    nwas.push_back(was[i]);
  }
  a.swap(na);
  was.swap(nwas);
  return pii(b, e);
}
 
void gist(const vu & a, ll up) {
  if (DEBUG < 2) return;
  vi c(10);
  for (int i = 0; i < a.size(); ++i) {
    assert(a[i] <= up);
    c[a[i] * (ll)c.size() / (up + 1)]++;
  }
  int mac = 0;
  for (int i = 0; i < c.size(); ++i) mac = max(mac, c[i]);
  cerr << "[";
  for (int i = 0; i < c.size(); ++i) cerr << (10*c[i]-1)/mac << ",";
  cerr << "] " << mac << "\n";
}
 
void gist(const vu & a) {
  vi c(20);
  for (int i = 0; i < a.size(); ++i) {
    c[min(a[i]/6, (unsigned)c.size() - 1)]++;
  }
  cerr << "[";
  for (int i = 0; i < c.size(); ++i) cerr << c[i] << ",";
  cerr << "]\n";
}
 
vi runrand_priority(vu a, vu was, unsigned int s) {
  vi res;
  ll up = a.back();
  while (!a.empty()) {
    int prev = res.size();
    double passed_time = 0;
    set<pii> q;
    for (int i = 0; i < a.size(); ++i) q.insert(pii(-(int)was[i], i));
    for (int i0 = 0; i0 < 2000; ++i0) {
      int i = q.begin()->second;
      passed_time = remaining_time();
      if (passed_time > time_limit) break;
      if (!was[i]) continue;
      for (int iter = 0; iter < 200; ++iter) {
        unsigned int s1 = s - a[i];
        int j = rand() % a.size();
        if (!was[j] || a[j] > s1 || (i == j && was[i] < 2)) continue;
        unsigned int c = s1 - a[j];
        int it = lower_bound(a.begin(), a.end(), c) - a.begin();
        if (it < a.size() && a[it] == c) {
          --was[i]; --was[j];
          if (was[it]) {
            ++was[i]; ++was[j];
            q.erase(pii(-(int)was[i], i));
            q.erase(pii(-(int)was[j], j));
            q.erase(pii(-(int)was[it], it));
            --was[i]; --was[j];
            --was[it];
            res.push_back(a[i]);
            res.push_back(a[j]);
            res.push_back(a[it]);
            q.insert(pii(-(int)was[i], i));
            q.insert(pii(-(int)was[j], j));
            q.insert(pii(-(int)was[it], it));
            break;
          } else {
            ++was[i]; ++was[j];
          }
        }
      }
    }
    if (passed_time > time_limit || prev == res.size()) break;
    filter(a, was, 0, 0);
  }
  if (DEBUG) gist(a, up);
  return res;
}
 
vi runrand(vu a, vu was, unsigned int s) {
  vi res;
  ll up = a.back();
  while (!a.empty()) {
    int prev = res.size();
    double passed_time = 0;
    for (int i = 0; i < a.size(); ++i) {
//    for (int rid = 0; rid < 2000 && !a.empty(); ++rid) {
      passed_time = remaining_time();
      if (passed_time > time_limit) break;
//      int i = rand() % a.size();
      if (!was[i]) continue;
      for (int iter = 0; iter < 200; ++iter) {
        unsigned int s1 = s - a[i];
        int j = rand() % a.size();
        if (!was[j] || a[j] > s1 || (i == j && was[i] < 2)) continue;
        unsigned int c = s1 - a[j];
        int it = lower_bound(a.begin(), a.end(), c) - a.begin();
        if (it < a.size() && a[it] == c) {
          --was[i]; --was[j];
          if (was[it]) {
            --was[it];
            res.push_back(a[i]);
            res.push_back(a[j]);
            res.push_back(a[it]);
            break;
          } else {
            ++was[i]; ++was[j];
          }
        }
      }
    }
    if (passed_time > time_limit || prev == res.size()) break;
    filter(a, was, 0, 0);
  }
  if (DEBUG) gist(a, up);
  return res;
}
 
vi runrand2(vu a, vu was, unsigned int s) {
  vi res;
  ll up = a.back();
  while (!a.empty()) {
    int prev = res.size();
    double passed_time = 0;
    for (int t = 0; t <= a.size()/2; ++t) {
      for (int i : {t, (int)a.size() - t - 1}) {
        passed_time = remaining_time();
        if (passed_time > time_limit) break;
        if (!was[i]) continue;
        for (int iter = 0; iter < 100; ++iter) {
          unsigned int s1 = s - a[i];
          int j = rand() % a.size();
          if (!was[j] || a[j] > s1 || (i == j && was[i] < 2)) continue;
          unsigned int c = s1 - a[j];
          int it = lower_bound(a.begin(), a.end(), c) - a.begin();
          if (it < a.size() && a[it] == c) {
            --was[i]; --was[j];
            if (was[it]) {
              --was[it];
              res.push_back(a[i]);
              res.push_back(a[j]);
              res.push_back(a[it]);
              break;
            } else {
              ++was[i]; ++was[j];
            }
          }
        }
      }
    }
    if (passed_time > time_limit || prev == res.size()) break;
    filter(a, was, 0, 0);
  }
  if (DEBUG) gist(a, up);
  return res;
}
 
void rundebug(vu a, unsigned int s) {
  vu cnt(a.size());
  int i = 0;
  int begit = lower_bound(a.begin(), a.end(), s-min(s, a[i]+a.back())) - a.begin();
  int enit = upper_bound(a.begin(), a.end(), s-min(s, a[i]+a[0])) - a.begin();
  for (; i < a.size(); i++) {
    unsigned int s1 = s - a[i];
    while (begit && a[begit-1]+a.back() >= s1) --begit;
    while (enit > begit && a[enit-1]+a[0] > s1) --enit;
    int beg = begit, en = enit;
    if (beg >= en || beg >= a.size()) continue;
    unsigned int it = lower_bound(a.begin(), a.end(), s1-min(s1, a[beg])) - a.begin();
    if (it == a.size()) --it;
    for (int j = beg; j < en; ++j) {
      unsigned int c = s1 - a[j];
      while (it && a[it-1] >= c) --it;
      if (a[it] == c) {
        ++cnt[i];
      }
    }
    if (i % 5000 == 0) gist(cnt);
  }
  gist(cnt);
}
 
vi runincdec(vu a, vu was, unsigned int s, int L) {
  vi res;
  int b = 0, e = a.size() - 1, zerores = res.size();
  ll up = a.back();
  const int LEN = 2000;
  while (!a.empty()) {
    double passed_time = 0;
    int prev = res.size();
    e = min(e, (int)a.size() - 1);
    if (b > e) {
      b = 0;
      e = a.size() - 1;
      zerores = res.size();
    }
    if (DEBUG) assert(b >= 0 && b < (int)a.size());
    if (true) {
      for (int type = 0; type < 2; ++type) {
        int b1, e1;
        if (b > e) break;
        if (type == 0) {
          b1 = b;
          e1 = min(e, b + LEN);
          b = e1 + 1;
        } else {
          b1 = max(b, e - LEN);
          e1 = e;
          e = b1 - 1;
        }
        int begit = lower_bound(a.begin(), a.end(), s-min(s, a[b1]+a.back())) - a.begin();
        int enit = upper_bound(a.begin(), a.end(), s-min(s, a[b1]+a[0])) - a.begin();
        for (int i = b1; i <= e1; i++) {
          passed_time = remaining_time();
          if (passed_time > time_limit) break;
          if (!was[i]) continue;
          unsigned int s1 = s - a[i];
          while (begit && a[begit-1]+a.back() >= s1) --begit;
          while (enit > begit && a[enit-1]+a[0] > s1) --enit;
          int beg = begit, en = enit;
          if (en > L + beg) {
            beg = beg + (myrand() % (en - beg - L));
            en = beg + L;
          }
          if (beg >= en || beg >= a.size()) continue;
          unsigned int it = lower_bound(a.begin(), a.end(), s1-min(s1, a[beg])) - a.begin();
          if (it == a.size()) --it;
          --was[i];
          for (int j = beg; j < en; ++j) {
            unsigned int c = s1 - a[j];
            while (it && a[it-1] >= c) --it;
            if (a[it] == c && was[j] && was[it] && (j != it || was[it] >= 2)) {
              --was[j];
              --was[it];
              --was[i];
              res.push_back(a[i]);
              res.push_back(a[j]);
              res.push_back(a[it]);
              break;
            }
          }
          ++was[i];
        }
      }
    } else {
      zerores = res.size();
      for (int iter = 0; iter < 5000; iter++) {
        passed_time = remaining_time();
        if (passed_time > time_limit) break;
        int i = rand() % a.size();
        if (!was[i]) continue;
        unsigned int s1 = s - a[i];
        int beg = lower_bound(a.begin(), a.end(), s1 - min(s1, a.back())) - a.begin();
        int en = upper_bound(a.begin(), a.end(), s1 - min(s1, a[0])) - a.begin();
        if (en > L + beg) {
          beg = beg + (myrand() % (en - beg - L));
          en = beg + L;
        }
        if (beg >= en || beg >= a.size()) continue;
        unsigned int it = lower_bound(a.begin(), a.end(), s1-min(s1, a[beg])) - a.begin();
        if (it == a.size()) --it;
        --was[i];
        for (int j = beg; j < en; ++j) {
          unsigned int c = s1 - a[j];
          while (it && a[it-1] >= c) --it;
          if (a[it] == c && was[j] && was[it] && (j != it || was[it] >= 2)) {
            --was[j];
            --was[it];
            --was[i];
            res.push_back(a[i]);
            res.push_back(a[j]);
            res.push_back(a[it]);
            break;
          }
        }
        ++was[i];
      }
    }
/*    if (DEBUG) {
      gist(a, up);
      cerr << res.size() / 3 << ' ' << b << ' ' << e << endl;
    }*/
    if (passed_time > time_limit) break;
    if (zerores == res.size()) break;
    auto nxi = filter(a, was, b, e);
    b = nxi.first;
    e = nxi.second;
  }
  if (DEBUG) {
    gist(a, up);
//    cerr << res.size() / 3 << ' ' << remaining_time() << endl;
//    assert(0);
  }
  return res;
}
 
vi runinc(vu a, vu was, unsigned int s, int L) {
  vi res;
  int i = 0, zerores = res.size();
  bool big = (a.back() - a[0] >= 8e8);
  if (big) L = 15000;
  ll up = a.back();
//  if (big) i = lower_bound(a.begin(), a.end(), s/4) - a.begin();
  while (!a.empty()) {
    double passed_time = 0;
    int prev = res.size();
    if (i >= a.size()) {
      i = 0;
      zerores = res.size();
    }
    if (DEBUG) assert(i >= 0 && i < (int)a.size());
    int begit = lower_bound(a.begin(), a.end(), s-min(s, a[i]+a.back())) - a.begin();
    int enit = upper_bound(a.begin(), a.end(), s-min(s, a[i]+a[0])) - a.begin();
    for (; i < a.size(); i++) {
      passed_time = remaining_time();
      if (passed_time > time_limit || (res.size() - prev) / 3 >= 5000) break;
      if (!was[i]) continue;
      unsigned int s1 = s - a[i];
      while (begit && a[begit-1]+a.back() >= s1) --begit;
      while (enit > begit && a[enit-1]+a[0] > s1) --enit;
      int beg = begit, en = enit;
      if (en > L + beg) {
//        beg = max(beg, min(i+1, en - L1 - 1));
        beg = beg + (myrand() % (en - beg - L));
        en = beg + L;
      }
      if (beg >= en || beg >= a.size()) continue;
      unsigned int it = lower_bound(a.begin(), a.end(), s1-min(s1, a[beg])) - a.begin();
      //unsigned int it = lower_bound(a.begin(), a.end(), s1-min(s1, a[en-1])) - a.begin();
      if (it == a.size()) --it;
      --was[i];
      //for (int j = en-1; j >= beg; --j) {
      for (int j = beg; j < en; ++j) {
        unsigned int c = s1 - a[j];
        //while (it+1 < a.size() && a[it+1] <= c) ++it;
        while (it && a[it-1] >= c) --it;
        if (a[it] == c && was[j] && was[it] && (j != it || was[it] >= 2)) {
          --was[j];
          --was[it];
          --was[i];
          res.push_back(a[i]);
          res.push_back(a[j]);
          res.push_back(a[it]);
          break;
        }
      }
      ++was[i];
    }
    if (passed_time > time_limit) break;
    if (zerores == res.size()) break;
    auto nxi = filter(a, was, i, 0);
//    if (big) i = nxi.first;
  }
  if (DEBUG) {
    gist(a, up);
//    cerr << res.size() / 3 << ' ' << remaining_time() << endl;
  }
  return res;
}
 
vi rundec(vu a, vu was, unsigned int s, int L) {
  unsigned int up = a.back();
  for (int i = 0; i < a.size(); ++i) {
    a[i] = up - a[i];
  }
  reverse(a.begin(), a.end());
  reverse(was.begin(), was.end());
  s = 3LL * up - s;
  vi randres = runrand(a, was, s);
  vi res = runinc(a, was, s, L);
  if (randres.size() > res.size()) res.swap(randres);
  for (int i = 0; i < res.size(); ++i) res[i] = up - res[i];
  return res;
}
 
vi genl(vi a0, vu was, ll s0, int L) {
  int neg = (s0 < 3 * (a0[0] + (ll)a0.back()) / 2) ? 1 : 0;
  if (neg) {
    s0 *= -1;
    for (int i = 0; i < a0.size(); ++i) a0[i] *= -1;
    reverse(a0.begin(), a0.end());
    reverse(was.begin(), was.end());
  }
  int low = a0[0], up = a0.back() + 1;
  unsigned int s = s0 - 3LL*low;
  vu a(a0.size());
  for (int i = 0; i < a.size(); ++i) a[i] = a0[i] - low;
  while (!a.empty() && a.back() > s) {
    a.pop_back();
    was.pop_back();
  }
  if (DEBUG) gist(a, up-low);
  vi res, newres;
  if (up - low < 7e8) {
    res = runincdec(a, was, s, L);
    newres = runinc(a, was, s, L);
  } else {
    res = runinc(a, was, s, L);
    newres = runincdec(a, was, s, L);
  }
  if (newres.size() > res.size()) res.swap(newres);
  if (remaining_time() < time_limit) {
    newres = runrand2(a, was, s);
    if (newres.size() > res.size()) res.swap(newres);
  }
  if (remaining_time() < time_limit) {
    newres = runrand(a, was, s);
    if (newres.size() > res.size()) res.swap(newres);
  }
  if (remaining_time() < time_limit) {
    newres = runrand_priority(a, was, s);
    if (newres.size() > res.size()) res.swap(newres);
  }
  if (remaining_time() < time_limit) {
    newres = rundec(a, was, s, L);
    if (newres.size() > res.size()) res.swap(newres);
  }
  if (remaining_time() < time_limit) {
    newres = runrand2(a, was, s);
    if (newres.size() > res.size()) res.swap(newres);
  }
//  if (DEBUG && up-(ll)low > 9e8) rundebug(a, s);
  for (int j = 0; j < res.size(); ++j) res[j] += low;
  if (neg) for (int j = 0; j < res.size(); ++j) res[j] *= -1;
  return res;
} 
 
vi solve(vi a) {
  vii a0(a.size());
  for (int i = 0; i < a.size(); ++i) a0[i] = pii(a[i], i+1);
  sort(a0.begin(), a0.end());
  sort(a.begin(), a.end());
  
  ll s0 = 0, low = a[0], up = a.back();
//  if (up - low <= (int)2e3) return vi();
  for (int i = 0; i < a.size(); ++i) {
    s0 += a[i];
  }
  int N = a0.size() / 3;
  if (s0 > 0) {
    s0 = (s0 + N / 2) / N;
  } else {
    s0 = (s0 - N / 2) / N;
  }
  int eps = (up-(ll)low) / N;
  vi cand(eps * 2 + 1);
  vi b = a;
  vu was(1, 1);
  a.resize(1);
  for (int i = 1; i < b.size(); ++i) {
    if (b[i] == b[i-1]) {
      ++was.back();
    } else {
      a.push_back(b[i]);
      was.push_back(1);
    }
  }
/*  const int L = 10000;
  const int L1 = 1000;
  //int begit = max(0, (int)(lower_bound(a.begin(), a.end(), min(max(a[0]-1LL, s0-a[0]-a.back()), (ll)a.back()+1)) - a.begin()));
  //int enit = min((int)a.size(), (int)(upper_bound(a.begin(), a.end(), min(max(a[0]-1LL, s0-a[0]-a[0]), (ll)a.back()+1)) - a.begin()));
  for (int i = 0; i < L; ++i) {
    //while (begit && a[begit-1] >= s0-eps-a.back()) --begit;
    //while (enit > begit && a[enit-1] > s0+eps-a[0]) --enit;
    //int beg = begit, en = enit;
    //if (en - beg > L1) {
    //  beg = beg + (rand() % (en - beg - L1));
    //  en = beg + L1;
    //}
    int beg = max(0, (int)a.size() - i - L1);
    int en = (int)a.size() + min(0, -i + L1);
    int it = lower_bound(a.begin(), a.end(), min(max(a[0]-1LL, s0-a[i]-a[beg]-eps), (ll)a.back()+1)) - a.begin();
    for (int j = beg; j < en; ++j) {
      ll c0 = s0 - a[i] - a[j];
      while (it && a[it-1] >= c0-eps) --it;
      for (; it < a.size() && a[it] <= c0+eps; ++it) {
        cand[a[it] - c0 + eps]++;
      }
    }
  }
  ll s = s0, ma = 0;
  for (int i = 0; i < cand.size(); ++i) {
    if (cand[i] > ma) {
      ma = cand[i];
      s = s0 + i - eps;
    }
  }*/
  ll s = s0;
//  if (abs(s/3 - 0) < (up - (ll)low) / 100) s = 0;
  vi res = genl(a, was, s, 5000);
  if (DEBUG) {
    cerr << s << ' ' << low << ' ' << up << ' ' << res.size()/3 << ' ' << remaining_time() << endl;
    check(res);
  }
  assert(s - 3LL*low < (1LL << 32));
  assert(s - 3LL*up > -(1LL << 32));
  vi ans(res.size());
  was.assign(a0.size(), 0);
  vi nx(a0.size());
  for (int i = 0; i < nx.size(); ++i) nx[i] = i + 1;
  for (int i = 0; i < res.size(); ++i) {
    int it = lower_bound(a0.begin(), a0.end(), pii(res[i], 0)) - a0.begin();
    if (was[it]) {
      nx[it]++;
      it = nx[it] - 1;
    }
    if (DEBUG) assert(res[i] == a0[it].first && !was[it]);
    ans[i] = a0[it].second;
    was[it] = 1;
  }
  if (up - low <= 200000) assert(ans.size() / 3 >= 99200);
  return ans;
}
 
int main() {
  srand(138);
  int n = 100000;
  double sum = 0, cnt = 0;
  while (scanf("%d", &n) == 1) {
    BEGIN = clock();
    vi a(3*n);
    for (int i = 0; i < a.size(); ++i) {
      int scan = scanf("%d", &a[i]);
    }
    vi res = solve(a);
    assert(!res.empty());
//    res.resize(max(0, (int)res.size() - 3000));
    printf("%d\n", (int)res.size() / 3);
    for (int i = 0; i < res.size(); ++i) {
      printf("%d ", res[i]);
    }
    sum += res.size() / 3;
    cnt += 1;
  }
  if (DEBUG) {
    cerr << sum / cnt << ' ' << cnt << endl;
  }
  return 0;
}
 