package com.gk.service;

import com.gk.pojo.Book;

import java.util.List;

public interface BookService {
    public abstract List<Book> findAll();
}
