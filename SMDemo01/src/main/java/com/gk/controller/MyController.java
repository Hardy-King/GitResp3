package com.gk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * mycontroller 控制器，控制类
 */
@Controller//放入springmvc容器
@RequestMapping("mvc1")
public class MyController {

    @RequestMapping(value = {"/test1","/test01"})
    public String test1() {
        System.out.println("控制单元被访问！！");
        return "/first.jsp";
    }
}
