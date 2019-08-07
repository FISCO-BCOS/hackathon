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

const CaliperUtils = require('../utils/caliper-utils');
let logger = CaliperUtils.getLogger('rateControl.js');

const builtInControllers = new Map([
    ['fixed-rate', './fixedRate.js'],
    ['fixed-backlog', './fixedBacklog.js'],
    ['composite-rate', './compositeRate.js'],
    ['zero-rate', './noRate.js'],
    ['record-rate', './recordRate.js'],
    ['replay-rate', './replayRate.js'],
    ['linear-rate', './linearRate.js'],
    ['fixed-feedback-rate', './fixedFeedbackRate.js']
]);

let RateControl = class {

    /**
     * Instantiates the proxy rate controller and creates the configured rate controller behind it.
     * @param {{type:string, opts:object}} rateControl The object describing the rate controller to use.
     * @param {number} clientIdx The 0-based index of the client who instantiates the controller.
     * @param {number} roundIdx The 1-based index of the round the controller is instantiated in.
     */
    constructor(rateControl, clientIdx, roundIdx) {
        logger.debug(`Creating rate controller for client#${clientIdx} for round#${roundIdx}`, rateControl);

        // resolve the type to a module path
        let modulePath = builtInControllers.has(rateControl.type)
            ? builtInControllers.get(rateControl.type) : CaliperUtils.resolvePath(rateControl.type);

        let factoryFunction = require(modulePath).createRateController;
        if (!factoryFunction) {
            throw new Error(`${rateControl.type} does not export the mandatory factory function`);
        }

        this.controller = factoryFunction(rateControl.opts, clientIdx, roundIdx);
    }

    /**
     * Initializes the rate controller for the round.
     *
     * @param {object} msg Client options with adjusted per-client load settings.
     * @param {string} msg.type The type of the message. Currently always 'test'
     * @param {string} msg.label The label of the round.
     * @param {object} msg.rateControl The rate control to use for the round.
     * @param {number} msg.trim The number/seconds of transactions to trim from the results.
     * @param {object} msg.args The user supplied arguments for the round.
     * @param {string} msg.cb The path of the user's callback module.
     * @param {string} msg.config The path of the network's configuration file.
     * @param {number} msg.numb The number of transactions to generate during the round.
     * @param {number} msg.txDuration The length of the round in SECONDS.
     * @param {number} msg.totalClients The number of clients executing the round.
     * @param {number} msg.clients The number of clients executing the round.
     * @param {object} msg.clientargs Arguments for the client.
     * @param {number} msg.clientIdx The 0-based index of the current client.
     * @param {number} msg.roundIdx The 1-based index of the current round.
     * @async
     */
    async init(msg) {
        await this.controller.init(msg);
    }

    /**
     * Perform the rate control action based on knowledge of the start time, current index, and previous results.
     * @param {number} start The epoch time at the start of the round (ms precision).
     * @param {number} idx Sequence number of the current transaction.
     * @param {object[]} recentResults The list of results of recent transactions.
     * @param {Array} resultStats The aggregated stats of previous results.
     * @async
     */
    async applyRateControl(start, idx, recentResults, resultStats) {
        await this.controller.applyRateControl(start, idx, recentResults, resultStats);
    }

    /**
     * Notify the rate controller about the end of the round.
     * @async
     */
    async end() {
        await this.controller.end();
    }
};

module.exports = RateControl;
