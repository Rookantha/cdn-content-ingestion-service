# Content Ingestion Service

This service provides an API for uploading content (currently focused on video files), extracting basic metadata, storing the metadata in a database, uploading the raw content to Google Cloud Storage (GCS), and sending a notification about the ingested content via Kafka.

## Overview

The service is built using Spring Boot and leverages the following technologies:

* **Spring Boot:** For building the application.
* **Spring Web:** For handling HTTP requests and building RESTful APIs.
* **Spring Data JPA:** For interacting with the database.
* **Spring Kafka:** For producing messages to a Kafka topic.
* **Google Cloud Storage (GCS) Client:** For interacting with Google Cloud Storage.
* **Swagger/OpenAPI:** For API documentation.
* **Lombok:** For reducing boilerplate code.
* **Slf4j:** For logging.

## Functionality

The service offers the following main functionality:

* **File Upload:** Accepts video files via a REST API endpoint.
* **Google Cloud Storage Integration:** Uploads the received video files to a configured GCS bucket.
* **Metadata Extraction:** Extracts basic metadata such as filename, uploader, and generates a unique video ID.
* **Database Persistence:** Stores the extracted metadata and the GCS file URL in a database.
* **Kafka Integration:** Sends a message to a Kafka topic upon successful content ingestion, including the content metadata.
* **Error Handling:** Implements global exception handling for file size limits and other potential errors.
* **API Documentation:** Provides comprehensive API documentation using OpenAPI (Swagger).

## API Endpoints

The following API endpoint is available:

* **`POST /api/v1/content/upload`**: Uploads a file.
    * **Request Parameters:**
        * `file` (MultipartFile, required): The video file to upload.
        * `uploadedBy` (String, required): The name of the user uploading the file.
    * **Request Body:** `multipart/form-data` containing the file and `uploadedBy` parameter.
    * **Responses:**
        * **`200 OK`**: File uploaded successfully, returns the GCS URL of the uploaded file.
        * **`400 Bad Request`**: No file selected for upload.
        * **`413 Payload Too Large`**: File size exceeds the maximum allowed size (50MB).
        * **`500 Internal Server Error`**: An error occurred during the upload process.

## Configuration

The following configuration properties can be set in your `application.properties` or `application.yml` file:

* **`google.cloud.credentials.file`**: Path to the Google Cloud service account credentials JSON file.
* **`google.gcs.raw-bucket`**: Name of the Google Cloud Storage bucket where raw uploaded files will be stored.
* **`google.gcs.input-bucket`**: Name of another Google Cloud Storage bucket (currently not directly used in the provided code but might be intended for future use).
* **`spring.kafka.bootstrap-servers`**: Comma-separated list of Kafka broker addresses.
* **`spring.kafka.producer.key-serializer`**: Serializer class for Kafka message keys (e.g., `org.apache.kafka.common.serialization.StringSerializer`).
* **`spring.kafka.producer.value-serializer`**: Serializer class for Kafka message values (e.g., `org.springframework.kafka.support.serializer.JsonSerializer`).
* **Database Configuration (e.g., Spring Data JPA properties)**: Configure your database connection details (e.g., `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`, `spring.jpa.hibernate.ddl-auto`).

## Getting Started

### Prerequisites

* Java Development Kit (JDK) 17 or higher
* Maven
* Google Cloud SDK configured with appropriate permissions for GCS.
* A Google Cloud Storage bucket created.
* A Google Cloud service account with permissions to write to the GCS bucket.
* Kafka broker running.
* A database configured (e.g., PostgreSQL, MySQL).

### Steps to Run the Service

1.  **Clone the repository:**
    ```bash
    git clone <repository_url>
    cd <repository_directory>
    ```

2.  **Configure Google Cloud Credentials:**
    * Download your Google Cloud service account credentials JSON file.
    * Update the `google.cloud.credentials.file` property in your `application.properties` or `application.yml` with the path to this file.

3.  **Configure GCS Buckets:**
    * Update the `google.gcs.raw-bucket` property with the name of your GCS bucket for raw uploads.

4.  **Configure Kafka:**
    * Update the `spring.kafka.bootstrap-servers` property with the address(es) of your Kafka broker(s).
    * Ensure the `spring.kafka.producer.key-serializer` and `spring.kafka.producer.value-serializer` are correctly set (using `org.springframework.kafka.support.serializer.JsonSerializer` is recommended for sending JSON objects like the `Content` entity).

5.  **Configure Database:**
    * Configure your database connection details in `application.properties` or `application.yml` (e.g., database URL, username, password, driver).
    * Ensure your database is running and accessible.

6.  **Build the application:**
    ```bash
    mvn clean install
    ```

7.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```

The service will start and be accessible on the configured port (default is 8080).

## API Documentation

The API documentation can be accessed through Swagger UI. Once the application is running, navigate to:

This will provide an interactive interface to explore and test the API endpoints.

## Future Enhancements

* Implement more sophisticated metadata extraction based on the file type (e.g., reading video file metadata).
* Add support for different content types (audio, images, etc.).
* Implement video processing workflows (e.g., transcoding, thumbnail generation).
* Add authentication and authorization to the API endpoints.
* Implement more robust error handling and logging.
* Consider using a more specific Kafka key for the `video_ingested` topic.
* Explore using Spring Cloud Stream for a more abstract approach to Kafka integration.
* Implement unit and integration tests.