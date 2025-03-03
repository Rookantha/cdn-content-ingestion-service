package com.contentnexus.ingestion.service.service;


import com.contentnexus.ingestion.service.entity.Content;
import org.springframework.stereotype.Service;

@Service
public class MetadataService {
    // method for extracting metadata
    public Content extractMetadata(String fileName, String uploadedBy, String videoId, String rawVideoPath) {
        // Stub implementation - replace with actual metadata extraction logic
        Content content = new Content();
        content.setVideoId(videoId);
        content.setTitle(fileName);
        content.setCategory("movie");
        content.setDescription("Auto-generated description for " + fileName);
        content.setType("video/mp4"); // Assuming mp4 for now
        content.setUploadedBy(uploadedBy);
        content.setRawVideoPath(rawVideoPath);
        content.setStatus("Uploaded");
        return content;
    }
}
