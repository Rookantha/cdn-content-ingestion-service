# 📂 Content Ingestion Service

🚀 This Spring Boot service enables file uploads to **Google Cloud Storage (GCS)**, extracts metadata, stores it in **PostgreSQL**, and publishes events to **Kafka**.

---

## 📌 Features
- 📂 **Upload files** to **Google Cloud Storage (GCS)**
- 🛢️ **Stores metadata** in **PostgreSQL**
- 📤 **Publishes events** to **Kafka**
- 📜 **Exposes OpenAPI documentation** via **Swagger**
- 🏗 **Supports AWS S3 integration**
- 🔄 **Handles large file uploads** (Up to **150MB**)

---

## 🛠️ Installation & Setup

### 1️⃣ Clone the Repository  
```sh
git clone https://github.com/Rookantha/cdn-content-ingestion-service.git
```
### 2️⃣ Configure Environment
Create an application.yml file with your Google Cloud Storage, PostgreSQL, and Kafka details.

```yaml
google:
  cloud:
    credentials:
      file: "D:/projects/CDN/GOOGLE_APPLICATION_CREDENTIALS/"

```

###  3️⃣ Start PostgreSQL & Kafka
Ensure PostgreSQL and Kafka are running:

🛢️ PostgreSQL Setup
```sh
docker run --name postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=contentIngestionDB -p 5432:5432 -d postgres
```
### 🔄 Kafka Setup
```sh
docker-compose up -d  # If using a Docker-based Kafka setup
```
### 4️⃣ Build & Run the Project
Using Maven
```sh
mvn clean install
mvn spring-boot:run
```
Using Docker
``` sh

docker build -t content-ingestion-service .
docker run -p 8085:8085 content-ingestion-service
```
### 📌 API Endpoints
📂 Upload File
POST /api/v1/content/upload
- Description: Uploads a file to Google Cloud Storage (GCS), extracts metadata, saves it, and sends Kafka messages.
---
Request Parameters:
- file (multipart) – The file to upload (Required)
- uploadedBy (String) – The uploader's name (Required)
---
Responses:
- ✅ 200 OK → File uploaded successfully
- ❌ 400 Bad Request → No file selected
- ❌ 413 Payload Too Large → File exceeds 150MB
- ❌ 500 Internal Server Error → Upload failed
--- 

 🛠️ Example Request (cURL)

```sh
curl -X POST "http://localhost:8085/api/v1/content/upload" \
  -H "Content-Type: multipart/form-data" \
  -F "file=@/path/to/video.mp4" \
  -F "uploadedBy=JohnDoe"
```
#### 📜 Swagger API Documentation
You can explore the API using Swagger UI:

Swagger UI
OpenAPI JSON
#### 🔧 Technologies Used
- Java 17 ☕
- Spring Boot 3.3.4 🚀
- PostgreSQL 🛢️
- Kafka 🔄
- Google Cloud Storage (GCS) 🌍
- SpringDoc OpenAPI (Swagger) 📜
- Lombok 🛠️
- Maven 📦
- Docker 🐳
---

#### 🏗️ Project Configuration
📦 pom.xml Dependencies
``` xml
Copy

<dependencies>
    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- PostgreSQL Driver -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>

    <!-- Kafka -->
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>

    <!-- Google Cloud Storage -->
    <dependency>
        <groupId>com.google.cloud</groupId>
        <artifactId>spring-cloud-gcp-starter-storage</artifactId>
    </dependency>

    <!-- SpringDoc OpenAPI -->
    <dependency>
        <groupId>org.springdoc</groupId>
        <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        <version>2.0.2</version>
    </dependency>
</dependencies>
```
⚙️ application.yml Configuration
```yaml

spring:
  datasource:
    url: ---- URL ----
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

  kafka:
    bootstrap-servers: ---URL ---
    consumer:
      group-id: video-ingestion-group
      auto-offset-reset: earliest
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
    admin:
      auto-create-topics: true
    topics:
      video_ingested:
        partitions: 3
        replication-factor: 1

google:
  cloud:
    project-id: ----peoject id -----
    credentials:
      file: D:/projects/CDN/GOOGLE_APPLICATION_CREDENTIALS/

server:
  port: 8085
  servlet:
    multipart:
      max-file-size: 150MB
      max-request-size: 150MB

```
#### 🚀 Running in Docker
Build and run the service using Docker:

``` sh
docker build -t ingestion-service .
docker run -p 8085:8085 ingestion-service
``` 
#### 🎯 Contributing
Fork the repo 🍴
- Create a new branch (feature-xyz) 🌿
- Commit your changes (git commit -m "Add new feature") ✅
- Push the branch (git push origin feature-xyz) 🚀
- Open a Pull Request (PR) 🛠️
---
#### 📄 License
This project is licensed under the MIT License.

#### 📬 Contact
For any questions or support, feel free to reach out! 😊

