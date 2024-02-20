package com.find.fiscoshard1;

import com.find.NodeSecurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class FiscoShard1ApplicationTests {

    @Autowired
    NodeSecurService nodeSecurService;

    @Test
    void contextLoads() {
    }

    @Test
    public void validation(){

        List<String> nodeIdList = new ArrayList<>();
        nodeIdList.add("client1");
        nodeIdList.add("client2");
        nodeIdList.add("client3");
        nodeIdList.add("client4");
        nodeIdList.add("client5");
        nodeIdList.add("client6");
        System.out.println(nodeIdList);
        List<String> validation = nodeSecurService.validation(nodeIdList);
        System.out.println(validation.toString());
    }

}
