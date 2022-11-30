package com.zgxt.springbootdemo.dao;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zgxt.springbootdemo.utils.HttpUtils;
import org.springframework.stereotype.Service;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Binge
 * @desc webase-front 交互逻辑层类
 * @date 2022/9/21 8:46
 */
@Service
public class WeBaseFrontDao {
    public JSONObject setNumber(int number) {
        ArrayList funcParam = new ArrayList();
        funcParam.add(number);
        JSONObject result = HttpUtils.writeContract("setNumber",funcParam);
        System.out.println(result);
        return result;
    }
    public JSONArray getNumber() {
        JSONArray result = HttpUtils.readContract("getNumber");
        System.out.println(result);
        return result;
    }
    public JSONArray getOwnerAddress() {
        JSONArray result = HttpUtils.readContract("getOwnerAddress");
        System.out.println(result);
        return result;
    }
}
