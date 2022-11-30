package smcrypto

import (
	"encoding/hex"
	"testing"
)

const sm2pem = `-----BEGIN PRIVATE KEY-----
MIGHAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBG0wawIBAQQg+CrrCyrgMb819otf
BAf5NqHYyib039ivru20fcNnV1ShRANCAASok9LNuBJVrfBosbXfPkG5gvZKZTFT
5s/W/BW5L7yoPAWwkfEgOWiZ14qtTFS/563b9ks8/HaNxFGsr5MBKIZ/
-----END PRIVATE KEY-----
`
const sm2Hex = "00f82aeb0b2ae031bf35f68b5f0407f936a1d8ca26f4dfd8afaeedb47dc3675754"
const sm2pubHex = "a893d2cdb81255adf068b1b5df3e41b982f64a653153e6cfd6fc15b92fbca83c05b091f120396899d78aad4c54bfe7addbf64b3cfc768dc451acaf930128867f"
const sm2Addr = "00d6bbe6a5b391f0eb24885c8ee833f0b756f6db"

// const sigData = "message digest    message digest"
// const signKeyHex = "3945208F7B2144B13F36E38AC6D39F95889393692860B51A42FB81EF4DF7C5B8"

// type sm2Signature struct {
// 	R *big.Int
// 	S *big.Int
// }

func TestHexToSM2(t *testing.T) {
	private, _ := HexToSM2(sm2Hex)
	pubKey := SM2PubBytes(&private.PublicKey)
	b, _ := hex.DecodeString(sm2pubHex)
	for i, v := range b {
		if v != pubKey[i] {
			t.Fatalf("public key mismatch at %d: want: %x have: %x", i, v, pubKey[i])
		}
	}

	addr := PubkeyToAddress(private.PublicKey)
	if sm2Addr != hex.EncodeToString(addr[:]) {
		t.Fatalf("address mismatch, want: %s have: %s", sm2Addr, hex.EncodeToString(addr[:]))
	}
	pem, err := SM2ToPEM(private)
	if err != nil {
		t.Fatalf("pem generate error: %v", err)
	}
	if pem != sm2pem {
		t.Fatalf("pem mismatch, want: %s have: %s", sm2pem, pem)
	}

}

// func TestSM2Sign(t *testing.T) {

// 	sig, err := Sign([]byte(sigData), signKeyHex)
// 	if err != nil {
// 		t.Fatalf("sm2 sign error: %v", err)
// 	}
// 	// fmt.Printf("r=%x \ns=%x \nv=%x\n", sig[:32], sig[32:64], sig[64:])

// 	if ret := verify([]byte(sigData), sig, signKeyHex); ret != nil {
// 		fmt.Printf("sm2 verify failure\n")
// 		t.Fatalf("sm2 verify failed. %s", hex.EncodeToString(sig))
// 	}

// 	// d, _ := new(big.Int).SetString("3945208F7B2144B13F36E38AC6D39F95889393692860B51A42FB81EF4DF7C5B8", 16)
// 	// dx, _ := new(big.Int).SetString("09F9DF311E5421A150DD7D161E4BC5C672179FAD1833FC076BB08FF356F35020", 16)
// 	// dy, _ := new(big.Int).SetString("CCEA490CE26775A52DC6EA718CC1AA600AED05FBF35E084A6632F6072DA9AD13", 16)
// 	// ee, _ := new(big.Int).SetString("F0B43E94BA45ACCAACE692ED534382EB17E6AB5A19CE7B31F4486FDFC0D28640", 16)

// 	// kInt, _ := new(big.Int).SetString("59276E27D506861A16680F3AD9C02DCCEF3CC1FA3CDBE4CE6D54B80DEAC1BC21", 16)

// 	// kx, _ := new(big.Int).SetString("04EBFC718E8D1798620432268E77FEB6415E2EDE0E073C0F4F640ECD2E149A73", 16)
// 	// ky, _ := new(big.Int).SetString("E858F9D81E5430A57B36DAAB8F950A3C64E6EE6A63094D99283AFF767E124DF0", 16)

// 	// rr, _ := new(big.Int).SetString("F5A03B0648D2C4630EEAC513E1BB81A15944DA3827D5B74143AC7EACEEE720B3", 16)
// 	// ss, _ := new(big.Int).SetString("B1B6AA29DF212FD8763182BC0D421CA1BB9038FD1F7F42D4840B69C485BBC1AA", 16)

// }

// func sm3HashGMSSL(b []byte) []byte {
// 	sm3ctx, _ := gmssl.NewDigestContext("SM3")
// 	sm3ctx.Reset()
// 	sm3ctx.Update(b)
// 	hash, _ := sm3ctx.Final()

// 	return hash
// }

// func signGMSSL(hash []byte, hexKey string) (sig []byte, err error) {
// 	if len(hash) != 32 {
// 		return nil, fmt.Errorf("hash is required to be exactly 32 bytes (%d)", len(hash))
// 	}
// 	if len(hexKey) < 64 {
// 		return nil, fmt.Errorf("hex private key is required to be exactly 64 bytes (%d)", len(hexKey))
// 	}
// 	key, err := HexToSM2(hexKey)
// 	if err != nil {
// 		return nil, err
// 	}
// 	pubBytes := SM2PubBytes(&key.PublicKey)

// 	pem, err := SM2ToPEM(key)
// 	if err != nil {
// 		return nil, err
// 	}
// 	sm2sk, _ := gmssl.NewPrivateKeyFromPEM(pem, "")
// 	sm2zid, _ := sm2sk.ComputeSM2IDDigest("1234567812345678")

// 	sm3ctx, _ := gmssl.NewDigestContext("SM3")
// 	sm3ctx.Reset()
// 	sm3ctx.Update(sm2zid)
// 	sm3ctx.Update(hash)
// 	tbs, _ := sm3ctx.Final()

// 	sig, _ = sm2sk.Sign("sm2sign", tbs, nil)
// 	var sigStruct sm2Signature
// 	if _, err := asn1.Unmarshal(sig, &sigStruct); err != nil {
// 		fmt.Println(err)
// 		return nil, errors.New("failed to parse signature")
// 	}
// 	fmt.Printf("sig=%x \n", sig)

// 	sig = sigStruct.R.Bytes()
// 	sig = append(sig, sigStruct.S.Bytes()...)
// 	sig = append(sig, pubBytes...)

// 	// fmt.Printf("v=%x \nr=%x \ns=%x\n", pubBytes, sigStruct.R.Bytes(), sigStruct.S.Bytes())

// 	return sig, nil
// }

// func verify(src, sig []byte, hexKey string) error {

// 	key, err := HexToSM2(hexKey)
// 	if err != nil {
// 		return err
// 	}
// 	pem, err := SM2ToPEM(key)
// 	if err != nil {
// 		return err
// 	}

// 	sm2sk, _ := gmssl.NewPrivateKeyFromPEM(pem, "")
// 	sm2zid, _ := sm2sk.ComputeSM2IDDigest("1234567812345678")
// 	sm3ctx, _ := gmssl.NewDigestContext("SM3")
// 	sm3ctx.Reset()
// 	sm3ctx.Update(sm2zid)
// 	sm3ctx.Update(src)
// 	tbs, _ := sm3ctx.Final()

// 	sigStruct := sm2Signature{R: new(big.Int).SetBytes(sig[0:32]), S: new(big.Int).SetBytes(sig[32:64])}
// 	sig, err = asn1.Marshal(sigStruct)
// 	// fmt.Printf("sig r=%x, s=%x\n", sigStruct.R.Bytes(), sigStruct.S.Bytes())

// 	sm2pkpem, _ := sm2sk.GetPublicKeyPEM()
// 	sm2pk, _ := gmssl.NewPublicKeyFromPEM(sm2pkpem)
// 	return sm2pk.Verify("sm2sign", tbs, sig, nil)
// }
