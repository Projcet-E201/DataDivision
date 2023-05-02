package com.example.data.netty;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.example.data.netty.analog.AnalogNettyServerSocket;
import com.example.data.netty.data.DataNettyServerSocket;
import com.example.data.netty.image.ImageNettyServerSocket;

import lombok.RequiredArgsConstructor;

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
