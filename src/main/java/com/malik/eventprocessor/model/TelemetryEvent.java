package com.malik.eventprocessor.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.Map;

@Document(collection = "telemetry_events")
public record TelemetryEvent(
        @Id String id,
        String eventType,
        String source,
        Instant timestamp,
        Map<String, Object> data,
        String status
) {}