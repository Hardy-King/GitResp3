package com.gk.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class MyController {

    @RequestMapping("/test")
    public String test(Map map) {
        System.out.println("进入MyController的test方法");
        /**
         * 模拟数据，从数据查询数据
         * 将模拟的数据放入到request
         */
        map.put("msg", "Hello,SpringMVC,TMD");

        return "/wh.jsp";
    }
    @RequestMapping("/test1")
    public String test1(HttpServletRequest request) {
        System.out.println("进入MyController的test1方法");
        /**
         * 模拟数据，从数据查询数据
         * 将模拟的数据放入到request
         */
        request.setAttribute("msg", "Hello,SpringMVC,CAO");

        return "/index.jsp";
    }

    @RequestMapping("/test2")
    public String test2(HttpServletRequest request) {
        System.out.println("进入MyController的test2方法");


        return "/index.jsp";
    }


}
