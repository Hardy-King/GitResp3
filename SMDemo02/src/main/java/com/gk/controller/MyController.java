package com.gk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceView;

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

    // 新增：POST 示例接口，不修改原有方法  GitHub Copilot Test
    @RequestMapping("/submit")
    @ResponseBody
    public String submit(@RequestParam(required = false) String name, Model model){
        // 将 name 放入 model（如果有），返回视图名 "result"
        if (name != null) {
            model.addAttribute("name", name);
        }
        return "result";
    }

    @RequestMapping("/test2")
    public String test2(){

        //重定向
        return "hello";
    }

    @RequestMapping("/test3")
    public View test3(){
        View view = new InternalResourceView("/WEB-INF/jsp/hello.jsp");

        //返回视图对象
        return view;
    }
}
