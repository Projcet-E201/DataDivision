package com.example.data.netty.data;

import java.util.Map;

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
public class DataNettyInboundHandler extends AbstractNettyInboundHandler {

	// 데이터를 읽어들임.
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
		Map<String, String> receiveData = parseData(msg.toString(CharsetUtil.UTF_8));
		log.info("Receive Data: {} {} {}" , receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataTime"));

	}
}