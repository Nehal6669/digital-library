package com.example.digital_library.service;

import com.example.digital_library.entity.ReadingProgress;
import com.example.digital_library.repository.ReadingProgressRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadingProgressService {
    private final ReadingProgressRepository repo;

    public ReadingProgressService(ReadingProgressRepository repo) {
        this.repo = repo;
    }

    public List<ReadingProgress> getAllProgress() {
        return repo.findAll();
    }

    public ReadingProgress addProgress(ReadingProgress progress) {
        return repo.save(progress);
    }
}
