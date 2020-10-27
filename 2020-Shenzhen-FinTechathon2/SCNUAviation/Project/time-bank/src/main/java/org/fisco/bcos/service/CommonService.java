package org.fisco.bcos.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.fisco.bcos.model.P2PEntry;
import org.fisco.bcos.model.P2PInfo;
import org.fisco.bcos.web3j.crypto.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@Service
public class CommonService {
    @Autowired
    P2PTimeService p2PTimeService;
    @Autowired
    CredentialsService credentialsService;
    public String GoodIn2Json(ArrayList<P2PEntry> items) throws Exception {
        Credentials credentials=credentialsService.getCredentials("0x98333491efac02f8ce109b0c499074d47e7779a6.p12","123456");
        if (items == null) return "";
        JSONArray array = new JSONArray();
        JSONObject jsonObject = null;
        P2PEntry info = null;
        for (int i = items.size()-1; i >=9; i--) {
            info = items.get(i);
            p2PTimeService.load(credentials,info.getP2pAddress());
            P2PInfo p2PInfo=p2PTimeService.getP2PAllInfo();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String sd = sdf.format(Long.parseLong(String.valueOf(p2PInfo.getPublish_time())));
            String state="待接取";
            if(info.getState()== BigInteger.valueOf(0)) state="已接取";
            jsonObject = new JSONObject();
            jsonObject.put("region", "天河区");
            jsonObject.put("location", "华师社区卫生服务中心");
            jsonObject.put("price", info.getPrice().toString());
            jsonObject.put("content",info.getTitle());
            jsonObject.put("type",p2PInfo.getSort());
            jsonObject.put("ownner_address",info.getOwnerAddress());
            jsonObject.put("contract_address",info.getP2pAddress());
            jsonObject.put("state",state);
            jsonObject.put("service_date",info.getDescription());
            jsonObject.put("publish_date",sd);
            array.add(jsonObject);
        }
        return array.toString();
    }
}
