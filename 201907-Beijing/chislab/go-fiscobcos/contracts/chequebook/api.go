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

package chequebook

import (
	"errors"
	"math/big"

	"github.com/chislab/go-fiscobcos/common"
)

const Version = "1.0"

var errNoChequebook = errors.New("no chequebook")

type API struct {
	chequebookf func() *Chequebook
}

func NewAPI(ch func() *Chequebook) *API {
	return &API{ch}
}

func (a *API) Balance() (string, error) {
	ch := a.chequebookf()
	if ch == nil {
		return "", errNoChequebook
	}
	return ch.Balance().String(), nil
}

func (a *API) Issue(beneficiary common.Address, amount *big.Int) (cheque *Cheque, err error) {
	ch := a.chequebookf()
	if ch == nil {
		return nil, errNoChequebook
	}
	return ch.Issue(beneficiary, amount)
}

func (a *API) Cash(cheque *Cheque) (txhash string, err error) {
	ch := a.chequebookf()
	if ch == nil {
		return "", errNoChequebook
	}
	return ch.Cash(cheque)
}

func (a *API) Deposit(amount *big.Int) (txhash string, err error) {
	ch := a.chequebookf()
	if ch == nil {
		return "", errNoChequebook
	}
	return ch.Deposit(amount)
}
