package com.example.data.kafka.data.global;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractNettyInboundHandler  extends SimpleChannelInboundHandler<ByteBuf> {

    protected Map<String, StringBuilder> dataMap = new ConcurrentHashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("Channel active: {}", ctx.channel());
    }

    // 클라이언트와 연결되어 트래픽을 생성할 준비가 되었을 때 호출하는 메서드
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        log.info("Channel inactive: {}", ctx.channel());
    }

    // 예외 발생시
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    protected Map<String, String> parseData(String receivedData) {
        String[] dataParts = receivedData.split(" ");
        Map<String, String> dataMap = new HashMap<>();

        if (dataParts.length == 4) {
            dataMap.put("dataServer", dataParts[0]);
            dataMap.put("dataType", dataParts[1]);
            dataMap.put("dataValue", dataParts[2]);
            dataMap.put("dataTime", dataParts[3]);
        }

        else if (dataParts.length == 5) {
            dataMap.put("dataServer", dataParts[0]);
            dataMap.put("dataType", dataParts[1]);
            dataMap.put("dataValue", dataParts[2]);
            dataMap.put("dataTime", dataParts[3]);
            dataMap.put("dataIdentifier", dataParts[4]);
        }

        else{
            log.error("데이터 양식이 이상합니다.");
        }

        return dataMap;
    }

    protected void processImageData(String base64ImageData) {
        byte[] decodedImageData = Base64.getDecoder().decode(base64ImageData);
        // 이미지 파일 저장 경로와 파일 이름 설정
        String filePath = "/path/to/save/image/file";
        String fileName = "image.jpg";

        try (FileOutputStream fos = new FileOutputStream(new File(filePath, fileName))) {
            fos.write(decodedImageData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}