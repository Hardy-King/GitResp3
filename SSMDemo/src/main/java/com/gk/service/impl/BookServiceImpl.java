package com.gk.service.impl;

import com.gk.mapper.BookMapper;
import com.gk.pojo.Book;
import com.gk.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookMapper bookMapper;

    @Override
    public List<Book> findAll() {
        return bookMapper.selectAll();
    }
}
