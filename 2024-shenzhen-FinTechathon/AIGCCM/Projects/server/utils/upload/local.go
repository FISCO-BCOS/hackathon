package upload

import (
	"errors"
	"fmt"
	"image"
	"image/jpeg"
	"io"
	"mime/multipart"
	"os"
	"path"
	"strings"
	"time"

	"github.com/disintegration/gift"
	"github.com/flipped-aurora/gin-vue-admin/server/global"
	"github.com/flipped-aurora/gin-vue-admin/server/utils"
	"go.uber.org/zap"
)

type Local struct{}

//@author: [piexlmax](https://github.com/piexlmax)
//@author: [ccfish86](https://github.com/ccfish86)
//@author: [SliverHorn](https://github.com/SliverHorn)
//@object: *Local
//@function: UploadFile
//@description: 上传文件
//@param: file *multipart.FileHeader
//@return: string, string, error

func (*Local) UploadFile(file *multipart.FileHeader) (string, string, string, error) {
	// 读取文件后缀
	ext := path.Ext(file.Filename)
	// 读取文件名并加密
	name := strings.TrimSuffix(file.Filename, ext)
	name = utils.MD5V([]byte(name))
	// 拼接新文件名
	filename := name + "_" + time.Now().Format("20060102150405") + ext
	fakername := "faker_" + name + "_" + time.Now().Format("20060102150405") + ext
	// 尝试创建此路径
	mkdirErr := os.MkdirAll(global.GVA_CONFIG.Local.StorePath, os.ModePerm)
	if mkdirErr != nil {
		global.GVA_LOG.Error("function os.MkdirAll() failed", zap.Any("err", mkdirErr.Error()))
		return "", "", "", errors.New("function os.MkdirAll() failed, err:" + mkdirErr.Error())
	}
	// 拼接路径和文件名
	p := global.GVA_CONFIG.Local.StorePath + "/" + filename
	filepath := global.GVA_CONFIG.Local.Path + "/" + filename
	fakerpath := global.GVA_CONFIG.Local.Path + "/" + fakername

	f, openError := file.Open() // 读取文件
	if openError != nil {
		global.GVA_LOG.Error("function file.Open() failed", zap.Any("err", openError.Error()))
		return "", "", "", errors.New("function file.Open() failed, err:" + openError.Error())
	}
	defer f.Close() // 创建文件 defer 关闭

	out, createErr := os.Create(p)
	if createErr != nil {
		global.GVA_LOG.Error("function os.Create() failed", zap.Any("err", createErr.Error()))

		return "", "", "", errors.New("function os.Create() failed, err:" + createErr.Error())
	}
	defer out.Close() // 创建文件 defer 关闭

	_, copyErr := io.Copy(out, f) // 传输（拷贝）文件
	if copyErr != nil {
		global.GVA_LOG.Error("function io.Copy() failed", zap.Any("err", copyErr.Error()))
		return "", "", "", errors.New("function io.Copy() failed, err:" + copyErr.Error())
	}

	if ext == ".jpg" {
		//进行图片模糊处理
		originfile, err := os.Open(filepath)
		if err != nil {
			fmt.Println("无法打开图片文件:", err)
			return "", "", "", errors.New("function file.Open() failed, err:" + openError.Error())
		}
		defer originfile.Close()

		img, err := jpeg.Decode(originfile)
		if err != nil {
			fmt.Println("无法解码图片:", err)
			return "", "", "", errors.New("function file.Open() failed, err:" + openError.Error())
		}

		g := gift.New(gift.GaussianBlur(5))
		blurred := image.NewRGBA(g.Bounds(img.Bounds()))
		g.Draw(blurred, img)

		outputFile, err := os.Create(fakerpath)
		if err != nil {
			fmt.Println("无法创建输出文件:", err)
			return "", "", "", errors.New("function file.Open() failed, err:" + openError.Error())
		}
		defer outputFile.Close()
		// 将模糊后的图片保存到输出文件
		jpeg.Encode(outputFile, blurred, nil)
	}

	return fakerpath, filepath, filename, nil
}

//@author: [piexlmax](https://github.com/piexlmax)
//@author: [ccfish86](https://github.com/ccfish86)
//@author: [SliverHorn](https://github.com/SliverHorn)
//@object: *Local
//@function: DeleteFile
//@description: 删除文件
//@param: key string
//@return: error

func (*Local) DeleteFile(key string) error {
	p := global.GVA_CONFIG.Local.StorePath + "/" + key
	fakerp := global.GVA_CONFIG.Local.StorePath + "/" + "faker_" + key
	if strings.Contains(p, global.GVA_CONFIG.Local.StorePath) {
		if err := os.Remove(p); err != nil {
			return errors.New("本地文件删除失败, err:" + err.Error())
		}
		if err := os.Remove(fakerp); err != nil {
			return errors.New("模糊文件删除失败, err:" + err.Error())
		}
	}
	return nil
}
