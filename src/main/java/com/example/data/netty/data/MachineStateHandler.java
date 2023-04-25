package com.example.data.netty.data;

import com.example.data.netty.global.handler.AbstractHandler;
import com.influxdb.client.InfluxDBClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MachineStateHandler extends AbstractHandler {

    private final InfluxDBClient influxDBClient;

    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
        Map<String, String> receiveData = parseData(msg.toString(CharsetUtil.UTF_8));
        log.info("Parse Data: {} {} {} {}", receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataValue"), receiveData.get("dataTime"));
        addTSData(receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataValue"), receiveData.get("dataTime"));
    }

    private void addTSData(String server, String type, String value, String time) {

    }

}
