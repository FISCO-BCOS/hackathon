package main

import (
	"encoding/json"
	"fmt"
	"image"
	"io"
	"net/http"
	"os"
	"time"

	// "time"

	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
	sd "github.com/seasonjs/stable-diffusion"

	"bytes"
	"image/color"
	"image/draw"
	"image/png"
	"math"
)

type Par struct {
	Prompt                   string  `json:"text"`
	NegativePrompt           string  `json:"negativetext"`
	Width                    int     `json:"width"`
	Height                   int     `json:"height"`
	CfgScale                 float32 `json:"cfgScale"`
	Samplesteps              int     `json:"samplingSteps"`
	Data                     int     `json:"data1"`
	Collection_id            string  `json:"collectionid"`
	Collection_name          string  `json:"collectionname"`
	Collection_make          string  `json:"collectionmake"`
	Certificate_time         string  `json:"certificatetime"`
	Owner_id                 string  `json:"owner_id"`
	Owner_id1                string  `json:"owner_id1"`
	Owner_id2                string  `json:"owner_id2"`
	Goods                    int     `json:"goods"`
	User_id                  string  `json:"user_id"`
	User_money               int     `json:"user_money"`
	User_name                string  `json:"user_name"`
	User_icon                string  `json:"user_icon"`
	Collection_record        string  `json:"collectionrecord"`
	Certificate_organization string  `json:"certificateorganization"`
	Collection_semantic      string  `json:"collectionsemantic"`
}

var collection_id string = "unique_id_123"
var collection_name string = "unique_id_123"
var collection_make string = "Artwork Maker"
var collection_record string = "Artwork Record"
var owner_id string = "owner_456"

// var certificate_time string = "2024-12-01T13:21:49Z"
var certificate_organization string = "Artwork Certification Org"
var collection_semantic string = "12345"

func runsd(c *gin.Context) {
	println("PredictStart")

	// raw, _ := c.GetRawData()
	// var j map[string]int
	// json.Unmarshal(raw, &j)
	// var samplesteps = j["samplesteps"]
	// print(samplesteps)
	// body, _ := c.GetRawData()
	// fmt.Println(string(body)) // 打印请求体内容

	var par Par
	// print(1)
	if err := c.ShouldBindJSON(&par); err != nil {
		// 如果绑定失败，返回错误信息
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		print(err.Error())
		return
	}
	fmt.Println("Params:", par)
	collection_id = par.Collection_id
	collection_name = par.Collection_name
	collection_make = par.Collection_make
	owner_id = par.Owner_id
	println("1:", owner_id)
	// certificate_time = par.Certificate_time

	var DefaultFullParams = sd.FullParams{
		// 负面提示列表 生成时避免
		// NegativePrompt:   "out of frame, lowers, text, error, cropped, worst quality, low quality, jpeg artifacts, ugly, duplicate, morbid, mutilated, out of frame, extra fingers, mutated hands, poorly drawn hands, poorly drawn face, mutation, deformed, blurry, dehydrated, bad anatomy, bad proportions, extra limbs, cloned face, disfigured, gross proportions, malformed limbs, missing arms, missing legs, extra arms, extra legs, fused fingers, too many fingers, long neck, username, watermark, signature",
		NegativePrompt:   par.NegativePrompt,
		CfgScale:         par.CfgScale,    // 遵循提示词的严格程度 https://blog.csdn.net/2401_85688943/article/details/140507683
		Width:            par.Width,       // 宽
		Height:           par.Height,      // 高
		SampleMethod:     0,               // 采样方法 https://zhuanlan.zhihu.com/p/690319301
		SampleSteps:      par.Samplesteps, // 采样步骤数
		Strength:         0.4,             // 添加噪点的强度？以图绘图？ https://www.bilibili.com/read/cv19739185/
		Seed:             42,              // 种子
		BatchCount:       1,               // 生成图像批次数量
		OutputsImageType: "PNG",
	}

	options := sd.DefaultOptions

	model, err := sd.NewAutoModel(options)
	if err != nil {
		print(err.Error())
		return
	}
	defer model.Close()

	// modelPath := "D:\\English_Way\\go\\go-sd\\model\\v1-5-pruned-emaonly.ckpt"
	//modelPath := "D:\\English_Way\\go\\go-sd\\model\\miniSD.ckpt"
	modelPath := "D:\\Environment\\miniSD.ckpt"

	err = model.LoadFromFile(modelPath)
	if err != nil {
		print(err.Error())
		return
	}
	var writers []io.Writer
	filenames := []string{
		"./PredicteImg.png",
	}
	for _, filename := range filenames {
		file, err := os.Create(filename)
		if err != nil {
			print(err.Error())
			return
		}
		defer file.Close()
		writers = append(writers, file)
	}

	err = model.Predict(par.Prompt, DefaultFullParams, writers)
	if err != nil {
		print(err.Error())
	}

	// c.String(http.StatusOK, "Predictsuccess")

	println("PredictEND")
	println("DecodeStart")

	// 读取图片
	file, err := os.Open(filenames[0])
	if err != nil {
		panic(err)
	}
	defer file.Close()

	// 设置响应头
	c.Header("Content-Type", "image/png")
	c.Header("Content-Disposition", "inline; filename="+filenames[0])

	// 将文件内容直接写入响应体
	_, err = io.Copy(c.Writer, file)
	if err != nil {
		c.Status(http.StatusInternalServerError)
		return
	}
}

func sendToBlock(c *gin.Context) {
	// fmt.Println(c.GetRawData())
	// var par Par
	// c.ShouldBindJSON(&par)
	// println("par:", par.Data)

	println("2:", owner_id)
	data_ := map[string]interface{}{
		// "collection_id":            collection_id,
		"collection_name":          collection_name,
		"collection_make":          collection_make,
		"collection_record":        collection_record,
		"owner_id":                 owner_id,
		"certificate_time":         time.Now(),
		"certificate_organization": certificate_organization,
		"collection_semantic":      collection_semantic,
	}
	jsonBytes, err := json.Marshal(data_)
	if err != nil {
		fmt.Println("Error:", err)
	}

	jsonStr := string(jsonBytes)
	// fmt.Println(jsonStr)

	// 添加水印
	inputpath := "./PredicteImg.png"
	outpath := "./" + collection_name + "WatermarkImg.png"
	ImgAddWatermark(inputpath, jsonStr, outpath)

	// 图片转为等大矩阵
	file, err := os.Open("./" + collection_name + "WatermarkImg.png")
	if err != nil {
		panic(err)
	}
	defer file.Close()

	img, _, err := image.Decode(file)
	if err != nil {
		panic(err)
	}

	matrix := imageToMatrix(img)
	// 转为float64
	matrixfloat := rgbaMatrixToFloat64Matrix(matrix)
	data := map[string]interface{}{
		// "collection_id":            collection_id,
		"collection_name":          collection_name,
		"collection_matrix":        matrixfloat,
		"collection_make":          collection_make,
		"collection_record":        collection_record,
		"owner_id":                 owner_id,
		"certificate_time":         time.Now(),
		"certificate_organization": certificate_organization,
		"collection_semantic":      collection_semantic,
	}
	// fmt.Println("添加图片矩阵的map:", data)
	url := "http://1.95.32.15:10100/collection/insert"

	println("3:", data["owner_id"].(string))
	data["owner_id"] = owner_id
	println("5:", data["owner_id"])
	resp, err := sendPostRequest(url, data)
	if err != nil {
		fmt.Printf("Error: %v\n", err)
		return
	}
	defer resp.Body.Close()

	fmt.Printf("Response status: %s\n", resp.Status)
	var result map[string]interface{}
	if err := json.NewDecoder(resp.Body).Decode(&result); err != nil {
		fmt.Printf("Error decoding response: %v\n", err)
		return
	}

	fmt.Printf("Response body: %v\n", result)
	collection_id = result["collection_id"].(string)
	tx := result["tx"].(string)
	// fmt.Println("collection_id:", collection_id)
	// fmt.Println("tx", tx)

	// c.String(http.StatusOK, "sendToBlockSuccess!")
	c.JSON(http.StatusOK, gin.H{
		"collection_id": collection_id,
		"tx":            tx,
	})
}

func selectFromChains(c *gin.Context) {
	// data := c.GetString("data")
	// println(data)

	var par Par
	// print(1)
	if err := c.ShouldBindJSON(&par); err != nil {
		// 如果绑定失败，返回错误信息
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		print(err.Error())
		return
	}
	fmt.Println(par)
	results := select_from_chains(par.Collection_id)
	// fmt.Println(results)
	collection_id := results["collection_id"]
	collection_name := results["collection_name"]
	owner_id := results["owner_id"]
	certificate_time := results["certificate_time"]
	certificate_organization := results["certificate_organization"]
	collection_semantic := results["collection_semantic"]
	println("4:", owner_id)
	// collection_matrix := results["collection_matrix"]
	// collection_matrixfloat := collection_matrix.([][]float64)

	// // 矩阵转图片
	// collection_matrixcolor := float64MatrixToRGBAMatrix(collection_matrixfloat, 512, 512)
	// img := matrixToImage(collection_matrixcolor, 512, 512)
	// // 保存图片到文件
	// filename := "MatrixImg.png"
	// err := saveImage(img, filename)

	// if err != nil {
	// 	panic(err)
	// }
	// tx := results["tx"].(string)

	c.JSON(http.StatusOK, gin.H{
		"collection_id":            collection_id,
		"collection_name":          collection_name,
		"owner_id":                 owner_id,
		"certificate_time":         certificate_time,
		"certificate_organization": certificate_organization,
		"collection_semantic":      collection_semantic,

		// "tx": tx,
	})

}

func selectForWatermark(c *gin.Context) {
	var par Par
	// print(1)
	if err := c.ShouldBindJSON(&par); err != nil {
		// 如果绑定失败，返回错误信息
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		print(err.Error())
		return
	}
	collection_name = par.Collection_name
	watermark := ImgExtractWaterMark("./" + collection_name + "WatermarkImg.png")

	// if watermark != " " {
	// 	fmt.Println(watermark)
	// }

	c.JSON(http.StatusOK, gin.H{
		"watermark": watermark,
	})
}

func updateToChains(c *gin.Context) {
	var par Par
	// print(1)
	if err := c.ShouldBindJSON(&par); err != nil {
		// 如果绑定失败，返回错误信息
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		print(err.Error())
		return
	}
	fmt.Println(par)
	results := update_to_chains(par.Collection_id, par.Collection_name, par.Collection_make, par.Collection_record, par.Owner_id, par.Certificate_organization, par.Collection_semantic)
	fmt.Println(results)
	// collection_id = results["collection_id"].(string)
	tx := results["tx"].(string)
	// record := results["record"].(string)

	c.JSON(http.StatusOK, gin.H{
		// "collection_id": collection_id,
		"tx": tx,
		// "record": record,
	})
}

func deleteFromChains(c *gin.Context) {
	var par Par
	// print(1)
	if err := c.ShouldBindJSON(&par); err != nil {
		// 如果绑定失败，返回错误信息
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		print(err.Error())
		return
	}
	fmt.Println(par)
	results := delete_from_chains(par.Collection_id)
	fmt.Println(results)
	tx := results["tx"]

	c.JSON(http.StatusOK, gin.H{
		"tx": tx,
	})
}

func transferToChains(c *gin.Context) {
	var par Par
	// print(1)
	if err := c.ShouldBindJSON(&par); err != nil {
		// 如果绑定失败，返回错误信息
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		print(err.Error())
		return
	}
	fmt.Println(par.Collection_id)
	fmt.Println(par.Owner_id1)
	fmt.Println(par.Owner_id2)
	fmt.Println(par.Goods)
	results := transfer_to_chains(par.Collection_id, par.Owner_id1, par.Owner_id2, par.Goods)
	fmt.Println(results)
	tx := results["tx"].(string)

	c.JSON(http.StatusOK, gin.H{
		"tx": tx,
	})

}

func addUserToChains(c *gin.Context) {
	var par Par
	// print(1)
	if err := c.ShouldBindJSON(&par); err != nil {
		// 如果绑定失败，返回错误信息
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		print(err.Error())
		return
	}
	fmt.Println(par)
	results := add_user_to_chains(par.User_id, par.User_money, par.User_name, par.User_icon)
	fmt.Println(results)
	// user_id := results["user_id"].(string)
	// user_money := results["user_money"].(string)
	// user_name := results["use_name"].(string)
	// user_icon := results["use_icon"].(string)
	tx := results["tx"].(string)

	c.JSON(http.StatusOK, gin.H{
		"userid":    par.User_id,
		"usermoney": par.User_money,
		"username":  par.User_name,
		"usericon":  par.User_icon,
		"tx":        tx,
	})

}

func selectUserFromChains(c *gin.Context) {
	var par Par
	// print(1)
	if err := c.ShouldBindJSON(&par); err != nil {
		// 如果绑定失败，返回错误信息
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		print(err.Error())
		return
	}
	fmt.Println(par)
	results := select_user_from_chains(par.User_id)
	fmt.Println(results)
	user_id := results["user_ids"]
	user_money := results["user_moneys"]
	user_name := results["user_names"]
	user_icon := results["user_icons"]
	// tx := results["tx"].(string)

	c.JSON(http.StatusOK, gin.H{
		"userid":    user_id,
		"usermoney": user_money,
		"username":  user_name,
		"usericon":  user_icon,
	})

}

func deleteUserFromChains(c *gin.Context) {
	var par Par
	// print(1)
	if err := c.ShouldBindJSON(&par); err != nil {
		// 如果绑定失败，返回错误信息
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		print(err.Error())
		return
	}
	fmt.Println(par)
	results := delete_user_from_chains(par.User_id)
	fmt.Println(results)
	tx := results["tx"].(string)

	c.JSON(http.StatusOK, gin.H{
		"tx": tx,
	})

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
		"collection_id":            collection_id,
		"collection_name":          collection_name,
		"collection_make":          collection_make,
		"collection_record":        collection_record,
		"owner_id":                 owner_id,
		"certificate_time":         time.Now(),
		"certificate_organization": certificate_organization,
		"collection_semantic":      collection_semantic,
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

func transfer_to_chains(collection_id string, owner_id1 string, owner_id2 string, goods int) map[string]interface{} {
	url := "http://1.95.32.15:10100/collection/transfer"
	data := map[string]interface{}{
		"user_id1":       owner_id1,
		"user_id2":       owner_id2,
		"collection_id":  collection_id,
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
	fmt.Println(result)
	return result
}

func add_user_to_chains(user_id string, user_money int, user_name string, user_icon string) map[string]interface{} {
	url := "http://1.95.32.15:10100/account/insert"
	data := map[string]interface{}{
		"user_id":    user_id,
		"user_money": user_money,
		"user_name":  user_name,
		"user_icon":  user_icon,
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
		"user_id": user_id,
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
		"user_id": user_id,
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

func main() {
	// 创建一个默认的路由引擎
	r := gin.Default()

	// 允许所有域名使用
	r.Use(cors.Default())

	// 配置路由
	r.GET("/", func(c *gin.Context) {
		c.JSON(200, gin.H{
			// c.JSON：返回 JSON 格式的数据
			"message": "Hello world!",
		})
	})

	// 运行SD
	r.POST("/runsd", runsd)
	r.POST("/sendtoblock", sendToBlock)
	r.POST("/selectforwatermark", selectForWatermark)
	r.POST("/selectfromchains", selectFromChains)
	r.POST("/updatetochains", updateToChains)
	r.POST("/deletefromchains", deleteFromChains)
	r.POST("/transfertochains", transferToChains)
	r.POST("/addusertochains", addUserToChains)
	r.POST("/selectuserfromchains", selectUserFromChains)
	r.POST("/deleteuserfromchains", deleteUserFromChains)

	r.GET("/hello", func(c *gin.Context) {
		c.String(http.StatusOK, "hello")
	})

	// r.GET("/user", func(c *gin.Context) {
	// 	id := c.Query("id")
	// 	c.JSON(200, gin.H{
	// 		"id": id,
	// 	})
	// })

	// r.GET("/user/:id", func(c *gin.Context) {
	// 	//id := c.Query("id")
	// 	id := c.Param("id")
	// 	c.JSON(200, gin.H{
	// 		"id": id,
	// 	})
	// })

	// r.POST("/login", func(c *gin.Context) {
	// 	c.String(200, "login")
	// })

	// r.POST("/json", func(c *gin.Context) {
	// 	raw, _ := c.GetRawData()
	// 	var j map[string]interface{}
	// 	json.Unmarshal(raw, &j)
	// 	var id = j["id"]
	// 	print(id)
	// 	fmt.Println(id)
	// 	c.JSON(200, gin.H{
	// 		"id": id,
	// 	})
	// })

	// r.POST("/addjson", func(c *gin.Context) {
	// 	id := c.PostForm("id")
	// 	name := c.PostForm("name")
	// 	c.JSON(200, gin.H{
	// 		"id":   id,
	// 		"name": name,
	// 	})
	// })

	r.Run(":8887") // 启动 HTTP 服务
}

// imageToMatrix 将图片转换为彩色矩阵
func imageToMatrix(img image.Image) [][]color.RGBA {
	bounds := img.Bounds()
	width, height := bounds.Dx(), bounds.Dy()

	matrix := make([][]color.RGBA, height)
	for i := range matrix {
		matrix[i] = make([]color.RGBA, width)
	}

	for y := 0; y < height; y++ {
		for x := 0; x < width; x++ {
			// 获取当前像素的RGBA值
			rgba := img.At(x, y).(color.RGBA)
			// 将RGBA值存储到矩阵中
			matrix[y][x] = rgba
		}
	}

	return matrix
}

// rgbaMatrixToFloat64Matrix 将color.RGBA类型矩阵转换为float64类型矩阵
func rgbaMatrixToFloat64Matrix(rgbaMatrix [][]color.RGBA) [][]float64 {
	float64Matrix := make([][]float64, len(rgbaMatrix))
	for i := range rgbaMatrix {
		float64Matrix[i] = make([]float64, len(rgbaMatrix[i])*4) // 每个像素有4个通道
	}

	for y, row := range rgbaMatrix {
		for x, rgba := range row {
			// 将uint8转换为float64，并归一化到0到1的范围内
			float64Matrix[y][x*4+0] = float64(rgba.R) / 255.0
			float64Matrix[y][x*4+1] = float64(rgba.G) / 255.0
			float64Matrix[y][x*4+2] = float64(rgba.B) / 255.0
			float64Matrix[y][x*4+3] = float64(rgba.A) / 255.0
		}
	}

	return float64Matrix
}

// float64MatrixToRGBAMatrix 将float64类型矩阵转换回color.RGBA类型矩阵
func float64MatrixToRGBAMatrix(float64Matrix [][]float64, width, height int) [][]color.RGBA {
	rgbaMatrix := make([][]color.RGBA, height)
	for i := range rgbaMatrix {
		rgbaMatrix[i] = make([]color.RGBA, width)
	}

	for y := 0; y < height; y++ {
		for x := 0; x < width; x++ {
			// 将float64值缩放回0到255的范围，并转换为uint8
			r := uint8(float64(float64Matrix[y][x*4+0]) * 255)
			g := uint8(float64(float64Matrix[y][x*4+1]) * 255)
			b := uint8(float64(float64Matrix[y][x*4+2]) * 255)
			a := uint8(float64(float64Matrix[y][x*4+3]) * 255)
			rgbaMatrix[y][x] = color.RGBA{R: r, G: g, B: b, A: a}
		}
	}

	return rgbaMatrix
}

// matrixToImage 将彩色矩阵转换回图片
func matrixToImage(matrix [][]color.RGBA, width, height int) *image.RGBA {
	img := image.NewRGBA(image.Rect(0, 0, width, height))
	for y := 0; y < height; y++ {
		for x := 0; x < width; x++ {
			img.Set(x, y, matrix[y][x])
		}
	}
	return img
}

// saveImage 保存图片到文件
func saveImage(img *image.RGBA, filename string) error {
	file, err := os.Create(filename)
	if err != nil {
		return err
	}
	defer file.Close()

	err = png.Encode(file, img)
	if err != nil {
		return err
	}
	return nil
}

// 发送HTTP POST请求
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

// 水印的相关函数
const (
	d = 16
)

// 添加水印
func ImgAddWatermark(inputimgpath string, watermark string, outputimgpath string) {
	imageByte, err := os.ReadFile(inputimgpath)
	if err != nil {
		print("ReadFileError:", err)
		return
	}
	img, _, err := image.Decode(bytes.NewBuffer(imageByte))
	if err != nil {
		print(err)
		return
	}

	wmImg, _ := AddWatermark(img, bytes.NewBufferString(watermark).Bytes())

	b, _ := ExtractWaterMask(wmImg)
	println("add watermark success! the watermark is: ", bytes.NewBuffer(b).String())

	f, err := os.Create(outputimgpath)
	png.Encode(f, wmImg)
}

// 提取水印
func ImgExtractWaterMark(imgpath string) string {

	imageByte, err := os.ReadFile(imgpath)
	if err != nil {
		print(err)
		return "readerr"
	}
	img, _, err := image.Decode(bytes.NewBuffer(imageByte))
	if err != nil {
		print(err)
		return "decodeerr"
	}

	wm, err := ExtractWaterMask(img)
	wmstr := bytes.NewBuffer(wm).String()

	// println("extract watermark success! the watermark is: ", bytes.NewBuffer(wm).String())
	println("extract watermark success! the watermark is: ", wmstr)
	return wmstr
}

func AddWatermark(img image.Image, wm []byte) (image.Image, error) {
	RGBAImage := image.NewRGBA(image.Rect(0, 0, img.Bounds().Dx(), img.Bounds().Dy()))
	draw.Draw(RGBAImage, RGBAImage.Bounds(), img, img.Bounds().Min, draw.Src)

	pix := make([][]float64, RGBAImage.Bounds().Max.X)
	for i := 0; i < RGBAImage.Bounds().Max.X; i++ {
		row := make([]float64, img.Bounds().Max.Y)
		for j := 0; j < RGBAImage.Bounds().Max.Y; j++ {
			r, _, _, _ := RGBAImage.At(i, j).RGBA()
			row[j] = float64(uint8(r))
		}
		pix[i] = row
	}

	ll, lh, hl, hh := Dwt2(pix)

	blocks := SwitchToBlocks(ll)

	blocks = EmbedWm(blocks, wm)

	//fmt.Println(bytes.NewBuffer(append(bytes.NewBufferString(wm).Bytes(), []byte{0,0}...)).Bytes())
	//fmt.Println(blocks)
	ll = RestoreSourceData(blocks)

	pix = Idwt2(ll, lh, hl, hh)

	for i, _ := range pix {
		for j, w := range pix[i] {
			if w > 255 {
				//fmt.Println(i, j, w)
				pix[i][j] = 255
			}
			if w < 0 {
				pix[i][j] = 0
			}
		}
	}

	for i := 0; i < RGBAImage.Bounds().Max.X; i++ {
		for j := 0; j < RGBAImage.Bounds().Max.Y; j++ {
			_, g, b, a := RGBAImage.At(i, j).RGBA()
			RGBAImage.Set(i, j, color.RGBA{
				R: uint8(pix[i][j] + 0.1),
				G: uint8(g),
				B: uint8(b),
				A: uint8(a),
			})
			//fmt.Println(RGBAImage.At(i,j).RGBA())
		}
	}

	return RGBAImage, nil
}

func ExtractWaterMask(img image.Image) ([]byte, error) {
	RGBAImage := image.NewRGBA(image.Rect(0, 0, img.Bounds().Dx(), img.Bounds().Dy()))
	draw.Draw(RGBAImage, RGBAImage.Bounds(), img, img.Bounds().Min, draw.Src)

	pix := make([][]float64, RGBAImage.Bounds().Max.X)
	for i := 0; i < RGBAImage.Bounds().Max.X; i++ {
		row := make([]float64, img.Bounds().Max.Y)
		for j := 0; j < RGBAImage.Bounds().Max.Y; j++ {
			r, _, _, _ := RGBAImage.At(i, j).RGBA()
			row[j] = float64(uint8(r))
		}
		pix[i] = row
	}

	ll, _, _, _ := Dwt2(pix)

	blocks := SwitchToBlocks(ll)

	wm := ExtractWm(blocks)

	return wm, nil
}

func EmbedWm(src [][][][]float64, wm []uint8) [][][][]float64 {
	startSymbol := []byte{1, 1, 1, 1}
	wm = append(append(startSymbol, wm...), []byte{0, 0, 0, 0}...)
	for i := 0; i < len(src); i++ {
		for j := 0; j < len(src[i]); j++ {
			pos := ((i*len(src[i]) + j) / 8) % len(wm)
			phase := (i*len(src[i]) + j) % 8
			//fmt.Println(i, j, pos, phase)
			//if pos >= len(wm) {
			//	//ExtractWm(src)
			//	return src
			//}

			bit := (wm[pos] >> phase) & 1
			//fmt.Println(bit)
			//fmt.Println((float64(uint64(src[i][j][0][0]/36)) + (1.0/4) + (1.0/2)*float64(b[i*len(src)+j])), (float64(uint64(src[i][j][0][0]/36)) + (1/4) + (1/2)*float64(b[i*len(src)+j])) * 36)
			//src[i][j][0][0] = (float64(uint64(src[i][j][0][0]/36)) + 1.0/4 + 1.0/2*bit) * 36
			//fmt.Println(src[i][j][0][0])
			src[i][j] = embedOneBitInBlock(src[i][j], bit)
		}
	}
	//fmt.Println(src)
	return src
}

func embedOneBitInBlock(block [][]float64, bit uint8) [][]float64 {
	//block = Dct2(block)

	block[0][0] = (float64(uint64(block[0][0]/d)) + 1.0/4 + 1.0/2*float64(bit)) * d
	//fmt.Println(block[0][0], uint64(block[0][0])%36)
	//block = Idct2(block)

	return block
}

func extractBitFromBlock(block [][]float64) uint8 {
	//block = Dct2(block)
	//fmt.Println(block[0][0], uint64(block[0][0])%36)
	//for i:=0;i<stride;i++{
	//	for j:=0;j<stride;j++{
	//		if FloatToUint8(block[i][j])%d == d/4|| FloatToUint8(block[i][j])%d==d*3/4{
	//			return 1
	//		}
	//	}
	//}
	if uint64(block[0][0])%d > d/2 {
		return 1
	}
	return 0
}

func ExtractWm(src [][][][]float64) []uint8 {
	wm := make([]uint8, 0)
	endCnt := 0
	startCnt := 0
	b := uint8(0)
	for i := 0; i < len(src); i++ {
		for j := 0; j < len(src[i]); j++ {
			//pos := (i*len(src[i]) + j) / 8
			phase := (i*len(src[i]) + j) % 8

			if extractBitFromBlock(src[i][j]) == 1 {
				b |= 1 << phase
			}

			if phase == 7 {
				//fmt.Println(b)
				if startCnt >= 4 {
					wm = append(wm, b)
					if b == 0 {
						endCnt++
					} else {
						endCnt = 0
					}
					if endCnt >= 4 {
						return wm[:len(wm)-4]
					}
				} else {
					if b == 1 {
						startCnt++
					} else {
						startCnt = 0
					}
				}
				b = 0
			}
		}
	}

	return wm
}

func dct(x []float64) []float64 {
	ret := make([]float64, len(x))
	for k, _ := range x {
		for n, _ := range x {
			q := math.Sqrt(1 / float64(len(x)))
			if k != 0 {
				q = math.Sqrt(2 / float64(len(x)))
			}
			ret[k] += q * x[n] * math.Cos(math.Pi*(float64(n)+0.5)*float64(k)/float64(len(x)))
		}
	}
	return ret
}

func idct(xk []float64) []float64 {
	ret := make([]float64, len(xk))
	for n, _ := range xk {
		for k, _ := range xk {
			q := math.Sqrt(1 / float64(len(xk)))
			if k != 0 {
				q = math.Sqrt(2 / float64(len(xk)))
			}
			ret[n] += q * xk[k] * math.Cos(math.Pi*(float64(n)+0.5)*float64(k)/float64(len(xk)))
		}
	}
	return ret
}

func Dct2(x [][]float64) [][]float64 {
	for i, v := range x {
		x[i] = dct(v)
	}
	x = switchRowAndColumns(x)
	for i, v := range x {
		x[i] = dct(v)
	}
	x = switchRowAndColumns(x)
	return x
}

func Idct2(x [][]float64) [][]float64 {
	x = switchRowAndColumns(x)
	for i, v := range x {
		x[i] = idct(v)
	}
	x = switchRowAndColumns(x)
	for i, v := range x {
		x[i] = idct(v)
	}
	return x
}

func Dwt2(src [][]float64) (aa [][]float64, ad [][]float64, da [][]float64, dd [][]float64) {
	rows := len(src)
	cols := len(src[0])
	if rows%2 == 1 || cols%2 == 1 {
		panic("data is not vaild")
	}
	//fmt.Println(rows, cols)
	for i, v := range src {
		//fmt.Println(i)
		dwt(v)
		src[i] = v
	}
	src = switchRowAndColumns(src)
	for i, v := range src {
		//fmt.Println(i)
		dwt(v)
		src[i] = v
	}
	src = switchRowAndColumns(src)

	for i := 0; i < rows; i++ {
		aa = append(aa, src[i][:cols/2])
		ad = append(ad, src[i][cols/2:])
	}

	return aa[:rows/2], ad[:rows/2], aa[rows/2:], ad[rows/2:]
}

func Idwt2(aa [][]float64, ad [][]float64, da [][]float64, dd [][]float64) (dst [][]float64) {
	for i := 0; i < len(aa); i++ {
		aa[i] = append(aa[i], ad[i]...)
		da[i] = append(da[i], dd[i]...)
	}
	dst = append(aa, da...)

	dst = switchRowAndColumns(dst)
	for i, v := range dst {
		idwt(v)
		dst[i] = v
	}

	dst = switchRowAndColumns(dst)
	for i, v := range dst {
		idwt(v)
		dst[i] = v
	}

	return dst
}

const (
	p1_97  = -1.586134342
	ip1_97 = -p1_97

	u1_97  = -0.05298011854
	iu1_97 = -u1_97

	p2_97  = 0.8829110762
	ip2_97 = -p2_97

	u2_97  = 0.4435068522
	iu2_97 = -u2_97

	scale97  = 1.149604398
	iscale97 = 1.0 / scale97
)

// Fwt97 performs a bi-orthogonal 9/7 wavelet transformation (lifting implementation)
// of the signal in slice xn. The length of the signal n = len(xn) must be a power of 2.
//
// The input slice xn will be replaced by the transformation:
//
// The first half part of the output signal contains the approximation coefficients.
// The second half part contains the detail coefficients (aka. the wavelets coefficients).
func dwt(xn []float64) {
	n := len(xn)

	// predict 1
	for i := 1; i < n-2; i += 2 {
		xn[i] += p1_97 * (xn[i-1] + xn[i+1])
	}
	xn[n-1] += 2 * p1_97 * xn[n-2]

	// update 1
	for i := 2; i < n; i += 2 {
		xn[i] += u1_97 * (xn[i-1] + xn[i+1])
	}
	xn[0] += 2 * u1_97 * xn[1]

	// predict 2
	for i := 1; i < n-2; i += 2 {
		xn[i] += p2_97 * (xn[i-1] + xn[i+1])
	}
	xn[n-1] += 2 * p2_97 * xn[n-2]

	// update 2
	for i := 2; i < n; i += 2 {
		xn[i] += u2_97 * (xn[i-1] + xn[i+1])
	}
	xn[0] += 2 * u2_97 * xn[1]

	// scale
	for i := 0; i < n; i++ {
		if i%2 != 0 {
			xn[i] *= iscale97
		} else {
			xn[i] /= iscale97
		}
	}

	// pack
	tb := make([]float64, n)
	for i := 0; i < n; i++ {
		if i%2 == 0 {
			tb[i/2] = xn[i]
		} else {
			tb[n/2+i/2] = xn[i]
		}
	}
	copy(xn, tb)
}

// Iwt97 performs an inverse bi-orthogonal 9/7 wavelet transformation of xn.
// This is the inverse function of Fwt97 so that Iwt97(Fwt97(xn))=xn for every signal xn of length n.
//
// The length of slice xn must be a power of 2.
//
// The coefficients provided in slice xn are replaced by the original signal.
func idwt(xn []float64) {
	n := len(xn)

	// unpack
	tb := make([]float64, n)
	for i := 0; i < n/2; i++ {
		tb[i*2] = xn[i]
		tb[i*2+1] = xn[i+n/2]
	}
	copy(xn, tb)

	// undo scale
	for i := 0; i < n; i++ {
		if i%2 != 0 {
			xn[i] *= scale97
		} else {
			xn[i] /= scale97
		}
	}

	// undo update 2
	for i := 2; i < n; i += 2 {
		xn[i] += iu2_97 * (xn[i-1] + xn[i+1])
	}
	xn[0] += 2 * iu2_97 * xn[1]

	// undo predict 2
	for i := 1; i < n-2; i += 2 {
		xn[i] += ip2_97 * (xn[i-1] + xn[i+1])
	}
	xn[n-1] += 2 * ip2_97 * xn[n-2]

	// undo update 1
	for i := 2; i < n; i += 2 {
		xn[i] += iu1_97 * (xn[i-1] + xn[i+1])
	}
	xn[0] += 2 * iu1_97 * xn[1]

	// undo predict 1
	for i := 1; i < n-2; i += 2 {
		xn[i] += ip1_97 * (xn[i-1] + xn[i+1])
	}
	xn[n-1] += 2 * ip1_97 * xn[n-2]
}

const (
	stride = 1
)

// 切分block,二维转四维
func SwitchToBlocks(src [][]float64) (blocks [][][][]float64) {

	rLen := len(src)
	cLen := len(src[0])
	//fmt.Println(rLen, cLen)
	for i := 0; i*stride+stride-1 < rLen; i++ {
		blockRow := make([][][]float64, 0)
		for j := 0; j*stride+stride-1 < cLen; j++ {
			block := [stride][]float64{}

			for m := 0; m < stride; m++ {
				rowInBlock := [stride]float64{}
				for n := 0; n < stride; n++ {
					//fmt.Println(i, j, m, n, i*stride+m, j*stride+n)
					//if j*stride+n>cLen{
					//
					//}
					rowInBlock[n] = src[i*stride+m][j*stride+n]
				}
				block[m] = rowInBlock[:]
			}
			blockRow = append(blockRow, block[:])
		}
		//fmt.Println(i, blockRow)
		blocks = append(blocks, blockRow)
	}
	return blocks
}

// 还原数据
func RestoreSourceData(blocks [][][][]float64) (src [][]float64) {
	rBlockLen := len(blocks)
	cBlockLen := len(blocks[0])

	src = make([][]float64, rBlockLen*stride)
	for i := 0; i < rBlockLen; i++ {
		for m := 0; m < stride; m++ {
			row := make([]float64, cBlockLen*stride)
			for j := 0; j < cBlockLen; j++ {
				for n := 0; n < stride; n++ {
					row[j*stride+n] = blocks[i][j][m][n]
				}
			}
			src[i*stride+m] = row
		}
	}
	return src
}

func switchRowAndColumns(data [][]float64) [][]float64 {
	ret := make([][]float64, len(data[0]))
	for j := 0; j < len(data[0]); j++ {
		ret[j] = make([]float64, len(data))
	}

	for i := 0; i < len(data); i++ {
		//fmt.Println(len(data[i]))
		for j := 0; j < len(data[i]); j++ {
			ret[j][i] = data[i][j]
		}
	}
	return ret
}

func FloatToUint8(f float64) uint8 {
	return uint8(f + 0.1)
}
