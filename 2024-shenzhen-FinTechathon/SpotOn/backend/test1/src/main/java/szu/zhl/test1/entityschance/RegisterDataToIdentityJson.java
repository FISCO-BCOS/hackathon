package szu.zhl.test1.entityschance;


import szu.zhl.test1.entity.IdentityJson;
import szu.zhl.test1.entity.RegisterData;

import java.util.ArrayList;

public class RegisterDataToIdentityJson {

    /**
     * 将 RegisterData 转换为 IdentityJson
     * @param registerData 注册数据
     * @return 转换后的 IdentityJson 对象
     */
    public static IdentityJson convert(RegisterData registerData) {
        IdentityJson identityJson = new IdentityJson();
        
        // 设置 ID
        identityJson.setId(registerData.getId());
        
        // 设置 oldfaceinfo
        identityJson.setOldfaceinfo(registerData.getOldfaceinfo());
        
        // 设置 randomnumbers
        identityJson.setRandomnumbers(registerData.getRandomnumbers());
        
        // 设置 commitment
        identityJson.setCommitment(registerData.getCommitment());
        
        // 设置 oldcmt
        identityJson.setOldcmt(registerData.getOldcmt());
        
          // 初始化 appnames 为空列表
        identityJson.setAppnames(new ArrayList<>()); // 或者使用 Collections.emptyList()

        
        return identityJson;
    }
}