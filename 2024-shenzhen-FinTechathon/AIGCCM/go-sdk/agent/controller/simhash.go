package controller

import (
	"fmt"
	"strings"
)

// Constants
const EMPTY_HASH = "0000000000000000000000000000000000000000000000000000000000000000"

var lookup = map[rune]string{
	'0': "0000", '1': "0001", '2': "0010", '3': "0011",
	'4': "0100", '5': "0101", '6': "0110", '7': "0111",
	'8': "1000", '9': "1001", 'a': "1010", 'b': "1011",
	'c': "1100", 'd': "1101", 'e': "1110", 'f': "1111",
	'A': "1010", 'B': "1011", 'C': "1100", 'D': "1101",
	'E': "1110", 'F': "1111",
}

func toBinary(s string) string {
	s = strings.TrimPrefix(s, "0x")
	var result strings.Builder
	for _, char := range s {
		if binary, ok := lookup[char]; ok {
			result.WriteString(binary)
		} else {
			// 如果字符不在lookup表中，可以选择返回错误或处理其他逻辑
			return "Invalid character in hex string"
		}
	}
	return result.String()
}

// Jenkins represents the structure with initial seeds
type Jenkins struct {
	pc uint32
	pb uint32
}

// NewJenkins creates a new Jenkins instance
func NewJenkins() *Jenkins {
	return &Jenkins{
		pc: 0,
		pb: 0,
	}
}

// Hash32 computes and returns 32-bit hash of given message
func (j *Jenkins) Hash32(msg string) string {
	h := lookup3(msg, j.pc, j.pb)
	return fmt.Sprintf("%x", h.c)
}

// Hash64 computes and returns 64-bit hash of given message
func (j *Jenkins) Hash64(msg string) string {
	h := lookup3(msg, j.pc, j.pb)
	// fmt.Println(len(fmt.Sprintf("%x%x", h.b, h.c)))
	return fmt.Sprintf("%x%x", h.b, h.c)
}

// lookup3 algorithm implementation
func lookup3(k string, pc, pb uint32) (h struct{ c, b uint32 }) {
	length := len(k)
	var a, b, c uint32
	var mixed struct{ a, b, c uint32 }

	a = 0xdeadbeef + uint32(length) + pc
	b = a
	c = a + pb

	offset := 0
	for length > 12 {
		a += uint32(k[offset+0])
		a += uint32(k[offset+1]) << 8
		a += uint32(k[offset+2]) << 16
		a += uint32(k[offset+3]) << 24

		b += uint32(k[offset+4])
		b += uint32(k[offset+5]) << 8
		b += uint32(k[offset+6]) << 16
		b += uint32(k[offset+7]) << 24

		c += uint32(k[offset+8])
		c += uint32(k[offset+9]) << 8
		c += uint32(k[offset+10]) << 16
		c += uint32(k[offset+11]) << 24

		mixed = mix(a, b, c)
		a = mixed.a
		b = mixed.b
		c = mixed.c

		length -= 12
		offset += 12
	}

	switch length {
	case 12:
		c += uint32(k[offset+11]) << 24
		fallthrough
	case 11:
		c += uint32(k[offset+10]) << 16
		fallthrough
	case 10:
		c += uint32(k[offset+9]) << 8
		fallthrough
	case 9:
		c += uint32(k[offset+8])
		fallthrough
	case 8:
		b += uint32(k[offset+7]) << 24
		fallthrough
	case 7:
		b += uint32(k[offset+6]) << 16
		fallthrough
	case 6:
		b += uint32(k[offset+5]) << 8
		fallthrough
	case 5:
		b += uint32(k[offset+4])
		fallthrough
	case 4:
		a += uint32(k[offset+3]) << 24
		fallthrough
	case 3:
		a += uint32(k[offset+2]) << 16
		fallthrough
	case 2:
		a += uint32(k[offset+1]) << 8
		fallthrough
	case 1:
		a += uint32(k[offset+0])
		fallthrough
	case 0:
		return struct{ c, b uint32 }{c: c, b: b}
	}

	mixed = finalMix(a, b, c)
	a = mixed.a
	b = mixed.b
	c = mixed.c

	return struct{ c, b uint32 }{c: c, b: b}
}

// Mixes 3 32-bit integers reversibly but fast
func mix(a, b, c uint32) struct{ a, b, c uint32 } {
	a -= c
	a ^= rot(c, 4)
	c += b
	b -= a
	b ^= rot(a, 6)
	a += c
	c -= b
	c ^= rot(b, 8)
	b += a
	a -= c
	a ^= rot(c, 16)
	c += b
	b -= a
	b ^= rot(a, 19)
	a += c
	c -= b
	c ^= rot(b, 4)
	b += a
	return struct{ a, b, c uint32 }{a: a, b: b, c: c}
}

// Final mixing of 3 32-bit values (a,b,c) into c
func finalMix(a, b, c uint32) struct{ a, b, c uint32 } {
	c ^= b
	c -= rot(b, 14)
	a ^= c
	a -= rot(c, 11)
	b ^= a
	b -= rot(a, 25)
	c ^= b
	c -= rot(b, 16)
	a ^= c
	a -= rot(c, 4)
	b ^= a
	b -= rot(a, 14)
	c ^= b
	c -= rot(b, 24)
	return struct{ a, b, c uint32 }{a: a, b: b, c: c}
}

// Rotate x by k distance
func rot(x, k uint32) uint32 {
	return (x << k) | (x >> (32 - k))
}

type Token struct {
	Text   string
	Weight int
}

// Simhash 函数计算一组词元的Simhash值
func Simhash(tokens []Token) string {
	if len(tokens) == 0 {
		return EMPTY_HASH
	}

	jenkins := NewJenkins() // 假设这是创建Jenkins实例的函数

	var memo []int
	for _, token := range tokens {
		hash := toBinary(jenkins.Hash64(token.Text))
		array := make([]int, 64) // 假设hash是64位的二进制字符串
		for i, char := range hash {
			sign := -1
			if char == '1' {
				sign = 1
			}
			array[i] = sign * token.Weight
		}

		// 合并数组
		if memo == nil {
			memo = make([]int, 64)
			copy(memo, array)
		} else {
			for i := range memo {
				memo[i] += array[i]
			}
		}
	}

	// 降维
	var result []byte
	for _, v := range memo {
		if v > 0 {
			result = append(result, '1')
		} else {
			result = append(result, '0')
		}
	}

	return string(result)
}

// Compute similarity between two hashes
func Similarity(a, b string) float64 {
	count := 0
	for i := range a {
		if a[i] == b[i] {
			count++
		}
	}
	return float64(count) / float64(len(a))
}

func GetSimhash(node *Node) string {
	tokens := []Token{
		{Text: node.Style, Weight: 1},
		{Text: node.Make, Weight: 1},
		{Text: node.Record, Weight: 1},
		{Text: matrixToString(node.Matrix), Weight: 5},
	}

	return Simhash(tokens)
}

func matrixToString(matrix [][]float64) string {
	var sb strings.Builder
	for _, row := range matrix {
		for _, num := range row {
			sb.WriteString(fmt.Sprintf("%f,", num))
		}
		sb.WriteString("\n")
	}
	return sb.String()
}
