// Copyright 2016 The go-fiscobcos Authors
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

package storage

import (
	"context"
	"io"
	"sort"
	"sync"

	"github.com/chislab/go-fiscobcos/swarm/chunk"
	"github.com/chislab/go-fiscobcos/swarm/storage/localstore"
)

/*
FileStore provides the client API entrypoints Store and Retrieve to store and retrieve
It can store anything that has a byte slice representation, so files or serialised objects etc.

Storage: FileStore calls the Chunker to segment the input datastream of any size to a merkle hashed tree of chunks. The key of the root block is returned to the client.

Retrieval: given the key of the root block, the FileStore retrieves the block chunks and reconstructs the original data and passes it back as a lazy reader. A lazy reader is a reader with on-demand delayed processing, i.e. the chunks needed to reconstruct a large file are only fetched and processed if that particular part of the document is actually read.

As the chunker produces chunks, FileStore dispatches them to its own chunk store
implementation for storage or retrieval.
*/

const (
	defaultLDBCapacity                = 5000000 // capacity for LevelDB, by default 5*10^6*4096 bytes == 20GB
	defaultCacheCapacity              = 10000   // capacity for in-memory chunks' cache
	defaultChunkRequestsCacheCapacity = 5000000 // capacity for container holding outgoing requests for chunks. should be set to LevelDB capacity
)

type FileStore struct {
	ChunkStore
	hashFunc SwarmHasher
	tags     *chunk.Tags
}

type FileStoreParams struct {
	Hash string
}

func NewFileStoreParams() *FileStoreParams {
	return &FileStoreParams{
		Hash: DefaultHash,
	}
}

// for testing locally
func NewLocalFileStore(datadir string, basekey []byte, tags *chunk.Tags) (*FileStore, error) {
	localStore, err := localstore.New(datadir, basekey, nil)
	if err != nil {
		return nil, err
	}
	return NewFileStore(chunk.NewValidatorStore(localStore, NewContentAddressValidator(MakeHashFunc(DefaultHash))), NewFileStoreParams(), tags), nil
}

func NewFileStore(store ChunkStore, params *FileStoreParams, tags *chunk.Tags) *FileStore {
	hashFunc := MakeHashFunc(params.Hash)
	return &FileStore{
		ChunkStore: store,
		hashFunc:   hashFunc,
		tags:       tags,
	}
}

// Retrieve is a public API. Main entry point for document retrieval directly. Used by the
// FS-aware API and httpaccess
// Chunk retrieval blocks on netStore requests with a timeout so reader will
// report error if retrieval of chunks within requested range time out.
// It returns a reader with the chunk data and whether the content was encrypted
func (f *FileStore) Retrieve(ctx context.Context, addr Address) (reader *LazyChunkReader, isEncrypted bool) {
	isEncrypted = len(addr) > f.hashFunc().Size()
	tag, err := f.tags.GetFromContext(ctx)
	if err != nil {
		tag = chunk.NewTag(0, "ephemeral-retrieval-tag", 0)
	}
	getter := NewHasherStore(f.ChunkStore, f.hashFunc, isEncrypted, tag)
	reader = TreeJoin(ctx, addr, getter, 0)
	return
}

// Store is a public API. Main entry point for document storage directly. Used by the
// FS-aware API and httpaccess
func (f *FileStore) Store(ctx context.Context, data io.Reader, size int64, toEncrypt bool) (addr Address, wait func(context.Context) error, err error) {
	tag, err := f.tags.GetFromContext(ctx)
	if err != nil {
		// some of the parts of the codebase, namely the manifest trie, do not store the context
		// of the original request nor the tag with the trie, recalculating the trie hence
		// loses the tag uid. thus we create an ephemeral tag here for that purpose

		tag = chunk.NewTag(0, "", 0)
		//return nil, nil, err
	}
	putter := NewHasherStore(f.ChunkStore, f.hashFunc, toEncrypt, tag)
	return PyramidSplit(ctx, data, putter, putter, tag)
}

func (f *FileStore) HashSize() int {
	return f.hashFunc().Size()
}

// GetAllReferences is a public API. This endpoint returns all chunk hashes (only) for a given file
func (f *FileStore) GetAllReferences(ctx context.Context, data io.Reader, toEncrypt bool) (addrs AddressCollection, err error) {
	tag := chunk.NewTag(0, "ephemeral-tag", 0) //this tag is just a mock ephemeral tag since we don't want to save these results

	// create a special kind of putter, which only will store the references
	putter := &hashExplorer{
		hasherStore: NewHasherStore(f.ChunkStore, f.hashFunc, toEncrypt, tag),
	}
	// do the actual splitting anyway, no way around it
	_, wait, err := PyramidSplit(ctx, data, putter, putter, tag)
	if err != nil {
		return nil, err
	}
	// wait for splitting to be complete and all chunks processed
	err = wait(ctx)
	if err != nil {
		return nil, err
	}
	// collect all references
	addrs = NewAddressCollection(0)
	for _, ref := range putter.references {
		addrs = append(addrs, Address(ref))
	}
	sort.Sort(addrs)
	return addrs, nil
}

// hashExplorer is a special kind of putter which will only store chunk references
type hashExplorer struct {
	*hasherStore
	references []Reference
	lock       sync.Mutex
}

// HashExplorer's Put will add just the chunk hashes to its `References`
func (he *hashExplorer) Put(ctx context.Context, chunkData ChunkData) (Reference, error) {
	// Need to do the actual Put, which returns the references
	ref, err := he.hasherStore.Put(ctx, chunkData)
	if err != nil {
		return nil, err
	}
	// internally store the reference
	he.lock.Lock()
	he.references = append(he.references, ref)
	he.lock.Unlock()
	return ref, nil
}
