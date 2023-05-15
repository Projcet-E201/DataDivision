package com.example.data.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        config.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 300000);     // 데이터 최대 처리시간 : 5분 (default 5분)
        config.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 500);            // 한 번에 가져오는 최대 메시지 수 : 100개 (default 500개)
        config.put(ConsumerConfig.FETCH_MIN_BYTES_CONFIG, 1024);            // 배치 최소 byte 크기 : 1KB (default 1byte)
//        config.put(ConsumerConfig.FETCH_MAX_BYTES_CONFIG, 1048576);          // 배치 최대 byte 크기 : 1MB (default: 50MB)
        config.put(ConsumerConfig.FETCH_MAX_WAIT_MS_CONFIG, 1000);          // 배치 데이터가 차길 기다리는 최대 대기시간 : 1초 (default : 500ms)

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> containerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(true);
        factory.getContainerProperties().setIdleBetweenPolls(5000);

        return factory;
    }
}
