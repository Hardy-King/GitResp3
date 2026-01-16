package com.gk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/*
* RestFUL风格
* 通过请求方式判断不同的逻辑，执行对应的方法
* 请求方式：
* get 查询
* post 新增
* put 修改
* delete 删除
* patch 更新
* */
@Controller
public class MyController2 {

    @RequestMapping("/user")
    public String test1(Integer id){
        System.out.println("id="+id);
        //请求转发
        return "hello";
    }

    /**
     * RESTFUL风格的URL
     * @param id
     * @return
     */
    @RequestMapping("/user/{id}")
    public String test2(@PathVariable Integer id){
        System.out.println("restful id="+id);
        //请求转发
        return "hello";
    }

    @DeleteMapping("/user/{id}")
    public String test3(@PathVariable Integer id){
        System.out.println("restful id="+id);
        //请求转发
        return "hello";
    }
}
