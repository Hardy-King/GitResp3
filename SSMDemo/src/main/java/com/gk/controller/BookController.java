package com.gk.controller;

import com.gk.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class BookController {

    @Autowired
    private BookService bookService;

    @RequestMapping("/findAllBooks")
    @ResponseBody
    public List findAll(){
        List list = bookService.findAll();
        return list;
    }
}
