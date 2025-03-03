package com.contentnexus.ingestion.service.service;

import com.contentnexus.ingestion.service.entity.Content;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, Content> kafkaTemplate;

    @Autowired
    public KafkaProducerService(KafkaTemplate<String, Content> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(Content content) {
        kafkaTemplate.send("video_ingested", content);
    }

}
