Cloud-Native Event Processor
A resilient telemetry ingestion service built with Java 21 and Spring Boot.

Resiliency-First Engineering
Drawing from my background in Non-Destructive Testing (NDT), I engineered this system to handle primary database failures as a statistical certainty rather than an edge case.

Circuit-Breaker Failover: Telemetry is persisted to MongoDB, with an automated failover to a Redis Dead Letter Queue (DLQ) during connection interruptions to prevent data loss.

Industrial Precision: Leverages Java 21 Instant types for sub-millisecond telemetry accuracy.

Automated Validation: Integrated GitHub Actions CI/CD pipeline utilizing Dockerized services to verify failover logic on every push.

Tech Stack
Core: Java 21, Spring Boot 4.0.6.

Infrastructure: MongoDB (Primary), Redis (DLQ), Docker.

Quality: JUnit 5, Mockito, GitHub Actions.

Getting Started
Infrastructure: docker-compose up -d

Run: ./mvnw spring-boot:run

Test: Import the collection in /postman to test the POST /api/events/ingest endpoint.
