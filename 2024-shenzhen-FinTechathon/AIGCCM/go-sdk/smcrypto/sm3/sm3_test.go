package sm3

import (
	"encoding/hex"
	"fmt"
	"testing"
)

func TestHash(t *testing.T) {
	var msg string = "message"
	ret1, _ := hex.DecodeString("1756AC517F85FFDA751DCDEBF3C89575272FC56904F9BAAD983EC44C36FEAC7B")
	var sm3 Context
	sm3.Reset()
	sm3.Append([]byte(msg))
	ret2 := sm3.Final()
	if hex.EncodeToString(ret1) != hex.EncodeToString(ret2) {
		t.Fatalf("sm3 hash mistach. %x != %x", ret1, ret2)
	}
}

func BenchmarkHash(b *testing.B) {
	var msg string = "message"
	var sm3 Context
	for i := 0; i < b.N; i++ {
		sm3.Reset()
		sm3.Append([]byte(msg))
		sm3.Final()
	}
}

func ExampleHash() {
	var msg string = "message"
	var sm3 Context
	sm3.Reset()
	sm3.Append([]byte(msg))
	hash := sm3.Final()
	fmt.Printf("%x", hash)
	// Output: 1756ac517f85ffda751dcdebf3c89575272fc56904f9baad983ec44c36feac7b
}
