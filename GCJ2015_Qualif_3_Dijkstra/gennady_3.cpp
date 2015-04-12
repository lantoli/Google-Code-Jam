#include <bits/stdc++.h>

using namespace std;

#define I 2
#define J 3
#define K 4

const int mat[5][5] = {{ 0, 0, 0, 0, 0},
                       { 0, 1, I, J, K},
                       { 0, I,-1, K,-J},
                       { 0, J,-K,-1, I},
                       { 0, K, J,-I,-1}};

inline int mul(int a, int b) {
  int sign = 1;
  if (a < 0) {
    a = -a;
    sign = -sign;
  }
  if (b < 0) {
    b = -b;
    sign = -sign;
  }
  return mat[a][b] * sign;
}

const int MAGIC = 42;

int main() {
  freopen("in", "r", stdin);
  freopen("out", "w", stdout);
  int tt;
  scanf("%d", &tt);
  for (int qq = 1; qq <= tt; qq++) {
    printf("Case #%d: ", qq);
    int len;
    long long X;
    string s;
    cin >> len >> X;
    cin >> s;
    if (X > MAGIC) {
      X -= (X - MAGIC) / 4 * 4;
    }
    bool seenI = false;
    bool seenJ = false;
    int cur = 1;
    for (int i = 0; i < X; i++) {
      for (int j = 0; j < len; j++) {
        int c = (s[j] == 'i' ? I : (s[j] == 'j' ? J : K));
        cur = mul(cur, c);
        if (cur == I && !seenI) {
          cur = 1;
          seenI = true;
        }
        if (cur == J && seenI && !seenJ) {
          cur = 1;
          seenJ = true;
        }
      }
    }
    puts(seenI && seenJ && cur == K ? "YES" : "NO");
  }
  return 0;
}
