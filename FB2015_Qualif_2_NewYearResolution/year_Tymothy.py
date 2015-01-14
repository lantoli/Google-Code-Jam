from itertools import *
I=lambda:map(int,raw_input().split())
for t in range(input()):
    e=I()
    a=[I()for i in"."*input()]
    print "Case #%d: %s" % (t+1, ["no", "yes"][any(map(sum,zip(*x))==e for r in range(1,len(a)+1) for x in combinations(a,r))])
