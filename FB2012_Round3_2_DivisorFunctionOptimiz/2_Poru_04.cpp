#include <iostream>
#include <sstream>
#include <string>
#include <vector>
#include <map>
#include <set>
#include <queue>
#include <cstdio>
#include <cstring>
#include <cmath>
#include <cctype>
#include <algorithm>
#include <numeric>
#include <utility>

using namespace std;

typedef long long ll;

const ll mod = 1000000007;

const int NUM_SMALL = 12;
int small_primes[NUM_SMALL] = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37};

const int MAX_P = (1<<25)+4;
int p_prod[MAX_P];
int num_primes[MAX_P];
char is_prime[MAX_P];

ll powmod(ll x, ll e) {
  ll ans = 1;
  ll cur = x;
  for (int i = 0; i < 60; i++) {
    if ((e>>i)&1)
      ans = ans * cur % mod;
    cur = cur * cur % mod;
  }
  return ans;
}

ll inv(ll x) {
  return powmod(x, mod-2);
}

pair <ll, ll> compute(int a, int b) {
  double r = a / (double) b;
  vector <ll> n_freqs(NUM_SMALL), d_freqs(NUM_SMALL);
  ll n = 1, d = 1;
  for (int k = 1; k < 40; k++) {
    // all numerators
    int y = (int) pow((k+1.0)/k, r), x = (int) pow((k+2.0)/(k+1), r);
    int num_p = num_primes[y] - num_primes[x];
    int top = k+1;
    for (int i = 0; i < NUM_SMALL; i++) {
      int p_i = small_primes[i];
      int e_i = 0;
      while (top % p_i == 0) {
	top /= p_i;
	e_i++;
      }
      n_freqs[i] += num_p * (ll) a * e_i;
    }
    // denominators of small primes
    for (int i = 0; i < NUM_SMALL; i++) {
      int p_i = small_primes[i];
      if (x < p_i && p_i <= y)
	d_freqs[i] += k*b;
    }
    // denominators of big primes
    d = d * powmod(p_prod[max(y, 40)] * inv(p_prod[max(x, 40)]) % mod, k*b) % mod;
  }
  // cancel small primes
  for (int i = 0; i < NUM_SMALL; i++) {
    ll m = min(n_freqs[i], d_freqs[i]);
    n = n * powmod(small_primes[i], n_freqs[i]-m) % mod;
    d = d * powmod(small_primes[i], d_freqs[i]-m) % mod;
  }
  return make_pair(n, d);
}

int main(void) {
  memset(is_prime, 1, sizeof(is_prime));
  for (int i = 2; i*i < MAX_P; i++)
    if (is_prime[i])
      for (int j = i*i; j < MAX_P; j += i)
	is_prime[j] = 0;
  p_prod[1] = 1;
  for (int p = 2; p < MAX_P; p++) {
    if (is_prime[p]) {
      num_primes[p] = num_primes[p-1] + 1;
      p_prod[p] = p_prod[p-1] * (ll) p % mod;
    }
    else {
      num_primes[p] = num_primes[p-1];
      p_prod[p] = p_prod[p-1];
    }
  }

  int T; cin >> T;
  for (int t = 1; t <= T; t++) {
    int b1, b2, a1, a2, c;
    cin >> b1 >> b2 >> a1 >> a2 >> c;
    ll n = 0, d = 0;
    for (int b = b1; b <= b2; b++)
      for (int a = a1; a <= a2 && a <= c*b; a++) {
	pair <ll, ll> nd = compute(a, b);
	n += nd.first;
	d += nd.second;
      }
    printf("Case #%d: %Ld %Ld\n", t, n, d);
  }
}
