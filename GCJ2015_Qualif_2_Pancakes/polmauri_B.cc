#include <iostream>
#include <vector>
using namespace std;

int main() {
  int tcas;
  cin >> tcas;
  for (int cas = 1; cas <= tcas; ++cas) {
    int n;
    cin >> n;
    vector<int> v(n);
    for (int i = 0; i < n; ++i) {
      cin >> v[i];
    }
    int m = 0;
    for (int i = 0; i < n; ++i) {
      m = max(m, v[i]);
    }
    int res = m;
    for (int t = 1; t < m; ++t) {
      int p = 0;
      for (int i = 0; i < n; ++i) {
        p += (v[i] + t - 1)/t - 1;
      }
      res = min(res, p + t);
    }
    cout << "Case #" << cas << ": " << res << endl;
  }
}
