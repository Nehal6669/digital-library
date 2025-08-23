package com.example.digital_library.controller;

import com.example.digital_library.entity.Book;
import com.example.digital_library.entity.ReadingProgress;
import com.example.digital_library.entity.User;
import com.example.digital_library.repository.BookRepository;
import com.example.digital_library.repository.ReadingProgressRepository;
import com.example.digital_library.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
@CrossOrigin(origins = "http://localhost:3000")
public class ReadingProgressController {

    private final ReadingProgressRepository progressRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public ReadingProgressController(ReadingProgressRepository progressRepository,
                                     BookRepository bookRepository,
                                     UserRepository userRepository) {
        this.progressRepository = progressRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{bookId}/{userId}")
    public ResponseEntity<ReadingProgress> getProgress(@PathVariable Long bookId,
                                                       @PathVariable Long userId) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        ReadingProgress progress = progressRepository.findByBookAndUser(book, user).orElse(new ReadingProgress());
        return ResponseEntity.ok(progress);
    }

    @PostMapping("/")
    public ResponseEntity<ReadingProgress> updateProgress(@RequestBody ReadingProgress progressRequest) {
        Book book = bookRepository.findById(progressRequest.getBook().getId()).orElseThrow();
        User user = userRepository.findById(progressRequest.getUser().getId()).orElseThrow();

        ReadingProgress progress = progressRepository.findByBookAndUser(book, user)
                .orElse(new ReadingProgress());

        progress.setBook(book);
        progress.setUser(user);
        progress.setCurrentPage(progressRequest.getCurrentPage());
        progress.setTotalPages(progressRequest.getTotalPages());

        ReadingProgress saved = progressRepository.save(progress);
        return ResponseEntity.ok(saved);
    }
}
