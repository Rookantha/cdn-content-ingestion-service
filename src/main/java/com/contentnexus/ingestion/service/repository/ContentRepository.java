package com.contentnexus.ingestion.service.repository;

import com.contentnexus.ingestion.service.entity.Content;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContentRepository extends MongoRepository<Content, Long> {
}
