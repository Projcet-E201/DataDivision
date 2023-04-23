package com.example.data.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(topics="Test", groupId = "consumer-1")
    public void consume(String message) {
        System.out.println("message = " + message);
    }
}
