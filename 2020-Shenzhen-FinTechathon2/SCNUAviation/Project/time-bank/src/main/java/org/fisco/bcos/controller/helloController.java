package org.fisco.bcos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class helloController {

    @RequestMapping("/hello")
    public ModelAndView hello() {

        ModelAndView mv = new ModelAndView();
        mv.setViewName("hello");
        mv.addObject("name", "hrh");
        mv.addObject("sex", "男");
        return mv;
    }

}
