#define _CRT_SECURE_NO_DEPRECATE
#define _SECURE_SCL 0

#include <algorithm>
#include <cmath>
#include <cstdio>
#include <cstdlib>
#include <ctime>
#include <map>
#include <set>
#include <string>
#include <utility>
#include <vector>
#include <iostream>
#include <queue>
#include <deque>
#include <stack>
#include <list>
#include <cctype>
#include <sstream>
#include <cassert>
#include <bitset>
#include <memory.h>
#include <complex>

using namespace std;

#pragma comment(linker, "/STACK:200000000")

typedef long long int64;

#define forn(i, n) for(int i = 0; i < (int)(n); i++)
#define ford(i, n) for(int i = (int)(n) - 1; i >= 0; i--)
#define fore(i, a, n) for(int i = (int)(a); i < (int)(n); i++)
#define pb push_back
#define mp make_pair
#define fs first
#define sc second
#define last(a) (int(a.size()) - 1)
#define all(a) a.begin(), a.end()

const double EPS = 1E-9;
const int INF = 1000000000;
const int64 INF64 = (int64) 1E18;
const double PI = 3.1415926535897932384626433832795;

const int MOD = INF + 7;

const int NMAX = 40000000;
bool isp[NMAX];
int p[NMAX], np;
int64 res1, res2, prod[NMAX], prod1[NMAX];

int find_pw(int a, int b, int p) {
	double prev = 0;
	for (int i = 0; i < 100; i++) {
		double cur = pow(i + 1, double(a) / b) / pow(p, (double)i);
		if (i > 0 && cur < prev)
			return i - 1;
		prev = cur;
	}
	throw;
}

int64 binpow(int64 a, int64 b) {
	int64 res = 1;
	while (b > 0) 
		if (b % 2 == 1) {
			res = res * a % MOD;
			b--;
		}
		else {
			a = a * a % MOD;
			b >>= 1;
		}
	return res;
}

int binsearch(int a, int b, int cur) {
	int cur_ans = find_pw(a, b, p[cur]);
	int l = cur;
	int r = np - 1;
	while (r - l > 1) {
		int mid = (l + r) >> 1;

		if (find_pw(a, b, p[mid]) == cur_ans)
			l = mid;
		else
			r = mid;
	}

	for (int i = r; i >= l; i--)
		if (find_pw(a, b, p[i]) == cur_ans)
			return i;
	throw;
}

int64 gcd(int64 a, int64 b) {
	return a == 0 ? b : gcd(b % a, a);
}

void solve_pair(int a, int b) {
	res1 = res2 = 1;
	if (a <= b)
		return;

	vector<int64> ans(50);
	forn(i, 20) {
		int cur_ans = find_pw(a, b, p[i]);

		ans[i] += cur_ans * b;
		int val = cur_ans + 1;
		forn(j, 20)
			while (val % p[j] == 0) {
				val /= p[j];
				ans[j] -= a;
			}
		if (val > 1)
			throw;
	}

	int cur = 20;
	while (true) {
		int cur_ans = find_pw(a, b, p[cur]);
		if (cur_ans == 0)
			break;

		int rg = binsearch(a, b, cur);

		res2 = res2 * binpow(prod[rg], cur_ans * b) % MOD * binpow(prod1[cur - 1], cur_ans * b) % MOD;
		int val = cur_ans + 1;
		forn(j, 20)
			while (val % p[j] == 0) {
				val /= p[j];
				ans[j] -= int64(rg - cur + 1) * a;
			}
		if (val > 1)
			throw;

		cur = rg + 1;
	}

	forn(i, 50) {
		if (ans[i] == 0)
			continue;
		if (i >= 20)
			throw;
		if (ans[i] < 0)
			res1 = res1 * binpow(p[i], -ans[i]) % MOD;
		else
			res2 = res2 * binpow(p[i], ans[i]) % MOD;
	}


//	res1 = binpow(res1, a);
//	res2 = binpow(res2, b);
}

void solve_one() {
	int64 sum1 = 0, sum2 = 0;
	
	int b1, b2, a1, a2, c;
	cin >> b1 >> b2 >> a1 >> a2 >> c;
	
	for (int b = b1; b <= b2; b++)
		for (int a = a1; a <= a2 && a <= c * b; a++) {
			solve_pair(a, b);
//			cerr << a << ' ' << b << ' ' << res1 << ' ' << res2 << endl;
//			System.out.println(a + " " + b + " " + res1 + " " + res2);
			sum1 = (sum1 + res1);
			sum2 = (sum2 + res2);
		}

	cout << sum1 << ' ' << sum2 << endl;
}

int main() {
#ifdef RADs_project
    freopen("input.txt", "rt", stdin);
    freopen("output.txt", "wt", stdout);
#endif

	for (int i = 2; i < NMAX; i++)
		if (!isp[i]) {
			p[np++] = i;
			for (int j = i + i; j < NMAX; j += i)
				isp[j] = true;
		}

	cerr << clock() << endl;

	prod[0] = prod1[0] = 1;
	for (int i = 1; i < np; i++) {
		prod[i] = prod[i - 1] * p[i] % MOD;
		prod1[i] = binpow(prod[i], MOD - 2);
		if (prod[i] * prod1[i] % MOD != 1)
			throw;
	}

	cerr << clock() << endl;
/*
	cerr << find_pw(25000, 1000, 2) << endl;
	for (int i = 2063691; i >= 2063687; i--)
		cerr << find_pw(25, 1, p[i]) << endl;

	for (int i = 1; i <= 5; i++)
		for (int j = 1; j <= 5; j++)
			solve_pair(i, j);
*/
	int tt;
	cin >> tt;
	forn(i, tt) {
		cerr << i << ' ' << clock() << endl;
		printf("Case #%d: ", i + 1);
		solve_one();
	}
	
	return 0;
}