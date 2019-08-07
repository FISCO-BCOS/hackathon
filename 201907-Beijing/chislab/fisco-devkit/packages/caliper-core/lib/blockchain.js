/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

'use strict';

/**
 * BlockChain class, define operations to interact with the blockchain system under test
 */
class Blockchain {
    /**
     * Constructor
     * @param {Object} bcObj the concrete blockchain object
     */
    constructor(bcObj) {
        this.bcObj = bcObj;
        this.bcType = bcObj.bcType;
    }

    /**
     * return the blockchain's type
     * @return {string} type of the blockchain
     */
    gettype() {
        return this.bcType;
    }

    /**
    * Initialise test environment, e.g. create a fabric channel for the test
    * @async
    */
    async init() {
        await this.bcObj.init();
    }

    /**
     * Perform required preparation for test clients, e.g. enroll clients and obtain key pairs
     * @param {Number} number count of test clients
     * @return {Promise} array of obtained material for test clients
     * @async
     */
    async prepareClients (number) {
        return await this.bcObj.prepareClients(number);
    }

    /**
    * Install smart contract(s), detail informations are defined in the blockchain configuration file
    * @async
    */
    async installSmartContract() {
        await this.bcObj.installSmartContract();
    }

    /**
     * Get a context for subsequent operations, e.g. invoke smart contract or query state
     * @param {String} name name of the context
     * @param {Object} args adapter specific arguments
     * @param {Integer} clientIdx the client index
     * @param {Object} txFile the file information for reading or writing.
     * @return {Promise} obtained context object
     * @async
     */
    async getContext(name, args, clientIdx, txFile) {
        return await this.bcObj.getContext(name, args, clientIdx, txFile);
    }

    /**
     * Release a context as well as related resources
     * @param {Object} context adapter specific object
     * @return {Promise} promise object
     */
    async releaseContext(context) {
        return await this.bcObj.releaseContext(context);
    }

    /**
     * Invoke smart contract/submit transactions and return corresponding transactions' status
     * @param {Object} context context object
     * @param {String} contractID identiy of the contract
     * @param {String} contractVer version of the contract
     * @param {Array} args array of JSON formatted arguments for multiple transactions
     * @param {Number} timeout request timeout, in second
     * @return {Promise} txStatus object or an array of txStatus objects
     */
    async invokeSmartContract(context, contractID, contractVer, args, timeout) {
        let arg, time;    // compatible with old version
        if(Array.isArray(args)) {
            arg = args;
        }
        else if(typeof args === 'object') {
            arg = [args];
        }
        else {
            throw new Error('Invalid args for invokeSmartContract()');
        }

        if(typeof timeout !== 'number' || timeout < 0) {
            time = 120;
        }
        else {
            time = timeout;
        }

        return await this.bcObj.invokeSmartContract(context, contractID, contractVer, arg, time);
    }

    /**
     * Query state from the ledger
     * @param {Object} context context object from getContext
     * @param {String} contractID identiy of the contract
     * @param {String} contractVer version of the contract
     * @param {String} key lookup key
     * @param {String=} [fcn] query function name
     * @return {Promise} as invokeSmateContract()
     */
    async queryState(context, contractID, contractVer, key, fcn) {
        return await this.bcObj.queryState(context, contractID, contractVer, key, fcn);
    }

    /**
    * Calculate the default transaction statistics
    * @param {Array} results array of txStatus
    * @param {Boolean} detail indicates whether to keep detailed information
    * @return {JSON} txStatistics JSON object
    */
    getDefaultTxStats(results, detail) {
        let succ = 0, fail = 0, delay = 0;
        let minFinal, maxFinal, minCreate, maxCreate;
        let maxLastFinal;
        let minDelay = 100000, maxDelay = 0;
        let delays = [];
        let sTPTotal = 0;
        let sTTotal = 0;
        let invokeTotal = 0;
        for(let i = 0 ; i < results.length ; i++) {
            let stat   = results[i];
            sTPTotal = sTPTotal + stat.Get('sTP');
            sTTotal = sTTotal + stat.Get('sT');
            invokeTotal += stat.Get('invokeLatency');
            let create = stat.GetTimeCreate();

            if(typeof minCreate === 'undefined') {
                minCreate = create;
                maxCreate = create;
            }
            else {
                if(create < minCreate) {
                    minCreate = create;
                }
                if(create > maxCreate) {
                    maxCreate = create;
                }
            }

            if(stat.IsCommitted()) {
                succ++;
                let final = stat.GetTimeFinal();
                let d     = (final - create) / 1000;
                if(typeof minFinal === 'undefined') {
                    minFinal = final;
                    maxFinal = final;
                }
                else {
                    if(final < minFinal) {
                        minFinal = final;
                    }
                    if(final > maxFinal) {
                        maxFinal = final;
                    }
                }

                delay += d;
                if(d < minDelay) {
                    minDelay = d;
                }
                if(d > maxDelay) {
                    maxDelay = d;
                }

                if(detail) {
                    delays.push(d);
                }
            }
            else {
                fail++;
            }

            let curFinal = stat.GetTimeFinal();
            if(typeof maxLastFinal === 'undefined') {
                maxLastFinal = curFinal;
            }
            else{
                if(curFinal > maxLastFinal){
                    maxLastFinal = curFinal;
                }
            }
        }

        let stats = {
            'succ' : succ,
            'fail' : fail,
            'create' : {'min' : minCreate/1000, 'max' : maxCreate/1000},    // convert to second
            'final'  : {'min' : minFinal/1000,  'max' : maxFinal/1000, 'last' : maxLastFinal/1000 },
            'delay'  : {'min' : minDelay,  'max' : maxDelay, 'sum' : delay, 'detail': (detail?delays:[]) },
            'out' : [],
            'sTPTotal': sTPTotal,
            'sTTotal': sTTotal,
            'invokeTotal': invokeTotal,
            'length': results.length
        };
        return stats;
    }

    /**
     * merge an array of default 'txStatistics', the result is in first object of the array
     * Note even failed the first object of the array may still be changed
     * @param {Array} results txStatistics array
     * @return {Number} 0 if failed; otherwise 1
     */
    static mergeDefaultTxStats(results) {
        try{
            // skip invalid result
            let skip = 0;
            for(let i = 0 ; i < results.length ; i++) {
                let result = results[i];

                if(!result.hasOwnProperty('succ') || !result.hasOwnProperty('fail') || (result.succ + result.fail) === 0) {
                    skip++;
                }
                else {
                    break;
                }
            }

            if(skip > 0) {
                results.splice(0, skip);
            }

            if(results.length === 0) {
                return 0;
            }

            let r = results[0];
            for(let i = 1 ; i < results.length ; i++) {
                let v = results[i];
                if(!v.hasOwnProperty('succ') || !v.hasOwnProperty('fail') || (v.succ + v.fail) === 0) {
                    continue;
                }
                r.succ += v.succ;
                r.fail += v.fail;
                r.sTPTotal += v.sTPTotal;
                r.sTTotal += v.sTTotal;
                r.invokeTotal += v.invokeTotal;
                r.length += v.length;
                r.out.push.apply(r.out, v.out);
                if(v.create.min < r.create.min) {
                    r.create.min = v.create.min;
                }
                if(v.create.max > r.create.max) {
                    r.create.max = v.create.max;
                }
                if(v.final.min < r.final.min) {
                    r.final.min = v.final.min;
                }
                if(v.final.max > r.final.max) {
                    r.final.max = v.final.max;
                }
                if(v.final.last > r.final.last){
                    r.final.last = v.final.last;
                }
                if(v.delay.min < r.delay.min) {
                    r.delay.min = v.delay.min;
                }
                if(v.delay.max > r.delay.max) {
                    r.delay.max = v.delay.max;
                }
                r.delay.sum += v.delay.sum;
                for(let j = 0 ; j < v.delay.detail.length ; j++) {
                    r.delay.detail.push(v.delay.detail[j]);
                }
            }
            return 1;
        }
        catch(err) {
            //throw err;
            return 0;
        }
    }

    /**
     * create a 'null' txStatistics object
     * @return {JSON} 'null' txStatistics object
     */
    static createNullDefaultTxStats() {
        return {succ: 0, fail: 0};
    }
}

module.exports = Blockchain;
