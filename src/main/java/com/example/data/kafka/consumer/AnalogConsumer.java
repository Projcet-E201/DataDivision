package com.example.data.kafka.consumer;

import com.example.data.kafka.consumer.global.AbstractHandler;
import com.example.data.sse.SseService;
import com.influxdb.client.WriteApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;

@Slf4j
//@Component
public class AnalogConsumer extends AbstractHandler {

    private static final String SAVE_PATH = "received_analog";
    private static final String ZIP_EXTENSION = ".zip";

    public AnalogConsumer(WriteApi writeApi, SseService sseService) {
        super(writeApi, sseService);
    }

    @KafkaListener(topics="ANALOG", groupId = "ANALOG-CONSUMER-GROUP", concurrency = "3")
    public void consumeAnalog(String message) throws IOException {
        Map<String, String> receiveData = parseData(message);
//		log.info("Parse Analog: {} {} {}" , receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataTime"));

        // 데이터를 나눠서 보내는 경우, 모아두는 코드
        dataMap.putIfAbsent(receiveData.get("dataIdentifier"), new StringBuilder());
        StringBuilder dataBuilder = dataMap.get(receiveData.get("dataIdentifier"));
        dataBuilder.append(receiveData.get("dataValue"));

        if (receiveData.get("dataValue").endsWith("|")) {
            String fullData = receiveData.get("dataValue").trim().replace("|", "");

            byte[] decodedData = Base64.getDecoder().decode(fullData);

            // 파일 이름 생성
            String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm")) + ZIP_EXTENSION;

            // 저장할 파일 경로 설정
            Path savePath = Paths.get(SAVE_PATH, fileName);

            // 경로에 폴더가 없으면 생성
            if (!Files.exists(savePath.getParent())) {
                log.info("Creating directories: {}", savePath.getParent());
                Files.createDirectories(savePath.getParent());
            }

            // 압축 데이터를 파일로 저장
            log.info("Saving analog data to: {}", savePath);
            Files.write(savePath, decodedData);
        }
    }
}
