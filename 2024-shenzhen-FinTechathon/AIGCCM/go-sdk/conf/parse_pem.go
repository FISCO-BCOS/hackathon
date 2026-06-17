package conf

import (
	"crypto/x509/pkix"
	"encoding/asn1"
	"encoding/pem"
	"errors"
	"fmt"
	"io/ioutil"
	"math/big"
)

const (
	secp256k1 = "secp256k1"
	sm2p256v1 = "sm2p256v1"
)

var (
	// oidNamedCurveP224      = asn1.ObjectIdentifier{1, 3, 132, 0, 33}
	// oidNamedCurveP256      = asn1.ObjectIdentifier{1, 2, 840, 10045, 3, 1, 7}
	// oidNamedCurveP384      = asn1.ObjectIdentifier{1, 3, 132, 0, 34}
	// oidNamedCurveP521      = asn1.ObjectIdentifier{1, 3, 132, 0, 35}
	oidNamedCurveSecp256k1 = asn1.ObjectIdentifier{1, 3, 132, 0, 10}
	oidNamedCurveSm2p256v1 = asn1.ObjectIdentifier{1, 2, 156, 10197, 1, 301}
)

// LoadECPrivateKeyFromPEM reads file, divides into key and certificates
func LoadECPrivateKeyFromPEM(path string) ([]byte, string, error) {
	raw, err := ioutil.ReadFile(path)
	if err != nil {
		return nil, "", err
	}

	block, _ := pem.Decode(raw)
	if block == nil {
		return nil, "", fmt.Errorf("Failure reading pem from \"%s\": %s", path, err)
	}
	if block.Type != "PRIVATE KEY" {
		return nil, "", fmt.Errorf("Failure reading private key from \"%s\": %s", path, err)
	}
	ecPirvateKey, curveName, err := parsePKCS8ECPrivateKey(block.Bytes)
	if err != nil {
		return nil, "", fmt.Errorf("Failure reading private key from \"%s\": %s", path, err)
	}
	return ecPirvateKey, curveName, nil
}

// LoadECPublicKeyFromPEM reads file, divides into key and certificates
func LoadECPublicKeyFromPEM(path string) ([]byte, string, error) {
	raw, err := ioutil.ReadFile(path)
	if err != nil {
		return nil, "", err
	}

	block, _ := pem.Decode(raw)
	if block == nil {
		return nil, "", fmt.Errorf("Failure reading pem from \"%s\": %s", path, err)
	}
	if block.Type != "PUBLIC KEY" {
		return nil, "", fmt.Errorf("Failure reading private key from \"%s\": %s", path, err)
	}
	return parsePKIXPublicKey(block.Bytes)
}

// parseECPrivateKey is a copy of x509.ParseECPrivateKey, supported secp256k1 and sm2p256v1
func parsePKCS8ECPrivateKey(der []byte) (keyHex []byte, curveName string, err error) {

	oidPublicKeyECDSA := asn1.ObjectIdentifier{1, 2, 840, 10045, 2, 1}
	// AlgorithmIdentifier represents the ASN.1 structure of the same name. See RFC
	// 5280, section 4.1.1.2.
	type AlgorithmIdentifier struct {
		Algorithm  asn1.ObjectIdentifier
		Parameters asn1.RawValue `asn1:"optional"`
	}
	var pkcs8 struct {
		Version    int
		Algo       AlgorithmIdentifier
		PrivateKey []byte
		// optional attributes omitted.
	}
	var privKey struct {
		Version       int
		PrivateKey    []byte
		NamedCurveOID asn1.ObjectIdentifier `asn1:"optional,explicit,tag:0"`
		PublicKey     asn1.BitString        `asn1:"optional,explicit,tag:1"`
	}
	if _, err := asn1.Unmarshal(der, &pkcs8); err != nil {
		return nil, "", errors.New("x509: failed to parse EC private key embedded in PKCS#8: " + err.Error())
	}
	if !pkcs8.Algo.Algorithm.Equal(oidPublicKeyECDSA) {
		return nil, "", fmt.Errorf("x509: PKCS#8 wrapping contained private key with unknown algorithm: %v", pkcs8.Algo.Algorithm)
	}
	bytes := pkcs8.Algo.Parameters.FullBytes
	namedCurveOID := new(asn1.ObjectIdentifier)
	if _, err := asn1.Unmarshal(bytes, namedCurveOID); err != nil {
		namedCurveOID = nil
		return nil, "", fmt.Errorf("parse namedCurveOID failed")
	}
	if _, err := asn1.Unmarshal(pkcs8.PrivateKey, &privKey); err != nil {
		return nil, "", errors.New("x509: failed to parse EC private key: " + err.Error())
	}
	var curveOrder *big.Int

	switch {
	case namedCurveOID.Equal(oidNamedCurveSecp256k1):
		curveName = "secp256k1"
		curveOrder, _ = new(big.Int).SetString("fffffffffffffffffffffffffffffffebaaedce6af48a03bbfd25e8cd0364141", 16)
	case namedCurveOID.Equal(oidNamedCurveSm2p256v1):
		curveName = "sm2p256v1"
		curveOrder, _ = new(big.Int).SetString("FFFFFFFEFFFFFFFFFFFFFFFFFFFFFFFF7203DF6B21C6052B53BBF40939D54123", 16)
	default:
		return nil, "", fmt.Errorf("unknown namedCurveOID:%+v", namedCurveOID)
	}

	k := new(big.Int).SetBytes(privKey.PrivateKey)
	if k.Cmp(curveOrder) >= 0 {
		return nil, "", errors.New("x509: invalid elliptic curve private key value")
	}
	return privKey.PrivateKey, curveName, nil
}

// parsePKIXPublicKey is a copy of x509.ParsePKIXPublicKey, supported secp256k1 and sm2p256v1
func parsePKIXPublicKey(derBytes []byte) (pub []byte, curveName string, err error) {

	type publicKeyInfo struct {
		Raw       asn1.RawContent
		Algorithm pkix.AlgorithmIdentifier
		PublicKey asn1.BitString
	}
	var pki publicKeyInfo
	if rest, err := asn1.Unmarshal(derBytes, &pki); err != nil {
		return nil, "", err
	} else if len(rest) != 0 {
		return nil, "", errors.New("x509: trailing data after ASN.1 of public-key")
	}

	oidPublicKeyECDSA := asn1.ObjectIdentifier{1, 2, 840, 10045, 2, 1}
	if !oidPublicKeyECDSA.Equal(oidPublicKeyECDSA) {
		return nil, "", errors.New("x509: unsupported public key algorithm")
	}
	asn1Data := pki.PublicKey.RightAlign()
	paramsData := pki.Algorithm.Parameters.FullBytes
	namedCurveOID := new(asn1.ObjectIdentifier)
	rest, err := asn1.Unmarshal(paramsData, namedCurveOID)
	if err != nil {
		return nil, "", errors.New("x509: failed to parse ECDSA parameters as named curve")
	}
	if len(rest) != 0 {
		return nil, "", errors.New("x509: trailing data after ECDSA parameters")
	}

	switch {
	case namedCurveOID.Equal(oidNamedCurveSecp256k1):
		curveName = "secp256k1"
	case namedCurveOID.Equal(oidNamedCurveSm2p256v1):
		curveName = "sm2p256v1"
	default:
		return nil, "", fmt.Errorf("unknown namedCurveOID:%+v", namedCurveOID)
	}

	byteLen := 32
	if len(asn1Data) != 1+2*byteLen {
		return nil, curveName, nil
	}
	if asn1Data[0] != 4 { // uncompressed form
		return nil, curveName, nil
	}
	return asn1Data, curveName, nil
}
