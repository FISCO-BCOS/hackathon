package user

type User_True struct {
	ID                    string // 用户 ID
	Password              string // 用户自己的密码
	BlockChain_PrivateKey string // 设置以太坊私钥
	Addr                  string // 以太坊公钥地址
}

type User struct {
	ID        string   // 用户 ID
	Key       string   // 加密的 AES 密钥
	Addr      string   // 以太坊公钥地址
	PL        string   // 加密的权限列表（给 KDC 设置有什么身份的客户端能使用）
	Attribute []string // 属性列表（赋予客户端对应的属性身份）
}

const (
	PROFESSION_1 = "Teacher"
	PROFESSION_2 = "Student"

	OFFICE_1 = "310"
	OFFICE_2 = "223"

	RESEARCHIN_1 = "Block-Chain"
	RESEARCHIN_2 = "Federated-Learning"
)
