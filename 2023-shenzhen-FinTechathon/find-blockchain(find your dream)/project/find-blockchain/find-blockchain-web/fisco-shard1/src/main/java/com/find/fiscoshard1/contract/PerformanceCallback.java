package com.find.fiscoshard1.contract; /**
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



import org.fisco.bcos.sdk.v3.model.TransactionReceipt;
import org.fisco.bcos.sdk.v3.model.callback.TransactionCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformanceCallback extends TransactionCallback {

    private static Logger logger = LoggerFactory.getLogger(PerformanceCallback.class);
    private Long startTime = System.currentTimeMillis();

    private PerformanceCollector collector;

    public PerformanceCollector getCollector() {
        return collector;
    }

    public void setCollector(PerformanceCollector collector) {
        this.collector = collector;
    }

    public PerformanceCallback() {}

    @Override
    public void onResponse(TransactionReceipt receipt) {
        Long cost = System.currentTimeMillis() - startTime;
        try {
            collector.onMessage(receipt, cost);
            //System.out.println("onresponse");

        } catch (Exception e) {
            logger.error("onMessage error: ", e);
        }
    }


}
