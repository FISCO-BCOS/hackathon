package example

import (
	"encoding/json"
	"errors"
	"fmt"
	"io"
	"mime/multipart"
	"net/http"
	"strconv"
	"strings"
	"time"

	"github.com/flipped-aurora/gin-vue-admin/server/global"
	"github.com/flipped-aurora/gin-vue-admin/server/model/chainuser"
	"github.com/flipped-aurora/gin-vue-admin/server/model/common/request"
	"github.com/flipped-aurora/gin-vue-admin/server/model/example"
	"github.com/flipped-aurora/gin-vue-admin/server/utils/upload"
)

//@author: [piexlmax](https://github.com/piexlmax)
//@function: Upload
//@description: 创建文件上传记录
//@param: file model.ExaFileUploadAndDownload
//@return: error

func (e *FileUploadAndDownloadService) Upload(file example.ExaFileUploadAndDownload) error {
	return global.GVA_DB.Create(&file).Error
}

//@author: [piexlmax](https://github.com/piexlmax)
//@function: FindFile
//@description:
//@param: id uint
//@return: model.ExaFileUploadAndDownload, error

func (e *FileUploadAndDownloadService) FindFile(id uint) (example.ExaFileUploadAndDownload, error) {
	var file example.ExaFileUploadAndDownload
	err := global.GVA_DB.Where("id = ?", id).First(&file).Error
	return file, err
}

//@author: [piexlmax](https://github.com/piexlmax)
//@function: DeleteFile
//@description: 删除文件记录
//@param: file model.ExaFileUploadAndDownload
//@return: err error

func (e *FileUploadAndDownloadService) DeleteFile(file example.ExaFileUploadAndDownload) (err error) {
	var fileFromDb example.ExaFileUploadAndDownload
	fileFromDb, err = e.FindFile(file.ID)
	if err != nil {
		return
	}
	oss := upload.NewOss()
	if err = oss.DeleteFile(fileFromDb.Key); err != nil {
		return errors.New("文件删除失败")
	}
	err = global.GVA_DB.Where("id = ?", file.ID).Unscoped().Delete(&file).Error
	return err
}

// EditFileName 编辑文件名或者
func (e *FileUploadAndDownloadService) EditFileName(file example.ExaFileUploadAndDownload) (err error) {
	var fileFromDb example.ExaFileUploadAndDownload
	return global.GVA_DB.Where("id = ?", file.ID).First(&fileFromDb).Update("name", file.Name).Error
}

//@author: [piexlmax](https://github.com/piexlmax)
//@function: GetFileRecordInfoList
//@description: 分页获取数据
//@param: info request.PageInfo
//@return: list interface{}, total int64, err error

func (e *FileUploadAndDownloadService) GetFileRecordInfoList(info request.PageInfo) (list interface{}, total int64, err error) {
	limit := info.PageSize
	offset := info.PageSize * (info.Page - 1)
	keyword := info.Keyword
	db := global.GVA_DB.Model(&example.ExaFileUploadAndDownload{})
	var fileLists []example.ExaFileUploadAndDownload
	if len(keyword) > 0 {
		db = db.Where("keywords LIKE ?", "%"+keyword+"%")
	}
	err = db.Count(&total).Error
	if err != nil {
		return
	}
	err = db.Limit(limit).Offset(offset).Order("updated_at desc").Find(&fileLists).Error
	return fileLists, total, err
}

func (e *FileUploadAndDownloadService) GetFileRecordInfoListofUser(info request.PageInfo) (list interface{}, total int64, err error) {
	limit := info.PageSize
	offset := info.PageSize * (info.Page - 1)
	keyword := info.Keyword
	fmt.Println(keyword)
	db := global.GVA_DB.Model(&example.ExaFileUploadAndDownload{})
	var fileLists []example.ExaFileUploadAndDownload
	if len(keyword) > 0 {
		db = db.Where("owner LIKE ?", "%"+keyword+"%")
	}
	err = db.Count(&total).Error
	if err != nil {
		return
	}
	err = db.Limit(limit).Offset(offset).Order("updated_at desc").Find(&fileLists).Error
	return fileLists, total, err
}

/*func (e *FileUploadAndDownloadService) FindKeywords() (list interface{}, err error) {
	var keywordsList []string
	db := global.GVA_DB.Model(&example.ExaKnowledge{})
	err = db.Find(&keywordsList).Error
	return keywordsList, err
}*/

func (e *FileUploadAndDownloadService) FindDetail(url string) (file example.ExaFileUploadAndDownload, err error) {
	err = global.GVA_DB.Where("url = ?", url).First(&file).Error
	return
}

func (e *FileUploadAndDownloadService) AddMoney(seller string, goods float64) (err error) {
	var chainus chainuser.Userchain
	db := global.GVA_DB.Model(&chainuser.Userchain{})
	err = db.Where("name = ?", seller).First(&chainus).Update("money", *chainus.Money+goods).Error
	return err
}

type ReqResponse struct {
	Record string `json:"record"`
	Tx     string `json:"tx"`
}

func (e *FileUploadAndDownloadService) Exchange(buyer string, seller string, work string, goods float64) (err error) {
	var fileFromDb example.ExaFileUploadAndDownload
	var chainus chainuser.Userchain
	goodsint := int(goods)
	url := "http://106.13.124.168:10010/collection/transfer"
	contentType := "application/json"

	data := `{
		"user_id1":"` + buyer + `",
		"user_id2":"` + seller + `",
		"collection_id":"` + work + `",
		"goods":"` + strconv.Itoa(goodsint) + `"
	}`
	client := &http.Client{}
	req, err := http.NewRequest("POST", url, strings.NewReader(data))
	req.Header.Set("Content-Type", contentType)
	resp, err := client.Do(req)
	if err != nil {
		fmt.Println("post failed, err:%v\n", err)
		return
	}
	defer resp.Body.Close()
	b, err := io.ReadAll(resp.Body)

	var res ReqResponse

	err = json.Unmarshal(b, &res)
	if err != nil {
		fmt.Println("get resp failed,err:%v\n", err)
		return
	}
	fmt.Println(res.Tx)

	err = global.GVA_DB.Where("url = ?", work).First(&fileFromDb).Update("owner", buyer).Error
	if err != nil {
		return err
	}
	db := global.GVA_DB.Model(&chainuser.Userchain{})
	err = db.Where("name = ?", buyer).First(&chainus).Update("money", *chainus.Money-goods).Error
	if err != nil {
		return err
	}
	err = e.AddMoney(seller, goods)
	return err
}

//@author: [piexlmax](https://github.com/piexlmax)
//@function: UploadFile
//@description: 根据配置文件判断文件上传到本地或者七牛云
//@param: header *multipart.FileHeader, noSave string
//@return: file model.ExaFileUploadAndDownload, err error

func (e *FileUploadAndDownloadService) UploadFile(header *multipart.FileHeader, noSave string) (file example.ExaFileUploadAndDownload, err error) {
	oss := upload.NewOss()
	fakerPath, filePath, key, uploadErr := oss.UploadFile(header)
	if header == nil {
		fmt.Println("header是空")
		return
	}
	if uploadErr != nil {
		panic(uploadErr)
	}
	s := strings.Split(header.Filename, ".")

	keykey := "服饰 凤凰 中国风"

	nowtime := time.Now().Format("2006-01-02 15:04:05")
	url := "http://106.13.124.168:10010/collection/insert"
	contentType := "application/json"
	data := `{
		"collection_id": "` + filePath + `",
        "collection_name": "` + header.Filename + `",
        "owner_id": "Yang",
        "certificate_time": "` + nowtime + `",
        "certificate_organization": "AI4C北京市国信公证处",
        "collection_semantic": "` + keykey + `"
	}`
	client := &http.Client{}
	req, err := http.NewRequest("POST", url, strings.NewReader(data))
	req.Header.Set("Content-Type", contentType)
	resp, err := client.Do(req)
	if err != nil {
		fmt.Println("post failed, err:%v\n", err)
		return
	}
	defer resp.Body.Close()
	b, err := io.ReadAll(resp.Body)

	var res ReqResponse

	err = json.Unmarshal(b, &res)
	if err != nil {
		fmt.Println("get resp failed,err:%v\n", err)
		return
	}
	fmt.Println(res.Tx)

	f := example.ExaFileUploadAndDownload{
		Url:                filePath,
		Name:               header.Filename,
		Tag:                s[len(s)-1],
		Key:                key,
		Faker:              fakerPath,
		Owner:              "Yang",
		Certi_organization: "AI4C北京公证处",
		Keywords:           keykey,
		Biaoshi:            res.Tx,
		Price:              1,
	}
	if noSave == "0" {
		return f, e.Upload(f)
	}
	return f, nil
}
