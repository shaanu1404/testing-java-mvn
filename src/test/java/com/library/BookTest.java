package com.library;

import com.library.model.Book;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void testBookCreation() {
        Book book = new Book(1, "Java Basics", "James", true);
        assertEquals("Java Basics", book.getTitle());
    }
}
