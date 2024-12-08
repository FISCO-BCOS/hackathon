package sm3

import (
	"bytes"
	"encoding/binary"
	"math/big"

	"github.com/sirupsen/logrus"
)

// var iv = []uint32{0x7380166f, 0x4914b2b9, 0x172442d7,
// 	0xda8a0600, 0xa96f30bc, 0x163138aa, 0xe38dee4d, 0xb0fb0e4e}

const paddingHeader = 128

// Context is the SM3 context
type Context struct {
	data []byte
	a    uint32
	b    uint32
	c    uint32
	d    uint32
	e    uint32
	f    uint32
	g    uint32
	h    uint32
}

// NewContext create a Context
func NewContext() *Context {
	sm3 := new(Context)
	sm3.Reset()
	return sm3
}

// Reset clear data and reset state
func (sm3 *Context) Reset() {
	sm3.data = []byte{}
}

// Append add new data for sm3 hash
func (sm3 *Context) Append(data []byte) {
	sm3.data = append(sm3.data, data...)
}

// Final calculate sm3 hash algorithm
func (sm3 *Context) Final() []byte {
	data := pad(sm3.data)
	buf := bytes.NewBuffer(data)
	b := make([]byte, 64)
	v := getV0()

	for {
		_, err := buf.Read(b)
		if err != nil {
			break
		}
		v = sm3.cf(v, b)
	}
	return v
}

func (sm3 *Context) cf(v, b []byte) []byte {
	// update a-h
	buf := bytes.NewBuffer(v)
	err := binary.Read(buf, binary.BigEndian, &sm3.a)
	if err != nil {
		logrus.Fatal(err)
	}
	err = binary.Read(buf, binary.BigEndian, &sm3.b)
	if err != nil {
		logrus.Fatal(err)
	}
	err = binary.Read(buf, binary.BigEndian, &sm3.c)
	if err != nil {
		logrus.Fatal(err)
	}
	err = binary.Read(buf, binary.BigEndian, &sm3.d)
	if err != nil {
		logrus.Fatal(err)
	}
	err = binary.Read(buf, binary.BigEndian, &sm3.e)
	if err != nil {
		logrus.Fatal(err)
	}
	err = binary.Read(buf, binary.BigEndian, &sm3.f)
	if err != nil {
		logrus.Fatal(err)
	}
	err = binary.Read(buf, binary.BigEndian, &sm3.g)
	if err != nil {
		logrus.Fatal(err)
	}

	binary.Read(buf, binary.BigEndian, &sm3.h)
	A, B, C, D, E, F, G, H := sm3.a, sm3.b, sm3.c, sm3.d, sm3.e, sm3.f, sm3.g, sm3.h
	w68, w64 := splitB(b)
	// for i:0->63
	for i := 0; i < 16; i++ {
		ss1 := cycleLeftRotate(cycleLeftRotate(sm3.a, 12)+sm3.e+cycleLeftRotate(constT(i), uint32(i)%32), 7)
		ss2 := ss1 ^ cycleLeftRotate(sm3.a, 12)
		tt1 := ff0(sm3.a, sm3.b, sm3.c) + sm3.d + ss2 + w64[i]
		tt2 := gg0(sm3.e, sm3.f, sm3.g) + sm3.h + ss1 + w68[i]
		sm3.d = sm3.c
		sm3.c = cycleLeftRotate(sm3.b, 9)
		sm3.b = sm3.a
		sm3.a = tt1
		sm3.h = sm3.g
		sm3.g = cycleLeftRotate(sm3.f, 19)
		sm3.f = sm3.e
		sm3.e = p0(tt2)
	}

	for i := 16; i < 64; i++ {
		ss1 := cycleLeftRotate(cycleLeftRotate(sm3.a, 12)+sm3.e+cycleLeftRotate(constT(i), uint32(i)%32), 7)
		ss2 := ss1 ^ cycleLeftRotate(sm3.a, 12)
		tt1 := ff1(sm3.a, sm3.b, sm3.c) + sm3.d + ss2 + w64[i]
		tt2 := gg1(sm3.e, sm3.f, sm3.g) + sm3.h + ss1 + w68[i]
		sm3.d = sm3.c
		sm3.c = cycleLeftRotate(sm3.b, 9)
		sm3.b = sm3.a
		sm3.a = tt1
		sm3.h = sm3.g
		sm3.g = cycleLeftRotate(sm3.f, 19)
		sm3.f = sm3.e
		sm3.e = p0(tt2)
	}

	// calculate next v and return
	var ret []byte
	retBuf := bytes.NewBuffer(ret)
	err = binary.Write(retBuf, binary.BigEndian, sm3.a^A)
	if err != nil {
		logrus.Fatal(err)
	}
	err = binary.Write(retBuf, binary.BigEndian, sm3.b^B)
	if err != nil {
		logrus.Fatal(err)
	}
	err = binary.Write(retBuf, binary.BigEndian, sm3.c^C)
	if err != nil {
		logrus.Fatal(err)
	}
	err = binary.Write(retBuf, binary.BigEndian, sm3.d^D)
	if err != nil {
		logrus.Fatal(err)
	}
	err = binary.Write(retBuf, binary.BigEndian, sm3.e^E)
	if err != nil {
		logrus.Fatal(err)
	}
	err = binary.Write(retBuf, binary.BigEndian, sm3.f^F)
	if err != nil {
		logrus.Fatal(err)
	}
	err = binary.Write(retBuf, binary.BigEndian, sm3.g^G)
	if err != nil {
		logrus.Fatal(err)
	}
	err = binary.Write(retBuf, binary.BigEndian, sm3.h^H)
	if err != nil {
		logrus.Fatal(err)
	}

	return retBuf.Bytes()
}
func getV0() []byte {
	v0, _ := new(big.Int).SetString("7380166f4914b2b9172442d7da8a0600a96f30bc163138aae38dee4db0fb0e4e", 16)
	return v0.Bytes()
}

func splitB(b []byte) (w68 [68]uint32, w64 [64]uint32) {
	if len(b) != 64 {
		panic("the length of b must be 64")
	}

	buf := bytes.NewBuffer(b)
	for i := 0; i < 16; i++ {
		err := binary.Read(buf, binary.BigEndian, &w68[i])
		if err != nil {
			logrus.Fatal(err)
		}
	}
	for i := 16; i < 68; i++ {
		w68[i] = p1(w68[i-16]^w68[i-9]^cycleLeftRotate(w68[i-3], 15)) ^ cycleLeftRotate(w68[i-13], 7) ^ w68[i-6]
	}
	for i := 0; i < 64; i++ {
		w64[i] = w68[i] ^ w68[i+4]
	}
	return w68, w64
}

func pad(src []byte) []byte {
	length := len(src) * 8
	padLength := 448 - length%512
	if padLength < 0 {
		padLength += 512
	}
	buf := bytes.NewBuffer(src)
	err := buf.WriteByte(paddingHeader)
	if err != nil {
		logrus.Fatal(err)
	}
	padLength -= 8
	for padLength > 0 {
		err = buf.WriteByte(0)
		if err != nil {
			logrus.Fatal(err)
		}
		padLength -= 8
	}
	binary.Write(buf, binary.BigEndian, uint64(length))
	return buf.Bytes()
}

func constT(j int) uint32 {
	if j <= 15 {
		return 0x79cc4519
	}
	return 0x7a879d8a
}

func ff0(x, y, z uint32) uint32 { return x ^ y ^ z }

func ff1(x, y, z uint32) uint32 { return (x & y) | (x & z) | (y & z) }

func gg0(x, y, z uint32) uint32 { return x ^ y ^ z }

func gg1(x, y, z uint32) uint32 { return (x & y) | (^x & z) }

func cycleLeftRotate(x, i uint32) uint32 { return (x<<(i%32) | x>>(32-i%32)) }

func p0(x uint32) uint32 { return x ^ cycleLeftRotate(x, 9) ^ cycleLeftRotate(x, 17) }

func p1(x uint32) uint32 { return x ^ cycleLeftRotate(x, 15) ^ cycleLeftRotate(x, 23) }

// Hash hash implement
func Hash(b []byte) []byte {
	var sm3 Context
	sm3.Reset()
	sm3.Append(b)
	hash := sm3.Final()
	return hash
}
