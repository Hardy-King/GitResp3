package com.gk.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public class MyInterceptor implements HandlerInterceptor {
    /*@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle");
        //controller方法执行前拦截，跳转到wh.jsp
        response.sendRedirect("/demo3/wh.jsp");
        return true;
    }*/
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
        System.out.println("postHandle");
        //对敏感词进行处理
        //取出数据
        Map<String, Object> map = modelAndView.getModel();
        String s = (String) map.get("msg");

        //替换输出敏感词
        if (s != null && (s.contains("TMD") || s.contains("CAO"))) {
            String newStr = s.replace("TMD", "***").replace("CAO", "***");
            map.put("msg", newStr);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);

        System.out.println("afterCompletion");
    }
}
