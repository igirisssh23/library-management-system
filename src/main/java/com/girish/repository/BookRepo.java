package com.girish.repository;

import java.sql.Connection;
import java.util.List;

import com.girish.model.Book;

public interface BookRepo {
	
	    void add(Book book);
	    List<Book> findAll();
	    List<Book> search(String keyword);

	    boolean decrementCopies(Connection con, int bookId);
	    boolean incrementCopies(Connection con, int bookId);
	}


