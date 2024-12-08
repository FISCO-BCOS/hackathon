package conf

import (
	"fmt"
	"testing"
)

const (
	standardKeyHex   = "b89d42f12290070f235fb8fb61dcf96e3b11516c5d4f6333f26e49bb955f8b62"
	standardCurve    = "secp256k1"
	standardSMKeyHex = "389bb3e29db735b5dc4f114923f1ac5136891efda282a18dc0768e34305c861b"
	standardSMCurve  = "sm2p256v1"
)

func TestParsepem(t *testing.T) {
	// test nosm private
	keyBytes, curve, err := LoadECPrivateKeyFromPEM("../.ci/0x83309d045a19c44dc3722d15a6abd472f95866ac.pem")
	if err != nil {
		t.Fatalf("parse nosm private key failed, err: %v", err)
	}
	if fmt.Sprintf("%064x", keyBytes) != standardKeyHex {
		t.Fatalf("nosm keyHex is inconsistent with the anticipation, keyHex: %s", fmt.Sprintf("%064x", keyBytes))
	}
	if curve != standardCurve {
		t.Fatalf("nosm curve is inconsistent with the anticipation, curve: %s", curve)
	}
	t.Logf("the output of parsing nosm private key, keyHex: %s\n curve: %s\n fileContent:\n %s", fmt.Sprintf("%064x", keyBytes), curve, fileContent)

	// test sm private
	keyBytes, curve, err = LoadECPrivateKeyFromPEM("../.ci/sm2p256v1_0x791a0073e6dfd9dc5e5061aebc43ab4f7aa4ae8b.pem")
	if err != nil {
		t.Fatalf("parse sm private key failed, err: %v", err)
	}
	if fmt.Sprintf("%064x", keyBytes) != standardSMKeyHex {
		t.Fatalf("sm keyHex is inconsistent with the anticipation, keyHex: %s", fmt.Sprintf("%064x", keyBytes))
	}
	if curve != standardSMCurve {
		t.Fatalf("sm curve is inconsistent with the anticipation, curve: %s", curve)
	}
	t.Logf("the output of parsing sm private key, keyHex: %s\n curve: %s\n fileContent:\n %s", fmt.Sprintf("%064x", keyBytes), curve, fileContent)

}
