package com.find;

import javax.annotation.Resource;
import java.util.List;

public interface NodeSecurService {

    public void createNodeAccount(String nodeId);

    //安全认证函数
    List<String> validation(List<String> nodeList);

    List<String> Upload_data(String data);

    public List<String> Transaction(List<String> nodeLIst);

    void queryNodeAccount();

}
