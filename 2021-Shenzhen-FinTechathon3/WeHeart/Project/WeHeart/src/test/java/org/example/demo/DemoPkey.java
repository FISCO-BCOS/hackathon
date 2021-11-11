package org.example.demo;

import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.protocol.response.BlockNumber;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.crypto.keypair.ECDSAKeyPair;
import org.fisco.bcos.sdk.crypto.keypair.SM2KeyPair;
import org.junit.Test;

public class DemoPkey {

    @Test
    public void keyGeneration() throws Exception {
        //ECDSA key generation
        CryptoKeyPair ecdsaKeyPair = new ECDSAKeyPair().generateKeyPair();
        System.out.println("ecdsa private key :"+ecdsaKeyPair.getHexPrivateKey());
        System.out.println("ecdsa public key :"+ecdsaKeyPair.getHexPublicKey());
        System.out.println("ecdsa address :"+ecdsaKeyPair.getAddress());
        //SM2 key generation
        CryptoKeyPair sm2KeyPair = new SM2KeyPair().generateKeyPair();
        System.out.println("sm2 private key :"+sm2KeyPair.getHexPrivateKey());
        System.out.println("sm2 public key :"+sm2KeyPair.getHexPublicKey());
        System.out.println("sm2 address :"+sm2KeyPair.getAddress());
    }
}
