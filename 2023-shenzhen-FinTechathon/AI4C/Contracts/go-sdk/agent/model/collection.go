package model

type Collection struct {
	CollectionId            string `json:"collection_id"`
	CollectionName          string `json:"collection_name"`
	OwnerId                 string `json:"owner_id"`
	CertificateTime         string `json:"certificate_time"`
	CertificateOrganization string `json:"certificate_organization"`
	CollectionSemantic      string `json:"collection_semantic"`
}
