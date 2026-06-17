package controller

type WhiteList struct {
	List []Identity `json:"list"`
}

type Identity struct {
	UserID     string `json:"userID"`
	PublicKey  string `json:"publicKey"`
	Authorized bool   `json:"authorized"`
}

func NewWhiteList() *WhiteList {
	return &WhiteList{
		List: []Identity{},
	}
}

func (wl *WhiteList) AddItemToWhiteList(item Identity) {
	wl.List = append(wl.List, item)
}

func (wl *WhiteList) FindItemInArray(publicKey string) *Identity {
	for _, item := range wl.List {
		if item.PublicKey == publicKey {
			return &item
		}
	}
	return nil
}

func (wl *WhiteList) ModifyWhiteList(publicKey string) {
	for i, item := range wl.List {
		if item.PublicKey == publicKey {
			wl.List[i].Authorized = !item.Authorized
			return
		}
	}
}

func (wl *WhiteList) DeleteItemFromWhiteList(publicKey string) {
	for i, item := range wl.List {
		if item.PublicKey == publicKey {
			wl.List = append(wl.List[:i], wl.List[i+1:]...)
			return
		}
	}
}
