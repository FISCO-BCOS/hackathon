package com.webank.webase.yidian;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateEntity {

    private String company_address;
    private String id;
    private String certificate_number;
    private String year;
    private String month;
    private String day;
    private String total_debit_amount;
    private String total_credit_amount;
    private String tabulator;
    private String company_id;
    private String create_timestamp;

    public boolean isIntact() {
        if (company_address == null || id == null || certificate_number == null || year == null || month == null || day == null || total_debit_amount == null || total_credit_amount == null || tabulator == null || company_id == null || create_timestamp == null || company_address == "" || id == "" || certificate_number == "" || year == "" || month == "" || day == "" || total_debit_amount == "" || total_credit_amount == "" || tabulator == "" || company_id == "" || create_timestamp == "") {
            return false;
        } else {
            return true;
        }
    }

    public String submit() {
        JsonArray listArray = new JsonArray();
//        final JsonObject object = new JsonObject();
//        for (int i = 0; i < 5; i++) {
//            final JsonObject obj = new JsonObject();
//            for (int n = 0; n < 3; n++) {
//                obj.addProperty("col" + n, "val" + i + n);
//            }
//            listArray.add(obj);
//        }
        listArray.add(id);
        listArray.add(certificate_number);
        listArray.add(year);
        listArray.add(month);
        listArray.add(day);
        listArray.add(total_debit_amount);
        listArray.add(total_credit_amount);
        listArray.add(tabulator);
        listArray.add(company_id);
        listArray.add(create_timestamp);

        JsonArray res = new JsonArray();
        res.add(company_address);
        res.add(listArray);

        System.out.println("CertificateEntity Submit String:" + res);

        return res.toString();
    }


}
