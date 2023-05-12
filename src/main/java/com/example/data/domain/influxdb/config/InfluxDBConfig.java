package com.example.data.domain.influxdb.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.WriteApi;
import com.influxdb.client.WriteOptions;
import com.influxdb.client.write.PointSettings;
import com.influxdb.client.write.events.BackpressureEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;


@Configuration
public class InfluxDBConfig {

    private WriteApi writeApi;
    @Value("${spring.influxdb.url}")
    private String url;

    @Value("${spring.influxdb.username}")
    private String username;

    @Value("${spring.influxdb.password}")
    private String password;

    @Value("${spring.influxdb.token}")
    private String token;


    @Bean
    public InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(url, token.toCharArray());
    }

    @Bean
    public WriteApi writeApi(InfluxDBClient influxDBClient) {
        WriteOptions options = WriteOptions.builder()
                .batchSize(200) // 한번에 보내는 데이터 량 (기본 1000)
                .bufferLimit(10_000) // 최대 네트워크 에러시 대략 10초 지연에 대해서 보장
                .build();

        WriteApi writeApi = influxDBClient.makeWriteApi(options);
        writeApi.listenEvents(BackpressureEvent.class, event -> {
           //  BackpressureEvent 처리 로직

        });

        return writeApi;
    }

    @PreDestroy
    public void onShutdown() {
        if(writeApi != null) {
            writeApi.close();
        }
    }
}
