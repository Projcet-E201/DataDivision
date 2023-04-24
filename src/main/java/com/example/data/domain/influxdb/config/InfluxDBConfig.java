package com.example.data.domain.influxdb.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDBConfig {

    @Value("${influxdb.url}")
    private String url;

    @Value("${influxdb.username}")
    private String username;

    @Value("${influxdb.password}")
    private String password;

    @Value("${spring.influx.token}")
    private char[] token;

    @Value("${spring.influx.org}")
    private String org;

    @Value("${spring.influx.bucket}")
    private String[] bucket;

    @Bean
    public InfluxDBClient influxDBClient() {
        return InfluxDBClientFactory.create(url, token, org, bucket[0]);
    }
    @Bean
    public InfluxDBClient influxDBClientDay() {
        return InfluxDBClientFactory.create(url, token, org, bucket[1]);
    }
    @Bean
    public InfluxDBClient influxDBClientWeek() {
        return InfluxDBClientFactory.create(url, token, org, bucket[2]);
    }
    @Bean
    public InfluxDBClient influxDBClientMonth() {
        return InfluxDBClientFactory.create(url, token, org, bucket[3]);
    }

}
