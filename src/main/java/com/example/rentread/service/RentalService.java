package com.example.rentread.service;

import com.example.rentread.model.Book;
import com.example.rentread.model.Rental;
import com.example.rentread.model.User;
import com.example.rentread.repository.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    public Optional<Rental> findByUserAndBook(User user, Book book) {
        return rentalRepository.findByUserAndBook(user, book);
    }

    public Rental rentBook(User user, Book book) {
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setBook(book);
        rental.setRentedAt(LocalDateTime.now());
        return rentalRepository.save(rental);
    }

    public void returnBook(Rental rental) {
        rental.setReturnedAt(LocalDateTime.now());
        rentalRepository.save(rental);
    }
}
