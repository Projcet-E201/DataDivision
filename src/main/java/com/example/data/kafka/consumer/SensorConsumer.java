package com.example.data.kafka.consumer;

import com.example.data.kafka.data.SensorHandler;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SensorConsumer extends SensorHandler {


    public SensorConsumer(WriteApi writeApi) {
        super(writeApi);
    }

    /**
     * 초당 약 11,200개의 데이터 수신 (최대, 명세상)
     * */
    @KafkaListener(
            topics="SENSOR",
            groupId = "SENSOR-CONSUMER-GROUP",
            containerFactory = "containerFactory",
            concurrency = "4")
    public void consumeServer1(List<ConsumerRecord<String, String>> records) {
        log.info("[SENSOR] batch size : " + records.size());
//        channelRead0(message);
    }
}
