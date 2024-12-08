package controller

import (
	"net/http"

	"github.com/ZhenTaoWei/bcec/biz"
	"github.com/ZhenTaoWei/bcec/model"
	"github.com/gin-gonic/gin"
)

// LoginRequest 用于接收前端登录请求
type LoginRequest struct {
	Username string `json:"username" binding:"required"`
	Password string `json:"password" binding:"required"`
}

// Login 处理用户登录请求
func Login(c *gin.Context) {
	var user model.User
	if err := c.ShouldBindJSON(&user); err != nil {
		c.JSON(400, gin.H{"error": err.Error()})
		return
	}

	storedUser, err := model.GetUserByName(user.Username)
	if err != nil {
		c.JSON(401, gin.H{"status": "unauthorized"})
		return
	}

	if user.Password == storedUser.Password {
		token, err := model.GenerateToken(storedUser)
		if err != nil {
			c.JSON(500, gin.H{"error": "Failed to generate token"})
			return
		}
		if err := model.UpdateToken(storedUser.Username, token); err != nil {
			c.JSON(500, gin.H{"error": "Failed to update token in database"})
			return
		}
		c.JSON(200, gin.H{"status": "success", "token": token})
	} else {
		c.JSON(401, gin.H{"status": "unauthorized"})
	}
}

type UploadParam struct {
	Username   string `form:"username" json:"username" xml:"username" binding:"required"`
	ModelParam string `form:"modelparam" json:"modelparam" xml:"modelparam" binding:"required"`
}

type TransactionParam struct {
	Username   string `form:"username" json:"username" xml:"username" binding:"required"`
	ModelParam string `form:"modelparam" json:"modelparam" xml:"modelparam" binding:"required"`
}

type VerifyParam struct {
	Username   string `form:"username" json:"username" xml:"username" binding:"required"`
	ModelParam string `form:"modelparam" json:"modelparam" xml:"modelparam" binding:"required"`
}

type SearchParam struct {
	Type  string `form:"type" json:"type"`
	Model string `form:"model" json:"model"`
}

type ModelUploadParam struct {
	Type  string `form:"type" json:"type"`
	Model string `form:"model" json:"model"`
}

func Upload(c *gin.Context) {
	params := UploadParam{}
	if err := c.ShouldBind(params); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	if err := biz.Upload(params.Username, params.ModelParam); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	c.JSON(http.StatusOK, gin.H{
		"message": "ok",
	})
}

func Transaction(c *gin.Context) {
	params := TransactionParam{}
	if err := c.ShouldBind(params); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	if err := biz.Transaction(params.Username, params.ModelParam); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	c.JSON(http.StatusOK, gin.H{
		"message": "ok",
	})
}

func Verify(c *gin.Context) {
	params := VerifyParam{}
	if err := c.ShouldBind(params); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	res, err := biz.Verify(params.Username, params.ModelParam)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	c.JSON(http.StatusOK, gin.H{
		"message": "ok",
		"result":  res,
	})
}

func Search(c *gin.Context) {
	params := SearchParam{}
	if err := c.ShouldBind(&params); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	res, err := biz.Search(params.Type, params.Model)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	c.JSON(http.StatusOK, gin.H{
		"message": "ok",
		"results": res,
	})
}

func ModelUpload(c *gin.Context) {
	files, err := c.MultipartForm()
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	if files == nil || len(files.File["files"]) == 0 {
		c.JSON(http.StatusBadRequest, gin.H{"error": "No files uploaded"})
		return
	}
	params := ModelUploadParam{}
	if err := c.ShouldBind(&params); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	mID, err := biz.ModelUpload(files, params.Type, params.Model)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	c.JSON(http.StatusOK, gin.H{
		"message": "ok",
		"modelID": mID,
	})
}

type BuyParam struct {
	Money   string `form:"money" json:"money"`
	ModelID string `form:"modelID" json:"modelID"`
}
type SoldParam struct {
	TranID string `form:"tranID" json:"tranID"`
}

func BuySubmit(c *gin.Context) {
	params := BuyParam{}
	if err := c.ShouldBind(&params); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	tranID, err := biz.BuySubmit(params.Money, params.ModelID)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	c.JSON(http.StatusOK, gin.H{
		"message": "ok",
		"tranID":  tranID,
	})
}

func SoldSubmit(c *gin.Context) {
	params := SoldParam{}
	if err := c.ShouldBind(&params); err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	file, err := c.FormFile("file")
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	err = biz.SoldSubmit(params.TranID, file)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{"error": err.Error()})
		return
	}
	c.JSON(http.StatusOK, gin.H{
		"message": "ok",
	})
}
