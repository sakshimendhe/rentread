package com.example.rentread.service;

import com.example.rentread.model.Book;
import com.example.rentread.model.AvailabilityStatus;
import com.example.rentread.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        Book book1 = new Book(1L, "Title1", "Author1", "Genre1", AvailabilityStatus.AVAILABLE);
        Book book2 = new Book(2L, "Title2", "Author2", "Genre2", AvailabilityStatus.RENTED);

        when(bookRepository.findAll()).thenReturn(Arrays.asList(book1, book2));

        List<Book> books = bookService.getAllBooks();

        assertNotNull(books);
        assertEquals(2, books.size());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void testGetBook_Success() {
        Book book = new Book(1L, "Title1", "Author1", "Genre1", AvailabilityStatus.AVAILABLE);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Book result = bookService.getBook(1L);

        assertNotNull(result);
        assertEquals("Title1", result.getTitle());
        assertEquals("Author1", result.getAuthor());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testGetBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> bookService.getBook(1L));
        assertEquals("Book not found", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateBook() {
        Book book = new Book(null, "New Title", "New Author", "New Genre", null);

        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> {
            Book savedBook = invocation.getArgument(0);
            savedBook.setId(1L);
            return savedBook;
        });

        Book createdBook = bookService.createBook(book);

        assertNotNull(createdBook);
        assertEquals(1L, createdBook.getId());
        assertEquals(AvailabilityStatus.AVAILABLE, createdBook.getAvailabilityStatus());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void testUpdateBook() {
        Book existingBook = new Book(1L, "Old Title", "Old Author", "Old Genre", AvailabilityStatus.AVAILABLE);
        Book updatedBook = new Book(null, "Updated Title", "Updated Author", "Updated Genre", null);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book result = bookService.updateBook(1L, updatedBook);

        assertNotNull(result);
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Author", result.getAuthor());
        assertEquals("Updated Genre", result.getGenre());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(existingBook);
    }

    @Test
    void testDeleteBook() {
        Book existingBook = new Book(1L, "Title", "Author", "Genre", AvailabilityStatus.AVAILABLE);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(existingBook));
        doNothing().when(bookRepository).delete(existingBook);

        
        bookService.deleteBook(1L);

        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).delete(existingBook);
    }

    @Test
    void testDeleteBook_NotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> bookService.deleteBook(1L));
        assertEquals("Book not found", exception.getMessage());
        verify(bookRepository, times(1)).findById(1L);
    }
}
