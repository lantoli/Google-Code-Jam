from collections import *
for T in range(input()):
    m,n=map(int,raw_input().split())
    board=[list(raw_input()) for i in range(m)]
    for i in range(m):
        for j in range(n):
            if board[i][j] == 'S':
                start = i,j
                board[i][j] = '.'
            elif board[i][j] == 'G':
                goal = i,j
                board[i][j] = '.'
    boards = []
    for _ in range(4):
        B = [r[:] for r in board]
        for i in range(m):
            for j in range(n):
                c = B[i][j]
                if c not in '^>v<': continue
                dx,dy = {'^':(0,-1), '>':(1,0), 'v':(0,1), '<':(-1,0)}[c]
                x,y = j+dx,i+dy
                while 0<=x<n and 0<=y<m and B[y][x] not in '^>v<#':
                    B[y][x]='*'
                    x += dx
                    y += dy
        boards.append(B)
        for i in range(m):
            for j in range(n):
                board[i][j] = {'^':'>','>':'v','v':'<','<':'^'}.get(board[i][j],board[i][j])
    q = deque([(start,0)])
    v = {(start,0):0}
    res = "impossible"
    while q:
        (Y,X),M = state = q.popleft()
        M = (M+1)%4
        c = v[state]
        if (Y,X) == goal:
            res = c
            break
        for dx,dy in [(0,-1), (1,0), (0,1), (-1,0)]:
            x,y=X+dx,Y+dy
            if 0<=x<n and 0<=y<m and boards[M][y][x] == '.':
                s2 = (y,x),M
                if s2 not in v:
                    v[s2] = c+1
                    q.append(s2)

    print "Case #%d: %s" % (T+1, res)
