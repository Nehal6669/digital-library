package com.example.digital_library.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReadingProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int currentPage;
    private int totalPages;

    @ManyToOne
    private User user;

    @ManyToOne
    private Book book;

    @Transient
    public double getPercentageCompleted() {
        if (totalPages == 0) return 0.0;
        return (currentPage * 100.0) / totalPages;
    }
}
