package com.contentnexus.ingestion.service.entity;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "contents")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String videoId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String uploadedBy;

    @Column(nullable = true)
    private String category;

    @Column(nullable = true)
    private String status;

    @Column(nullable = false)
    private String rawVideoPath;
}
