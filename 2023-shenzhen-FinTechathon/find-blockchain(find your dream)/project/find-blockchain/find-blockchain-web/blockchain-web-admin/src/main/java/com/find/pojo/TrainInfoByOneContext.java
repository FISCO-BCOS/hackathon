package com.find.pojo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description:
 * Author: Su
 * Date: 2023/11/6
 */

@Service
public class TrainInfoByOneContext {

    private static ConcurrentHashMap<String, TrainInfo> TrainInfoHashMap = new ConcurrentHashMap<>();

    public void putNode(String name, TrainInfo trainInfo) {
        TrainInfoHashMap.put(name, trainInfo);
    }

    public TrainInfo getNode(String name) {
        return TrainInfoHashMap.get(name);
    }

    public List<String> getAllNode() {

        ArrayList<String> nodeIdList = new ArrayList<>();
        for (Map.Entry<String, TrainInfo> FlNodes: TrainInfoHashMap.entrySet()) {
            nodeIdList.add(FlNodes.getValue().getNodeId());
        }
        return nodeIdList;
    }

    public void removeAllNode(){
        TrainInfoHashMap.clear();
    }

    //移除某个节点
    public void removeNodeById(String ndoeId){
        TrainInfoHashMap.remove(ndoeId);
    }

    public JSONArray getJsonNodeList(int page, int limit) {

        JSONArray array = new JSONArray();

        Set<Map.Entry<String, TrainInfo>> entries = TrainInfoHashMap.entrySet();
        Iterator<Map.Entry<String, TrainInfo>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, TrainInfo> next = iterator.next();
            TrainInfo trainInfo = next.getValue();
            array.add(JSONObject.toJSON(trainInfo));
        }

//        System.out.println("array size is "+String.valueOf(array.size()));

//        System.out.println("onelinemap size is "+String.valueOf(onlineNodeHashMap.size()));
        if ((page-1)*limit+limit<=TrainInfoHashMap.size()){


            return new JSONArray(array.subList((page-1)*limit,(page-1)*limit+limit));
        }else if ((page-1)*limit<TrainInfoHashMap.size()){
            return new JSONArray(array.subList((page-1)*limit,TrainInfoHashMap.size()));
        }
        else {
            return null;
        }

    }


    public int getSize() {
        return TrainInfoHashMap.size();
    }
}
