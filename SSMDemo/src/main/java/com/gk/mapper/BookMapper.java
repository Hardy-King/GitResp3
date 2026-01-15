package com.gk.mapper;

import com.gk.pojo.Book;

import java.util.List;

public interface BookMapper {
    public abstract List<Book> selectAll();
}
