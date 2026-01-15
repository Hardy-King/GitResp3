package com.gk.controller;

import com.gk.pojo.Clazz;
import com.gk.pojo.Student;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @ResponseBody 响应普通文本
 *
 */
//@Controller//放入springmvc容器
@RestController//返回的文本，如果是重定向或者转发到某个页面不能在此处用RestController
public class MyController9 {

    @RequestMapping(value = "/demo91", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String demo01() {

        return "/first.jsp,你是谁？";
    }

    @RequestMapping(value = "/demo92", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String demo02() {
        Clazz  clazz = new Clazz();
        clazz.setCid(1001);
        clazz.setCname("test");
        return clazz.toString();
    }

    @RequestMapping(value = "/demo93")
    public Student demo03() {
        Student s = new Student();
        s.setId(17);
        s.setName("丽丽");
        s.setDate(new Date());
        return s;
    }
}
