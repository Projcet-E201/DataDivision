package com.example.data.netty.data;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.example.data.netty.global.socket.AbstractNettyServerSocket;

import io.netty.bootstrap.ServerBootstrap;

@Component
public class DataNettyServerSocket extends AbstractNettyServerSocket {
	@Autowired
	public DataNettyServerSocket(@Qualifier("dataServerBootstrap") ServerBootstrap serverBootstrap, @Qualifier("dataInitSocketAddress") InetSocketAddress tcpPort) {
		super(serverBootstrap, tcpPort);
	}
}