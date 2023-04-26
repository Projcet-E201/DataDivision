package com.example.data.kafka.consumer;

import com.example.data.kafka.data.SensorHandler;
import com.influxdb.client.InfluxDBClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SensorConsumer extends SensorHandler {

    public SensorConsumer(InfluxDBClient influxDBClient) {
        super(influxDBClient);
    }

    @KafkaListener(topics="CLIENT1", groupId = "CONSUMER-GROUP-1", concurrency = "3")
    public void consumeServer1(String message) {
        log.info("Receive Sensor : {}", message);
        channelRead0(message);
    }
}
