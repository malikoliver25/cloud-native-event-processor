package com.malik.eventprocessor.controller;

import com.malik.eventprocessor.model.TelemetryEvent;
import com.malik.eventprocessor.service.EventProcessorService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventProcessorService service;

    public EventController(EventProcessorService service) {
        this.service = service;
    }

    /**
     * POST /api/events/ingest
     */
    @PostMapping("/ingest")
    public TelemetryEvent ingest(@RequestBody TelemetryEvent event) {
        return service.processAndSave(event);
    }

    /**
     * GET /api/events/all
     */
    @GetMapping("/all")
    public List<TelemetryEvent> getAll() {
        return service.getAllEvents();
    }
}