# 🚀 Notification System (Kafka + Redis + AWS SNS)

A production-grade, event-driven notification system built using Spring Boot, Apache Kafka, Redis, and AWS SNS.

This system demonstrates how modern distributed systems handle:
- Asynchronous communication
- Idempotency
- Retry & Dead Letter Queues (DLQ)
- External service failures (AWS SNS)
- Circuit breaking & resilience

---

## 🧠 Architecture Overview

- Producer (REST API) → publishes notification events to Kafka
- Kafka → acts as message broker
- Consumer → processes events with idempotency (Redis)
- Notification Service → sends notifications via AWS SNS
- DLQ + Retry → handles failures robustly

---

## 🧩 Tech Stack

- Java 17
- Spring Boot 3
- Apache Kafka
- Redis (Idempotency)
- AWS SNS
- Resilience4j (Circuit Breaker)
- Docker

---

## ⚙️ Features

### ✅ Idempotency (Redis)
Prevents duplicate processing using:
notif_lock:<transactionId>

### ✅ Retry Mechanism
- Exponential backoff
- Automatic retries (Kafka)

### ✅ Dead Letter Queue (DLQ)
- Failed messages routed to notification-topic-dlq
- Retry limit enforced

### ✅ Circuit Breaker (Resilience4j)
- Protects system from SNS outages
- Fallback handling

### ✅ Failure Classification
- Retryable failures → retried
- Permanent failures → skipped

---

## 📦 API

### Send Notification

POST /api/v1/notifications/send

### Request Body

json {   "transactionId": "{{$guid}}",   "userId": "user@example.com",   "channel": "EMAIL",   "message": "Hello 🚀" }

---

## 🔁 Event Flow

1. Client sends request
2. Producer publishes to Kafka
3. Consumer reads event
4. Redis checks idempotency
5. Notification sent via SNS
6. On failure:
    - Retry
    - DLQ
    - Final failure handling

---

## 🧪 Running Locally

### 1. Start Kafka & Redis

bash docker-compose up -d

---

### 2. Set Environment Variables

bash export AWS_REGION=ap-south-1 export AWS_ACCESS_KEY_ID=xxxx export AWS_SECRET_ACCESS_KEY=xxxx export AWS_SNS_TOPIC_ARN=arn:aws:sns:...

---

### 3. Run Application

bash ./gradlew bootRun

---

## 🧠 Design Decisions

### Why Kafka?
- Decouples producer & consumer
- Enables scalability

### Why Redis?
- Fast idempotency check
- Prevents duplicate processing

### Why DLQ?
- Ensures no message is lost
- Allows manual inspection

### Why Circuit Breaker?
- Prevent cascading failures
- Handle external system downtime

---

## ⚠️ Edge Cases Handled

- Duplicate requests
- SNS failure
- Invalid email
- Partial failures
- Retry exhaustion

---

## 🔥 Future Improvements

- Metrics (Micrometer + Prometheus)
- Rate limiting
- Multi-channel support (SMS, Push)
- UI dashboard for DLQ monitoring

---