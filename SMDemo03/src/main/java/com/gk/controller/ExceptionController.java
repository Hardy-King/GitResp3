package com.gk.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class ExceptionController {

    @RequestMapping("/testexc")
    public String test(Map map) {
        System.out.println("test exception...");
        int a = 5 / 0; // 故意制造异常
        return "/wh.jsp";
    }
    // 解决方式1. 加入下面方法（异常处理器），如果出现该异常，就走入error.jsp页面
    @ExceptionHandler(value = {ArithmeticException.class, NullPointerException.class})
    public String myexception(){
        return "redirect:/error.jsp";
    }


}
