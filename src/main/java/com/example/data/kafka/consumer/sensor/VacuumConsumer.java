package com.example.data.kafka.consumer.sensor;

import com.example.data.kafka.consumer.global.AbstractHandler;
import com.example.data.sse.SseService;
import com.example.data.util.DataInfo;
import com.example.data.util.DataSet;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Component
public class VacuumConsumer extends AbstractHandler {

    public VacuumConsumer(WriteApi writeApi, SseService sseService) {
        super(writeApi, sseService);
    }

    @KafkaListener(topics="VACUUM", groupId = "VACUUM-CONSUMER-GROUP", concurrency = "3")
    public void consumeMotor(String message) {
        Map<String, String> receiveData = parseData(message);

        // Client1 + Motor1,...,10 => 키값
        String key = receiveData.get("dataServer") + "_" + receiveData.get("dataType");

        // 큐 -> 값을 저장, 없으면 키값을 생성하고 값을 저장
        DataSet dataSet = new DataSet(receiveData.get("dataValue"), receiveData.get("dataTime"));
        dataQueueMap.computeIfAbsent(key, k -> new ConcurrentLinkedQueue<>()).add(dataSet);

        // 스케쥴러 키값에서 타입별로 스케쥴러
        dataDivisionScheduler.scheduleAtFixedRate(() -> {
            DataSet data = new DataSet("0", "0");

            for (Map.Entry<String, ConcurrentLinkedQueue<DataSet>> entry : dataQueueMap.entrySet()) {
                // 가공
                String[] split = entry.getKey().split("_");
                ConcurrentLinkedQueue<DataSet> value1 = entry.getValue();
                while (!value1.isEmpty()){
                    data = value1.poll();
                }

                // 저장
                addTSData(split[0], split[1], data.getValue(), data.getTime());

                value1.clear();
            }

        }, DataInfo.VACUUM_CALCULATE_TIME, DataInfo.VACUUM_CALCULATE_TIME, DataInfo.VACUUM_CALCULATE_TIME_UNIT);
    }
}
