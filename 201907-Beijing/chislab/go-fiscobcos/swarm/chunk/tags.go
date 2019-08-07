// Copyright 2019 The go-fiscobcos Authors
// This file is part of the go-fiscobcos library.
//
// The go-fiscobcos library is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// The go-fiscobcos library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with the go-fiscobcos library. If not, see <http://www.gnu.org/licenses/>.

package chunk

import (
	"context"
	"errors"
	"math/rand"
	"sync"
	"time"

	"github.com/chislab/go-fiscobcos/swarm/sctx"
)

// Tags hold tag information indexed by a unique random uint32
type Tags struct {
	tags *sync.Map
	rng  *rand.Rand
}

// NewTags creates a tags object
func NewTags() *Tags {
	return &Tags{
		tags: &sync.Map{},
		rng:  rand.New(rand.NewSource(time.Now().Unix())),
	}
}

// New creates a new tag, stores it by the name and returns it
// it returns an error if the tag with this name already exists
func (ts *Tags) New(s string, total int64) (*Tag, error) {
	t := &Tag{
		Uid:       ts.rng.Uint32(),
		Name:      s,
		startedAt: time.Now(),
		total:     total,
	}
	if _, loaded := ts.tags.LoadOrStore(t.Uid, t); loaded {
		return nil, errExists
	}
	return t, nil
}

// All returns all existing tags in Tags' sync.Map
// Note that tags are returned in no particular order
func (ts *Tags) All() (t []*Tag) {
	ts.tags.Range(func(k, v interface{}) bool {
		t = append(t, v.(*Tag))

		return true
	})

	return t
}

// Get returns the undelying tag for the uid or an error if not found
func (ts *Tags) Get(uid uint32) (*Tag, error) {
	t, ok := ts.tags.Load(uid)
	if !ok {
		return nil, errors.New("tag not found")
	}
	return t.(*Tag), nil
}

// GetFromContext gets a tag from the tag uid stored in the context
func (ts *Tags) GetFromContext(ctx context.Context) (*Tag, error) {
	uid := sctx.GetTag(ctx)
	t, ok := ts.tags.Load(uid)
	if !ok {
		return nil, errTagNotFound
	}
	return t.(*Tag), nil
}

// Range exposes sync.Map's iterator
func (ts *Tags) Range(fn func(k, v interface{}) bool) {
	ts.tags.Range(fn)
}

func (ts *Tags) Delete(k interface{}) {
	ts.tags.Delete(k)
}
