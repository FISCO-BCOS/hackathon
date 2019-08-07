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
const RateInterface = require('./rateInterface.js');
const RateControl = require('./rateControl.js');
const CaliperUtils = require('../utils/caliper-utils');
const logger = CaliperUtils.getLogger('compositeRate.js');

/**
 * Encapsulates a controller and its scheduling information.
 *
 * Time related values are expressed in millisecond!
 *
 * @property {number} weight The weight associated with the controller.
 * @property {boolean} last Indicates whether the controller is the last in the round.
 * @property {object} controllerOptions The supplied options for the controller.
 * @property {RateControl} controller The controller instance.
 * @property {number} firstTxIndex The first Tx index associated with the controller.
 *                                 It is used to calculate the adjusted Tx index passed to the controller.
 * @property {number} startTimeDifference The difference between the start time of the round and the controller.
 *                                        It is used to calculate the adjusted start time passed to the controller.
 * @property {number} lastTxIndex The last Tx index associated with the controller based on its weight.
 *                                Only used in Tx number-based rounds.
 * @property {number} relFinishTime The finish time of the controller based on its weight, relative to the start time of the round.
 *                                  Only used in duration-based rounds.
 */
class ControllerData {
    /**
     * Initialize a new instance of the ControllerData class.
     * @param {number} weight The weight associated with the controller.
     * @param {object} controllerOptions The specified options for the controller.
     * @param {RateControl} controller The controller object.
     */
    constructor(weight, controllerOptions, controller) {
        this.weight = weight;
        this.isLast = false;
        this.controllerOptions = controllerOptions;
        this.controller = controller;
        this.firstTxIndex = 0; // correct default value for the first sub-controller
        this.startTimeDifference = 0; // correct default value for the first sub-controller
        this.lastTxIndex = 0;
        this.relFinishTime = 0;
    }
}

/**
 * Composite rate controller for applying different rate controllers after one an other in the same round.
 *
 * Time related values are expressed in millisecond!
 *
 * @property {object} options The user-supplied options for the controller.
 * @property {number[]} options.weights The list of weights for the different controllers.
 * @property {object[]} options.rateControllers The list of descriptors of the controllers.
 * @property {boolean} options.logChange Indicates whether to log when switching to a new controller.
 * @property {ControllerData[]} controllers The list of relevant controllers and their scheduling information.
 * @property {number} activeControllerIndex The index of the currently active controller.
 * @property {number} clientIdx The index of the current client.
 * @property {function} controllerSwitch Duration-based or Tx number-based function for handling controller switches.
 */
class CompositeRateController extends RateInterface{
    /**
     * Creates a new instance of the CompositeRateController class.
     * @constructor
     * @param {object} opts Options for the rate controller.
     * @param {number} clientIdx The 0-based index of the client who instantiates the controller.
     * @param {number} roundIdx The 1-based index of the round the controller is instantiated in.
     * @throws {Error} Throws error if there is a problem with the weight and/or controller options.
     */
    constructor(opts, clientIdx, roundIdx) {
        super(opts);

        this.controllers = [];
        this.activeControllerIndex = 0;
        this.clientIdx = -1;
        this.controllerSwitch = null;
        this.logControllerChange = (this.options.logChange &&
            typeof(this.options.logChange) === 'boolean' && this.options.logChange) || false;

        this.__prepareControllers(clientIdx, roundIdx);
    }

    /**
     * Internal method for preparing the controllers for further use.
     * @param {number} clientIdx The 0-based index of the client who instantiates the controller.
     * @param {number} roundIdx The 1-based index of the round the controller is instantiated in.
     * @private
     */
    __prepareControllers(clientIdx, roundIdx) {
        let weights = this.options.weights;
        let rateControllers = this.options.rateControllers;

        if (!Array.isArray(weights) || !Array.isArray(rateControllers)) {
            throw new Error('Weight and controller definitions must be arrays.');
        }

        if (weights.length !== rateControllers.length) {
            throw new Error('The number of weights and controllers must be the same.');
        }

        const nan = weights.find(w => isNaN(Number(w)));
        if (nan) {
            throw new Error('Not-a-number element among weights: ' + nan);
        }

        // store them explicitly as numbers to avoid surprises later
        weights = weights.map(w => Number(w));

        // check for negative weights
        // zero weights should be fine to allow easy temporary removal of controllers from the config file
        const negativeWeight = weights.find(w => w < 0);
        if (negativeWeight) {
            throw new Error('Negative element among weights: ' + negativeWeight);
        }

        // normalize weights
        const weightSum = weights.reduce((prev, w) => prev + w, 0);
        if (weightSum === 0) {
            throw new Error('Every weight is zero.');
        }
        weights = weights.map(w => w / weightSum);

        // create controller instances, skip zero-weight cases
        for (let i = 0; i < weights.length; ++i) {
            if (weights[i] === 0) {
                continue;
            }

            let info = new ControllerData(weights[i], rateControllers[i], new RateControl(rateControllers[i], clientIdx, roundIdx));
            this.controllers.push(info);
        }

        // mark the last controller
        this.controllers[this.controllers.length - 1].isLast = true;
    }

    /**
     * Internal method for switching controller (when needed) in a duration-based round.
     * @param {number} start The start time of the round provided by the client module.
     * @param {number} idx The index of the current Tx provided by the client module.
     * @private
     * @async
     */
    async __controllerSwitchForDuration(start, idx) {
        let active = this.controllers[this.activeControllerIndex];
        const timeNow = Date.now();
        if (active.isLast || ((timeNow - start) < active.relFinishTime)) {
            return;
        }

        await active.controller.end();

        this.activeControllerIndex++;
        active = this.controllers[this.activeControllerIndex];
        active.firstTxIndex = idx;
        active.startTimeDifference = Date.now() - start;
        if (this.logControllerChange) {
            logger.debug(`Switching controller in Client#${this.clientIdx} at Tx#${idx} after ${active.startTimeDifference}ms.`);
        }
    }

    /**
     * Internal method for switching controller (when needed) in a Tx number-based round.
     * @param {number} start The start time of the round provided by the client module.
     * @param {number} idx The index of the current Tx provided by the client module.
     * @private
     * @async
     */
    async __controllerSwitchForTxNumber(start, idx) {
        let active = this.controllers[this.activeControllerIndex];
        if (active.isLast || (idx <= active.lastTxIndex)) {
            return;
        }

        await active.controller.end();

        this.activeControllerIndex++;
        active = this.controllers[this.activeControllerIndex];
        active.firstTxIndex = idx ;
        active.startTimeDifference = Date.now() - start;
        if (this.logControllerChange) {
            logger.debug(`Switching controller in Client#${this.clientIdx} at Tx#${idx} after ${active.startTimeDifference}ms.`);
        }
    }

    /**
     * Initializes the rate controller.
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
        let currentSum = 0;
        this.clientIdx = msg.clientIdx + 1;

        // pre-set switch logic to avoid an other condition check during rate control
        this.controllerSwitch = msg.numb ? this.__controllerSwitchForTxNumber : this.__controllerSwitchForDuration;

        for (let i = 0; i < this.controllers.length; ++i) {
            let controllerData = this.controllers[i];
            currentSum += controllerData.weight;

            // create a modified copy of the client options according to the weights
            let controllerMsg = Object.assign({}, msg);
            controllerMsg.rateControl = controllerData.controllerOptions;

            // scale down number of txs or duration
            if (msg.numb) {
                controllerMsg.numb = Math.floor(msg.numb * controllerData.weight);
                controllerData.lastTxIndex = Math.floor(msg.numb * currentSum);
            } else {
                controllerMsg.txDuration = Math.floor(msg.txDuration * controllerData.weight);
                controllerData.relFinishTime = Math.floor(msg.txDuration * 1000 * currentSum);
            }

            await controllerData.controller.init(controllerMsg);
        }

        this.activeControllerIndex = 0;
    }

    /**
     * Perform the rate control by delegating to the currently active controller
     * and switching controller (if necessary).
     * @param {number} start The epoch time at the start of the round (ms precision).
     * @param {number} idx Sequence number of the current transaction.
     * @param {object[]} recentResults The list of results of recent transactions.
     * @param {object[]} resultStats The aggregated stats of previous results.
     * @async
     */
    async applyRateControl(start, idx, recentResults, resultStats) {
        await this.controllerSwitch(start, idx);
        const active = this.controllers[this.activeControllerIndex];
        // NOTE: since we don't know much about the transaction indices corresponding to
        // the recent results (the list is emptied periodically), pass it as it is

        // if (idx - this.firstTxIndex + 1) >= recentResults.length  ==> everything is transparent, the rate controller
        // has been running long enough, so every recent result belongs to it
        // otherwise ==> some results MUST belong to the previous controller, but we don't know which result index
        // corresponds to active.firstTxIndex, maybe none of them, because this phase hasn't produced results yet

        // lie to the controller about the parameters to make this controller transparent
        await active.controller.applyRateControl(start + active.startTimeDifference, idx - active.firstTxIndex,
            recentResults, resultStats);
    }

    /**
     * Notify the rate controller about the end of the round.
     * @async
     */
    async end() {
        await this.controllers[this.activeControllerIndex].controller.end();
    }
}

/**
 * Creates a new rate controller instance.
 * @param {object} opts The rate controller options.
 * @param {number} clientIdx The 0-based index of the client who instantiates the controller.
 * @param {number} roundIdx The 1-based index of the round the controller is instantiated in.
 * @return {RateInterface} The rate controller instance.
 */
function createRateController(opts, clientIdx, roundIdx) {
    return new CompositeRateController(opts, clientIdx, roundIdx);
}

module.exports.createRateController = createRateController;
