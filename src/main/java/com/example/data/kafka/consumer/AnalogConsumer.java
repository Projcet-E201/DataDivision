package com.example.data.kafka.consumer;

import com.example.data.kafka.data.AnalogHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class AnalogConsumer extends AnalogHandler {

    @KafkaListener(topics="ANALOG", groupId = "ANALOG-CONSUMER-GROUP", concurrency = "3")
    public void consumeAnalog(String message) throws IOException {
        log.info("Receive Analog: {}" , message);
        channelRead0(message);
    }
}
