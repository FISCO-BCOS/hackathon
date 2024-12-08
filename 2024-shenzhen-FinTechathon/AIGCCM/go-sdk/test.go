package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"net/http"
	"time"
)

func sendPostRequest(url string, data interface{}) (*http.Response, error) {
	jsonData, err := json.Marshal(data)
	if err != nil {
		return nil, fmt.Errorf("error marshalling data: %v", err)
	}

	req, err := http.NewRequest("POST", url, bytes.NewBuffer(jsonData))
	if err != nil {
		return nil, fmt.Errorf("error creating request: %v", err)
	}

	req.Header.Set("Content-Type", "application/json")

	client := &http.Client{}
	resp, err := client.Do(req)
	if err != nil {
		return nil, fmt.Errorf("error sending request: %v", err)
	}

	return resp, nil
}

func insert_to_chains(collection_name string, collection_matrix [][]float64, collection_make string, collection_record string, owner_id string, certificate_organization string, collection_semantic string) map[string]interface{} {
	url := "http://1.95.32.15:10100/collection/insert"
	data := map[string]interface{}{
		"collection_name":         collection_name,
		"collection_matrix":       collection_matrix,
		"collection_make":         collection_make,
		"collection_record":       collection_record,
		"owner_id":                owner_id,
		"certificate_time":        time.Now(),
		"certificate_organization": certificate_organization,
		"collection_semantic":     collection_semantic,
	}

	resp, err := sendPostRequest(url, data)
	if err != nil {
		fmt.Printf("Error: %v\n", err)
		return nil
	}
	defer resp.Body.Close()

	fmt.Printf("Response status: %s\n", resp.Status)
	var result map[string]interface{}
	if err := json.NewDecoder(resp.Body).Decode(&result); err != nil {
		fmt.Printf("Error decoding response: %v\n", err)
		return nil
	}
	return result
}

func select_from_chains(collection_id string) map[string]interface{} {
	url := "http://1.95.32.15:10100/collection/select"
	data := map[string]interface{}{
		"collection_id": collection_id,
	}
	resp, err := sendPostRequest(url, data)
	if err != nil {
		fmt.Printf("Error: %v\n", err)
		return nil
	}
	defer resp.Body.Close()

	fmt.Printf("Response status: %s\n", resp.Status)
	var result map[string]interface{}
	if err := json.NewDecoder(resp.Body).Decode(&result); err != nil {
		fmt.Printf("Error decoding response: %v\n", err)
		return nil
	}
	return result
}

func update_to_chains(collection_id string, collection_name string, collection_make string, collection_record string, owner_id string, certificate_organization string, collection_semantic string) map[string]interface{} {
	url := "http://1.95.32.15:10100/collection/update"
	data := map[string]interface{}{
		"collection_id":           collection_id,
		"collection_name":         collection_name,
		"collection_make":         collection_make,
		"collection_record":       collection_record,
		"owner_id":                owner_id,
		"certificate_time":        time.Now(),
		"certificate_organization": certificate_organization,
		"collection_semantic":     collection_semantic,
	}
	resp, err := sendPostRequest(url, data)
	if err != nil {
		fmt.Printf("Error: %v\n", err)
		return nil
	}
	defer resp.Body.Close()

	fmt.Printf("Response status: %s\n", resp.Status)
	var result map[string]interface{}
	if err := json.NewDecoder(resp.Body).Decode(&result); err != nil {
		fmt.Printf("Error decoding response: %v\n", err)
		return nil
	}
	return result
}

func delete_from_chains(collection_id string) map[string]interface{} {
	url := "http://1.95.32.15:10100/collection/delete"
	data := map[string]interface{}{
		"collection_id": collection_id,
	}
	resp, err := sendPostRequest(url, data)
	if err != nil {
		fmt.Printf("Error: %v\n", err)
		return nil
	}
	defer resp.Body.Close()

	fmt.Printf("Response status: %s\n", resp.Status)
	var result map[string]interface{}
	if err := json.NewDecoder(resp.Body).Decode(&result); err != nil {
		fmt.Printf("Error decoding response: %v\n", err)
		return nil
	}
	return result
}

func transfer_to_chains(collection_id string, owner_id1 string, owner_id2 string,goods int) map[string]interface{} {
	url := "http://1.95.32.15:10100/collection/transfer"
	data := map[string]interface{}{
		"user_id1": owner_id1,
    "user_id2": owner_id2,
    "collection_id": collection_id,
    "transfer_money": goods,
	}
	resp, err := sendPostRequest(url, data)
	if err != nil {
		fmt.Printf("Error: %v\n", err)
		return nil
	}
	defer resp.Body.Close()

	fmt.Printf("Response status: %s\n", resp.Status)
	var result map[string]interface{}
	if err := json.NewDecoder(resp.Body).Decode(&result); err != nil {
		fmt.Printf("Error decoding response: %v\n", err)
		return nil
	}
	return result
}

func add_user_to_chains(user_id string,user_money int,user_name string,user_icon string) map[string]interface{} {
	url := "http://1.95.32.15:10100/account/insert"
	data := map[string]interface{}{
		"user_id":     user_id,
		"user_money":  user_money,
		"user_name":   user_name,
		"user_icon":   user_icon,
	}
	resp, err := sendPostRequest(url, data)
	if err != nil {
		fmt.Printf("Error: %v\n", err)
		return nil
	}
	defer resp.Body.Close()

	fmt.Printf("Response status: %s\n", resp.Status)
	var result map[string]interface{}
	if err := json.NewDecoder(resp.Body).Decode(&result); err != nil {
		fmt.Printf("Error decoding response: %v\n", err)
		return nil
	}
	return result
}

func select_user_from_chains(user_id string) map[string]interface{} {
	url := "http://1.95.32.15:10100/account/select"
	data := map[string]interface{}{
		"user_id":     user_id,
	}

	resp, err := sendPostRequest(url, data)
	if err != nil {
		fmt.Printf("Error: %v\n", err)
		return nil
	}

	defer resp.Body.Close()

	fmt.Printf("Response status: %s\n", resp.Status)
	var result map[string]interface{}
	if err := json.NewDecoder(resp.Body).Decode(&result); err != nil {
		fmt.Printf("Error decoding response: %v\n", err)
		return nil
	}
	return result
}
func delete_user_from_chains(user_id string) map[string]interface{} {
	url := "http://1.95.32.15:10100/account/delete"
	data := map[string]interface{}{
		"user_id":     user_id,
	}

	resp, err := sendPostRequest(url, data)
	if err != nil {
		fmt.Printf("Error: %v\n", err)
		return nil
	}

	defer resp.Body.Close()

	fmt.Printf("Response status: %s\n", resp.Status)
	var result map[string]interface{}
	if err := json.NewDecoder(resp.Body).Decode(&result); err != nil {
		fmt.Printf("Error decoding response: %v\n", err)
		return nil
	}
	return result
}
var result map[string]interface{}
func main(){
	// matrix:=[][]float64{{1,2,3},{4,5,6},{7,8,9}}
	// matrix2:=[][]float64{{1,2,4},{3,5,6},{9,7,8}}

	result=add_user_to_chains("15131408881",1000,"luanboyue","icon1")
	fmt.Println(result)
	result=add_user_to_chains("13161612056",1000,"weirenshuo","icon2")
	fmt.Println(result)
	result=add_user_to_chains("18254329198",1000,"lvyiran","icon3")
	fmt.Println(result)


	// result=insert_to_chains("photo1",[][]float64{{1,2,3},{4,5,6},{7,8,9}},"123","123","123","123","123")
	// fmt.Println(result)
	// id1:=result["collection_id"].(string)
	// fmt.Println(id1)
	// result=select_from_chains(id1)
	// fmt.Println(result)
	
	// result=delete_user_from_chains("123")
	// fmt.Println(result)
	// result=delete_user_from_chains("124")
	// fmt.Println(result)

	result=select_user_from_chains("15131408881")
	fmt.Println(result)
	result=select_user_from_chains("18254329198")
	fmt.Println(result)
	result=select_user_from_chains("13161612056")
	fmt.Println(result)


	// result=insert_to_chains("photo1",matrix,"123","123","123","123","123")
	// fmt.Println(result)
	// id1:=result["collection_id"].(string)


	// result=insert_to_chains("photo2",matrix2,"124","123","123","123","123")
	// fmt.Println(result)
	// id2:=result["collection_id"].(string)

	// result=select_from_chains(id1)
	// fmt.Println(result)
	// result=select_from_chains(id2)
	// fmt.Println(result)

	// result=update_to_chains(id1,"photo2", "124", "124", "124", "124", "124")
	// fmt.Println(result)

	// result=delete_from_chains(id2)
	// fmt.Println(result)

	// result=transfer_to_chains(id1,"123","124",10)
	// fmt.Println(result)


	// result=select_user_from_chains("123")
	// fmt.Println(result)
	// result=select_user_from_chains("124")
	// fmt.Println(result)
	
	// result=select_from_chains(id1)
	// fmt.Println(result)

	// result=delete_user_from_chains("123")
	// fmt.Println(result)
	// result=delete_user_from_chains("124")
	// fmt.Println(result)

}