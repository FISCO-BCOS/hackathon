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
const logger = require('../utils/caliper-utils').getLogger('client-util.js');
let processes  = {}; // {pid:{obj, promise}}

/**
 * Call the Promise function for a process
 * @param {String} pid pid of the client process
 * @param {Boolean} isResolve indicates resolve(true) or reject(false)
 * @param {Object} msg input for the Promise function
 * @param {Boolean} isReady indicates promise type ready(true) promise(false)
 */
function setPromise(pid, isResolve, msg, isReady) {
    const client = processes[pid];
    if (client) {
        const type = isReady ? 'ready' : 'promise';
        const clientObj = client.obj;
        if(clientObj && clientObj[type] && typeof clientObj[type] !== 'undefined') {
            if(isResolve) {
                clientObj[type].resolve(msg);
            }
            else {
                clientObj[type].reject(msg);
            }
        } else {
            throw new Error('Unconditioned case within setPromise()');
        }
    }
}

/**
 * Push test result from a child process into the global array
 * @param {String} pid pid of the child process
 * @param {Object} data test result
 */
function pushResult(pid, data) {
    let p = processes[pid];
    if(p && p.results && typeof p.results !== 'undefined') {
        p.results.push(data);
    }
}

/**
 * Push update value from a child process into the global array
 * @param {String} pid pid of the child process
 * @param {Object} data update value
 */
function pushUpdate(pid, data) {
    let p = processes[pid];
    if(p && p.updates && typeof p.updates !== 'undefined') {
        p.updates.push(data);
    }
}

/**
 * Launch a client process to do the test
 * @param {Array} updates array to save txUpdate results
 * @param {Array} results array to save the test results
 * @param {Object} clientFactory a factory to spawn clients
 * @param {Array} readyPromises array to hold ready promises
 */
function launchClient(updates, results, clientFactory, readyPromises) {
    let client = clientFactory.spawnWorker();
    let pid   = client.pid.toString();

    logger.info('Launching client with PID ', pid);
    processes[pid] = {obj: client, results: results, updates: updates};

    let p = new Promise((resolve, reject) => {
        client.ready = {
            resolve: resolve,
            reject:  reject
        };
    });

    readyPromises.push(p);

    client.on('message', function(msg) {
        if(msg.type === 'ready') {
            logger.info('Client ready message recieved');
            setPromise(pid, true, null, true);
        }
        else if(msg.type === 'testResult') {
            pushResult(pid, msg.data);
            setPromise(pid, true, null);
        }
        else if(msg.type === 'error') {
            setPromise(pid, false, new Error('Client encountered error:' + msg.data));
        }
        else if(msg.type === 'txUpdated') {
            pushUpdate(pid, msg.data);
        }
    });

    client.on('error', function(){
        setPromise(pid, false, new Error('Client encountered unexpected error'));
    });

    client.on('exit', function(code, signal){
        logger.info(`Client exited with code ${code}`);
        setPromise(pid, false, new Error('Client already exited'));
    });
}

/**
 * Start a test
 * @param {Number} number test clients' count
 * @param {JSON} message start message
 * @param {Array} clientArgs each element contains specific arguments for a client
 * @param {Array} updates array to save txUpdate results
 * @param {Array} results array to save the test results
 * @param {Object} clientFactory a factory to spawn test clients
 * @async
 */
async function startTest(number, message, clientArgs, updates, results, clientFactory) {
    let count = 0;
    for(let i in processes) {
        i;  // avoid eslint error
        count++;
    }

    const readyPromises = [];
    if (count !== number) {
        // launch clients
        processes = {};
        for(let i = 0 ; i < number ; i++) {
            launchClient(updates, results, clientFactory, readyPromises);
        }
    }

    // wait for all clients to have initialised
    logger.info(`Waiting for ${readyPromises.length} clients to be ready... `);

    await Promise.all(readyPromises);

    logger.info(`${readyPromises.length} clients ready, starting test phase`);

    let txPerClient;
    let totalTx = message.numb;
    if (message.numb) {
        // Run specified number of transactions
        txPerClient  = Math.floor(message.numb / number);

        // trim should be based on client number if specified with txNumber
        if (message.trim) {
            message.trim = Math.floor(message.trim / number);
        }

        if(txPerClient < 1) {
            txPerClient = 1;
        }
        message.numb = txPerClient;
    } else if (message.txDuration) {
        // Run for time specified txDuration based on clients
        // Do nothing, we run for the time specified within message.txDuration
    } else {
        throw new Error('Unconditioned transaction rate driving mode');
    }

    message.clients = number;

    let promises = [];
    let idx = 0;
    for(let id in processes) {
        let client = processes[id];
        let p = new Promise((resolve, reject) => {
            client.obj.promise = {
                resolve: resolve,
                reject:  reject
            };
        });
        promises.push(p);
        client.results = results;
        client.updates = updates;
        message.clientargs = clientArgs[idx];
        message.clientIdx = idx;

        if(totalTx % number !== 0 && idx === number-1){
            message.numb = totalTx - txPerClient*(number - 1);
        }

        // send message to client and update idx
        client.obj.send(message);
        idx++;
    }

    await Promise.all(promises);
    // clear promises
    for(let client in processes) {
        delete client.promise;
    }
}
module.exports.startTest = startTest;

/**
 * Send message to all child processes
 * @param {JSON} message message
 * @return {Number} number of child processes
 */
function sendMessage(message) {
    for(let pid in processes) {
        processes[pid].obj.send(message);
    }
    return processes.length;
}
module.exports.sendMessage = sendMessage;

/**
 * Stop all test clients(child processes)
 */
function stop() {
    for(let pid in processes) {
        processes[pid].obj.kill();
    }
    processes = {};
}
module.exports.stop = stop;
