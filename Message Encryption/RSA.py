
import libnum
import random

bits_length = 56  # number of bits to match key length


def gcd(a, b):
    while b != 0:
        a, b = b, a % b
    return a


def get_keys():
    p = libnum.generate_prime(bits_length)
    q = libnum.generate_prime(bits_length)
    n = p * q
    PHI = (p - 1) * (q - 1)

    e = 65537
    # Use Euclid's Algorithm to verify that e and phi(n) are comprime
    g = gcd(e, PHI)
    while g != 1:
        e = random.randrange(1, PHI)
        g = gcd(e, PHI)

    d = libnum.invmod(e, PHI)
    #  public, private, n
    return n, e, d


# string -> byte[] -> int
#

def rsa_encrypt(message, public_key):
    n, e = public_key
    return pow(message, e, n)


def rsa_decrypt(ciphertext, private_key):
    n, d = private_key
    return pow(ciphertext, d, n)



def test_for_RSA():
    public, private, n = get_keys()
    print(int.from_bytes(b"Hi there", 'big'))
    enc = rsa_encrypt(b"Hi there", public, n)
    print(enc)
    print(int.to_bytes(rsa_decrypt(enc, private, n), length=8, byteorder='big').decode())


# test_for_RSA()
