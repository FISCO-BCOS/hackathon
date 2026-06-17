package smcrypto

import (
	"bytes"
	"crypto/rand"
	"encoding/binary"
	"math/big"

	"github.com/FISCO-BCOS/crypto/ecdsa"
	"github.com/FISCO-BCOS/crypto/elliptic"
	"github.com/FISCO-BCOS/go-sdk/smcrypto/sm3"
	"github.com/sirupsen/logrus"
)

const defaultSM2ID = "1234567812345678"

// SM2PreProcess compute z value of id and return z||m
func SM2PreProcess(src []byte, id string, priv *ecdsa.PrivateKey) ([]byte, error) {
	length := uint16(len(id) * 8)
	var data []byte
	buf := bytes.NewBuffer(data)
	err := binary.Write(buf, binary.BigEndian, length)
	if err != nil {
		logrus.Fatal(err)
	}
	_, err = buf.Write([]byte(id))
	if err != nil {
		logrus.Fatal(err)
	}
	_, err = buf.Write(elliptic.Sm2p256v1().Params().A.Bytes())
	if err != nil {
		logrus.Fatal(err)
	}
	_, err = buf.Write(elliptic.Sm2p256v1().Params().B.Bytes())
	if err != nil {
		logrus.Fatal(err)
	}
	_, err = buf.Write(elliptic.Sm2p256v1().Params().Gx.Bytes())
	if err != nil {
		logrus.Fatal(err)
	}
	_, err = buf.Write(elliptic.Sm2p256v1().Params().Gy.Bytes())
	if err != nil {
		logrus.Fatal(err)
	}
	_, err = buf.Write(priv.X.Bytes())
	if err != nil {
		logrus.Fatal(err)
	}
	_, err = buf.Write(priv.Y.Bytes())
	if err != nil {
		logrus.Fatal(err)
	}
	z := sm3.Hash(buf.Bytes())
	// fmt.Printf("digest sm3 hash :%x\n", z)
	return append(z, src...), nil
}

// SM2Sign return sm2 signature
func SM2Sign(src []byte, priv *ecdsa.PrivateKey) (r, s *big.Int, err error) {
	data, err := SM2PreProcess(src, defaultSM2ID, priv)
	if err != nil {
		return nil, nil, err
	}
	e := sm3.Hash(data)
	// fmt.Printf("message sm3 hash :%x\n", e)
	eInt := new(big.Int).SetBytes(e)
	n := elliptic.Sm2p256v1().Params().N
	d := priv.D

	for {
		k, x, _, err := elliptic.GenerateKey(elliptic.Sm2p256v1(), rand.Reader)
		if err != nil {
			return nil, nil, err
		}
		kInt := new(big.Int).SetBytes(k)

		if big.NewInt(0).Cmp(kInt) == 0 {
			continue
		}
		r = new(big.Int).Add(eInt, x)
		r.Mod(r, n)
		if new(big.Int).Add(r, kInt).Cmp(n) == 0 {
			continue
		}
		if big.NewInt(0).Cmp(r) == 0 {
			continue
		}

		tmp := new(big.Int).Add(d, big.NewInt(1))
		tmp.Exp(tmp, new(big.Int).Sub(n, big.NewInt(2)), n)
		s = new(big.Int).Mul(r, d)
		s.Sub(kInt, s)
		s.Mul(s, tmp)
		s.Mod(s, n)
		if big.NewInt(0).Cmp(s) == 0 {
			continue
		}
		return r, s, err
	}
}
