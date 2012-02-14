#include <vector>
#include <list>
#include <map>
#include <set>
#include <deque>
#include <queue>
#include <stack>
#include <bitset>
#include <algorithm>
#include <functional>
#include <numeric>
#include <utility>
#include <sstream>
#include <iostream>
#include <iomanip>
#include <cstdio>
#include <cmath>
#include <cstdlib>
#include <cctype>
#include <string>
#include <cstring>
#include <cstdio>
#include <cmath>
#include <cstdlib>
#include <ctime>

using namespace std;

#define SIZE(X) ((int)(X.size()))
#define MP(X,Y) make_pair(X,Y)
typedef long long int64;
#define two(X) (1<<(X))
#define contain(S,X) (((S)&two(X))!=0)
template<class T> inline void checkmin(T &a,T b){if(b<a) a=b;}
template<class T> inline void checkmax(T &a,T b){if(b>a) a=b;}
typedef pair<int,int> ipair;

//#define DEBUG

const int maxn=50000+5;
const int maxsize=3<<20;

typedef vector<int> VI;

int n,m;
VI a[maxn];
int x[maxn],y[maxsize];
int idx[maxn];
ipair q[maxn];
int first[maxn];
VI g[maxsize],g2[maxsize];
bool exists[maxn];
VI b[maxn];
int c[maxn];

int sizeq2;
int q2[maxsize];
bool visited[maxsize];
int exp_color,color[maxsize];

int random2()
{
	int v1=rand()&32767;
	int v2=rand()&32767;
	return (v1<<15)|v2;
}
void generate(string filename)
{
	freopen(filename.c_str(),"w",stdout);
	printf("20\n");
	for (int i=0;i<20;i++)
	{
		//int n=rand()%15+2;
		//int m=rand()%(n-1);
		int n=rand()%49999+2;
		int m=rand()%1501;
		printf("%d %d\n",n,m);
		printf("%d %d %d %d\n",random2()%1000000000+1,random2()%1000000000+1,random2()%1000000000+1,random2()%1000000000+1);
		for (int i=0;i<m;i++)
			printf("%d %d %d %d\n",random2()%(1000000/m)+1,random2()%n,random2()%1000000000+1,random2()%1000000000+1);
	}
	exit(0);
}

void readin()
{
	int x0,a,b,p;
	scanf("%d%d",&n,&m);
	scanf("%d%d%d%d",&x0,&a,&b,&p);
	x[0]=x0;
	for (int i=1;i<n;i++) x[i]=(int)(((int64)x[i-1]*(int64)a+(int64)b)%p);
	for (int i=0;i<m;i++)
	{
		int size,y0;
		scanf("%d%d%d%d",&size,&y0,&a,&b);
		y[0]=y0;
		for (int j=1;j<size;j++) y[j]=(int)(((int64)y[j-1]*(int64)a+(int64)b)%n);
		set<int> S;
		for (int j=0;j<size;j++) S.insert(y[j]);
		::a[i]=VI(S.begin(),S.end());
	}
}

int slow()
{
	if (n>20) return -1;
	int mask=0;
	for (int i=0;i<m;i++) for (int j=0;j<SIZE(a[i]);j++) mask|=two(a[i][j]);
	int ret=0;
	mask=two(n)-1-mask;
	for (int set=0;set<two(n);set++) if ((mask&set)==0)
	{
		bool ok=true;
		for (int i=0;i<m;i++)
		{
			int cnt=0;
			for (int j=0;j<SIZE(a[i]);j++) if (contain(set,a[i][j])) cnt++;
			if (cnt>=2) { ok=false; break; }
		}
		if (!ok) continue;
		VI key;
		for (int i=0;i<n;i++) if (!contain(set,i)) key.push_back(x[i]);
		sort(key.begin(),key.end());
		int current=2100000000;
		for (int i=0;i+1<SIZE(key);i++) checkmin(current,key[i+1]-key[i]);
		checkmax(ret,current);
	}
	return ret;
}

void add(int a,int b)
{
	g[a].push_back(b);
	//printf("ADD %d %d\n",a,b);
}

int sk[maxsize],sksize,last[maxsize];

void DFS1(int p)
{
/*
	visited[p]=true;
	for (int i=SIZE(g[p])-1;i>=0;i--)
		if (!visited[g[p][i]])
			DFS1(g[p][i]);
	q2[sizeq2++]=p;
*/
	sksize=0;
	sk[sksize++]=p;
	last[p]=SIZE(g[p]);
	visited[p]=true;
	while (sksize>0)
	{
		int p=sk[sksize-1];
		if (last[p]==0)
		{
			q2[sizeq2++]=p;
			sksize--;
		}
		else
		{
			last[p]--;
			int other=g[p][last[p]];
			if (!visited[other])
			{
				visited[other]=true;
				last[other]=SIZE(g[other]);
				sk[sksize++]=other;
			}
		}
	}
}
void DFS2(int p)
{
/*
	color[p]=exp_color;
	for (int i=SIZE(g2[p])-1;i>=0;i--)
		if (color[g2[p][i]]<0)
			DFS2(g2[p][i]);
*/
	sksize=0;
	sk[sksize++]=p;
	last[p]=SIZE(g2[p]);
	color[p]=exp_color;
	while (sksize>0)
	{
		int p=sk[sksize-1];
		if (last[p]==0)
			sksize--;
		else
		{
			last[p]--;
			int other=g2[p][last[p]];
			if (color[other]<0)
			{
				color[other]=exp_color;
				last[other]=SIZE(g2[other]);
				sk[sksize++]=other;
			}
		}
	}
}

bool checkit(int limit)
{
	int totalsize=0;
	for (int i=0;i<m;i++) totalsize+=SIZE(a[i]);
	int size=n+n+totalsize+totalsize;
	int current_pos=n+n;
	for (int i=0;i<m;i++)
	{
		first[i]=current_pos;
		current_pos+=SIZE(a[i])*2;
	}
	for (int i=0;i<size;i++) g[i].clear();
	for (int i=0;i<n;i++) exists[i]=false;
	for (int i=0;i<m;i++) for (int j=0;j<SIZE(a[i]);j++) exists[a[i][j]]=true;
	for (int i=0;i<n;i++) if (!exists[i])
		add(n+i,i);
	for (int i=0;i<n;i++) b[i].clear();
	for (int i=0;i<m;i++) for (int j=0;j<SIZE(a[i]);j++) b[a[i][j]].push_back(i);
	for (int i=0;i<m;i++) c[i]=0;
	int k1=0,k2=0,c_bad=0;
	for (int i=0;i<n;i++)
	{
		for (;k2<n && x[k2]-x[i]<=limit;k2++) 
		{
			if (SIZE(b[k2])==0) c_bad++;
			for (int j=0;j<SIZE(b[k2]);j++) if ((++c[b[k2][j]])==2) c_bad++;
		}
		for (;k1<n && x[i]-x[k1]>limit;k1++)
		{
			if (SIZE(b[k1])==0) c_bad--;
			for (int j=0;j<SIZE(b[k1]);j++) if ((--c[b[k1][j]])==1) c_bad--;
		}
		if (SIZE(b[i])==0) c_bad--;
		for (int j=0;j<SIZE(b[i]);j++) if ((--c[b[i][j]])==1) c_bad--;
		if (c_bad>0) 
			add(i,n+i);
		else
		{
			for (int k=k1;k<k2;k++) if (k!=i) add(i,n+k);
		}
		if (SIZE(b[i])==0) c_bad++;
		for (int j=0;j<SIZE(b[i]);j++) if ((++c[b[i][j]])==2) c_bad++;
	}
	for (int k=0;k<m;k++)
	{
		int first_idx=first[k];
		VI key=a[k];
		int size=SIZE(key);
		int second_idx=first_idx+size;
		for (int i=0;i<size;i++)
		{
			if (i+1<size) add(first_idx+i,first_idx+i+1);
			add(first_idx+i,key[i]);
			if (i-1>=0) add(second_idx+i,second_idx+i-1);
			add(second_idx+i,key[i]);
		}
		for (int i=0;i<size;i++)
		{
			if (i-1>=0) add(n+key[i],second_idx+i-1);
			if (i+1<size) add(n+key[i],first_idx+i+1);
		}
	}
	sizeq2=0;
	for (int i=0;i<size;i++) visited[i]=false;
	for (int i=0;i<size;i++) g2[i].clear();
	for (int i=0;i<size;i++) for (int j=0;j<SIZE(g[i]);j++) g2[g[i][j]].push_back(i);
	for (int i=0;i<size;i++) if (!visited[i]) DFS1(i);
	exp_color=0;
	for (int i=0;i<size;i++) color[i]=-1;
	for (int i=sizeq2-1;i>=0;i--)
	{
		int key=q2[i];
		if (color[key]>=0) continue;
		DFS2(key);
		exp_color++;
	}
	for (int i=0;i<n;i++) if (color[i]==color[i+n]) return false;
	return true;
}

int solve()
{
	for (int i=0;i<n;i++) q[i]=MP(x[i],i);
	sort(q,q+n);
	for (int i=0;i<n;i++) y[i]=x[i];
	for (int i=0;i<n;i++) idx[q[i].second]=i;
	for (int i=0;i<n;i++) x[idx[i]]=y[i];
	for (int i=0;i<m;i++) for (int j=0;j<SIZE(a[i]);j++) a[i][j]=idx[a[i][j]];
	for (int i=0;i<m;i++) sort(a[i].begin(),a[i].end());
	int H=-1,T=2100000000;
	for (;H+1<T;)
	{
		int M=H+(T-H)/2;
	//	M=0;
	//	M=15317;
		if (checkit(M))
			H=M;
		else
			T=M;
	}
	return T;
}

int main(int argc,char **args)
{
#ifdef DEBUG
	if (0) generate("B.in");
	//freopen("input.txt","r",stdin);
	freopen("large.txt","r",stdin);
#else
	freopen("C.in","r",stdin);
	int __M=1,__C=0;
	if (argc==1)
		freopen("C.out","w",stdout);
	else if (argc==3)
	{
		__M=atoi(args[1]);
		__C=atoi(args[2]);
		char ofilename[1024];
		sprintf(ofilename,"C_%d_%d.out",__M,__C);
		freopen(ofilename,"w",stdout);
	}
#endif
	int testcase;
	scanf("%d",&testcase);
	for (int case_id=1;case_id<=testcase;case_id++)
	{
		readin();
#ifndef DEBUG
		if ((case_id-1)%__M!=__C) continue;
#endif
		printf("Case #%d: ",case_id);
#ifdef DEBUG
		//compare
		int ret2=slow();
		int ret=solve();
		if (ret!=ret2)
		{
			printf("ERROR\n");
			return 0;
		}
#else
		int ret=solve();
#endif
		printf("%d\n",ret);
		fflush(stdout);
	}
	return 0;
}
