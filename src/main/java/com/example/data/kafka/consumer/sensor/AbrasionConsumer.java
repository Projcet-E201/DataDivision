package com.example.data.kafka.consumer.sensor;

import com.example.data.kafka.consumer.global.AbstractHandler;
import com.example.data.sse.SseService;
import com.example.data.util.DataInfo;
import com.influxdb.client.WriteApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 생성 주기 : 1min
 * 센서 수 : 5
 * 가공 여부 : X
 * => 1sec 당 0.9개 데이터
 * */
@Slf4j
@Component
public class AbrasionConsumer extends AbstractHandler {

    public AbrasionConsumer(WriteApi writeApi, SseService sseService) {
        super(writeApi, sseService);
    }

    @KafkaListener(topics="ABRASION", groupId = "ABRASION-CONSUMER-GROUP", containerFactory = "containerFactory", concurrency = "3")
    public void consumeMotor(ConsumerRecords<String, String> records) {
        for (ConsumerRecord<String, String> record : records) {
            Map<String, String> receiveData = parseData(record.value());
            addTSData(receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataValue"), receiveData.get("dataTime"), DataInfo.ABRASION_BATCH_SIZE);
        }
    }
}
