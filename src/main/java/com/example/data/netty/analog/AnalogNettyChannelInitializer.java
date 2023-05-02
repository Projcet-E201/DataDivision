package com.example.data.netty.analog;

import org.springframework.stereotype.Component;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AnalogNettyChannelInitializer extends ChannelInitializer<SocketChannel> {

	private final AnalogNettyInboundHandler analogNettyInboundHandler;

	@Override
	protected void initChannel(SocketChannel socketChannel) {
		ChannelPipeline pipeline = socketChannel.pipeline();

		ByteBuf delimiter = Unpooled.copiedBuffer("\n", CharsetUtil.UTF_8);
		int maxFrameLength = 5 * 1024 * 1024; // 5MB
		pipeline.addLast(new DelimiterBasedFrameDecoder(maxFrameLength, delimiter));

		// Inbound 핸들러 등록
		pipeline.addLast(analogNettyInboundHandler);
	}
}
