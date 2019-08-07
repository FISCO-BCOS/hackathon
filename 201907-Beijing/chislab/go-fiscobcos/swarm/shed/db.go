// Copyright 2018 The go-fiscobcos Authors
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

// Package shed provides a simple abstraction components to compose
// more complex operations on storage data organized in fields and indexes.
//
// Only type which holds logical information about swarm storage chunks data
// and metadata is Item. This part is not generalized mostly for
// performance reasons.
package shed

import (
	"fmt"
	"strconv"
	"strings"
	"time"

	"github.com/chislab/go-fiscobcos/metrics"
	"github.com/chislab/go-fiscobcos/swarm/log"
	"github.com/syndtr/goleveldb/leveldb"
	"github.com/syndtr/goleveldb/leveldb/iterator"
	"github.com/syndtr/goleveldb/leveldb/opt"
)

const (
	openFileLimit              = 128 // The limit for LevelDB OpenFilesCacheCapacity.
	writePauseWarningThrottler = 1 * time.Minute
)

// DB provides abstractions over LevelDB in order to
// implement complex structures using fields and ordered indexes.
// It provides a schema functionality to store fields and indexes
// information about naming and types.
type DB struct {
	ldb  *leveldb.DB
	quit chan struct{} // Quit channel to stop the metrics collection before closing the database
}

// NewDB constructs a new DB and validates the schema
// if it exists in database on the given path.
// metricsPrefix is used for metrics collection for the given DB.
func NewDB(path string, metricsPrefix string) (db *DB, err error) {
	ldb, err := leveldb.OpenFile(path, &opt.Options{
		OpenFilesCacheCapacity: openFileLimit,
	})
	if err != nil {
		return nil, err
	}
	db = &DB{
		ldb: ldb,
	}

	if _, err = db.getSchema(); err != nil {
		if err == leveldb.ErrNotFound {
			// save schema with initialized default fields
			if err = db.putSchema(schema{
				Fields:  make(map[string]fieldSpec),
				Indexes: make(map[byte]indexSpec),
			}); err != nil {
				return nil, err
			}
		} else {
			return nil, err
		}
	}

	// Create a quit channel for the periodic metrics collector and run it
	db.quit = make(chan struct{})

	go db.meter(metricsPrefix, 10*time.Second)

	return db, nil
}

// Put wraps LevelDB Put method to increment metrics counter.
func (db *DB) Put(key []byte, value []byte) (err error) {
	err = db.ldb.Put(key, value, nil)
	if err != nil {
		metrics.GetOrRegisterCounter("DB.putFail", nil).Inc(1)
		return err
	}
	metrics.GetOrRegisterCounter("DB.put", nil).Inc(1)
	return nil
}

// Get wraps LevelDB Get method to increment metrics counter.
func (db *DB) Get(key []byte) (value []byte, err error) {
	value, err = db.ldb.Get(key, nil)
	if err != nil {
		if err == leveldb.ErrNotFound {
			metrics.GetOrRegisterCounter("DB.getNotFound", nil).Inc(1)
		} else {
			metrics.GetOrRegisterCounter("DB.getFail", nil).Inc(1)
		}
		return nil, err
	}
	metrics.GetOrRegisterCounter("DB.get", nil).Inc(1)
	return value, nil
}

// Has wraps LevelDB Has method to increment metrics counter.
func (db *DB) Has(key []byte) (yes bool, err error) {
	yes, err = db.ldb.Has(key, nil)
	if err != nil {
		metrics.GetOrRegisterCounter("DB.hasFail", nil).Inc(1)
		return false, err
	}
	metrics.GetOrRegisterCounter("DB.has", nil).Inc(1)
	return yes, nil
}

// Delete wraps LevelDB Delete method to increment metrics counter.
func (db *DB) Delete(key []byte) (err error) {
	err = db.ldb.Delete(key, nil)
	if err != nil {
		metrics.GetOrRegisterCounter("DB.deleteFail", nil).Inc(1)
		return err
	}
	metrics.GetOrRegisterCounter("DB.delete", nil).Inc(1)
	return nil
}

// NewIterator wraps LevelDB NewIterator method to increment metrics counter.
func (db *DB) NewIterator() iterator.Iterator {
	metrics.GetOrRegisterCounter("DB.newiterator", nil).Inc(1)

	return db.ldb.NewIterator(nil, nil)
}

// WriteBatch wraps LevelDB Write method to increment metrics counter.
func (db *DB) WriteBatch(batch *leveldb.Batch) (err error) {
	err = db.ldb.Write(batch, nil)
	if err != nil {
		metrics.GetOrRegisterCounter("DB.writebatchFail", nil).Inc(1)
		return err
	}
	metrics.GetOrRegisterCounter("DB.writebatch", nil).Inc(1)
	return nil
}

// Close closes LevelDB database.
func (db *DB) Close() (err error) {
	close(db.quit)
	return db.ldb.Close()
}

func (db *DB) meter(prefix string, refresh time.Duration) {
	// Meter for measuring the total time spent in database compaction
	compTimeMeter := metrics.NewRegisteredMeter(prefix+"compact/time", nil)
	// Meter for measuring the data read during compaction
	compReadMeter := metrics.NewRegisteredMeter(prefix+"compact/input", nil)
	// Meter for measuring the data written during compaction
	compWriteMeter := metrics.NewRegisteredMeter(prefix+"compact/output", nil)
	// Meter for measuring the write delay number due to database compaction
	writeDelayMeter := metrics.NewRegisteredMeter(prefix+"compact/writedelay/duration", nil)
	// Meter for measuring the write delay duration due to database compaction
	writeDelayNMeter := metrics.NewRegisteredMeter(prefix+"compact/writedelay/counter", nil)
	// Meter for measuring the effective amount of data read
	diskReadMeter := metrics.NewRegisteredMeter(prefix+"disk/read", nil)
	// Meter for measuring the effective amount of data written
	diskWriteMeter := metrics.NewRegisteredMeter(prefix+"disk/write", nil)

	// Create the counters to store current and previous compaction values
	compactions := make([][]float64, 2)
	for i := 0; i < 2; i++ {
		compactions[i] = make([]float64, 3)
	}
	// Create storage for iostats.
	var iostats [2]float64

	// Create storage and warning log tracer for write delay.
	var (
		delaystats      [2]int64
		lastWritePaused time.Time
	)

	// Iterate ad infinitum and collect the stats
	for i := 1; true; i++ {
		// Retrieve the database stats
		stats, err := db.ldb.GetProperty("leveldb.stats")
		if err != nil {
			log.Error("Failed to read database stats", "err", err)
			continue
		}
		// Find the compaction table, skip the header
		lines := strings.Split(stats, "\n")
		for len(lines) > 0 && strings.TrimSpace(lines[0]) != "Compactions" {
			lines = lines[1:]
		}
		if len(lines) <= 3 {
			log.Error("Compaction table not found")
			continue
		}
		lines = lines[3:]

		// Iterate over all the table rows, and accumulate the entries
		for j := 0; j < len(compactions[i%2]); j++ {
			compactions[i%2][j] = 0
		}
		for _, line := range lines {
			parts := strings.Split(line, "|")
			if len(parts) != 6 {
				break
			}
			for idx, counter := range parts[3:] {
				value, err := strconv.ParseFloat(strings.TrimSpace(counter), 64)
				if err != nil {
					log.Error("Compaction entry parsing failed", "err", err)
					continue
				}
				compactions[i%2][idx] += value
			}
		}
		// Update all the requested meters
		if compTimeMeter != nil {
			compTimeMeter.Mark(int64((compactions[i%2][0] - compactions[(i-1)%2][0]) * 1000 * 1000 * 1000))
		}
		if compReadMeter != nil {
			compReadMeter.Mark(int64((compactions[i%2][1] - compactions[(i-1)%2][1]) * 1024 * 1024))
		}
		if compWriteMeter != nil {
			compWriteMeter.Mark(int64((compactions[i%2][2] - compactions[(i-1)%2][2]) * 1024 * 1024))
		}

		// Retrieve the write delay statistic
		writedelay, err := db.ldb.GetProperty("leveldb.writedelay")
		if err != nil {
			log.Error("Failed to read database write delay statistic", "err", err)
			continue
		}
		var (
			delayN        int64
			delayDuration string
			duration      time.Duration
			paused        bool
		)
		if n, err := fmt.Sscanf(writedelay, "DelayN:%d Delay:%s Paused:%t", &delayN, &delayDuration, &paused); n != 3 || err != nil {
			log.Error("Write delay statistic not found")
			continue
		}
		duration, err = time.ParseDuration(delayDuration)
		if err != nil {
			log.Error("Failed to parse delay duration", "err", err)
			continue
		}
		if writeDelayNMeter != nil {
			writeDelayNMeter.Mark(delayN - delaystats[0])
		}
		if writeDelayMeter != nil {
			writeDelayMeter.Mark(duration.Nanoseconds() - delaystats[1])
		}
		// If a warning that db is performing compaction has been displayed, any subsequent
		// warnings will be withheld for one minute not to overwhelm the user.
		if paused && delayN-delaystats[0] == 0 && duration.Nanoseconds()-delaystats[1] == 0 &&
			time.Now().After(lastWritePaused.Add(writePauseWarningThrottler)) {
			log.Warn("Database compacting, degraded performance")
			lastWritePaused = time.Now()
		}
		delaystats[0], delaystats[1] = delayN, duration.Nanoseconds()

		// Retrieve the database iostats.
		ioStats, err := db.ldb.GetProperty("leveldb.iostats")
		if err != nil {
			log.Error("Failed to read database iostats", "err", err)
			continue
		}
		var nRead, nWrite float64
		parts := strings.Split(ioStats, " ")
		if len(parts) < 2 {
			log.Error("Bad syntax of ioStats", "ioStats", ioStats)
			continue
		}
		if n, err := fmt.Sscanf(parts[0], "Read(MB):%f", &nRead); n != 1 || err != nil {
			log.Error("Bad syntax of read entry", "entry", parts[0])
			continue
		}
		if n, err := fmt.Sscanf(parts[1], "Write(MB):%f", &nWrite); n != 1 || err != nil {
			log.Error("Bad syntax of write entry", "entry", parts[1])
			continue
		}
		if diskReadMeter != nil {
			diskReadMeter.Mark(int64((nRead - iostats[0]) * 1024 * 1024))
		}
		if diskWriteMeter != nil {
			diskWriteMeter.Mark(int64((nWrite - iostats[1]) * 1024 * 1024))
		}
		iostats[0], iostats[1] = nRead, nWrite

		// Sleep a bit, then repeat the stats collection
		select {
		case <-db.quit:
			// Quit requesting, stop hammering the database
			return
		case <-time.After(refresh):
			// Timeout, gather a new set of stats
		}
	}
}
