package com.example.data.kafka.consumer;

import com.example.data.kafka.data.MachineStateHandler;
import com.influxdb.client.WriteApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class MachineStateConsumer extends MachineStateHandler {

    public MachineStateConsumer(WriteApi writeApi) {
        super(writeApi);
    }

    @KafkaListener(
            topics="MACHINE_STATE",
            groupId = "MACHINE_STATE-CONSUMER-GROUP",
            containerFactory = "containerFactory",
            concurrency = "4")
    public void consume(List<ConsumerRecord<String, String>> records) {
//        channelRead0(message);
        for (ConsumerRecord<String, String> record : records) {
            String generate_time = record.value().split(" ")[3];
            log.info("generate_time : {}, receive_time : {}", generate_time, LocalDateTime.now());
        }
        log.info("[MACHINE STATE] batch size : " + records.size());
    }

}
