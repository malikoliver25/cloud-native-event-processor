package com.malik.eventprocessor.repository;

import com.malik.eventprocessor.model.TelemetryEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<TelemetryEvent, String> {

    // Custom query: Find events by their source (e.g., "Mobile-App")
    List<TelemetryEvent> findBySource(String source);

    // Custom query: Find events that are currently "FAILED"
    List<TelemetryEvent> findByStatus(String status);
}