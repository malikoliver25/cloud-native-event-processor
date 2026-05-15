package com.malik.eventprocessor;

import com.malik.eventprocessor.model.TelemetryEvent;
import com.malik.eventprocessor.service.EventProcessorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class EventProcessorServiceTest {

    @Autowired
    private EventProcessorService service;

    @Test
    void testCriticalErrorRoutingToRedisDLQ() {
        // Arrange: Create a "Critical" event
        TelemetryEvent event = new TelemetryEvent(null, "CRITICAL_ERROR", "JUnit-Test", null, null, "NEW");

        // Act: Process it through our service logic
        TelemetryEvent result = service.processAndSave(event);

        // Assert: Verify it was routed to the DLQ as per our resume claim
        assertEquals("FAILED_ROUTED_TO_DLQ", result.status());
    }
}