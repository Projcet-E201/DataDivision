package com.example.data.kafka.consumer;

import com.example.data.kafka.data.MachineStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MachineStateConsumer extends MachineStateHandler {

    @KafkaListener(
            topics="MACHINE_STATE",
            groupId = "MACHINE_STATE-CONSUMER-GROUP",
            containerFactory = "containerFactory",
            concurrency = "4")
    public void consume(String message) {
        channelRead0(message);
    }

}
