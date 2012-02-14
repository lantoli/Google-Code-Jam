#include <cstdio>
#include <numeric>
#include <iostream>
#include <vector>
#include <set>
#include <cstring>
#include <string>
#include <map>
#include <cmath>
#include <ctime>
#include <algorithm>
#include <bitset>
#include <queue>
#include <sstream>
#include <deque>

using namespace std;

#define mp make_pair
#define pb push_back
#define rep(i,n) for(int i = 0; i < (n); i++)
#define re return
#define fi first
#define se second
#define sz(x) ((int) (x).size())
#define all(x) (x).begin(), (x).end()
#define sqr(x) ((x) * (x))
#define sqrt(x) sqrt(abs(x))
#define y0 y3487465
#define y1 y8687969
#define fill(x,y) memset(x,y,sizeof(x))
                         
typedef vector<int> vi;
typedef long long ll;
typedef long double ld;
typedef double D;
typedef pair<int, int> ii;
typedef pair<ll, ll> pll;
typedef vector<ii> vii;
typedef vector<string> vs;
typedef vector<vi> vvi;
typedef vector<ll> vll;
typedef vector<pll> vpll;

template<class T> T abs(T x) { re x > 0 ? x : -x; }

const int MAX = 1 << 20;

ll N, K;
ll M, X, A, B, p, q;

ll prior;

ll aa[400000], bb[400000], cc[400000], dd[400000];

ll a[400000], b[400000], c[400000], d[400000];
map<ll, int> all1, all2;

int tree[2 * MAX + 1];
int add[2 * MAX + 1];

int CR[MAX];

vll v1, v2;
vector<pair<ii, ii> > v;

ll next () {
	prior = (A * prior + B) % M;
	re prior;
}

int upd (int x, int l, int r, int lc, int rc, int z) {
	if (r < lc || l > rc) re tree[x] + add[x];
	if (l >= lc && r <= rc) {
		add[x] += z;
//		printf ("upd %d , %d %d , %d %d = %d + %d\n", x, l, r, lc, rc, tree[x], add[x]);
		re tree[x] + add[x];
	}
	int s = (l + r) / 2;
	tree[x] = min (upd (x * 2, l, s, lc, rc, z), upd (x * 2 + 1, s + 1, r, lc, rc, z));
//	printf ("upd %d , %d %d , %d %d = %d + %d\n", x, l, r, lc, rc, tree[x], add[x]);
	re tree[x] + add[x];
}

int get (int x, int l, int r, int lc, int rc) {
	if (r < lc || l > rc) re 1e9;
	if (l >= lc && r <= rc) re tree[x] + add[x];
	int s = (l + r) / 2;
	re min (get (x * 2, l, s, lc, rc), get (x * 2 + 1, s + 1, r, lc, rc)) + add[x];
}

int main () {
	int tt;
	cin >> tt;
	for (int it = 1; it <= tt; it++) {
		scanf ("%I64d%I64d", &N, &K);
		scanf ("%I64d%I64d%I64d%I64d%I64d%I64d", &X, &A, &B, &M, &p, &q);
		for (int i = 0; i < K; i++)
			scanf ("%I64d%I64d%I64d%I64d", &aa[i], &bb[i], &cc[i], &dd[i]);

		prior = X;

		for (int i = K; i < N; i++) {
			aa [i] = aa [i - K] + next() % (2 * p) - p;
			bb [i] = aa [i] + 1 + next() % (2 * (bb [i % K] - aa [i % K]));
			cc [i] = cc [i - K] + next() % (2 * q) - q;
			dd [i] = cc [i] + 1 + next() % (2 * (dd [i % K] - cc [i % K]));				
		}

		v1.clear ();
		v2.clear ();

		for (int i = 0; i < N; i++) {
			a [i] = aa [i] * 1000000 + i;
			b [i] = bb [i] * 1000000 + i;
			c [i] = cc [i] * 1000000 + i;
			d [i] = dd [i] * 1000000 + i;
			if (a[i] > b[i]) swap (a[i], b[i]);
			if (c[i] > d[i]) swap (c[i], d[i]);
			v1.pb (a[i]);
			v1.pb (b[i]);
			v2.pb (c[i]);
			v2.pb (d[i]);
		}

		sort (all (v1));
		v1.resize (unique (all (v1)) - v1.begin ());
		sort (all (v2));
		v2.resize (unique (all (v2)) - v2.begin ());

		all1.clear ();
		all2.clear ();

		for (int i = 0; i < sz (v1); i++) all1[v1[i]] = i;
		for (int i = 0; i < sz (v2); i++) all2[v2[i]] = i;

		memset (tree, 0, sizeof (tree));
		memset (add, 0, sizeof (add));
		v.clear ();
		for (int i = 0; i < N; i++) {
			a[i] = all1[a[i]] * 2;
			b[i] = all1[b[i]] * 2;
			c[i] = all2[c[i]] * 2;
			d[i] = all2[d[i]] * 2;
			v.pb (mp (mp (a[i], 0), mp (c[i], d[i])));
			v.pb (mp (mp (b[i], 1), mp (c[i], d[i])));
//			printf ("%I64d %I64d\n", a[i] / 2, b[i] / 2);
//			printf ("	%I64d %I64d +1\n", c[i], d[i]);
			upd (1, 0, MAX - 1, c[i], MAX - 1, 1);
		}

		sort (all (v));

		int ans = N + 1, cur = 0;
		int cr = 0, nr = 0;
		for (int i = sz (v) - 1; i >= 0; i--) {
			if (v[i].fi.fi != v[i + 1].fi.fi) cr = nr;
			CR[i] = cr;
			if (v[i].fi.se == 0)
				nr = max (nr, v[i].se.fi);
		}
		int cl = MAX + 1;
		for (int i = 0; i + 1 < sz (v); i++) {
			if (v[i].fi.se == 0) {
				cur++;
				upd (1, 0, MAX - 1, v[i].se.fi, MAX - 1, -1);
//				printf ("%d %d -1\n", v[i].se.fi, v[i].se.se);
			} else {
				cur--;
				upd (1, 0, MAX - 1, 0, v[i].se.se, 1);
				cl = min (cl, v[i].se.se);
//				printf ("%d %d +1\n", v[i].se.fi, v[i].se.se);
			}
			cr = CR[i];
//			printf ("%d, %d - %d %d = %d\n", v[i].fi.fi + 1, cur, cl + 1, cr - 1,get (1, 0, MAX - 1, cl + 1, cr - 1));
//			for (int j = cl + 1; j <= cr - 1; j++) printf ("%d ", get (1, 0, MAX - 1, j, j));
//			printf ("\n");
			if (v[i + 1].fi != v[i].fi && cl + 1 <= cr - 1)
				ans = min (ans, cur + get (1, 0, MAX - 1, cl + 1, cr - 1));	
		}

		if (ans >= N) ans = -1;

		cout << "Case #" << it << ": " << ans;
		cout << endl;

		cerr << it << endl;
	}
	return 0;
}