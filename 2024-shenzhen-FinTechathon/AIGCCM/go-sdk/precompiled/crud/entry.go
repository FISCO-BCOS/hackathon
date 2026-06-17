package crud

type Entry struct {
	fields map[string]string
}

func NewEntry() *Entry {
	return &Entry{fields: make(map[string]string)}
}

func (e *Entry) GetFields() map[string]string {
	return e.fields
}

func (e *Entry) SetFields(newFields map[string]string) {
	e.fields = newFields
}

func (e *Entry) Put(key string, value string) {
	e.fields[key] = value
}

func (e *Entry) Get(key string) string {
	return e.fields[key]
}
