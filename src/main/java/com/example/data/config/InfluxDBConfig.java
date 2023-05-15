package com.example.data.config;

import com.influxdb.client.*;
import com.influxdb.client.write.PointSettings;
import com.influxdb.client.write.events.BackpressureEvent;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.influx.InfluxDbProperties;
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
                .connectTimeout(40, TimeUnit.SECONDS)       // 모두 default 10
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS);

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
                .batchSize(5_000) // 한번에 보내는 데이터 량 (기본 5,000)
                .bufferLimit(10_000) // 최대 네트워크 에러시 대략 10초 지연에 대해서 보장 (기본 10,000)
                .flushInterval(1000)
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
