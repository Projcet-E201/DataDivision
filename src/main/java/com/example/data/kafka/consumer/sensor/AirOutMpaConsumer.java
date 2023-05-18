package com.example.data.kafka.consumer.sensor;

import com.example.data.kafka.consumer.global.AbstractHandler;
import com.example.data.sse.SseService;
import com.example.data.util.DataInfo;
import com.example.data.util.DataSet;
import com.influxdb.client.WriteApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 생성 주기 : 1sec
 * 센서 수 : 5
 * 가공 여부 : 10초마다 max 값 추출
 * => 1sec 당 60개 데이터
 * */
@Slf4j
@Component
public class AirOutMpaConsumer extends AbstractHandler {

    public AirOutMpaConsumer(WriteApi writeApi, SseService sseService) {
        super(writeApi, sseService);
    }

    @KafkaListener(topics="AIR_OUT_MPA", groupId = "AIR_OUT_MPA-CONSUMER-GROUP", concurrency = "3")
    public void consumeMotor(ConsumerRecords<String, String> records) {
        for (ConsumerRecord<String, String> record : records) {
            Map<String, String> receiveData = parseData(record.value());

            // Client1 + Motor1,...,10 => 키값
            String key = receiveData.get("dataServer") + "-" + receiveData.get("dataType");

            // 큐 -> 값을 저장, 없으면 키값을 생성하고 값을 저장
            DataSet dataSet = new DataSet(receiveData.get("dataValue"), receiveData.get("dataTime"));
            dataQueueMap.computeIfAbsent(key, k -> new ConcurrentLinkedQueue<>()).add(dataSet);
        }
    }

    @PostConstruct
    private void saveScheduler() {
        // 스케쥴러 키값에서 타입별로 스케쥴러
        dataDivisionScheduler.scheduleAtFixedRate(() -> {

            for (Map.Entry<String, ConcurrentLinkedQueue<DataSet>> entry : dataQueueMap.entrySet()) {
                DataSet valueAndTime = new DataSet("0", "0");

                // 가공
                String[] nameAndType = entry.getKey().split("-");
                ConcurrentLinkedQueue<DataSet> motorQueue = entry.getValue();

                while (!motorQueue.isEmpty()){
                    valueAndTime = motorQueue.poll();
                }

                // 저장
                if(!valueAndTime.getTime().equals("0")) {   // 빈값 제거
                    String absValue = Math.abs(Integer.parseInt(valueAndTime.getValue())) + "";
                    log.info(entry.getKey() + " " + nameAndType[0] + " " + nameAndType[1] +  " " + absValue + " " + valueAndTime.getTime());
                    addTSData(nameAndType[0], nameAndType[1], absValue, valueAndTime.getTime(), DataInfo.AIR_OUT_MPA_BATCH_SIZE);
                }
            }

        }, DataInfo.AIR_OUT_MPA_CALCULATE_TIME, DataInfo.AIR_OUT_MPA_CALCULATE_TIME, DataInfo.AIR_OUT_MPA_CALCULATE_TIME_UNIT);
    }
}
