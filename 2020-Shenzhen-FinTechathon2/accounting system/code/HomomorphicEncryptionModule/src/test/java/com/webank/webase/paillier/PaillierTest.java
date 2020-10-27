package com.webank.webase.paillier;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.junit.Assert;
import org.junit.Test;

public class PaillierTest {

    @Test
    public void goodKeyPairTest() {
        KeyPair keypair = PaillierKeyPair.generateGoodKeyPair();
        RSAPublicKey pubKey = (RSAPublicKey) keypair.getPublic();
        RSAPrivateCrtKey priKey = (RSAPrivateCrtKey) keypair.getPrivate();
        // System.out.println("e:" + priKey.getPublicExponent().intValue());

        String publicKeyStr = PaillierKeyPair.publicKeyToPem(pubKey);
        String privateKeyStr = PaillierKeyPair.privateKeyToPem(priKey);

        RSAPublicKey pubKey1 = (RSAPublicKey) PaillierKeyPair.pemToPublicKey(publicKeyStr);
        RSAPrivateKey priKey1 = (RSAPrivateKey) PaillierKeyPair.pemToPrivateKey(privateKeyStr);
        Assert.assertEquals(pubKey, pubKey1);
        Assert.assertEquals(priKey, priKey1);
    }

    @Test
    public void strongKeyPairTest() {
        KeyPair keypair = PaillierKeyPair.generateStrongKeyPair();
        RSAPublicKey pubKey = (RSAPublicKey) keypair.getPublic();
        RSAPrivateCrtKey priKey = (RSAPrivateCrtKey) keypair.getPrivate();

        String publicKeyStr = PaillierKeyPair.publicKeyToPem(pubKey);
        String privateKeyStr = PaillierKeyPair.privateKeyToPem(priKey);

        RSAPublicKey pubKey1 = (RSAPublicKey) PaillierKeyPair.pemToPublicKey(publicKeyStr);
        RSAPrivateKey priKey1 = (RSAPrivateKey) PaillierKeyPair.pemToPrivateKey(privateKeyStr);
        Assert.assertEquals(pubKey, pubKey1);
        Assert.assertEquals(priKey, priKey1);
    }

    @Test
    public void encryptTest() {
        KeyPair keypair = PaillierKeyPair.generateGoodKeyPair();
        RSAPublicKey pubKey = (RSAPublicKey) keypair.getPublic();

        BigInteger i1 = BigInteger.valueOf(1000000);
        String c1 = PaillierCipher.encrypt(i1, pubKey);
        System.out.println("c1.length:" + c1.length());
        System.out.println("c1:" + c1);

        BigInteger i2 = BigInteger.valueOf(-20000);
        String c2 = PaillierCipher.encrypt(i2, pubKey);
        System.out.println("c2.length:" + c2.length());
        System.out.println("c2:" + c2);
    }

    @Test
    public void decryptTest() {
        String privateKeyStr =
                "-----BEGIN PRIVATE KEY-----\n"
                        + "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC1f/Oa//I+SFNN\n"
                        + "v+PmHGJ1vLGFDu6A0IQV9DcX/hf2R2JFW/ONxfjlrtAh4dW7itMwIA6u64UlKiml\n"
                        + "4UuVBT9ca4wv7R87DFqhxquh+ObMzo2x6QAaXnvL5WMmu8+/aLg6Nc06BXtJHsiw\n"
                        + "TO7B09AcDNccdDyI+gXP4LEsIoh9HR1p41cq1RKe5c9khLryc5zQqf6NWlw4aWr9\n"
                        + "AI5w4m7zhsTiDH1R9qLSJugOltY+9hLF7E9LexMLwXoQUYyi0WGxRe0h03+EQszv\n"
                        + "4gQA4paJPAnk8k6VrUyTlBdeoYneInxZ0FEtFffk/rqVt4B80jjVRTbleSmDNvb7\n"
                        + "KFp+KHSrAgMBAAECggEAG0jM2jQ3ul0tCLccD2+c7Y4cMaB5AixWbuZzkcvE1mUM\n"
                        + "xNh52Io2THDnIPDOLI9GCCoJiwokzd10vVcNAa30RHR2co327+1/gmpXStYb/BXg\n"
                        + "/ynDtjMV8STeruf05xVa/IUyANLqIafbC4XFLqYk1tKnU8O1hfHwBbwFZlkao5QH\n"
                        + "IN9CyDIsyyv30JbHz47HJePZkTm/2iOq4ru+pKYFHeDz4EIEJRGl8HD7wtpo3F5E\n"
                        + "mxe9uI5GMCdbswGnmw1a0u/VrSXpxwVyQMGHP6KWZ0dIunFW0Sh4DEdf9/tygtwx\n"
                        + "nJ+/RYGqTKLv59+OkJGH/e6J6BV9rKYfjTiRC+hAAQKBgQD/1dW9WSXnquVGdJkq\n"
                        + "QnohnMp/EkaXA3e3RiS9Bx9hhNwfdijqx3zyV1LnHGhuEaCXSCoy6zIOLQxOkkA8\n"
                        + "rLCzpN/hp3c8pQUwYlQodZ+G/0272jCsD2vmdOuRP2WuFA9hqGgFvUbXkXh4mcEB\n"
                        + "xKJY/jAddZvmnvs/dcLt6oJMqwKBgQC1nd18hcjQmIRARRUGs1ZU75yTaLKp+lXf\n"
                        + "M7lg8RKE9sWf4ZoT3Snj+pimUOqliE10LiHruawa137q/UC+iI0/I4H9AUhv2mAw\n"
                        + "m5drd+G4s6uTiCf7OQxBTmGHEvv5xH7gQih6sjOJI+N57xanC1XMGxijMOSy/D+O\n"
                        + "sLxB8yJ4AQKBgQDYYX3kJnB+3vYIfznEmnE92KUUkNqPg2lP483S6yFJk9ux6Hh3\n"
                        + "Cr7NIbqGqmpRHiubiHfYlUDC6KsOEXivWMgjSQHqk3+wFUqsP546kjGZNnoCtmqQ\n"
                        + "PILgameLc/mGIIVZ7dv9brdqQCmKp1CtNCiz6Fm9sOlpR3HtnKaAH+aQ/QKBgFVW\n"
                        + "37tidfEmqYY1r+KdJGT2zqEpokJi4jTmbiZSQPx/pG8zKB5LXyLEHzSPcyLjQFnm\n"
                        + "T4Qfk/Js7jNnWyPssEpJ2gvTrYD5oRdWFTmndEZBDs9dPEQ9Ezggp40763D61w9z\n"
                        + "pue4kqTPW1Vxdjh6CA/Hb7VHBT/hbdAT1fI7WCgBAoGAZq1rFESL3roi8DtOWl51\n"
                        + "nduNO20Yloe6tlhUAKo63krRHKBeKTyLXycpZHcq6UEfys3dixFfu0lN6002lUku\n"
                        + "MTbmNOJWhOCa2xuZY0CeINKFnKBnbiauBpo6x+2J0PoWFn8wd1tzFJPbodk8Km1f\n"
                        + "qySov+6mrQxHojQYBu9/yYQ=\n"
                        + "-----END PRIVATE KEY-----";

        String cipher =
                "0100B57FF39AFFF23E48534DBFE3E61C6275BCB1850EEE80D08415F43717FE17F64762455BF38DC5F8E5AED021E1D5BB8AD330200EAEEB85252A29A5E14B95053F5C6B8C2FED1F3B0C5AA1C6ABA1F8E6CCCE8DB1E9001A5E7BCBE56326BBCFBF68B83A35CD3A057B491EC8B04CEEC1D3D01C0CD71C743C88FA05CFE0B12C22887D1D1D69E3572AD5129EE5CF6484BAF2739CD0A9FE8D5A5C38696AFD008E70E26EF386C4E20C7D51F6A2D226E80E96D63EF612C5EC4F4B7B130BC17A10518CA2D161B145ED21D37F8442CCEFE20400E296893C09E4F24E95AD4C9394175EA189DE227C59D0512D15F7E4FEBA95B7807CD238D54536E579298336F6FB285A7E2874AB6914BF4FF089BFD98EC18D9E8B3D7FB2F5CFC20715C62D34F08E36D84F2CDABA2D1A1798C95161B7831167ED27E8894F1EB25D4E74DF382BF276D9ACBEADB56795F3DF8A4E6CE9DF7B6CFEFD5C66F0BC45D24CCC8E8095A7BF5CE69FEC5579B874A4C9B7C8F13126EC59D7C6DF0404816F638C7D4A84FE038E6F00B5667AC88E4307990E4C06B3864D86B7349275B20A3FB50FBA64706F214CC642219DCEF4453C30B89790F6FB1566A5D557AD7EC5890CA50E80111319F9742943FBD675D18753E5ABD21941832A11332ED902C334309E3770512AC042E1556C3F0ECCFC056C66D7362BA4E7896EA0807412817C68D7B5434AFA0D95A12B950573994F081F996545B871E485C392288E2D61C3B0CBB9FC4E68C1C558A598B03BACFF27967BE8AEA8F1322EC3E0957A3ED84810164A59BDEE2D1514EA68228CB96B59D8BA1E9234A24D57E5F8D7E55724EF0AE9D83F6E2A84B9A1E47B59091201B1B65542BBBB5A988CBBD5395335C4DF821ACEF289D20444B74CABC406A7C4F810EFF85838994DBDD38EDF74D4821153A5128AB98C15409C73891415B194803B3ABF761CEE57D1F58813A7125260E58864970CA2650E0D46C239ED92FCC3491C5FA372838B475D14E4946FCC3C421A76C434C5310D1A17A744551CFB5F99547BB216AD7C1ADA5C27CA64B34C29152D29B0A4B90B0C72A7A18BD19CF278B6F39186A39F91FB4D";
        RSAPrivateKey priKey1 = (RSAPrivateKey) PaillierKeyPair.pemToPrivateKey(privateKeyStr);

        BigInteger plain = PaillierCipher.decrypt(cipher, priKey1);
        Assert.assertEquals(BigInteger.valueOf(1000000), plain);
    }

    @Test
    public void homAddTest() {
        KeyPair keypair = PaillierKeyPair.generateGoodKeyPair();
        RSAPublicKey pubKey = (RSAPublicKey) keypair.getPublic();
        RSAPrivateCrtKey priKey = (RSAPrivateCrtKey) keypair.getPrivate();
        // System.out.println("e:" + priKey.getPublicExponent().intValue());

        BigInteger i1 = BigInteger.valueOf(100000);
        String c1 = PaillierCipher.encrypt(i1, pubKey);

        BigInteger i2 = BigInteger.valueOf(-20000);
        String c2 = PaillierCipher.encrypt(i2, pubKey);

        String c3 = PaillierCipher.ciphertextAdd(c1, c2);

        BigInteger o3 = PaillierCipher.decrypt(c3, priKey);
        Assert.assertEquals(BigInteger.valueOf(80000), o3);
    }

    @Test
    public void printTest() {
        KeyPair keypair = PaillierKeyPair.generateGoodKeyPair();
        RSAPublicKey pubKey = (RSAPublicKey) keypair.getPublic();
        RSAPrivateCrtKey priKey = (RSAPrivateCrtKey) keypair.getPrivate();

        String publicKeyStr = PaillierKeyPair.publicKeyToPem(pubKey);
        String privateKeyStr = PaillierKeyPair.privateKeyToPem(priKey);

        System.out.println("public key:" + publicKeyStr);
        System.out.println("private key:" + privateKeyStr);

        RSAPublicKey pubKey1 = (RSAPublicKey) PaillierKeyPair.pemToPublicKey(publicKeyStr);
        RSAPrivateKey priKey1 = (RSAPrivateKey) PaillierKeyPair.pemToPrivateKey(privateKeyStr);

        BigInteger i1 = BigInteger.valueOf(1000000);
        String c1 = PaillierCipher.encrypt(i1, pubKey1);

        System.out.println("c1.length:" + c1.length());
        System.out.println("c1:" + c1);

        BigInteger o1 = PaillierCipher.decrypt(c1, priKey1);
        System.out.println("o1:" + o1);

        BigInteger i2 = BigInteger.valueOf(-20000);
        String c2 = PaillierCipher.encrypt(i2, pubKey1);
        System.out.println("c2.length:" + c2.length());
        System.out.println("c2:" + c2);

        BigInteger o2 = PaillierCipher.decrypt(c2, priKey1);
        System.out.println("o2:" + o2);

        String c3 = PaillierCipher.ciphertextAdd(c1, c2);
        System.out.println("c3.length:" + c3.length());
        System.out.println("c3:" + c3);

        BigInteger o3 = PaillierCipher.decrypt(c3, priKey1);
        System.out.println("o3:" + o3);
    }
}
