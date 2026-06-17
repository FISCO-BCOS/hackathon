package model

type Collection struct {
	CollectionId            string   `json:"collection_id"`
	CollectionName          string   `json:"collection_name"`
	CollectionMatrix        [][]float64 `json:"collection_matrix"` // 确保类型定义正确
	CollectionMake          string   `json:"collection_make"` // 确保字段名之间没有意外的字符
	CollectionRecord        string   `json:"collection_record"`
	OwnerId                 string   `json:"owner_id"`
	OldOwnerId              string   `json:"old_owner_id"`
	CertificateTime         string   `json:"certificate_time"`
	CertificateOrganization string   `json:"certificate_organization"`
	CollectionSemantic      string   `json:"collection_semantic"`
}
