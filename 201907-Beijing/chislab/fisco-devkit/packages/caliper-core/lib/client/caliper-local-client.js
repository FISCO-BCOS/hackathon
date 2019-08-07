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

const cfUtil = require('../config/config-util.js');
const CaliperUtils = require('../utils/caliper-utils.js');
const Logger = CaliperUtils.getLogger('local-client.js');
const bc   = require('../blockchain.js');
const RateControl = require('../rate-control/rateControl.js');

/**
 * Class for Client Interaction
 */
class CaliperLocalClient {

    /**
     * Create the test client
     * @param {Object} bcClient blockchain client
     */
    constructor(bcClient) {
        this.blockchain = new bc(bcClient);
        this.results      = [];
        this.txNum        = 0;
        this.txLastNum    = 0;
        this.resultStats  = [];
        this.trimType = 0;
        this.trim = 0;
        this.startTime = 0;
    }

    /**
     * Calculate realtime transaction statistics and send the txUpdated message
     */
    txUpdate() {
        let newNum = this.txNum - this.txLastNum;
        this.txLastNum += newNum;

        let newResults = this.results.slice(0);
        this.results = [];
        if(newResults.length === 0 && newNum === 0) {
            return;
        }

        let newStats;
        if(newResults.length === 0) {
            newStats = bc.createNullDefaultTxStats();
        }
        else {
            newStats = this.blockchain.getDefaultTxStats(newResults, false);
        }
        process.send({type: 'txUpdated', data: {submitted: newNum, committed: newStats}});

        if (this.resultStats.length === 0) {
            switch (this.trimType) {
            case 0: // no trim
                this.resultStats[0] = newStats;
                break;
            case 1: // based on duration
                if (this.trim < (Date.now() - this.startTime)/1000) {
                    this.resultStats[0] = newStats;
                }
                break;
            case 2: // based on number
                if (this.trim < newResults.length) {
                    newResults = newResults.slice(this.trim);
                    newStats = this.blockchain.getDefaultTxStats(newResults, false);
                    this.resultStats[0] = newStats;
                    this.trim = 0;
                } else {
                    this.trim -= newResults.length;
                }
                break;
            }
        } else {
            this.resultStats[1] = newStats;
            bc.mergeDefaultTxStats(this.resultStats);
        }
    }

    /**
     * Add new test result into global array
     * @param {Object} result test result, should be an array or a single JSON object
     */
    addResult(result) {
        if(Array.isArray(result)) { // contain multiple results
            for(let i = 0 ; i < result.length ; i++) {
                this.results.push(result[i]);
            }
        }
        else {
            this.results.push(result);
        }
    }


    /**
     * Call before starting a new test
     * @param {JSON} msg start test message
     */
    beforeTest(msg) {
        this.results  = [];
        this.resultStats = [];
        this.txNum = 0;
        this.txLastNum = 0;

        // conditionally trim beginning and end results for this test run
        if (msg.trim) {
            if (msg.txDuration) {
                this.trimType = 1;
            } else {
                this.trimType = 2;
            }
            this.trim = msg.trim;
        } else {
            this.trimType = 0;
        }
    }

    /**
     * Callback for new submitted transaction(s)
     * @param {Number} count count of new submitted transaction(s)
     */
    submitCallback(count) {
        this.txNum += count;
    }

    /**
     * Put a task to immediate queue of NodeJS event loop
     * @param {function} func The function needed to be exectued immediately
     * @return {Promise} Promise of execution
     */
    setImmediatePromise(func) {
        return new Promise((resolve) => {
            setImmediate(() => {
                func();
                resolve();
            });
        });
    }

    /**
     * Perform test with specified number of transactions
     * @param {JSON} msg start test message
     * @param {Object} cb callback module
     * @param {Object} context blockchain context
     * @return {Promise} promise object
     */
    async runFixedNumber(msg, cb, context) {
        Logger.info('Info: client ' + process.pid +  ' start test runFixedNumber()' + (cb.info ? (':' + cb.info) : ''));
        let rateControl = new RateControl(msg.rateControl, msg.clientIdx, msg.roundIdx);
        await rateControl.init(msg);

        await cb.init(this.blockchain, context, msg.args);
        this.startTime = Date.now();

        let promises = [];
        while(this.txNum < msg.numb) {
            // If this function calls cb.run() too quickly, micro task queue will be filled with unexecuted promises,
            // and I/O task(s) will get no chance to be execute and fall into starvation, for more detail info please visit:
            // https://snyk.io/blog/nodejs-how-even-quick-async-functions-can-block-the-event-loop-starve-io/
            await this.setImmediatePromise(() => {
                promises.push(cb.run().then((result) => {
                    this.addResult(result);
                    return Promise.resolve();
                }));
            });
            await rateControl.applyRateControl(this.startTime, this.txNum, this.results, this.resultStats);
        }

        await Promise.all(promises);
        await rateControl.end();
        return await this.blockchain.releaseContext(context);
    }

    /**
     * Perform test with specified test duration
     * @param {JSON} msg start test message
     * @param {Object} cb callback module
     * @param {Object} context blockchain context
     * @return {Promise} promise object
     */
    async runDuration(msg, cb, context) {
        Logger.info('Info: client ' + process.pid +  ' start test runDuration()' + (cb.info ? (':' + cb.info) : ''));
        let rateControl = new RateControl(msg.rateControl, msg.clientIdx, msg.roundIdx);
        await rateControl.init(msg);
        const duration = msg.txDuration; // duration in seconds

        await cb.init(this.blockchain, context, msg.args);
        this.startTime = Date.now();

        let promises = [];
        while ((Date.now() - this.startTime)/1000 < duration) {
            // If this function calls cb.run() too quickly, micro task queue will be filled with unexecuted promises,
            // and I/O task(s) will get no chance to be execute and fall into starvation, for more detail info please visit:
            // https://snyk.io/blog/nodejs-how-even-quick-async-functions-can-block-the-event-loop-starve-io/
            await this.setImmediatePromise(() => {
                promises.push(cb.run().then((result) => {
                    this.addResult(result);
                    return Promise.resolve();
                }));
            });
            await rateControl.applyRateControl(this.startTime, this.txNum, this.results, this.resultStats);
        }

        await Promise.all(promises);
        await rateControl.end();
        return await this.blockchain.releaseContext(context);
    }

    /**
     * Clear the update interval
     * @param {Object} txUpdateInter the test transaction update interval
     */
    clearUpdateInter(txUpdateInter) {
        // stop reporter
        if(txUpdateInter) {
            clearInterval(txUpdateInter);
            txUpdateInter = null;
            this.txUpdate();
        }
    }

    /**
     * Perform the test
     * @param {JSON} msg start test message
     * @return {Promise} promise object
     */
    async doTest(msg) {
        Logger.debug('doTest() with:', msg);
        let cb = require(CaliperUtils.resolvePath(msg.cb, msg.root));

        this.beforeTest(msg);

        let txUpdateTime = cfUtil.get(cfUtil.keys.CoreTxUpdateTime, 1000);
        Logger.info('txUpdateTime: ' + txUpdateTime);
        const self = this;
        let txUpdateInter = setInterval( () => { self.txUpdate();  } , txUpdateTime);

        try {
            let context = await this.blockchain.getContext(msg.label, msg.clientargs, msg.clientIdx, msg.txFile);
            if(typeof context === 'undefined') {
                context = {
                    engine : {
                        submitCallback : (count) => { self.submitCallback(count); }
                    }
                };
            }
            else {
                context.engine = {
                    submitCallback : (count) => { self.submitCallback(count); }
                };
            }

            if (msg.txDuration) {
                await this.runDuration(msg, cb, context);
            } else {
                await this.runFixedNumber(msg, cb, context);
            }

            this.clearUpdateInter(txUpdateInter);
            await cb.end();

            if (this.resultStats.length > 0) {
                return this.resultStats[0];
            }
            else {
                return this.blockchain.createNullDefaultTxStats();
            }
        } catch (err) {
            this.clearUpdateInter();
            Logger.info(`Client[${process.pid}] encountered an error: ${(err.stack ? err.stack : err)}`);
            throw err;
        }
    }
}

module.exports = CaliperLocalClient;
