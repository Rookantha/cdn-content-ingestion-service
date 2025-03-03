package com.contentnexus.ingestion.service.repository;

import com.contentnexus.ingestion.service.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {
}
