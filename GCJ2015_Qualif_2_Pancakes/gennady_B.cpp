#include <bits/stdc++.h>

using namespace std;

int a[1234567];

int main() {
  freopen("in", "r", stdin);
  freopen("out", "w", stdout);
  int tt;
  scanf("%d", &tt);
  for (int qq = 1; qq <= tt; qq++) {
    printf("Case #%d: ", qq);
    int n;
    scanf("%d", &n);
    int mx = 0;
    for (int i = 0; i < n; i++) {
      scanf("%d", a + i);
      mx = max(mx, a[i]);
    }
    int ans = mx;
    for (int eat = 1; eat <= mx; eat++) {
      int cur = eat;
      for (int i = 0; i < n; i++) {
        cur += (a[i] - 1) / eat;
      }
      ans = min(ans, cur);
    }
    printf("%d\n", ans);
  }
  return 0;
}
