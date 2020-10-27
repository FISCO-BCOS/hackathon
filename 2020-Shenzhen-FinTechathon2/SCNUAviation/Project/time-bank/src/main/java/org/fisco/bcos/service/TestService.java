package org.fisco.bcos.service;

import org.fisco.bcos.model.P2PEntry;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service("testService")
public class TestService {

    public ArrayList<P2PEntry> getP2PEntries() {

        ArrayList<P2PEntry> p2pEntries = new ArrayList<>();

//        P2PEntry s1 = new P2PEntry();
//        s1.setName("run hua");
//        s1.setNum("10");
//
//        Student s2 = new Student();
//        s2.setName("jun jing");
//        s2.setNum("20");
//
//        students.add(s1);
//        students.add(s2);
        return p2pEntries;
    }

}
