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
        log.info("Receive Sensor from CLIENT1 : {}", message);
        channelRead0(message);
    }

//    @KafkaListener(topics="CLIENT2", groupId = "CONSUMER-GROUP-2", concurrency = "3")
//    public void consumeServer2(String message) {
//        log.info("Receive Sensor from CLIENT2 : {}", message);
//        channelRead0(message);
//    }
//
//    @KafkaListener(topics="CLIENT3", groupId = "CONSUMER-GROUP-3", concurrency = "3")
//    public void consumeServer3(String message) {
//        log.info("Receive Sensor from CLIENT3 : {}", message);
//        channelRead0(message);
//    }
//
//    @KafkaListener(topics="CLIENT4", groupId = "CONSUMER-GROUP-4", concurrency = "3")
//    public void consumeServer4(String message) {
//        log.info("Receive Sensor from CLIENT4 : {}", message);
//        channelRead0(message);
//    }
//
//    @KafkaListener(topics="CLIENT5", groupId = "CONSUMER-GROUP-5", concurrency = "3")
//    public void consumeServer5(String message) {
//        log.info("Receive Sensor from CLIENT5 : {}", message);
//        channelRead0(message);
//    }
//
//    @KafkaListener(topics="CLIENT6", groupId = "CONSUMER-GROUP-6", concurrency = "3")
//    public void consumeServer6(String message) {
//        log.info("Receive Sensor from CLIENT6 : {}", message);
//        channelRead0(message);
//    }
}
