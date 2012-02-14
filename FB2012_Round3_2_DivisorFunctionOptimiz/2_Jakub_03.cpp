#include <vector>
#include <list>
#include <map>
#include <set>
#include <queue>
#include <deque>
#include <stack>
#include <bitset>
#include <algorithm>
#include <functional>
#include <numeric>
#include <utility>
#include <sstream>
#include <iostream>
#include <iomanip>
#include <cstdio>
#include <cmath>
#include <cstdlib>
#include <ctime>
#include <cstring>
#include <cassert>

using namespace std;

typedef long long LL;
typedef long double LD;
typedef pair<int, int> PII;
#define MP make_pair
#define FOR(v,p,k) for(int v=p;v<=k;++v)
#define FORD(v,p,k) for(int v=p;v>=k;--v)
#define REP(i,n) for(int i=0;i<(n);++i)
#define VAR(v,i) __typeof(i) v=(i)
#define FORE(i,c) for(__typeof(c.begin()) i=(c.begin());i!=(c).end();++i)
#define PB push_back
#define ST first
#define ND second
#define SIZE(x) (int)x.size()
#define ALL(c) c.begin(),c.end()
#define ZERO(x) memset(x,0,sizeof(x))

const int MX = (1 << 25) + 1000;
const int MAXP = 2100000;
const int MAXE = 40;

const int MD = (int) 1e9 + 7;

int p[MAXP + 5];
int nP;
bool np[MX + 5];

LD loga[MAXP + 5];

int prod[MAXP + 5];

void era() {
    for (int i = 2; i <= MX; ++i) {
        if (!np[i]) {
            assert(nP + 1 < MAXP);
            p[++nP] = i;
            for (int j = i + i; j <= MX; j += i) {
                np[j] = true;
            }
        }
    }
}

int pot(int a, LL b) {
    int res = 1;
    for (; b > 0; b >>= 1) {
        if (b & 1) {
            res = (res * (LL) a) % MD;
        }
        a = (a * (LL) a) % MD;
    }
    return res;
}

LL t[MAXE + 5];

void add(int x, LL val) {
    for (int i = 1; p[i] <= MAXE; ++i) {
        while (x % p[i] == 0) {
            t[i] += val;
            x /= p[i];
        }
    }
}

PII calc(int a, int b) {
    ZERO(t);
    PII res(1, 1);
    int prevCnt = 0;
    for (int i = MAXE; i >= 2; --i) {
        LD maxlog = (logl(i) - logl(i - 1)) * (LD) a / (LD) b;
        int cnt = lower_bound(loga + 1, loga + nP + 1, maxlog) - loga - 1;
        if (i == MAXE) {
            assert(cnt == 0);
        }
        add(i, (cnt - prevCnt) * (LL) a);
        for (int j = 1; j <= cnt && p[j] <= MAXE; ++j) {
            t[j] -= b;
        }
        res.ND = (res.ND * (LL) pot(prod[cnt], b)) % MD;
        prevCnt = cnt;
    }
    for (int i = 1; i <= MAXE; ++i) {
        if (t[i] > 0) {
            res.ST = (res.ST * (LL) pot(p[i], t[i])) % MD;
        } else if (t[i] < 0) {
            res.ND = (res.ND * (LL) pot(p[i], -t[i])) % MD;
        }
    }
    return res;
}

void alg() {
    int a1, a2;
    int b1, b2;
    int c;
    scanf("%d %d %d %d %d", &b1, &b2, &a1, &a2, &c);
    LL rp = 0, rq = 0;
    for (int a = a1; a <= a2; ++a) {
        for (int b = max((a + c - 1) / c, b1); b <= b2; ++b) {
            PII cur = calc(a, b);
            rp += cur.ST;
            rq += cur.ND;
        }
    }
    printf("%lld %lld\n", rp, rq);
}

int main() {
    era();
    prod[0] = 1;
    for (int i = 1; i <= nP; ++i) {
        loga[i] = logl(p[i]);
        prod[i] = (prod[i - 1] * (LL) (p[i] > MAXE ? p[i] : 1)) % MD;
    }
    int d;
    scanf("%d", &d);
    for (int testCase = 1; testCase <= d; ++testCase) {
        printf("Case #%d: ", testCase);
        alg();
    }
}
