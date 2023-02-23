package com.watcha.seleniumtest.Entity.repository;

import com.watcha.seleniumtest.Entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByBookTitleAndBookWriterAndBookPage(String title, String writer, String page);
}
