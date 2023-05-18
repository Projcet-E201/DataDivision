package com.example.data.kafka.consumer;

import com.example.data.kafka.consumer.global.AbstractHandler;
import com.example.data.sse.SseService;
import com.influxdb.client.WriteApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.Map;

/**
 * 생성 주기 : Boolean 70s / Double 50s / Int 40s / String 100s
 * 데이터 수 : 10
 * 가공 여부 : X
 * => 1sec 당 0.70 + 0.50 + 0.40 + 0.01개 데이터
 * */
@Slf4j
//@Component
public class MachineStateConsumer extends AbstractHandler {

    public MachineStateConsumer(WriteApi writeApi, SseService sseService) {
        super(writeApi, sseService);
    }

    @KafkaListener(
            topics="MACHINE_STATE",
            groupId = "MACHINE_STATE-CONSUMER-GROUP",
            containerFactory = "containerFactory",
            concurrency = "4")
    public void consume(String message) {
        Map<String, String> receiveData = parseData(message);
        machineDivision(receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataValue"), receiveData.get("dataTime"));
    }
}
