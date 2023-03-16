package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Book;
import com.example.demo.service.BookService;

@RestController
public class BookController {
	
	@Autowired
	private BookService service;
	
	@PostMapping("/book")
    public Book saveBook(@RequestBody Book book) {
        return service.saveBook(book);
    }

    @GetMapping("/book")
    public List<Book> findBooks() {
        return service.getAllBooks();
    }


    @GetMapping("/book/{id}")
    public Book findBook(@PathVariable int id) throws Exception {
    	return service.getBookById(id);
    }

}
