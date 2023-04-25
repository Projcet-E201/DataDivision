package com.example.data.kafka;

import com.example.data.netty.analog.AnalogHandler;
import com.example.data.netty.data.MachineStateHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class MachineStateConsumer {

    @KafkaListener(topics="MACHINE_STATE", groupId = "MACHINE_STATE-CONSUMER-GROUP")
    public void consumeServer1(String message) throws IOException {
        log.info("Receive MachineState: {}", message);

    }

}
