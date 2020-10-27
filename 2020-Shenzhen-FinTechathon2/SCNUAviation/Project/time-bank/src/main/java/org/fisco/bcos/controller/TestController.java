package org.fisco.bcos.controller;

import io.netty.util.internal.StringUtil;
import org.fisco.bcos.model.P2PEntry;
import org.fisco.bcos.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;


@Controller
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping("/test")
    public String test() {
        return "login";
    }

    @RequestMapping("/test/login")
    public String testLogin(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            Model model, HttpSession session) {

        if (!StringUtil.isNullOrEmpty(username)) {
            ArrayList<P2PEntry> p2pEntries = testService.getP2PEntries();
            model.addAttribute("p2pEntries", p2pEntries);

            return "testLogin";
        }else {
            model.addAttribute("msg", "请输入用户名");
            return "login";
        }

    }


}
