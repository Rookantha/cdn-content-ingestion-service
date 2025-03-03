package com.contentnexus.ingestion.service.controller;

import com.contentnexus.ingestion.service.entity.Content;
import com.contentnexus.ingestion.service.repository.ContentRepository;
import com.contentnexus.ingestion.service.service.GcsService;
import com.contentnexus.ingestion.service.service.KafkaProducerService;
import com.contentnexus.ingestion.service.service.MetadataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/content")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Content API", description = "API for handling content ingestion and processing")
public class ContentController {
    private static final Logger logger = LoggerFactory.getLogger(ContentController.class);
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

    private final GcsService gcsService;
    private final MetadataService metadataService;
    private final ContentRepository contentRepository;
    private final KafkaProducerService kafkaProducerService;

    public ContentController(GcsService gcsService, MetadataService metadataService,
                             ContentRepository contentRepository, KafkaProducerService kafkaProducerService) {
        this.gcsService = gcsService;
        this.metadataService = metadataService;
        this.contentRepository = contentRepository;
        this.kafkaProducerService = kafkaProducerService;
    }

    @Operation(
            summary = "Upload a file",
            description = "Uploads a file to Google Cloud Storage, extracts metadata, saves it in the database, and sends a message to Kafka."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "File uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request (e.g., no file selected)"),
            @ApiResponse(responseCode = "413", description = "File size exceeds limit"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile( @Parameter(description = "File to upload", required = true)
                                                  @RequestParam("file") MultipartFile file,
                                              @Parameter(description = "Uploader's name", required = true)
                                              @RequestParam("uploadedBy") String uploadedBy) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No file selected for upload.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .body("File size exceeds the maximum allowed size of 50MB.");
        }

        try {
            // Generate or retrieve a videoId
            String videoId = generateVideoId(); // You can implement this method to generate a unique ID

            // Upload the file to GCS with the videoId
            String fileUrl = gcsService.uploadFile(file, videoId);

            // Extract metadata from the uploaded file
            Content content = metadataService.extractMetadata(file.getOriginalFilename(), uploadedBy, videoId, fileUrl);

            // Save the content information to the database
            contentRepository.save(content);
            logger.info("Content saved to database: {}", content);

            // Send the content information to Kafka
            kafkaProducerService.sendMessage(content);
            logger.info("Sending content information to Kafka: {}", content);

            // Successful upload message
            return ResponseEntity.ok("File uploaded successfully: " + fileUrl);


        } catch (Exception e) {
            logger.error("Error uploading file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error uploading file: " + e.getMessage());
        }
    }

    private String generateVideoId() {
        return UUID.randomUUID().toString(); // Generate a unique video ID using UUID
    }

}
