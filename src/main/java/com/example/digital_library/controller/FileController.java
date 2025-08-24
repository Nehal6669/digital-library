package com.example.digital_library.controller;

import com.example.digital_library.entity.Book;
import com.example.digital_library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private BookRepository bookRepository;

    private final Path uploadDir = Paths.get("uploads");

    // List all files
    @GetMapping
    public List<Book> getAllFiles() {
        return bookRepository.findAll();
    }

    // Upload file
    @PostMapping("/upload")
    public ResponseEntity<Book> uploadFile(@RequestParam("file") MultipartFile file,
                                           @RequestParam("title") String title,
                                           @RequestParam("author") String author,
                                           @RequestParam(value = "category", required = false) String category,
                                           @RequestParam(value = "totalPages", required = false) Integer totalPages) throws IOException {

        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        String filename = file.getOriginalFilename();
        Path targetPath = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        book.setTotalPages(totalPages != null ? totalPages : 0);
        book.setFilePath(filename);
        bookRepository.save(book);

        return ResponseEntity.ok(book);
    }

    // Read PDF inline
    @GetMapping("/read/{filename:.+}")
    public ResponseEntity<Resource> readFile(@PathVariable String filename) throws MalformedURLException {
        return serveFile(filename, false);
    }

    // Download PDF
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws MalformedURLException {
        return serveFile(filename, true);
    }

    private ResponseEntity<Resource> serveFile(String filename, boolean download) throws MalformedURLException {
        Path filePath = uploadDir.resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }

        String disposition = download ? "attachment" : "inline";
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition + "; filename=\"" + filename + "\"")
                .body(resource);
    }

    // Delete file
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) throws IOException {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) return ResponseEntity.notFound().build();

        Path filePath = uploadDir.resolve(book.getFilePath()).normalize();
        Files.deleteIfExists(filePath);

        bookRepository.delete(book);
        return ResponseEntity.ok().build();
    }
}
