package router

type RouterGroup struct {
	CustomerServiceRouter
}

var RouterGroupApp = new(RouterGroup)
