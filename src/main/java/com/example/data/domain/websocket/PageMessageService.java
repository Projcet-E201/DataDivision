package com.example.data.domain.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.influxdb.client.InfluxDBClient;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@Getter @Setter
public class PageMessageService {
    private final ObjectMapper objectMapper;
    private final InfluxDBClient influxDBClient;

    public PageMessageService(ObjectMapper objectMapper, InfluxDBClient influxDBClient) {
        this.objectMapper = objectMapper;
        this.influxDBClient = influxDBClient;
    }


}
