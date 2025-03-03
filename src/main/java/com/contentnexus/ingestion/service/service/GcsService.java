package com.contentnexus.ingestion.service.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class GcsService {
    private final Storage storage;

    @Value("${google.gcs.raw-bucket}")
    private String rawBucketName;

    @Value("${google.gcs.input-bucket}")
    private String inputBucketName;

    public GcsService(@Value("${google.cloud.credentials.file}") String credentialsPath) throws IOException {
        this.storage = StorageOptions.newBuilder()
                .setCredentials(com.google.auth.oauth2.GoogleCredentials.fromStream(Files.newInputStream(Paths.get(credentialsPath))))
                .build()
                .getService();
    }

    public String uploadFile(MultipartFile file, String videoId) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String blobName = videoId + "/original/" + fileName;

        BlobInfo blobInfo = BlobInfo.newBuilder(rawBucketName, blobName).build();
        Blob blob = storage.create(blobInfo, file.getBytes());

        return "gs://" + rawBucketName + "/" + blobName;
    }

    public void uploadToGcs(Path filePath, String bucketName, String blobName) throws IOException {
        BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, blobName).build();
        Blob blob = storage.create(blobInfo, Files.readAllBytes(filePath));
    }

    public void downloadFromGcs(String blobUri, Path destinationPath) throws IOException {
        String[] parts = blobUri.replace("gs://", "").split("/", 2);
        String bucketName = parts[0];
        String blobName = parts[1];

        Blob blob = storage.get(bucketName, blobName);
        blob.downloadTo(destinationPath);
    }
}
