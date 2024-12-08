package handler

import (
	"fmt"
	"hufu/controller"
	"hufu/supervisor"
	"log"
	"net/http"
	"os"
	"strconv"
	"time"

	"io"
	"path/filepath"
	"strings"

	"github.com/SSSaaS/sssa-golang"
	"github.com/gin-gonic/gin"
)

func CheckTransaction(c *gin.Context) {
	c.JSON(
		http.StatusOK,
		gin.H{
			"message": "success",
		},
	)
}

func GetPrivateKey(c *gin.Context) {
	// 从multipart form获取数据
	walletID := c.PostForm("wallet_id")
	if walletID == "" {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "缺少wallet_id参数",
		})
		return
	}

	// 解析wallet_id为uint
	wID, err := strconv.ParseUint(walletID, 10, 32)
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "无效的wallet_id格式",
		})
		return
	}

	// 获取上传的文件
	file, err := c.FormFile("evidence")
	if err != nil {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "获取evidence文件失败",
		})
		return
	}

	// 读取文件内容
	f, err := file.Open()
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "读取文件失败",
		})
		return
	}
	defer f.Close()

	evidence, err := io.ReadAll(f)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "读取文件内容失败",
		})
		return
	}

	// 解析文件内容，提取异常证据
	evidenceStr := string(evidence)
	var actualEvidence string

	fileName := fmt.Sprintf("wallet-%d-%s-failed", wID, time.Now().Format("20060102150405"))
	filePath := fmt.Sprintf("./evidence/%s.txt", fileName)
	err = os.WriteFile(filePath, evidence, 0644)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "保存证据文件失败",
		})
		return
	}

	// 按行分割文本
	lines := strings.Split(evidenceStr, "\n")
	for i := 0; i < len(lines); i++ {
		line := strings.TrimSpace(lines[i])
		if strings.HasPrefix(line, "异常证据:") || strings.HasPrefix(line, "异常证据：") {
			actualEvidence = strings.TrimSpace(strings.TrimPrefix(strings.TrimPrefix(line, "异常证据:"), "异常证据："))
			break
		}
	}

	if actualEvidence == "" {
		c.JSON(http.StatusBadRequest, gin.H{
			"error": "文件格式错误：未找到异常证据",
		})
		return
	}

	log.Println("actualEvidence", actualEvidence)

	// 调用处理函数，使用解析出的异常证据
	res, err := controller.ProcessPrivateKey(uint(wID), actualEvidence)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "处理私钥失败: " + err.Error(),
		})
		return
	}

	pk, err := sssa.Combine(res)
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": "处理私钥失败: " + err.Error(),
		})
		return
	}

	// 重命名文件
	fileName = fmt.Sprintf("wallet-%d-%s-success", wID, time.Now().Format("20060102150405"))
	os.Rename(filePath, fmt.Sprintf("./evidence/%s.txt", fileName))

	c.JSON(http.StatusOK, gin.H{
		"message": "success",
		"data":    pk,
		"shares":  res,
	})
}

// 获取异常交易
func GetAbnormalTransaction(c *gin.Context) {
	// 从数据库获取异常交易列表
	abnormalTxs, err := controller.GetAbnormalTransactions()
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"code":    -1,
			"message": "获取异常交易失败",
			"error":   err.Error(),
		})
		return
	}

	c.JSON(http.StatusOK, gin.H{
		"code":    0,
		"message": "success",
		"data":    abnormalTxs,
	})
}

// 获取决策
func GetDecision(c *gin.Context) {
	decision, err := supervisor.GetDecision()
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": err.Error(),
		})
	}

	c.JSON(http.StatusOK, gin.H{
		"message": "success",
		"data":    decision,
	})
}

// 获取事件
func GetEvent(c *gin.Context) {
	event, err := supervisor.GetEvent()
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{
			"error": err.Error(),
		})
	}

	c.JSON(http.StatusOK, event)
}

// GetApplication 获取申请记录
func GetApplication(c *gin.Context) {
	// 查找匹配的文件
	files, err := filepath.Glob("./evidence/wallet-*")
	if err != nil {
		c.JSON(http.StatusInternalServerError, gin.H{"error": "查找文件失败"})
		return
	}

	applications := make([]map[string]interface{}, 0)
	for _, file := range files {
		// 读取文件内容
		content, err := os.ReadFile(file)
		if err != nil {
			continue
		}

		// 从文件名解析信息
		fileName := filepath.Base(file)
		// 移除.txt后缀
		fileName = strings.TrimSuffix(fileName, ".txt")
		parts := strings.Split(fileName, "-")
		if len(parts) < 4 {
			continue
		}

		// 构建返回数据
		application := map[string]interface{}{
			"wallet_id": parts[1],                             // 钱包ID (12)
			"timestamp": parts[2],                             // 时间戳 (20241107124506)
			"status":    strings.TrimSuffix(parts[3], ".txt"), // 状态 (success/failed)
			"content":   string(content),
		}
		applications = append(applications, application)
	}

	c.JSON(http.StatusOK, gin.H{
		"code": 0,
		"msg":  "success",
		"data": applications,
	})
}
