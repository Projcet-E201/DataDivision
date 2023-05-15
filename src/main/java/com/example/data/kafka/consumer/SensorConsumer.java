package com.example.data.kafka.consumer;

import com.example.data.kafka.data.SensorHandler;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class SensorConsumer extends SensorHandler {


    public SensorConsumer(WriteApi writeApi) {
        super(writeApi);
    }

    @KafkaListener(topics="CLIENT1", groupId = "CONSUMER-GROUP-1", containerFactory = "containerFactory" )
    public void consumeServer1(List<ConsumerRecord<String, String>> records) {
        for (ConsumerRecord<String, String> record : records) {
            String generate_time = record.value().split(" ")[3];
            log.info("generate_time : {}, receive_time : {}", generate_time, LocalDateTime.now());
        }
        log.info("[CLIENT1] batch size : " + records.size());
//        channelRead0(message);
    }

    @KafkaListener( topics="CLIENT2", groupId = "CONSUMER-GROUP-2", containerFactory = "containerFactory" )
    public void consumeServer2(List<ConsumerRecord<String, String>> records) {
        for (ConsumerRecord<String, String> record : records) {
            String generate_time = record.value().split(" ")[3];
            log.info("generate_time : {}, receive_time : {}", generate_time, LocalDateTime.now());
        }
        log.info("[CLIENT2] batch size : " + records.size());
//        channelRead0(message);
    }

    @KafkaListener( topics="CLIENT3", groupId = "CONSUMER-GROUP-3", containerFactory = "containerFactory" )
    public void consumeServer3(List<ConsumerRecord<String, String>> records) {
        for (ConsumerRecord<String, String> record : records) {
            String generate_time = record.value().split(" ")[3];
            log.info("generate_time : {}, receive_time : {}", generate_time, LocalDateTime.now());
        }
        log.info("[CLIENT3] batch size : " + records.size());
//        channelRead0(message);
    }

    @KafkaListener( topics="CLIENT4", groupId = "CONSUMER-GROUP-4", containerFactory = "containerFactory" )
    public void consumeServer4(List<ConsumerRecord<String, String>> records) {
        for (ConsumerRecord<String, String> record : records) {
            String generate_time = record.value().split(" ")[3];
            log.info("generate_time : {}, receive_time : {}", generate_time, LocalDateTime.now());
        }
        log.info("[CLIENT4] batch size : " + records.size());
//        channelRead0(message);
    }

    @KafkaListener( topics="CLIENT5", groupId = "CONSUMER-GROUP-5", containerFactory = "containerFactory" )
    public void consumeServer5(List<ConsumerRecord<String, String>> records) {
        for (ConsumerRecord<String, String> record : records) {
            String generate_time = record.value().split(" ")[3];
            log.info("generate_time : {}, receive_time : {}", generate_time, LocalDateTime.now());
        }
        log.info("[CLIENT5] batch size : " + records.size());
//        channelRead0(message);
    }

    @KafkaListener( topics="CLIENT6", groupId = "CONSUMER-GROUP-6", containerFactory = "containerFactory" )
    public void consumeServer6(List<ConsumerRecord<String, String>> records) {
        for (ConsumerRecord<String, String> record : records) {
            String generate_time = record.value().split(" ")[3];
            log.info("generate_time : {}, receive_time : {}", generate_time, LocalDateTime.now());
        }
        log.info("[CLIENT6] batch size : " + records.size());
//        channelRead0(message);
    }

    @KafkaListener(topics="CLIENT7", groupId = "CONSUMER-GROUP-7", containerFactory = "containerFactory" )
    public void consumeServer7(List<ConsumerRecord<String, String>> records) {
        for (ConsumerRecord<String, String> record : records) {
            String generate_time = record.value().split(" ")[3];
            log.info("generate_time : {}, receive_time : {}", generate_time, LocalDateTime.now());
        }
        log.info("[CLIENT7] batch size : " + records.size());
//        channelRead0(message);
    }

    @KafkaListener(topics="CLIENT8", groupId = "CONSUMER-GROUP-8", containerFactory = "containerFactory" )
    public void consumeServer8(List<ConsumerRecord<String, String>> records) {
        for (ConsumerRecord<String, String> record : records) {
            String generate_time = record.value().split(" ")[3];
            log.info("generate_time : {}, receive_time : {}", generate_time, LocalDateTime.now());
        }
        log.info("[CLIENT8] batch size : " + records.size());
//        channelRead0(message);
    }

    @KafkaListener(topics="CLIENT9", groupId = "CONSUMER-GROUP-9", containerFactory = "containerFactory" )
    public void consumeServer9(List<ConsumerRecord<String, String>> records) {
        for (ConsumerRecord<String, String> record : records) {
            String generate_time = record.value().split(" ")[3];
            log.info("generate_time : {}, receive_time : {}", generate_time, LocalDateTime.now());
        }
        log.info("[CLIENT9] batch size : " + records.size());
//        channelRead0(message);
    }

    @KafkaListener(topics="CLIENT10", groupId = "CONSUMER-GROUP-10", containerFactory = "containerFactory" )
    public void consumeServer10(List<ConsumerRecord<String, String>> records) {
        for (ConsumerRecord<String, String> record : records) {
            String generate_time = record.value().split(" ")[3];
            log.info("generate_time : {}, receive_time : {}", generate_time, LocalDateTime.now());
        }
        log.info("[CLIENT10] batch size : " + records.size());
//        channelRead0(message);
    }

    @KafkaListener(topics="CLIENT11", groupId = "CONSUMER-GROUP-11", containerFactory = "containerFactory" )
    public void consumeServer11(List<ConsumerRecord<String, String>> records) {
        for (ConsumerRecord<String, String> record : records) {
            String generate_time = record.value().split(" ")[3];
            log.info("generate_time : {}, receive_time : {}", generate_time, LocalDateTime.now());
        }
        log.info("[CLIENT11] batch size : " + records.size());
//        channelRead0(message);
    }

    @KafkaListener(topics="CLIENT12", groupId = "CONSUMER-GROUP-12", containerFactory = "containerFactory" )
    public void consumeServer12(List<ConsumerRecord<String, String>> records) {
        for (ConsumerRecord<String, String> record : records) {
            String generate_time = record.value().split(" ")[3];
            log.info("generate_time : {}, receive_time : {}", generate_time, LocalDateTime.now());
        }
        log.info("[CLIENT12] batch size : " + records.size());
//        channelRead0(message);
    }
}
