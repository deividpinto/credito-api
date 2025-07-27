package com.credito.api.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(
            topics = "consulta-credito",
            groupId = "credito-group",
            autoStartup = "true",
            errorHandler = "kafkaErrorHandler"
    )
    public void consumir(@Payload String mensagem,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topico,
                         @Header(KafkaHeaders.RECEIVED_PARTITION) int particao,
                         @Header(KafkaHeaders.OFFSET) long offset) {
        try {
            log.info("Mensagem recebida - Mensagem: {}, Tópico: {}, Partição: {}, Offset: {}",
                    mensagem, topico, particao, offset);
            // Processa a mensagem
        } catch (Exception e) {
            log.error("Erro ao processar mensagem do Kafka: {}", e.getMessage());
        }
    }
}
