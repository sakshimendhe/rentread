package com.example.rentread.controller;

import com.example.rentread.model.Book;
import com.example.rentread.model.Rental;
import com.example.rentread.model.User;
import com.example.rentread.service.BookService;
import com.example.rentread.service.RentalService;
import com.example.rentread.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookRentalController {

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody User loginRequest) {
      
        User user = userService.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
    
        boolean isPasswordValid = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());
        System.out.println(isPasswordValid);
        if (!isPasswordValid) {
            throw new RuntimeException("Invalid email or password");
        }
    
        return "Login successful for user: " + user.getEmail();
    }
    

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping("/books")
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    @PutMapping("/books/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBook(id, book);
    }

    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }


    @PostMapping("/books/{bookId}/rent")
    public Rental rentBook(@PathVariable Long bookId, @RequestParam Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookService.getBook(bookId);

        return rentalService.rentBook(user, book);
    }

    @PostMapping("/books/{bookId}/return")
    public void returnBook(@PathVariable Long bookId, @RequestParam Long userId) {
        User user = userService.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookService.getBook(bookId);

        Rental rental = rentalService.findByUserAndBook(user, book)
                .orElseThrow(() -> new RuntimeException("Rental not found"));

        rentalService.returnBook(rental);
    }
}
