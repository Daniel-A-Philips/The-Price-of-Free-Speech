def readData():
    a = []
    for line in open("Data//SMVI_Data.txt"):
        a.append((int)(line.split(":")[1]))
    f = V1(a[0],a[1],a[2],a[3],a[4],a[5])
    foo = V2(12,123,a[4],a[5])

def V1(aa,ba,am,bm,T,t):
    leftTop = (aa-ba)
    leftBottom = (bm-am)
    right = T/t
    left = leftTop/leftBottom
    total = abs(left * right)
    print(total)
    return total

def V2(va,vm,T,t):
    left = (va/vm)
    right = T/t
    total = abs(left*right)
    print(total)
    return total
readData()

