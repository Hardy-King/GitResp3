package com.gk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Map;

@Controller
public class ExceptionController2 {

    @RequestMapping("/testexc2")
    public String test(Map map) throws IOException {
        System.out.println("test exception2...");
        int a = 5 / 0; // 故意制造异常
        return "/wh.jsp";
    }

    /**
     * 解决方式3基于配置文件的全局异常处理
     * @param map
     * @return
     * @throws IOException
     */
    @RequestMapping("/testexc3")
    public String test3(Map map) throws IOException {
        System.out.println("test exception3...");
        int a = 5 / 0; // 故意制造异常
        return "/wh.jsp";
    }

    /**
     * 解决方式4：根据状态码跳转到指定页面
     * @param map
     * @return
     * @throws IOException
     */
    @RequestMapping("/testexc4")
    public String test4(Map map) throws IOException {
        System.out.println("test exception3...");
        int a = 5 / 0; // 故意制造异常
        return "/wh.jsp";
    }



}
