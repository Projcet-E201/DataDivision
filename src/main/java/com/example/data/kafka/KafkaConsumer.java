package com.example.data.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(topics="SERVER1", groupId = "CONSUMER-GROUP-1")
    public void consumeServer1(String message) {
        System.out.println("message = " + message);
    }
}
