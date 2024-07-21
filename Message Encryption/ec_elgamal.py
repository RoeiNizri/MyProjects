# -*- coding: utf-8 -*-
"""
Created on Fri Dec 10 11:43:28 2021

El-Gamal Digital Signature on Elliptic Curve (EC)

Creates digital signature for a message using El-Gamal
algorithm, calculations based on ECC.

@author: Ayala Cohen
"""

from tinyec.ec import SubGroup, Curve, Point, mod_inv
import hashlib
import binascii
from random import randrange


class ElGamalEcc:
    prKey = 0  # private key
    myPublicK = 0  # public key

    field = SubGroup(p=29, g=(5, 7), n=31, h=1)  # Field parameters

    curve = Curve(a=-1, b=16, field=field, name='p1707')  # Elliptic curve parameters
    G = curve.g  # Generator of curve
    n = 31  # Prime order (P)

    def __init__(self, prKey=None):
        """
        Private Key must be in range [1, n-1]
        """
        if prKey is not None:
            if prKey < 1 or prKey > self.n:
                raise ValueError("Invalid private key for El-Gamal! Key must be in range [1, {}]".format(self.n - 1))
            self.prKey = prKey
            self.myPublicK = prKey * self.G

    @staticmethod
    def generate_ec_elgamal_key_pair():
        """
        Generate a key pair for EC ElGamal.
        Returns a tuple (private_key, public_key).
        """
        private_key = randrange(1, ElGamalEcc.n)
        public_key = private_key * ElGamalEcc.G
        return private_key, public_key

    def ec_elgamal_sign(self, private_key, message):
        """
        Sign a message using EC ElGamal.
        Returns a tuple (R, s).
        """
        if private_key < 1 or private_key > self.n:
            raise ValueError("Invalid private key for EC ElGamal! Key must be in range [1, {}]".format(self.n - 1))

        e = hashlib.sha256(message.encode('utf-8')).hexdigest()  # Compute hash of the message
        e = bin(int(e, 16))  # Convert hash to binary

        z = int(e[2:self.n + 2], 2)  # Extract n leftmost bits from binary

        while True:
            k = randrange(1, self.n)  # Generate a random number k in [1, n-1]

            point = k * self.G  # Compute a point on the curve as (x1, y1) = k * G
            r = int(point.x) % self.n  # Calculate r = x1 mod n

            inv_k = mod_inv(k, self.n)  # Compute inverse of k
            s = (inv_k * (z + r * private_key)) % self.n  # Calculate s = (k^-1 * (z + r * private_key)) mod n

            if r != 0 and s != 0:
                break

        return point, s

    def ec_elgamal_verify(self, public_key, message, signature):
        """
        Verify the digital signature of a message using EC ElGamal.
        Returns True if the signature is valid, False otherwise.
        """
        if public_key == 0:
            raise ValueError("Invalid public key for EC ElGamal!")

        r, s = signature

        if s < 1 or s > self.n or not self.curve.on_curve(r.x, r.y):
            return False

        e = hashlib.sha256(message.encode('utf-8')).hexdigest()  # Compute hash of the message
        e = bin(int(e, 16))  # Convert hash to binary
        z = int(e[2:self.n + 2], 2)  # Extract n leftmost bits from binary

        V1 = s * r
        V2 = z * self.G + r.x * public_key

        return V1 == V2
