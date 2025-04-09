package com.contentnexus.ingestion.service.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Document(collection = "contents")
public class Content {
    @Id
    private String id; // MongoDB IDs are typically stored as strings (ObjectId)

    private String videoId;
    private String title;
    private String description;
    private String type;
    private String uploadedBy;
    private String category;
    private String status;
    private String rawVideoPath;
}
