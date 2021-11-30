package com.media.service.imp;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.media.service.SmsSendService;
import com.media.utils.Constants;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YZR
 * @date 2020/11/6 9:45
 */

@Service
@Transactional
public class SmsSendImp implements SmsSendService {

    @Override
    public boolean sendCode(BigInteger phoneNumber, String templateCode, int code) {
        Map<String, Object> codeMap = new HashMap<>();
        codeMap.put("code", code);
        //连接阿里云
        DefaultProfile profile = DefaultProfile.getProfile(
                Constants.SMS_REGION_ID,
                Constants.SMS_ACCESS_KEY_ID,
                Constants.SMS_ACCESS_KEY_SECRET);
        IAcsClient client = new DefaultAcsClient(profile);
        //构建请求
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        // 下面的set固定
        request.setDomain(Constants.SMS_REQUEST_DOMAIN);
        request.setVersion(Constants.SMS_REQUEST_VERSION);
        request.setAction(Constants.SMS_REQUEST_ACTION);
        //自定义参数(手机号,签名,模板,验证码)
        request.putQueryParameter("PhoneNumbers", phoneNumber + "");
        request.putQueryParameter("SignName", Constants.SMS_SIGN_NAME);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", JSON.toJSONString(codeMap));
        try {
            CommonResponse response = client.getCommonResponse(request);
            String result = response.getData().substring(12, 14);
            return result.equals("OK");
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }

}
