package com.webank.webase.yidian;

import com.google.gson.JsonArray;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpendUtxoEntity {

    private String company_address;
    private String company_id;
    private String[] id;
    private String is_unspent;
    private String spent_timestamp;
    private String spent_for_certificate_id;

    public String submit(){

        JsonArray res = new JsonArray();
        res.add(company_address);
        res.add(company_id);
        res.add(stringArrayToJson(id));
        res.add(is_unspent);
        res.add(spent_timestamp);
        res.add(spent_for_certificate_id);

        System.out.println("SpendUtxoEntity Submit String:"+res);

        return res.toString();
    }

    public static JsonArray stringArrayToJson(String[] strArray){
        JsonArray listArray = new JsonArray();
        for(int i = 0; i< strArray.length; i++){
            listArray.add(strArray[i]);
        }
        return listArray;
    }
}
