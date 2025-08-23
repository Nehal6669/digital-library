package com.example.digital_library.service;

import com.example.digital_library.entity.UploadedFile;
import com.example.digital_library.repository.UploadedFileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootLocation;
    private final UploadedFileRepository fileRepository;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir,
                              UploadedFileRepository fileRepository) {
        this.rootLocation = Paths.get(uploadDir);
        this.fileRepository = fileRepository;

        try {
            Files.createDirectories(rootLocation); // ensures "uploads" folder exists
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage!", e);
        }
    }

    // ✅ Method 1: Store file (with unique name)
    public UploadedFile store(MultipartFile file) {
        try {
            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path destinationFile = this.rootLocation.resolve(uniqueFileName).normalize().toAbsolutePath();

            // Use Files.copy for better compatibility
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            // Save metadata in DB (store relative file name instead of full path)
            UploadedFile uploadedFile = new UploadedFile(
                    file.getOriginalFilename(),   // original name
                    file.getContentType(),
                    file.getSize(),
                    uniqueFileName                // store relative name
            );

            return fileRepository.save(uploadedFile);

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file: " + file.getOriginalFilename(), e);
        }
    }

    // ✅ Method 2: Fetch all uploaded files
    public List<UploadedFile> getAllFiles() {
        return fileRepository.findAll();
    }

    // ✅ Method 3: Find file by ID
    public Optional<UploadedFile> getFileById(Long id) {
        return fileRepository.findById(id);
    }

    // ✅ Method 4: Delete file (from disk + DB)
    public void deleteFile(Long id) {
        fileRepository.findById(id).ifPresent(file -> {
            try {
                Path path = this.rootLocation.resolve(file.getFilePath()).normalize().toAbsolutePath();
                Files.deleteIfExists(path);

                fileRepository.deleteById(id);

            } catch (IOException e) {
                throw new RuntimeException("Failed to delete file: " + file.getFileName(), e);
            }
        });
    }
}
