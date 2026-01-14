package com.gk.controller;

import com.gk.pojo.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

/**
 * mycontroller 控制器，控制类
 */
@Controller//放入springmvc容器
public class MyController4 {

    @RequestMapping("/testform")// 当前方法的映射路径
    public String demo01(HttpServletRequest req){
        String name = req.getParameter("name");
        String pwd = req.getParameter("pwd");
        String sex = req.getParameter("sex");
        String[] hobbies = req.getParameterValues("hobby");
        System.out.println(name + "---" + pwd + "---" + sex + "---" + Arrays.toString(hobbies));
        // 跳转到index.jsp中
        return "/index.jsp";
    }

    /**
     * String[] hobby 也可以使用list集合 需要指定属性名字 @RequestParam("hobby")
     * @param age
     * @param name
     * @param pwd
     * @param sex
     * @return
     */
    @RequestMapping("/testform2")// 当前方法的映射路径
    public String demo02(@RequestParam(defaultValue = "18")Integer age, @RequestParam("uname") String name, String pwd, String sex, /*String[] hobby*/@RequestParam("hobby") List hobby){
        System.out.println(name + "---" + age + "---" + pwd + "---" + sex + "---" + hobby);
        // 跳转到index.jsp中
        return "/index.jsp";
    }

    @RequestMapping("/testform3")// 当前方法的映射路径
    public String demo03(@RequestParam(required = true) String name){
        System.out.println(name);
        // 跳转到index.jsp中
        return "/index.jsp";
    }

    @RequestMapping("/test6")
    public String test6(User user){
        System.out.println(user);
        return "/index.jsp";
    }
}
