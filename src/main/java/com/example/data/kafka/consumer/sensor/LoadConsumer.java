package com.example.data.kafka.consumer.sensor;

import com.example.data.kafka.consumer.global.AbstractHandler;
import com.example.data.sse.SseService;
import com.influxdb.client.WriteApi;
import lombok.extern.slf4j.Slf4j;
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
//@Component
public class LoadConsumer extends AbstractHandler {

    public LoadConsumer(WriteApi writeApi, SseService sseService) {
        super(writeApi, sseService);
    }

    @KafkaListener(topics="LOAD", groupId = "LOAD-CONSUMER-GROUP", concurrency = "3")
    public void consumeMotor(String message) {
        Map<String, String> receiveData = parseData(message);
        addTSData(receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataValue"), receiveData.get("dataTime"));
    }
}
