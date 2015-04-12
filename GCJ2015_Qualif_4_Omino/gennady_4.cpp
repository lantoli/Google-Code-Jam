#include <bits/stdc++.h>

using namespace std;

int main() {
  freopen("in", "r", stdin);
  freopen("out", "w", stdout);
  int tt;
  scanf("%d", &tt);
  for (int qq = 1; qq <= tt; qq++) {
    printf("Case #%d: ", qq);
    int x, r, c;
    scanf("%d %d %d", &x, &r, &c);
    if (r > c) {
      swap(r, c);
    }
    bool win = (r * c % x != 0 || x >= 7);
    if (x == 3) {
      win |= (r == 1);
    }
    if (x == 4) {
      win |= (r == 1);
      win |= (r == 2);
    }
    if (x == 5) {
      win |= (r == 1);
      win |= (r == 2);
      win |= (r == 3 && c == 5);
    }
    if (x == 6) {
      win |= (r == 1);
      win |= (r == 2);
      win |= (r == 3);
    }
    puts(win ? "RICHARD" : "GABRIEL");
  }
  return 0;
}
