import hashlib
import secrets
import random
import RSA
import ec_elgamal as ec_elgamal
import salsa20 as salsa20


def salsa20_core(state):
    for _ in range(10):
        # Column round
        state[4] ^= ((state[0] + state[12]) << 7 | (state[0] + state[12]) >> 25) & 0xffffffff
        state[8] ^= ((state[4] + state[0]) << 9 | (state[4] + state[0]) >> 23) & 0xffffffff
        state[12] ^= ((state[8] + state[4]) << 13 | (state[8] + state[4]) >> 19) & 0xffffffff
        state[0] ^= ((state[12] + state[8]) << 18 | (state[12] + state[8]) >> 14) & 0xffffffff

        # Diagonal round
        state[9] ^= ((state[5] + state[1]) << 7 | (state[5] + state[1]) >> 25) & 0xffffffff
        state[13] ^= ((state[9] + state[5]) << 9 | (state[9] + state[5]) >> 23) & 0xffffffff
        state[1] ^= ((state[13] + state[9]) << 13 | (state[13] + state[9]) >> 19) & 0xffffffff
        state[5] ^= ((state[1] + state[13]) << 18 | (state[1] + state[13]) >> 14) & 0xffffffff

        state[14] ^= (state[10] + state[6]) & 0xffffffff
        state[2] ^= (state[14] + state[10]) & 0xffffffff
        state[6] ^= (state[2] + state[14]) & 0xffffffff
        state[10] ^= (state[6] + state[2]) & 0xffffffff

        state[3] ^= ((state[15] + state[11]) << 7 | (state[15] + state[11]) >> 25) & 0xffffffff
        state[7] ^= ((state[3] + state[15]) << 9 | (state[3] + state[15]) >> 23) & 0xffffffff
        state[11] ^= ((state[7] + state[3]) << 13 | (state[7] + state[3]) >> 19) & 0xffffffff
        state[15] ^= ((state[11] + state[7]) << 18 | (state[11] + state[7]) >> 14) & 0xffffffff


def salsa20_encrypt_decrypt(key, nonce, data):
    def generate_key_stream():
        state = [0] * 16
        state[0:4] = [0x61707865, 0x3320646e, 0x79622d32, 0x6b206574]
        state[4:12] = [int(key[i:i+8], 16) for i in range(0, len(key), 8)]
        state[12:14] = [int(nonce[i:i+8], 16) for i in range(0, len(nonce), 8)]
        state[14:16] = [0, 0]

        keystream = bytearray()

        for _ in range(len(data) // 64):
            salsa20_core(state)
            block = bytearray(64)
            for i in range(16):
                block[i * 4:(i + 1) * 4] = state[i].to_bytes(4, 'little')
            keystream.extend(block)

            state[8] = (state[8] + 1) & 0xffffffff
            if state[8] == 0:
                state[9] = (state[9] + 1) & 0xffffffff

        remaining = len(data) % 64
        if remaining > 0:
            salsa20_core(state)
            block = bytearray(remaining)
            for i in range(remaining // 4):
                block[i * 4:(i + 1) * 4] = state[i].to_bytes(4, 'little')
            keystream.extend(block)

        return keystream

    key_stream = generate_key_stream()
    encrypted_data = bytearray()
    for i, byte in enumerate(data):
        encrypted_data.append(byte ^ key_stream[i])

    return encrypted_data


def main():
    key = secrets.token_hex(128)  # salsa private key
    nonce = secrets.token_hex(64)

    with open('email.txt', 'r') as file:
        message = file.read()

    print("Email Security Service:")
    print("Bob wants to send a message to Alice\n")
    print("Message:\n", message)
    print("\n")
    encrypted_message = salsa20_encrypt_decrypt(key, nonce, message.encode('utf-8'))
    encrypted_message_hex = encrypted_message.hex()
    print("The ciphertext is:")
    chunk_length = 80  # Adjust the chunk length as needed
    chunks = [encrypted_message_hex[i:i + chunk_length] for i in range(0, len(encrypted_message_hex), chunk_length)]
    for chunk in chunks:
        print(chunk)
    print("\n")

    # RSA
    n, e, d = RSA.get_keys()
    print("Bob request from Alice the RSA variables\n")
    print("public RSA variables that Alice is sending to Bob: ")
    print("n = ", n)
    print("e = ", e)
    print("d = ", d)
    public_key, private_key = (n, e), (n, d)
    encrypted_key = RSA.rsa_encrypt(int.from_bytes(bytes.fromhex(key), 'big'), public_key)
    print("\n")
    print("Encrypted Key: ", encrypted_key)

    # ElGamalEcc
    elgamal = ec_elgamal.ElGamalEcc()  # Create an instance of the ElGamalEcc class
    ec_private_key, ec_public_key = elgamal.generate_ec_elgamal_key_pair()
    signature = elgamal.ec_elgamal_sign(ec_private_key, message)  # Call the method on the instance
    print("Bob sign and The Digital Signature is:", signature)
    print("\n")
    print("----------------------------------------------------\n Sending Message To Alice....\n----------------------------------------------------\n")
    print("Alice verify the digital signature to check if Bob send it")
    signature_valid = elgamal.ec_elgamal_verify(ec_public_key, message, signature)
    print("Signature Valid: ", signature_valid)
    decrypted_message = salsa20_encrypt_decrypt(key, nonce, encrypted_message)
    print("\n=> The decrypted message is:\n", decrypted_message.decode('utf-8'))
if __name__ == '__main__':
    main()
