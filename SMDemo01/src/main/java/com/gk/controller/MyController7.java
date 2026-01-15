package com.gk.controller;

import com.gk.pojo.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Date;

/** 请求转发 返回值String，View，ModelAndView
 * mycontroller 控制器，控制类
 */
@Controller//放入springmvc容器
public class MyController7 {

    @RequestMapping("/demo71")
    public String demo01() {

        //转发
        return "forward:/first.jsp";
    }

    @RequestMapping("/demo72")
    public String demo02() {

        //重定向
        return "redirect:/first.jsp";
    }
    @RequestMapping("/demo73")
    public View demo03() {

        View v = new InternalResourceView("/first.jsp");
        //转发
        return v;
    }

    @RequestMapping("/demo74")
    public View demo04(HttpServletRequest request) {

        View v = new RedirectView(request.getContextPath() + "/first.jsp");
        //转发
        return v;
    }
    @RequestMapping("/demo75")
    public ModelAndView demo05(HttpServletRequest request) {

        ModelAndView mv = new ModelAndView();
        mv.setView(new InternalResourceView("/first.jsp"));
        //转发
        return mv;
    }
    @RequestMapping("/demo76")
    public ModelAndView demo06(HttpServletRequest request) {

        ModelAndView mv = new ModelAndView();
        mv.setView(new RedirectView(request.getContextPath() + "/first.jsp"));
        //转发
        return mv;
    }

}
