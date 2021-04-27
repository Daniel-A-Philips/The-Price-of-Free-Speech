def readData():
    a = []
    for line in open("Data//SMVI_Data.txt"):
        if "\\" in line:
            line = line[:-2]
        a.append((float)(line.split(":")[1]))
    foo = V2(a[0],a[1],a[2],a[3],a[4],a[5])
    print(foo)

def V1(aa,ba,am,bm,T,t):
    leftTop = (aa-ba)
    leftBottom = (bm-am)
    right = T/t
    left = leftTop/leftBottom
    total = abs(left * right)
    return total

def V2(va,na,vb,nb,T,t):
    left = (va/vb)
    right = T/t
    midtop = (nb-1)
    midlow = (na-1)
    mid = midtop/midlow
    total = abs(left*mid*right)
    return total
if __name__ == "__main__":
    readData()

