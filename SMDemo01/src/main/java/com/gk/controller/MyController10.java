package com.gk.controller;

import com.gk.pojo.Clazz;
import com.gk.pojo.MyUser;
import com.gk.pojo.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 *
 *
 */
@Controller//放入springmvc容器
//@RestController//返回的文本，如果是重定向或者转发到某个页面不能在此处用RestController
public class MyController10 {

    @RequestMapping(value = "/demo101", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String demo01(@RequestBody MyUser myUser) {
        System.out.println(myUser);
        return "ok";
    }
}
