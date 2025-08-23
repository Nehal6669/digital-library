package com.example.digital_library.repository;

import com.example.digital_library.entity.ReadingProgress;
import com.example.digital_library.entity.Book;
import com.example.digital_library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ReadingProgressRepository extends JpaRepository<ReadingProgress, Long> {
    Optional<ReadingProgress> findByBookAndUser(Book book, User user);

    @Transactional
    void deleteByBook(Book book);
}
