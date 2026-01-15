package com.gk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MyController {

    @RequestMapping("/test1")
    public String test1(){
        //请求转发
        return "hijsp";
    }

    @RequestMapping("/test4")
    public String test4(){
        //请求转发
        return "forward:/index.jsp";
    }
}
