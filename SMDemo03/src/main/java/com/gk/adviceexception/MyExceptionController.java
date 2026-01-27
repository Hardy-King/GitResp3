package com.gk.adviceexception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
/**
 * 解决方式2.全局配置 -使用@ControllerAdvice注解方式
 * 需要在springmvc.xml中配置组件扫描：
 *  <context:component-scan base-package="com.gk.controller,com.gk.adviceexception"/>
 */
@ControllerAdvice
public class MyExceptionController {
    @ExceptionHandler(value = {ArithmeticException.class, NullPointerException.class})
    public String myexception(){
        return "/error.jsp";
    }
}
