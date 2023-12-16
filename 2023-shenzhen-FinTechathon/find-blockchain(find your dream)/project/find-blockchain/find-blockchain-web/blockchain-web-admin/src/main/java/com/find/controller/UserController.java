package com.find.controller;

import com.find.annotation.VerifyParam;
import com.find.dao.AccountDao;
import com.find.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    AccountDao accountDao;

    @PostMapping("/login")
    public String login(HttpServletRequest request, @RequestParam("username") @VerifyParam(required = true,NotNull = true) String username,
                        @RequestParam("password") @VerifyParam(required = true,NotNull = true) String password) {
        // {username: "admin", password: "111111"}

        //根据账号查看数据库中的用户信息，判断是否匹配
        User user = accountDao.selectUser(username);
        if(user != null && password.equals(user.getPassword())){
            HttpSession session = request.getSession();
            //todo:对密码进行加密
            session.setAttribute("login", password);
            return "redirect:/home";
        }

        return "redirect:/index";

    }
}
