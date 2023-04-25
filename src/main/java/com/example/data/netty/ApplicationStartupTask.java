package com.example.data.netty;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.example.data.netty.analog.AnalogNettyServerSocket;
import com.example.data.netty.data.DataNettyServerSocket;
import com.example.data.netty.image.ImageNettyServerSocket;

import lombok.RequiredArgsConstructor;

/**
 * Netty TCP 소켓통신 연결 설정을 위한 클래스입니다.
 * (kafka를 사용하지 않을 때 주석을 풀어주세요)
 * */


@Component
@RequiredArgsConstructor
public class ApplicationStartupTask implements ApplicationListener<ApplicationReadyEvent> {

	private final DataNettyServerSocket dataNettyServerSocket;
	private final AnalogNettyServerSocket analogNettyServerSocket;
	private final ImageNettyServerSocket imageNettyServerSocket;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		dataNettyServerSocket.connect();
		analogNettyServerSocket.connect();
		imageNettyServerSocket.connect();
	}
}
