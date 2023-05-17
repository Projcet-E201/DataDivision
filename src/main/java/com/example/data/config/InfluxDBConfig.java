package com.example.data.config;

import com.influxdb.client.*;
import com.influxdb.client.write.events.BackpressureEvent;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;


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
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(40, TimeUnit.SECONDS)
                .readTimeout(40, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES)); // Connection Pool 설정

        InfluxDBClientOptions options = InfluxDBClientOptions.builder()
                .url(url)
                .authenticateToken(token.toCharArray())
                .okHttpClient(okHttpClientBuilder)
                .build();

        return InfluxDBClientFactory.create(options);
    }

    @Bean
    public WriteApi writeApi(InfluxDBClient influxDBClient) {
        WriteOptions options = WriteOptions.builder()
                .batchSize(10_000) // 한번에 보내는 데이터 량 (기본 1000)
                .bufferLimit(10_000_000)
                .flushInterval(1)
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
