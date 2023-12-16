package com.find.fiscoshard2.contract; /**
 * Copyright 2014-2020 [fisco-dev]
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */


import org.fisco.bcos.sdk.model.JsonRpcResponse;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class PerformanceCollector {
    private static Logger logger = LoggerFactory.getLogger(PerformanceCollector.class);
    private AtomicLong less50 = new AtomicLong(0);
    private AtomicLong less100 = new AtomicLong(0);
    private AtomicLong less200 = new AtomicLong(0);
    private AtomicLong less400 = new AtomicLong(0);
    private AtomicLong less1000 = new AtomicLong(0);
    private AtomicLong less2000 = new AtomicLong(0);
    private AtomicLong timeout2000 = new AtomicLong(0);
    private AtomicLong totalCost = new AtomicLong(0);

    private AtomicInteger total = new AtomicInteger(0);
    private AtomicInteger received = new AtomicInteger(0);
    private AtomicInteger error = new AtomicInteger(0);
    private Long startTimestamp = System.currentTimeMillis();

    public void resetStartTimestamp(){startTimestamp = System.currentTimeMillis();}

    public void resetCounter(){
        less50.set(0);
        less100.set(0);
        less200.set(0);
        less400.set(0);
        less1000.set(0);
        less2000.set(0);
        timeout2000.set(0);
        totalCost.set(0);

        total.set(0);
        received.set(0);
        error.set(0);
    }

    public Integer getTotal() {
        return total.get();
    }

    public void setTotal(Integer total) {
        this.total.set(total);
    }

    public void addTotal() {
        this.total.incrementAndGet();
    }

    public Integer getReceived() {
        return received.get();
    }

    public void setReceived(Integer received) {
        this.received.getAndSet(received);
    }

    public void onRpcMessage(JsonRpcResponse response, Long cost) {
        try {
            boolean errorMessage = false;
            if (response.getError() != null && response.getError().getCode() != 0) {
                logger.warn("receive error jsonRpcResponse: {}", response.toString());
                errorMessage = true;
            }
            stat(errorMessage, cost);

            System.out.println("onrpc");

        } catch (Exception e) {
            logger.error("onRpcMessage exception: {}", e.getMessage());
        }
    }

    public void onMessage(TransactionReceipt receipt, Long cost) {
        try {
            boolean errorMessage = false;
            if (!receipt.isStatusOK()) {
                logger.error(
                        "error receipt, status: {}, output: {}, message: {}",
                        receipt.getStatus(),
                        receipt.getOutput(),
                        receipt.getMessage());
                errorMessage = true;
            }
            stat(errorMessage, cost);
        } catch (Exception e) {
            logger.error("error:", e);
        }
    }

    public void stat(boolean errorMessage, Long cost) {
        if (errorMessage) {
            error.addAndGet(1);
        }
        received.incrementAndGet();
    }

    public void setStartTimestamp(Long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Long getStartTimestamp() {
        return startTimestamp;
    }

    public void addReceive() {
        received.incrementAndGet();
//        System.out.println(received.toString());
    }
}
