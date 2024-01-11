import random
from hashlib import sha256

# from pyunit_prime import get_large_prime_length    # get random prime length
# from pyunit_prime import is_prime                  # check if the num is prime
# from pyunit_prime import prime_range               # generate prime within range

p = 18604511303632357477261733749289932684042548414204891841229696446591
q = 2238810024504495484628367478855587567273471529988554974877219789
g = 12340


# def primeFactorization():
#     p=0
#     q=get_large_prime_length(64)
#     while True:
#         d=random.randint(2,10000)
#         if d%2==0:
#             p=q*d+1
#             if is_prime(p)==True:
#                 break
#     primeList=prime_range(2,int(math.sqrt(d)))
#     result=[[0,0] for i in range(len(primeList))]
#     for i in range(len(primeList)):
#         result[i][0]=primeList[i]
#         while d%primeList[i]==0:
#             result[i][1]+=1
#             d=d//primeList[i]
#     if d!=1:
#         result.append([d,1])
#     result.append([q,1])
#     return p, q, result


def getGenerator(result, p=p, q=q):  # get g, result is multiplicative group of Zp, denote as Zp*
    generator = random.randint(1, 1000)
    while True:
        # discrete log g^q mod p,
        if quickPower(generator, q, p) != 1:
            generator += 1
        else:
            # if share common factor
            for i in range(len(result)):
                # pick i from Zp*, check if g^(p-1)/i mod p didn't share the common factor, if true
                if quickPower(generator, int((p - 1) / result[i][0]), p) == 1:
                    break
            if i != len(result) - 1:
                generator += 1
            else:
                break
    return generator


def getSecretKey(q=q):  # get SK,x
    x = random.randint(1, q)
    return x


def getPublicKey(x, g=g, p=p):  # get PK,h
    h = quickPower(g, x, p)
    return h


def quickPower(a, b, c):
    result = 1
    while b > 0:
        if b % 2 == 1:
            result = result * a % c
        a = a * a % c
        b >>= 1
    return result


def exgcd(a, b):  # Extended Euclidean
    if b == 0:
        return 1, 0, a
    else:
        x, y, gcd = exgcd(b, a % b)
        x, y = y, (x - (a // b) * y)
        return x, y, gcd


def str2int(msg):  # msg to int
    # msg_b = bytes(msg, encoding="utf8")
    # return int.from_bytes(msg_b, byteorder='big', signed=False)
    return int.from_bytes(sha256(bytes(msg, encoding='utf8')).digest(), byteorder='big', signed=False)

# def int2str(msg):                                # msg to bytes
#     return str(msg.to_bytes(length=2,byteorder='big',signed=False))

###################################### Chameleon Hash with secret sharing ######################################


def KeyGen(p, q, g, n):
    SKlist = getSecretKeySplit(n, q)
    PKlist = getPublicKeySplit(g, SKlist, n, p)
    return SKlist, PKlist


def getSecretKeySplit(n, q):  # get SKlist,x1,x2……xn
    SKlist = []
    for _ in range(n):
        SKlist.append(random.randint(1, q))
    return SKlist


def getPublicKeySplit(g, SKlist, n, p):  # get PKlist,h1,h2……hn
    PKlist = []
    for i in range(n):
        PKlist.append(quickPower(g, SKlist[i], p))
    return PKlist


def getr(n, q):  # rlist,r1,r2……rn
    rlist = []
    for _ in range(n):
        rlist.append(random.randint(1, q))
    return rlist