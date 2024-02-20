package com.find.pojo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class OnlineNodeContext {

    private static ConcurrentHashMap<String, OnlineNode> onlineNodeHashMap = new ConcurrentHashMap<>();

    public void putNode(String name, OnlineNode onlineNode) {
        onlineNodeHashMap.put(name, onlineNode);
    }

    public OnlineNode getNode(String name) {
        return onlineNodeHashMap.get(name);
    }

    public List<String> getAllNode() {

        ArrayList<String> nodeIdList = new ArrayList<>();
        for (Map.Entry<String, OnlineNode> FlNodes: onlineNodeHashMap.entrySet()) {
            nodeIdList.add(FlNodes.getValue().getNodeId());
        }
        return nodeIdList;
    }

    public void removeAllNode(){
        onlineNodeHashMap.clear();
    }

    //移除某个节点
    public void removeNodeById(String ndoeId){
        onlineNodeHashMap.remove(ndoeId);
    }

    public JSONArray getJsonNodeList(int page,int limit) {

        JSONArray array = new JSONArray();

        Set<Map.Entry<String, OnlineNode>> entries = onlineNodeHashMap.entrySet();
        Iterator<Map.Entry<String, OnlineNode>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, OnlineNode> next = iterator.next();
            OnlineNode onlineNode = next.getValue();
            array.add(JSONObject.toJSON(onlineNode));
        }

//        System.out.println("array size is "+String.valueOf(array.size()));

//        System.out.println("onelinemap size is "+String.valueOf(onlineNodeHashMap.size()));
        if ((page-1)*limit+limit<=onlineNodeHashMap.size()){


            return new JSONArray(array.subList((page-1)*limit,(page-1)*limit+limit));
        }else if ((page-1)*limit<onlineNodeHashMap.size()){
            return new JSONArray(array.subList((page-1)*limit,onlineNodeHashMap.size()));
        }
        else {
            return null;
        }

    }


    public int getSize() {
        return onlineNodeHashMap.size();
    }

}
