package com.example.data.netty.image;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.example.data.netty.global.handler.AbstractNettyInboundHandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class ImageNettyInboundHandler extends AbstractNettyInboundHandler {

	private static final String IMAGE_SAVE_PATH = "received_images";

	// 데이터를 읽어들임.
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
		Map<String, String> receiveData = parseData(msg.toString(CharsetUtil.UTF_8));
		log.info("Receive Image: {} {} {} {}" , receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataTime"), receiveData.get("dataIdentifier"));

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
				log.info("Creating directories: {}", savePath.getParent());
				Files.createDirectories(savePath.getParent());
			}

			// 이미지 데이터를 파일로 저장
			log.info("Saving image to: {}", savePath);
			Files.write(savePath, decodedImageData);
		}
	}
}