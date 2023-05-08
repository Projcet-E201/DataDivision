package com.example.data.kafka.consumer;

import com.example.data.kafka.data.SensorHandler;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
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
//        log.info("Receive Sensor from CLIENT1 : {}", message);
        channelRead0(message);
    }

    @KafkaListener(topics="CLIENT2", groupId = "CONSUMER-GROUP-2", concurrency = "3")
    public void consumeServer2(String message) {
//        log.info("Receive Sensor from CLIENT2 : {}", message);
        channelRead0(message);
    }

    @KafkaListener(topics="CLIENT3", groupId = "CONSUMER-GROUP-3", concurrency = "3")
    public void consumeServer3(String message) {
//        log.info("Receive Sensor from CLIENT3 : {}", message);
        channelRead0(message);
    }

    @KafkaListener(topics="CLIENT4", groupId = "CONSUMER-GROUP-4", concurrency = "3")
    public void consumeServer4(String message) {
//        log.info("Receive Sensor from CLIENT4 : {}", message);
        channelRead0(message);
    }

    @KafkaListener(topics="CLIENT5", groupId = "CONSUMER-GROUP-5", concurrency = "3")
    public void consumeServer5(String message) {
//        log.info("Receive Sensor from CLIENT5 : {}", message);
        channelRead0(message);
    }

    @KafkaListener(topics="CLIENT6", groupId = "CONSUMER-GROUP-6", concurrency = "3")
    public void consumeServer6(String message) {
//        log.info("Receive Sensor from CLIENT6 : {}", message);
        channelRead0(message);
    }

    @KafkaListener(topics="CLIENT7", groupId = "CONSUMER-GROUP-7", concurrency = "3")
    public void consumeServer7(String message) {
//        log.info("Receive Sensor from CLIENT7 : {}", message);
        channelRead0(message);
    }

    @KafkaListener(topics="CLIENT8", groupId = "CONSUMER-GROUP-8", concurrency = "3")
    public void consumeServer8(String message) {
//        log.info("Receive Sensor from CLIENT8 : {}", message);
        channelRead0(message);
    }

    @KafkaListener(topics="CLIENT9", groupId = "CONSUMER-GROUP-9", concurrency = "3")
    public void consumeServer9(String message) {
//        log.info("Receive Sensor from CLIENT9 : {}", message);
        channelRead0(message);
    }

    @KafkaListener(topics="CLIENT10", groupId = "CONSUMER-GROUP-10", concurrency = "3")
    public void consumeServer10(String message) {
//        log.info("Receive Sensor from CLIENT10 : {}", message);
        channelRead0(message);
    }

    @KafkaListener(topics="CLIENT11", groupId = "CONSUMER-GROUP-11", concurrency = "3")
    public void consumeServer11(String message) {
//        log.info("Receive Sensor from CLIENT11 : {}", message);
        channelRead0(message);
    }

    @KafkaListener(topics="CLIENT12", groupId = "CONSUMER-GROUP-12", concurrency = "3")
    public void consumeServer12(String message) {
//        log.info("Receive Sensor from CLIENT12 : {}", message);
        channelRead0(message);
    }
}
