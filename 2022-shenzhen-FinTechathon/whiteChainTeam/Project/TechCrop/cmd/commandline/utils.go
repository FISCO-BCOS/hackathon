package commandline

import (
	"encoding/json"
	"fmt"
)

const (
	DefaultSuccessMessage string = "success"
)

func ListToJSONStr(list interface{}, name string) (string, error) {
	var mapObject = make(map[string]interface{})
	mapObject[name] = list
	jsonBytes, err := json.Marshal(mapObject)
	if err != nil {
		return "", fmt.Errorf("ListToJsonStr failed, json.Marshal err: %v", err)
	}
	return string(jsonBytes), nil
}

// support 0x prefix and no-0x prefix
func IsValidAccount(address string) bool {
	if len(address) != 42 && len(address) != 40 {
		return false
	}
	if len(address) == 40 {
		address = "0x" + address
	}
	ok, _ := isValidHex(address)
	return ok
}
