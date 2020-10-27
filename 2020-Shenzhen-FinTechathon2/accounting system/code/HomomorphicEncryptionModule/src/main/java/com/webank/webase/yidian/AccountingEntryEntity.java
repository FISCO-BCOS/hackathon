package com.webank.webase.yidian;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountingEntryEntity {

    private int num;

    private String company_address;
    private String[] id;
    private String[] certificate_id;
    private String[] actual_certificate_id;
    private String[] abstraction;
    private String[] accounting_title_serial_number;
    private String[] accounting_title_name;
    private String[] accounting_entry_type;
    private String[] spent_accounting_entry_id;
    private String[] original_accounting_entry_id;
    private String[] debit_amount;
    private String[] credit_amount;
    private String[] original_document_hash;
    private String[] company_id;
    private String[] is_unspent;
    private String[] spent_timestamp;
    private String[] spent_for_certificate_id;
    private String[] create_timestamp;

    public String step_first(){

        JsonArray res = new JsonArray();
        res.add(company_address);
        res.add(stringArrayToJson(id));
        res.add(stringArrayToJson(certificate_id));
        res.add(stringArrayToJson(accounting_title_serial_number));
        res.add(stringArrayToJson(accounting_title_name));
        res.add(stringArrayToJson(accounting_entry_type));
        res.add(stringArrayToJson(debit_amount));
        res.add(stringArrayToJson(credit_amount));

        System.out.println("AccountingEntryEntity First-Submit String:"+res);

        return res.toString();
    }

    public String step_second(){

        JsonArray res = new JsonArray();
        res.add(company_address);
        res.add(stringArrayToJson(id));
        res.add(stringArrayToJson(actual_certificate_id));
        res.add(stringArrayToJson(abstraction));
        res.add(stringArrayToJson(spent_accounting_entry_id));
        res.add(stringArrayToJson(original_accounting_entry_id));
        res.add(stringArrayToJson(original_document_hash));

        System.out.println("AccountingEntryEntity Second-Submit String:"+res);

        return res.toString();
    }

    public String step_third(){

        JsonArray res = new JsonArray();
        res.add(company_address);
        res.add(stringArrayToJson(id));
        res.add(stringArrayToJson(company_id));
        res.add(stringArrayToJson(is_unspent));
        res.add(stringArrayToJson(spent_timestamp));
        res.add(stringArrayToJson(spent_for_certificate_id));
        res.add(stringArrayToJson(create_timestamp));

        System.out.println("AccountingEntryEntity Third-Submit String:"+res);

        return res.toString();
    }

    public static JsonArray stringArrayToJson(String[] strArray){
        JsonArray listArray = new JsonArray();
        for(int i = 0; i< strArray.length; i++){
            listArray.add(strArray[i]);
        }
        return listArray;
    }

    public static void main(String[] args) {
        String[] arr_String  = {"a","b","c"};

        JsonArray res = new JsonArray();
        res.add(stringArrayToJson(arr_String));

        System.out.println("String:"+res);
    }
}
