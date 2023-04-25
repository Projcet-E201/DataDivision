package com.example.data.netty.analog;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.example.data.netty.global.handler.AbstractNettyInboundHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class AnalogNettyInboundHandler extends AbstractNettyInboundHandler {

    private static final String SAVE_PATH = "received_analog";
    private static final String ZIP_EXTENSION = ".zip";

    // 데이터를 읽어들임.
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws IOException {
        Map<String, String> receiveData = parseData(msg.toString(CharsetUtil.UTF_8));
        log.info("Receive Analog: {} {} {}" , receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataTime"));

        dataMap.putIfAbsent(receiveData.get("dataIdentifier"), new StringBuilder());
        StringBuilder dataBuilder = dataMap.get(receiveData.get("dataIdentifier"));
        dataBuilder.append(receiveData.get("dataValue"));

        if (receiveData.get("dataValue").endsWith("|")) {
            String fullData = dataBuilder.toString().trim().replace("|", "");
            dataMap.remove(receiveData.get("dataIdentifier"));

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