#include <cstdio>
#include <iostream>
#include <vector>
#include <cmath>
#include <algorithm>
#include <string>
#include <set>
#include <map>
#include <ctime>
#include <cstring>
#include <cassert>
#include <sstream>
#include <complex>

using namespace std;

#define forn(i, n) for(int i = 0; i < (int)(n); i++)
#define ford(i, n) for(int i = (int)(n) - 1; i >= 0; i--)
#define pb push_back
#define mp make_pair
#define fs first
#define sc second
#define last(a) int(a.size() - 1)
#define all(a) a.begin(), a.end()
#define seta(a,x) memset (a, x, sizeof (a))
#define I (int)

typedef long long int64;
typedef pair <int, int> pii;
typedef long double ldb;

const long double eps = 1e-9;
const int inf = 1e9;//(1 << 30) - 1;
const int64 inf64 = ((int64)1 << 62) - 1;
const long double pi = 3.1415926535897932384626433832795;
const string task = "";

template <class T> T sqr (T x) {return x * x;}

const int nmax = 100100;

long long x[nmax];
long long a, b, p;
int n, m;
vector<int> wh[nmax];
set<int> g[nmax];
vector<pii> e;
int col[nmax], buf[nmax];
vector<int> v;
set<int> act;
              /*
long long abs(long long x){
	if (x < 0) return -x;
	return x;
}               */

int dfs(int u, int c, int val){
	if (col[u] != -1){
		return col[u] == c;
	}
	col[u] = c;
	if (c == 0){
		forn(j, v.size())
			if (v[j] != u && abs(x[u] - x[v[j]]) < val)
				if (!dfs(v[j], 1, val))
					return 0;
	} else {
		set<int> :: iterator k;
		forn(j, wh[u].size())
			for (k = g[wh[u][j]].begin(); k != g[wh[u][j]].end(); k++)
				if (*k != u && act.count(*k) > 0)
					if (!dfs(*k, 0, val))
						return 0;
	}
	return 1;
}

/*void build_g(vector<int> v, int val){
	forn(i, v.size())
		forn(j, v.size()){
			if (abs(x[v[i]] - x[v[j]]) < val){
				kill[i][0].pb(mp(j, 1));
				kill[j][0].pb(mp(i, 1));
			}
//			if (interseq(v[i], v[j])){
//				kill[i][1].pb(mp(j, 0));
//				kill[j][1].pb(mp(i, 0));
//			}
		}
}
*/
bool check(int val){
	int cnt = 0;
	forn(i, e.size()){
		for (int  j = i + 1; j < (int) e.size(); j ++){
			if (e[j].fs - e[i].fs >= val) break;
			cnt ++;
			if (cnt > 3 * m) return 0;
		}
	}
//	forn(i, n)
//		kill[i].clear();
	v.clear();
	forn(i, e.size())
		for (int j = i + 1; j < (int)e.size(); j ++){
			if (e[j].fs - e[i].fs >= val) break;
			v.pb(e[i].sc);
			v.pb(e[j].sc);
		}	
	sort(all(v));
	v.erase(unique(all(v)), v.end());
//	build_g(v, val);
	random_shuffle(all(v));
	act.clear();
	forn(i, v.size())
		act.insert(v[i]);

	forn(i, n)
		col[i] = -1;
	forn(i, v.size())
		if (wh[v[i]].size() == 0)
			if (!dfs(v[i], 0, val))
				return 0;
	forn(i, v.size()){
		forn(j, v.size())
			buf[v[j]] = col[v[j]];
		int cur = rand() % 2;
		if (!dfs(v[i], cur, val)){
			forn(j, v.size())
				col[v[j]] = buf[v[j]];
			if (!dfs(v[i], cur ^ 1, val))
				return 0;
		}
	}
	return 1;
}

void read(){
	cerr << "here" << endl;
	cin >> n >> m;
	cin >> x[0] >> a >> b >> p;
//	x[0] %= p;
	for (int i = 1; i <= n; i ++)
		x[i] = (x[i-1] * a + b) % p;	
	e.clear();
	forn(i, n)
		e.pb(mp(x[i], i));
	sort(all(e));
	forn(i, n)
		wh[i].clear();
	forn(i, m){
		int sz;
		long long a, b, y0;
		cin >> sz >> y0 >> a >> b;
		assert(y0 < n);
		g[i].clear();
		g[i].insert(y0);
		forn(q, sz - 1){
			y0 = (y0 * a + b) % n;
			g[i].insert(y0);
		}
		set<int> :: iterator j;
		for (j = g[i].begin(); j != g[i].end(); j++)
			wh[*j].pb(i);
	}
}

void solve(){
	read();
//	forn(i, n)
//		cerr << x[i] << " ";
//	cerr << endl;
//	cerr << endl;
//	forn(i, m){
//		set<int> :: iterator j;
//		for (j = g[i].begin(); j != g[i].end(); j++)
//			cerr << *j << " ";
//		cerr << endl;	
//	}
//	cerr << endl;



//	forn(i, e.size()){
//		if (i > 0 && e[i].fs - e[i-1].fs >= 50000)
//			cerr << endl;
//		cerr << e[i].fs << " " << e[i].sc << endl;
//	}

	int l = 0, r = (int)2e9;
	while (l < r){
		int m = (l + r + 1) / 2;
		if (check(m))
			l = m;
		else
			r = m - 1;
	}
	cout << l << endl;
	
}

int main (){
	freopen("input.txt", "rt", stdin);
	freopen("output.txt", "wt", stdout);

	int tst;
	cin >> tst;
	forn (i, tst) {
		printf("Case #%d: ", i + 1);
		solve();
	}

	
	return 0;
}
