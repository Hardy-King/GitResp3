package com.gk.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

/**
 * 作用域
 *
 */
@Controller//放入springmvc容器
public class MyController8 {

    @RequestMapping("/demo81")
    public String demo01(HttpServletRequest request, HttpSession session) {
        request.setAttribute("reqmsg","req");
        session.setAttribute("seqmsg","seq");
        ServletContext servletContext = request.getServletContext();
        servletContext.setAttribute("appmsg","app");
        //转发
        return "/first.jsp";
    }

    @RequestMapping("/demo82")
    public String demo02(Map map) {
        map.put("reqmsg","req-82");
        return "/first.jsp";
    }
    @RequestMapping("/demo83")
    public String demo03(Model model) {
       model.addAttribute("reqmsg","req-83");
        return "/first.jsp";
    }
    @RequestMapping("/demo84")
    public String demo04(Model model) {
        Map map = new HashMap();
        map.put("reqmsg","req-84");
        model.addAllAttributes(map);
        return "/first.jsp";
    }



}
