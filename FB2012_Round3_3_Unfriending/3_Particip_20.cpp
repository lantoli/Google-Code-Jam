#include <stdio.h>
#include <vector>
#include <algorithm>
#include <set>
using namespace std;

vector< pair<long long,int> > xx; //
long long X[50000];
int xmap[50000];
int n, m;

vector<int> gp[1500]; //
int gpc[1500];
vector<int> gg[50000];//

set< pair<long long, int> > tlist;//
bool tc[50000];
bool fc[50000];
bool ext[50000];

int wcol;
int tcol[50000];
int fcol[50000];

vector< pair<int, int> > rev;//

long long wdet;
void got(int);
void gof(int);
void got_(int);
void gof_(int);

void got(int x) {
	tc[x] = true;

	long long pos = xx[x].first;
	long long next;
	set< pair<long long, int> >::iterator p, q;
	next = pos - wdet + 1;
	while(1){
		p = tlist.lower_bound(make_pair(next, 0));
		if( p == tlist.end() ) break;
		int gx = (*p).first ;
		int gy = (*p).second ;

		if (gx >= pos + wdet) break;
		if (gy != x) {
			tlist.erase(p);
			gof(gy);
		}
		next = gx + 1;
	}

	rev.push_back( make_pair(x, 0) );
}

void gof(int x) {
	fc[x] = true;
	int i, j;
	for (i=0;i<gg[x].size();i++){
		int g = gg[x][i];
		while (gpc[g] < gp[g].size() && tc[ gp[g][ gpc[g] ] ]) gpc[g] ++;
		for(j=gpc[g];j<gp[g].size();j++) {
			if (j < gpc[g]) j = gpc[g];
			if (j >= gp[g].size()) break;
			int p = gp[g][j];
			if (!tc[p] && p != x) got(p);
			while (gpc[g] < gp[g].size() && tc[ gp[g][ gpc[g] ] ]) gpc[g] ++;
		}
	}

	if (!ext[x] && !tc[x]) {
		got(x);
	}

	rev.push_back( make_pair(x, 1) );
}

void got_(int x){
	tc[x] = true;
	tcol[x] = wcol;

	int i, j;
	for (i=0;i<gg[x].size();i++){
		int g = gg[x][i];
		while (gpc[g] < gp[g].size() && fc[ gp[g][ gpc[g] ] ]) gpc[g] ++;
		for(j=gpc[g];j<gp[g].size();j++) {
			if (j < gpc[g]) j = gpc[g];
			if (j >= gp[g].size()) break;
			int p = gp[g][j];
			if (!fc[p] && p != x) gof_(p);
			while (gpc[g] < gp[g].size() && fc[ gp[g][ gpc[g] ] ]) gpc[g] ++;
		}
	}
	
	if (!ext[x] && !fc[x]) {
		gof_(x);
	}
}

void gof_(int x) {
	fc[x] = true;
	fcol[x] = wcol;

	long long pos = xx[x].first;
	set< pair<long long, int> >::iterator p, q;
	long long next = pos - wdet + 1;
	while(1){
		p = tlist.lower_bound(make_pair(next, 0));
		if( p == tlist.end() ) break;
		int gx = (*p).first ;
		int gy = (*p).second ;

		if (gx >= pos + wdet) break;
		if (gy != x) {
			tlist.erase(p);
			got_(gy);
		}
		next = gx + 1;
	}

	rev.push_back( make_pair(x, 0) );
}

bool possible(long long t) { // t미만 떨어진건 제거
	//printf("Possible %d\n", t);
	int i;
	wdet = t;

	for(i=0;i<n;i++){
		tlist.insert( make_pair(xx[i].first, i) );
		tc[i] = false;
		fc[i] = false;
	}

	for(i=0;i<m;i++) gpc[i] = 0;
	for(i=0;i<n;i++){
		if (tc[i] == false) {
			got(i);
		}
		if (fc[i] == false) {
			gof(i);
		}
	}

	for(i=0;i<n;i++){
		tc[i] = false;
		fc[i] = false;
		tcol[i] = fcol[i] = -1;
	}

	tlist.clear();

	for(i=0;i<n;i++){
		tlist.insert( make_pair(xx[i].first, i) );
		tc[i] = false;
		fc[i] = false;
	}

	wcol = 1;
	
	for(i=0;i<m;i++) gpc[i] = 0;
	for(i=rev.size()-1; i>=0; i-- ){
		int x = rev[i].first;
		int y = rev[i].second;
		if (y == 0){ // t
			if (!tc[x]) {
				got_(x);
				wcol ++;
			}
		}
		if (y == 1){ // f
			if (!fc[x]) {
				gof_(x);
				wcol ++;
			}
		}
	}
	
	tlist.clear();
	rev.clear();

	bool result = true;
	for(i=0;i<n;i++){
		if (tcol[i] == fcol[i]){
			result = false;
			break;
		}
	}
	return result;
}
int main(){
	freopen("unfriending.txt","r",stdin);
	freopen("output.txt","w",stdout);
	int T;
	scanf("%d",&T);
	while(T-->0) {
		long long a, b, p;
		scanf("%d %d",&n,&m);
		int i, j;
		scanf("%lld %lld %lld %lld", &X[0], &a, &b, &p);
		//X[0] %= p;
		for(i=1;i<n;i++){
			X[i] = (X[i-1] * a + b) % p;
		}
		for(i=0;i<n;i++) ext[i] = false;
		for(i=0;i<n;i++){
			xx.push_back( make_pair(X[i], i) );
		}
		sort(xx.begin(), xx.end());
		for(i=0;i<n;i++){
			xmap[ xx[i].second ] = i;
		}
		
/*
		for(i=0;i<n;i++) printf("%lld ", X[i]);
		printf("\n");
		int D = 7392;
		for(i=0;i<n;i++)for(j=0;j<n;j++) {
			if(i != j && X[i] - X[j] < D && -D < X[i] - X[j]) printf("%d %d!\n", i, j);
			if (X[i] - X[j] == D) printf("%d %d D\n", i, j);
		}
*/		

		long long y0;
		int sz;
		// 2x 2x+1 
		for(i=0;i<m;i++){
			scanf("%d %lld %lld %lld", &sz, &y0, &a, &b);

			for(j=0;j<sz;j++){
//				printf("%d ", y0);
				ext[xmap[y0]] = true;
				gp[i].push_back(xmap[y0]);
				gg[xmap[y0]].push_back(i);
				y0 = (y0 * a + b)%n;
			}
//			printf("\n");
		}
		{
			long long s, e, m;
			s = 1; e = xx[n-1].first - xx[0].first;
			while(s<=e) {
				m = (s+e) / 2;
				if (possible(m)) { // m미만은 제거할때, 즉 이상은 됨
					s = m+1;
				} else {
					e = m-1;
				}
			}
			static int cs = 1;
			printf("Case #%d: %lld\n", cs ++, s-1);
		}

		for(i=0;i<m;i++) gp[i].clear();
		for(i=0;i<n;i++) gg[i].clear();
		xx.clear();
	}
	return 0;
}