package crud

import "strconv"

type EnumOP string

const (
	EQ    EnumOP = "eq"
	NE    EnumOP = "ne"
	GT    EnumOP = "gt"
	GE    EnumOP = "ge"
	LT    EnumOP = "lt"
	LE    EnumOP = "le"
	Limit EnumOP = "limit"
)

type Condition struct {
	conditions map[string]map[EnumOP]string
}

func NewCondition() *Condition {
	return &Condition{conditions: make(map[string]map[EnumOP]string)}
}

func (c *Condition) EQ(key string, value string) {
	newMap := make(map[EnumOP]string)
	newMap[EQ] = value
	c.conditions[key] = newMap
}

func (c *Condition) NE(key string, value string) {
	newMap := make(map[EnumOP]string)
	newMap[NE] = value
	c.conditions[key] = newMap
}

func (c *Condition) GT(key string, value string) {
	newMap := make(map[EnumOP]string)
	newMap[GT] = value
	c.conditions[key] = newMap
}

func (c *Condition) GE(key string, value string) {
	newMap := make(map[EnumOP]string)
	newMap[GE] = value
	c.conditions[key] = newMap
}

func (c *Condition) LT(key string, value string) {
	newMap := make(map[EnumOP]string)
	newMap[LT] = value
	c.conditions[key] = newMap
}

func (c *Condition) LE(key string, value string) {
	newMap := make(map[EnumOP]string)
	newMap[LE] = value
	c.conditions[key] = newMap
}

func (c *Condition) Limit(count int) {
	c.limit(0, count)
}

func (c *Condition) limit(offset int, count int) {
	newMap := make(map[EnumOP]string)
	if offset < 0 {
		offset = 0
	}
	if count < 0 {
		count = 0
	}
	newMap[Limit] = strconv.Itoa(offset) + "," + strconv.Itoa(count)
	c.conditions["limit"] = newMap
}

func (c *Condition) GetConditions() map[string]map[EnumOP]string {
	return c.conditions
}

func (c *Condition) SetConditions(newMap map[string]map[EnumOP]string) {
	c.conditions = newMap
}
