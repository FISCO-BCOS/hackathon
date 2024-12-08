package controller

import (
	"crypto/ecdsa"
	"crypto/rand"
	"crypto/sha256"
	"fmt"
	"log"
	"math"
	"math/big"
	"strconv"
)

type Node struct {
	Hash      string             `json:"hash"`
	Prehash   string             `json:"prehash"`
	Matrix    [][]float64        `json:"matrix"`
	Style     string             `json:"style"`
	Make      string             `json:"make"`
	Record    string             `json:"record"`
	Owner     string             `json:"owner"`
	HSnMinus1 string             `json:"HSnMinus1"`
	HMn       string             `json:"HMn"`
	SIGN      []byte             `json:"SIGN"`
	Children  map[string][]*Node `json:"children"`
}

type DAG struct {
	Root *Node `json:"root"`
}

func NewNode(simhash, prehash string, matrix [][]float64, style, maker, record, owner, hsnminus1, hmn, sigi string) *Node {
	return &Node{
		Hash:      simhash,
		Prehash:   prehash,
		Matrix:    matrix,
		Style:     style,
		Make:      maker,
		Record:    record,
		Owner:     owner,
		HSnMinus1: hsnminus1,
		HMn:       hmn,
		SIGN:      []byte(sigi),
		Children:  make(map[string][]*Node),
	}
}

func (n *Node) AddChild(level int, childNode *Node) {
	// 检查map是否初始化，如果没有初始化则初始化它
	if n.Children == nil {
		n.Children = make(map[string][]*Node)
	}

	levelString := strconv.Itoa(level)

	// 检查指定level的slice是否初始化，如果没有则初始化它
	if _, exists := n.Children[levelString]; !exists {
		n.Children[levelString] = []*Node{}
	}

	// 添加子节点到指定level的slice中
	n.Children[levelString] = append(n.Children[levelString], childNode)
}

func (n *Node) RemoveChild(childNode *Node) bool {
	for level, children := range n.Children {
		for i, child := range children {
			if child == childNode {
				n.Children[level] = append(n.Children[level][:i], n.Children[level][i+1:]...)
				if len(n.Children[level]) == 0 {
					delete(n.Children, level)
				}
				return true
			}
		}
	}
	return false
}

func NewDAG(rootNode *Node) *DAG {
	return &DAG{Root: rootNode}
}

func (dag *DAG) findBestMatch(currentNode *Node, newSimhash string, similarityValue float64) (*Node, float64) {
	bestMatch := currentNode
	bestSimilarity := similarityValue

	for _, children := range currentNode.Children {
		for _, child := range children {
			childSimilarity := Similarity(child.Hash, newSimhash)
			if childSimilarity > bestSimilarity {
				bestMatch, bestSimilarity = dag.findBestMatch(child, newSimhash, childSimilarity)
			}
		}
	}
	return bestMatch, bestSimilarity
}

func (dag *DAG) Insert(newNode *Node) {
	currentNode := dag.Root
	rootSimilarity := Similarity(currentNode.Hash, newNode.Hash)
	bestMatch, _ := dag.findBestMatch(currentNode, newNode.Hash, rootSimilarity)
	newSimilarity := Similarity(bestMatch.Hash, newNode.Hash)
	level := getIntervalNumber(newSimilarity)
	newNode.Prehash = bestMatch.Hash
	bestMatch.AddChild(level, newNode)
	dag.UpdateAffectedNodes(newNode)
}

func (dag *DAG) GetPath(queryNode *Node) []Node {
	var path []Node
	currentNode := dag.Root
	path = append(path, *currentNode)

	for currentNode.Hash != queryNode.Hash {
		bestMatch := new(Node)
		bestSimilarity := -math.MaxFloat64

		similarityValue := Similarity(currentNode.Hash, queryNode.Hash)
		level := getIntervalNumber(similarityValue)
		levelString := strconv.Itoa(level)
		if children, ok := currentNode.Children[levelString]; ok {
			for _, child := range children {
				childSimilarity := Similarity(child.Hash, queryNode.Hash)

				if childSimilarity >= bestSimilarity {
					bestSimilarity = childSimilarity
					bestMatch = child
				}
			}
		}
		if bestMatch == nil {
			return nil
		}

		path = append(path, *bestMatch)
		currentNode = bestMatch
	}

	return path
}

func (dag *DAG) ChangeNodeOwner(searchHash, owner string) {
	node := dag.FindNodeByHash(searchHash)
	node.Owner = owner
	dag.UpdateAffectedNodes(node)
}

func (dag *DAG) FindNodeByHash(searchHash string) *Node {
	currentNode := dag.Root
	if currentNode == nil {
		fmt.Println("Root node is null of undefined")
		return nil
	}

	if currentNode.Hash == searchHash {
		return currentNode
	}

	queue := []*Node{currentNode}
	for len(queue) > 0 {
		currentNode = queue[0]
		queue = queue[1:]

		if currentNode.Hash == searchHash {
			return currentNode
		}

		for _, children := range currentNode.Children {
			queue = append(queue, children...)
		}
	}
	fmt.Printf("No matching node found for hash: %s\n", searchHash)
	return nil
}

func (dag *DAG) GetAllNodes() []*Node {
	var nodes []*Node
	var dfs func(*Node, int) *Node
	dfs = func(node *Node, level int) *Node {
		nodeInfo := &Node{
			Matrix:   node.Matrix,
			Hash:     node.Hash,
			Children: make(map[string][]*Node),
		}

		for childLevel, children := range node.Children {
			for _, child := range children {
				nodeInfo.Children[childLevel] = append(nodeInfo.Children[childLevel], dfs(child, level+1))
			}
		}

		return nodeInfo
	}

	nodes = append(nodes, dfs(dag.Root, 0))
	return nodes
}

func (dag *DAG) Delete(deleteHash string) bool {
	currentNode := dag.Root
	var parentNode *Node
	var parentLevel int

	for currentNode.Hash != deleteHash {
		bestMatch := (*Node)(nil)
		bestSimilarity := -math.MaxFloat64

		similarityValue := Similarity(currentNode.Hash, deleteHash)
		level := getIntervalNumber(similarityValue)
		levelString := strconv.Itoa(level)
		if children, ok := currentNode.Children[levelString]; ok {
			for i, child := range children {
				if child.Hash == deleteHash {
					if len(child.Children) > 0 {
						for childLevel, childChildren := range child.Children {
							currentNode.Children[childLevel] = append(currentNode.Children[childLevel], childChildren...)
						}
					}
					currentNode.Children[levelString] = append(children[:i], children[i+1:]...)
					if len(currentNode.Children[levelString]) == 0 {
						delete(currentNode.Children, levelString)
					}
					return true
				}
				childSimilarity := Similarity(child.Hash, deleteHash)
				if childSimilarity >= bestSimilarity {
					bestSimilarity = childSimilarity
					bestMatch = child
					parentNode = currentNode
					parentLevel = level
				}
			}
		}
		if bestMatch == nil {
			return false
		}

		currentNode = bestMatch
	}
	if parentNode == nil {
		return false
	}

	parentLevelString := strconv.Itoa(parentLevel)
	parentNode.Children[parentLevelString] = filter(parentNode.Children[parentLevelString], func(child *Node) bool {
		return child.Hash != deleteHash
	})
	if len(parentNode.Children[parentLevelString]) == 0 {
		delete(parentNode.Children, parentLevelString)
	}

	return true
}

func (dag *DAG) UpdateAffectedNodes(node *Node) {
	if node.Prehash == "" {
		hashm := sha256.New()
		hashm.Write([]byte(node.Record))
		node.HMn = fmt.Sprintf("%x", hashm.Sum(nil))

		hashs := sha256.New()
		hashs.Write([]byte("init"))
		node.HSnMinus1 = fmt.Sprintf("%x", hashs.Sum(nil))

		node.SIGN = sign(node.HSnMinus1 + node.HMn)
	} else {
		hashm := sha256.New()
		hashm.Write([]byte(node.Record))
		node.HMn = fmt.Sprintf("%x", hashm.Sum(nil))

		hashs := sha256.New()
		preNode := dag.FindNodeByHash(node.Prehash)
		hashs.Write([]byte(preNode.HSnMinus1 + preNode.HMn + fmt.Sprintf("%x", preNode.SIGN)))
		node.HSnMinus1 = fmt.Sprintf("%x", hashs.Sum(nil))

		node.SIGN = sign(node.HMn + node.HSnMinus1)
	}

	for _, children := range node.Children {
		for _, child := range children {
			dag.UpdateAffectedNodes(child)
		}
	}
}

func (dag *DAG) VerifySignature(path []Node) bool {
	for i := len(path) - 1; i > 0; i-- {
		if !dag.normalSignatureVerify(path[i]) {
			return false
		}
		if !dag.verifyHashAgreement(path[i], path[i-1]) {
			return false
		}
	}
	return true
}

func (dag *DAG) normalSignatureVerify(node Node) bool {
	return verify(node.HMn+node.HSnMinus1, node.SIGN)
}

func (dag *DAG) verifyHashAgreement(node, preNode Node) bool {
	hash := sha256.New()
	hash.Write([]byte(preNode.HSnMinus1 + preNode.HMn + fmt.Sprintf("%x", preNode.SIGN)))
	preMsg := fmt.Sprintf("%x", hash.Sum(nil))
	return node.HSnMinus1 == preMsg
}

func filter(nodes []*Node, condition func(*Node) bool) []*Node {
	var result []*Node
	for _, node := range nodes {
		if condition(node) {
			result = append(result, node)
		}
	}
	return result
}

func sign(data string) []byte {
	hash := sha256.Sum256([]byte(data))
	r, s, err := ecdsa.Sign(rand.Reader, PrivateKey, hash[:])
	if err != nil {
		log.Fatalf("Failed to sign data: %v", err)
	}
	signature := append(r.Bytes(), s.Bytes()...)
	return signature
}

func verify(data string, signature []byte) bool {
	hash := sha256.Sum256([]byte(data))
	r := big.Int{}
	s := big.Int{}
	sigLen := len(signature)
	r.SetBytes(signature[:(sigLen / 2)])
	s.SetBytes(signature[(sigLen / 2):])
	return ecdsa.Verify(PublicKey, hash[:], &r, &s)
}

func getIntervalNumber(value float64) int {
	intervalNumber := (1 - value) * 10
	return int(math.Round(intervalNumber))
}
