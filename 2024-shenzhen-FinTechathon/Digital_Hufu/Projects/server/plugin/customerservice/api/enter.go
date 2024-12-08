package api

type ApiGroup struct {
	CustomerServiceApi
	AdminServiceApi
}

var ApiGroupApp = new(ApiGroup)
