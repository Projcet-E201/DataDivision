package com.example.data.netty.analog;

import java.net.InetSocketAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.example.data.netty.global.socket.AbstractNettyServerSocket;
import io.netty.bootstrap.ServerBootstrap;

@Component
public class AnalogNettyServerSocket extends AbstractNettyServerSocket {
	@Autowired
	public AnalogNettyServerSocket(@Qualifier("analogServerBootstrap") ServerBootstrap serverBootstrap, @Qualifier("analogInitSocketAddress") InetSocketAddress tcpPort) {
		super(serverBootstrap, tcpPort);
	}
}
