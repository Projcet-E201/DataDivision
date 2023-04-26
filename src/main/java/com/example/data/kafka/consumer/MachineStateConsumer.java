package com.example.data.kafka.consumer;

import com.example.data.kafka.data.MachineStateHandler;
import com.influxdb.client.InfluxDBClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class MachineStateConsumer extends MachineStateHandler {

    public MachineStateConsumer(InfluxDBClient influxDBClient) {
        super(influxDBClient);
    }

    @KafkaListener(topics="MACHINE_STATE", groupId = "MACHINE_STATE-CONSUMER-GROUP", concurrency = "3")
    public void consumeServer1(String message) throws IOException {
//        log.info("Receive MachineState: {}", message);
//        channelRead0(message);
    }

}
