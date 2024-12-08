package controller

import "fmt"

type CopyrightTree struct {
	F               int             `json:"f"`
	T               *Lsh            `json:"t"`
	IndexList       []*Node         `json:"indexList"`
	SimilarityTrees map[string]*DAG `json:"similarityTrees"`
	Trees           int             `json:"trees"`
}

func NewCopyrightTree(f, numProjections, width int) *CopyrightTree {
	return &CopyrightTree{
		F:               f,
		T:               NewLsh(f, numProjections, width),
		IndexList:       []*Node{},
		SimilarityTrees: make(map[string]*DAG),
		Trees:           numProjections,
	}
}

func (ct *CopyrightTree) AddInitialNode(newNode *Node) {
	ct.IndexList = append(ct.IndexList, newNode)
	ct.SimilarityTrees[newNode.Hash] = NewDAG(newNode)
	ct.T.Add(newNode.Hash, newNode.Matrix)
}

func (ct *CopyrightTree) AddInitialNodes(newNodes []*Node) {
	ct.IndexList = newNodes
	for _, node := range newNodes {
		ct.SimilarityTrees[node.Hash] = NewDAG(node)
		ct.SimilarityTrees[node.Hash].UpdateAffectedNodes(ct.SimilarityTrees[node.Hash].Root)
		ct.T.Add(node.Hash, node.Matrix)
	}
}

func (ct *CopyrightTree) InsertIndexNode(newNode *Node) {

	nearestIndex := ct.T.Query(newNode.Matrix, 1)

	fmt.Println("Debug: ", nearestIndex)

	if len(nearestIndex) > 0 {
		index := nearestIndex[0]
		fmt.Println("Index: ", index)
		ct.SimilarityTrees[index].Insert(newNode)
	} else {
		ct.IndexList = append(ct.IndexList, newNode)
		fmt.Println("New Index List: ", ct.IndexList)
		newIndex := newNode.Hash
		fmt.Println("New Index: ", newIndex)
		ct.SimilarityTrees[newIndex] = NewDAG(newNode)
		ct.T.Add(newNode.Hash, newNode.Matrix)
		fmt.Println("New Index: ", newIndex)
		nearestIndex := ct.T.Query(newNode.Matrix, 1)
		fmt.Println("Debug: ", nearestIndex)
	}
}

func (ct *CopyrightTree) FindNodeByHash(queryNode *Node) *Node {
	//fmt.Println("Query Node: ", queryNode.Matrix)
	nearestIndex := ct.T.Query(queryNode.Matrix, 1)
	fmt.Println("Debug: ", nearestIndex)
	if len(nearestIndex) > 0 {
		index := nearestIndex[0]
		return ct.SimilarityTrees[index].FindNodeByHash(queryNode.Hash)
	}
	return nil
}

func (ct *CopyrightTree) FindPath(queryNode *Node) []Node {
	nearestIndex := ct.T.Query(queryNode.Matrix, 1)
	if len(nearestIndex) > 0 {
		index := nearestIndex[0]
		return ct.SimilarityTrees[index].GetPath(queryNode)
	}
	return []Node{}
}

func (ct *CopyrightTree) VerifySongPath(queryNode *Node) bool {
	nearestIndex := ct.T.Query(queryNode.Matrix, 1)
	if len(nearestIndex) > 0 {
		index := nearestIndex[0]
		path := ct.SimilarityTrees[index].GetPath(queryNode)
		return ct.SimilarityTrees[index].VerifySignature(path)
	}
	return false
}

func (ct *CopyrightTree) ChangeNodeOwner(queryNode *Node, newOwner string) {
	nearestIndex := ct.T.Query(queryNode.Matrix, 1)
	if len(nearestIndex) > 0 {
		index := nearestIndex[0]
		ct.SimilarityTrees[index].ChangeNodeOwner(queryNode.Hash, newOwner)
	}
}

func (ct *CopyrightTree) DeleteNode(node *Node) {
	nearestIndex := ct.T.Query(node.Matrix, 1)
	//fmt.Println("Debug: ", nearestIndex)
	if len(nearestIndex) > 0 {
		index := nearestIndex[0]
		ct.SimilarityTrees[index].Delete(node.Hash)
	}
}

func (ct *CopyrightTree) GetAllDAGs() [][]*Node {
	var result [][]*Node
	for _, dag := range ct.SimilarityTrees {
		allNodes := dag.GetAllNodes()
		result = append(result, allNodes)
	}
	return result
}
