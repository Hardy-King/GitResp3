package com.gk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * mycontroller 控制器，控制类
 */
@Controller//放入springmvc容器
public class MyController3 {

    @RequestMapping(value = {"/mc3"},method = {RequestMethod.POST,RequestMethod.DELETE})
    public String test1() {
        System.out.println("控制单元被访问！！controller03");
        return "/first.jsp";
    }

    @RequestMapping(value = {"/mc4"},params = {"name"})
    public String test2() {
        System.out.println("控制单元被访问！！controller03");
        return "/first.jsp";
    }

    @RequestMapping(value = {"/mc5"},headers = {"Accept-Language"})
    public String test3() {
        System.out.println("控制单元被访问！！controller03");
        return "/first.jsp";
    }
}
