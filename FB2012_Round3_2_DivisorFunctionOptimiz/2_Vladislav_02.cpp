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
typedef vector<ii> vii;
typedef vector<string> vs;
typedef vector<vi> vvi;

template<class T> T abs(T x) { re x > 0 ? x : -x; }

const int MAX = 40000000;
const int mod = 1000000000 + 7;

int n;
int m;

int f[MAX];
int q[3000000];
int fm[3000000];
int a1, b1, a2, b2, c;
int r = 0;

int cnt[37];
ll small[37];

int find (int a, int b, int c) {
	int l = -1, r = ::r - 1;
	while (r - l > 1) {
		int s = (l + r) / 2;
		if (1.0 / (pow (q[s], (b + 0.0) / a) - 1) > c - 1e-15) l = s; else r = s;
	}
	re r;
}

int power (int a, int b) {
	int c = 1;
	while (b) {
		if (b & 1) c = ((ll)c * a) % mod;
		b /= 2;
		a = ((ll)a * a) % mod;
	}
	re c;
}

ii calc (int a, int b) {
	int ap = 1, aq = 1;
	for (int i = 1; i <= 36; i++) {
		cnt[i] = find (a, b, i);
//		printf ("%d = %d\n", i, q[cnt[i] - 1]);
	}
	memset (small, 0, sizeof (small));
	for (int i = 1; i <= 35; i++) {
		if (cnt[i] - cnt[i + 1] > 0) {
//			printf (" *= %d ^ %d\n", (i + 1), (cnt[i] - cnt[i + 1]) * a);
			int k = i + 1;
			for (int j = 2; j * j <= k; j++)
				while (k % j == 0) {
					small[j] += (ll)(cnt[i] - cnt[i + 1]) * a;
					k /= j;
				}
			if (k > 1) small[k] += (ll)(cnt[i] - cnt[i + 1]) * a;
		}
//		printf (" /= %d ^ %d\n", fm[cnt[i]], b);
		aq = ((ll)aq * fm[cnt[i]]) % mod;
		for (int j = 0; j < cnt[i] && q[j] <= 36; j++) small[q[j]] -= b;
	}	
	ap = power (ap, a);
	aq = power (aq, b);
//	printf ("%d %d\n", ap, aq);
	for (int i = 2; i < 36; i++)
		if (small[i] > 0)
			ap = ((ll)ap * power (i, small[i] % (mod - 1))) % mod;
		else	
		if (small[i] < 0)
			aq = ((ll)aq * power (i, (-small[i]) % (mod - 1))) % mod;
//	printf ("%d %d = %d %d\n", a, b, ap, aq);
	re mp (ap, aq);
}

int main () {
        for (int i = 2; i < MAX; i++)
        	if (!f[i]) {
        		q[r++] = i;
        		if ((ll)i * i < MAX)
        			for (int j = i * i; j < MAX; j += i) f[j] = 1;
        	}
	fm[0] = 1;
	for (int j = 0; j < r; j++) {
		if (q[j] > 36)
			fm[j + 1] = ((ll)fm[j] * q[j]) % mod;
		else	
			fm[j + 1] = fm[j];
	}	

	cerr << "primes" << endl;

	int tt;
	cin >> tt;
	for (int it = 1; it <= tt; it++) {
	        cin >> b1 >> b2 >> a1 >> a2 >> c;
	 	ll ap = 0, aq = 0;
	 	int kkk = 0;
        	for (int b = b1; b <= b2; b++)
		        for (int a = a1; a <= a2 && a <= b * c; a++) {
		        	ii tmp = calc (a, b);
		        	ap = ap + tmp.fi;
		        	aq = aq + tmp.se;
		        	kkk++;
		        	if (kkk % 10000 == 0) cerr << kkk << " " << a << " " << b << endl;
		        }
		        	
		cout << "Case #" << it << ": " << ap << " " << aq;
		cout << endl;
		cerr << it << endl;
	}
	return 0;
}