package com.example.data.netty.image;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.example.data.netty.global.socket.AbstractNettyServerSocket;

import io.netty.bootstrap.ServerBootstrap;

@Component
public class ImageNettyServerSocket extends AbstractNettyServerSocket {
	@Autowired
	public ImageNettyServerSocket(@Qualifier("imageServerBootstrap") ServerBootstrap serverBootstrap, @Qualifier("imageInitSocketAddress") InetSocketAddress tcpPort) {
		super(serverBootstrap, tcpPort);
	}
}
