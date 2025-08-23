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
import java.net.URLDecoder;
import java.nio.file.*;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:3000") // allow React frontend
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
        book.setFilePath(filename); // store only filename
        bookRepository.save(book);

        return ResponseEntity.ok(book);
    }

    // Download file (fixed to handle spaces and special characters)
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws MalformedURLException {
        try {
            // Decode URL-encoded filename
            String decodedFilename = URLDecoder.decode(filename, "UTF-8");

            Path filePath = uploadDir.resolve(decodedFilename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + decodedFilename + "\"")
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
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
