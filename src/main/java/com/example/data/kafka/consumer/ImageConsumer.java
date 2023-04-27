package com.example.data.kafka.consumer;

import com.example.data.kafka.data.ImageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ImageConsumer extends ImageHandler {

    @KafkaListener(topics="IMAGE", groupId = "IMAGE-CONSUMER-GROUP")
    public void consumeImage(String message) throws Exception {
        channelRead0(message);
    }
}
