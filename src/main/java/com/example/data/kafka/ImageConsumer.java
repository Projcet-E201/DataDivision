package com.example.data.kafka;

import com.example.data.netty.image.ImageHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@RequiredArgsConstructor
public class ImageConsumer extends ImageHandler {

    @KafkaListener(topics="IMAGE", groupId = "IMAGE-CONSUMER-GROUP")
    public void consumeImage(String message) throws Exception {
        log.info("Receive Image: {}", message);
        channelRead0(message);
    }
}
