package com.credito.api.service;

import com.credito.api.config.KafkaHealthIndicator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaHealthIndicator kafkaHealthIndicator;
    private static final String TOPIC_CONSULTA_CREDITO = "consulta-credito";

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate,
                                KafkaHealthIndicator kafkaHealthIndicator) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaHealthIndicator = kafkaHealthIndicator;
    }

    @Async
    public void enviarEventoConsultaCredito(Object mensagem) {
        if (!kafkaHealthIndicator.isKafkaAvailable()) {
            log.info("Kafka indisponível, mensagem ignorada: Mensagem {}", mensagem);
            return;
        }

        try {
            kafkaTemplate.send(TOPIC_CONSULTA_CREDITO, "consultar-credito", mensagem)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            log.info("Mensagem enviada com sucesso para o tópico {}", TOPIC_CONSULTA_CREDITO);
                        } else {
                            log.warn("Falha ao enviar mensagem para o Kafka: {}", ex.getMessage());
                        }
                    });
        } catch (Exception e) {
            log.error("Erro ao tentar enviar mensagem para o Kafka: {}", e.getMessage());
        }
    }
}
