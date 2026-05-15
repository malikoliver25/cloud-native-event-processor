package com.malik.eventprocessor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.malik.eventprocessor.model.TelemetryEvent;
import com.malik.eventprocessor.repository.EventRepository;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Service layer utilizing the Singleton Design Pattern.
 * Manages resilient telemetry ingestion and asynchronous routing.
 */
@Service
public class EventProcessorService {

    private final EventRepository repository;
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper; // Spring will provide this

    private static final String DLQ_KEY = "dlq:failed_events";

    // Standard constructor-based dependency injection
    public EventProcessorService(EventRepository repository,
                                 StringRedisTemplate redisTemplate,
                                 ObjectMapper objectMapper) {
        this.repository = repository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public TelemetryEvent processAndSave(TelemetryEvent event) {
        TelemetryEvent enrichedEvent = new TelemetryEvent(
                (event.id() == null) ? UUID.randomUUID().toString() : event.id(),
                event.eventType(),
                event.source(),
                Instant.now(),
                event.data(),
                "PROCESSED"
        );

        try {
            // Simulated Failover Trigger
            if ("CRITICAL_ERROR".equals(event.eventType())) {
                throw new RuntimeException("Simulated Database Connection Failure");
            }
            return repository.save(enrichedEvent);

        } catch (Exception e) {
            System.err.println("ALERT: System failure. Routing to Redis DLQ: " + e.getMessage());

            try {
                TelemetryEvent failedEvent = new TelemetryEvent(
                        enrichedEvent.id(),
                        enrichedEvent.eventType(),
                        enrichedEvent.source(),
                        enrichedEvent.timestamp(),
                        enrichedEvent.data(),
                        "FAILED_ROUTED_TO_DLQ"
                );

                String eventJson = objectMapper.writeValueAsString(failedEvent);
                redisTemplate.opsForList().rightPush(DLQ_KEY, eventJson);

                return failedEvent;

            } catch (Exception redisEx) {
                return enrichedEvent;
            }
        }
    }

    public List<TelemetryEvent> getAllEvents() {
        return repository.findAll();
    }
}