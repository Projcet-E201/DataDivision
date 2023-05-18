package com.example.data.kafka.consumer;

import com.example.data.kafka.consumer.global.AbstractHandler;
import com.example.data.sse.SseService;
import com.influxdb.client.WriteApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

/**
 * 생성 주기 : 1H
 * 데이터 수 : 1
 * 가공 여부 : X
 * */
@Slf4j
@Component
public class ImageConsumer extends AbstractHandler {

    private static final String IMAGE_SAVE_PATH = "received_images";

    public ImageConsumer(WriteApi writeApi, SseService sseService) {
        super(writeApi, sseService);
    }

    @KafkaListener(topics="IMAGE", groupId = "IMAGE-CONSUMER-GROUP")
    public void consumeImage(String message) throws Exception {
        Map<String, String> receiveData = parseData(message);

        // 데이터를 나눠서 보내는 경우, 모아두는 코드
        dataMap.putIfAbsent(receiveData.get("dataIdentifier"), new StringBuilder());
        StringBuilder dataBuilder = dataMap.get(receiveData.get("dataIdentifier"));
        dataBuilder.append(receiveData.get("dataValue"));

        if (receiveData.get("dataValue").endsWith("|")) {
            String fullData = dataBuilder.toString().trim().replace("|", "");
            dataMap.remove(receiveData.get("dataIdentifier")); // 데이터 처리 후 식별자 삭제


            // 데이터 파싱
            byte[] decodedImageData = Base64.getDecoder().decode(fullData);

            // 파일 이름 생성
            String fileName = UUID.randomUUID().toString() + ".jpg";

            // 저장할 파일 경로 설정
            Path savePath = Paths.get(IMAGE_SAVE_PATH, fileName);

            // 경로에 폴더가 없으면 생성
            if (!Files.exists(savePath.getParent())) {
//				log.info("Creating directories: {}", savePath.getParent());
                Files.createDirectories(savePath.getParent());
            }

            // 이미지 데이터를 파일로 저장
//			log.info("Saving image to: {}", savePath);
            Files.write(savePath, decodedImageData);
        }
    }
}
