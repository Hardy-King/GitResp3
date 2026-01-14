package com.gk.controller;

import com.gk.pojo.Clazz;
import com.gk.pojo.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * mycontroller 控制器，控制类
 */
@Controller//放入springmvc容器
public class MyController6 {

    @RequestMapping("/demo01")
    public String demo01(@DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        System.out.println(date);
        return "/first.jsp";
    }
    @RequestMapping("/demo02")
    public String test6(User user){
        System.out.println(user);
        return "/index.jsp";
    }

}
