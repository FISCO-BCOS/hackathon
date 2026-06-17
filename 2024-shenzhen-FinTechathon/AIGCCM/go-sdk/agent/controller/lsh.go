package controller

import (
	"math"
	"math/rand"
	"sort"
)

type Lsh struct {
	Dimensions     int               `json:"dimensions"`
	NumProjections int               `json:"numProjections"`
	Width          int               `json:"width"`
	Projections    [][]float64       `json:"projections"`
	HashTables     []map[int][]Entry `json:"hashTables"`
}

type Entry struct {
	ID     string      `json:"id"`
	Vector [][]float64 `json:"vector"`
}

func NewLsh(dimensions, numProjections, width int) *Lsh {
	lsh := &Lsh{
		Dimensions:     dimensions,
		NumProjections: numProjections,
		Width:          width,
		Projections:    generateProjections(dimensions, numProjections),
		HashTables:     make([]map[int][]Entry, numProjections),
	}

	for i := range lsh.HashTables {
		lsh.HashTables[i] = make(map[int][]Entry)
	}

	return lsh
}

func generateProjections(dimensions, numProjections int) [][]float64 {
	projections := make([][]float64, numProjections)
	for i := 0; i < numProjections; i++ {
		projection := make([]float64, dimensions*10)
		for j := 0; j < dimensions*10; j++ {
			projection[j] = rand.Float64()*2 - 1
		}
		projections[i] = projection
	}
	return projections
}

func (lsh *Lsh) hash(vector [][]float64, projection []float64) int {
	// 将二维转成一维
	flattenedVector := flatten(vector)
	var sum float64
	for i, v := range flattenedVector {
		sum += v * projection[i]
	}
	return int(math.Floor(sum / float64(lsh.Width)))
}

func (lsh *Lsh) Add(id string, vector [][]float64) {
	for i, projection := range lsh.Projections {
		hash := lsh.hash(vector, projection)
		if lsh.HashTables[i][hash] == nil {
			lsh.HashTables[i][hash] = []Entry{}
		}
		lsh.HashTables[i][hash] = append(lsh.HashTables[i][hash], Entry{id, vector})
	}
}

func (lsh *Lsh) Query(vector [][]float64, k int) []string {
	candidates := make(map[string]struct{})
	for i, projection := range lsh.Projections {
		hashValue := lsh.hash(vector, projection)
		if entries, found := lsh.HashTables[i][hashValue]; found {
			for _, entry := range entries {
				candidates[entry.ID] = struct{}{}
			}
		}
	}

	var candidatesArray []Entry
	for id := range candidates {
		candidateVector := lsh.findVectorById(id)
		candidatesArray = append(candidatesArray, Entry{id, candidateVector})
	}

	sort.Slice(candidatesArray, func(i, j int) bool {
		return lsh.euclideanDistance(vector, candidatesArray[i].Vector) < lsh.euclideanDistance(vector, candidatesArray[j].Vector)
	})

	result := []string{}
	for i := 0; i < k && i < len(candidatesArray); i++ {
		result = append(result, candidatesArray[i].ID)
	}

	return result
}

func (lsh *Lsh) euclideanDistance(vec1, vec2 [][]float64) float64 {
	flattenedVec1 := flatten(vec1)
	flattenedVec2 := flatten(vec2)
	var sum float64
	for i := range flattenedVec1 {
		diff := flattenedVec1[i] - flattenedVec2[i]
		sum += diff * diff
	}
	return math.Sqrt(sum)
}

func (lsh *Lsh) findVectorById(id string) [][]float64 {
	for _, table := range lsh.HashTables {
		for _, entries := range table {
			for _, entry := range entries {
				if entry.ID == id {
					return entry.Vector
				}
			}
		}
	}
	return nil
}

func flatten(matrix [][]float64) []float64 {
	flattened := []float64{}
	for _, row := range matrix {
		flattened = append(flattened, row...)
	}
	return flattened
}
