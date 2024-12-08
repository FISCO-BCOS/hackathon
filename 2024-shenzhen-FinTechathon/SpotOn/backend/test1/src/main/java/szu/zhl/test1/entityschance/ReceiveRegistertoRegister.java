package szu.zhl.test1.entityschance;

import szu.zhl.test1.entity.ReceiveRegisterData;
import szu.zhl.test1.entity.RegisterData;

import java.util.List;

public class ReceiveRegistertoRegister {
    public static RegisterData convert(ReceiveRegisterData receiveData, List<String> oldcmt) {
        RegisterData registerData = new RegisterData();

        // 设置 ID
        registerData.setId(receiveData.getId());

        // 设置 oldfaceinfo
        registerData.setOldfaceinfo(receiveData.getOldfaceinfo());

        // 设置 randomnumbers
        registerData.setRandomnumbers(receiveData.getRandomnumbers());

        // 设置 commitment
        registerData.setCommitment(receiveData.getCommitment());

        // 设置 oldcmt (假设需要从其他来源获取或初始化为空)
        registerData.setOldcmt(oldcmt); // 或者根据需要初始化

        return registerData;
    }
}
