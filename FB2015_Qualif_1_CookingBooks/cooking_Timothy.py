for t in range(input()):
    x = raw_input()
    a = b = int(x)
    x = list(x)
    for i in range(len(x)):
        for j in range(i):
            x[i],x[j] = x[j],x[i]
            if x[0] != '0':
                v = int("".join(x))
                a = min(a, v)
                b = max(b, v)
            x[i],x[j] = x[j],x[i]
    print "Case #%d: %d %d" % (t+1, a, b)
