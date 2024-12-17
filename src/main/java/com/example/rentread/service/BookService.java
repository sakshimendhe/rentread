package com.example.rentread.service;

import com.example.rentread.model.Book;
import com.example.rentread.model.AvailabilityStatus;
import com.example.rentread.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBook(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new RuntimeException("Book not found"));
    }

    public Book createBook(Book book) {
        book.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        return bookRepository.save(book);
    }

    public Book updateBook(Long id, Book book) {
        Book existingBook = getBook(id);
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setGenre(book.getGenre());
        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long id) {
        Book existingBook = getBook(id);
        bookRepository.delete(existingBook);
    }
}
