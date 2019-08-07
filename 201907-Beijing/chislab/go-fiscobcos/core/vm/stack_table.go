// Copyright 2017 The go-fiscobcos Authors
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

package vm

import (
	"github.com/chislab/go-fiscobcos/params"
)

func minSwapStack(n int) int {
	return minStack(n, n)
}
func maxSwapStack(n int) int {
	return maxStack(n, n)
}

func minDupStack(n int) int {
	return minStack(n, n+1)
}
func maxDupStack(n int) int {
	return maxStack(n, n+1)
}

func maxStack(pop, push int) int {
	return int(params.StackLimit) + pop - push
}
func minStack(pops, push int) int {
	return pops
}
