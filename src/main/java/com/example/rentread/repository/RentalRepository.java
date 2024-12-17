package com.example.rentread.repository;

import com.example.rentread.model.Rental;
import com.example.rentread.model.User;
import com.example.rentread.model.Book;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    Optional<Rental> findByUserAndBook(User user, Book book);
}
