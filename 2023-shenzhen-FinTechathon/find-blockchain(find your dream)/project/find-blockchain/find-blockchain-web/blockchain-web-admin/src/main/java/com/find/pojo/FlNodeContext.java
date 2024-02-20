package com.find.pojo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.find.pojo.FlNode;
import com.find.service.PlotService;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Getter
@Setter
public class FlNodeContext {

    private static ConcurrentHashMap<String, FlNode> nodeHashMap = new ConcurrentHashMap<>();
    private static Map<Integer, List<Float>> nodeAccMap = new HashMap<>();

    @Autowired
    PlotService plotService;

    public void putNode(String name, FlNode flNode) throws IOException {

        String cliIdNum = flNode.getId().substring(flNode.getId().length()-1);
        int cliId=Integer.parseInt(cliIdNum);

//        System.out.println("cliID in putNode is :"+cliId);

        List<Float> list = nodeAccMap.get(cliId);
        if (list == null) {
            list = new ArrayList<>();
            list.add(flNode.getAccuracy());
        } else {
            list.add(flNode.getAccuracy());
        }
        nodeAccMap.put(cliId, list);

        nodeHashMap.put(name, flNode);

        try {
            plotService.getAccGraphById(cliIdNum, getAccListById(cliId));
        } catch (PythonExecutionException e) {
            e.printStackTrace();
        }
    }

    public FlNode getNode(String name) {
        return nodeHashMap.get(name);
    }

    public JSONArray getJsonNodeList(int page,int limit) {

        JSONArray array = new JSONArray();

        Set<Map.Entry<String, FlNode>> entries = nodeHashMap.entrySet();
        Iterator<Map.Entry<String,  FlNode>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String,  FlNode> next = iterator.next();
            FlNode flNode = next.getValue();
            array.add(JSONObject.toJSON(flNode));
        }


//        System.out.println("array size is "+String.valueOf(array.size()));

//        System.out.println("nodeHashMap size is "+String.valueOf(nodeHashMap.size()));
        if ((page-1)*limit+limit<=nodeHashMap.size()){

            return new JSONArray(array.subList((page-1)*limit,(page-1)*limit+limit));
        }else if ((page-1)*limit<nodeHashMap.size()){
            return new JSONArray(array.subList((page-1)*limit,nodeHashMap.size()));
        }
        else {
            return null;
        }

//        return array;
    }


    public int getSize() {
        return nodeHashMap.size();
    }

    public static List<Float> getAccListById(int cliId){
//        for (int i = 0; i < 3; i++) {
//            System.out.println(allAccList.get(i).size());
//        }
//        System.out.println("length of nodeAccMap is :"+ nodeAccMap.size());
//        System.out.println("cliID in getAccListByID is : "+cliId);
        List<Float> list = nodeAccMap.get(cliId);
//        System.out.println("nodeAccMap is : "+nodeAccMap);
//        System.out.println("cliId:" + cliId + "list:" + list);
        return list;
    }

    public void removeFlNodeContext(){
        nodeHashMap.clear();
        nodeAccMap.clear();
    }

}

