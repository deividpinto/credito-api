package com.credito.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class KafkaHealthIndicator {
    private final AtomicBoolean kafkaAvailable = new AtomicBoolean(false);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaHealthIndicator(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRate = 30000)
    public void checkKafkaAvailability() {
        try {
            kafkaTemplate.getDefaultTopic();
            if (!kafkaAvailable.get()) {
                log.info("Conexão com Kafka estabelecida");
                kafkaAvailable.set(true);
            }
        } catch (Exception e) {
            if (kafkaAvailable.get()) {
                log.warn("Conexão com Kafka perdida");
                kafkaAvailable.set(false);
            }
        }
    }

    public boolean isKafkaAvailable() {
        return kafkaAvailable.get();
    }
}
