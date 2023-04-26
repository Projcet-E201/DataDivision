package com.example.data.kafka.data;

import java.time.Instant;
import java.util.Map;

import com.example.data.kafka.data.global.AbstractHandler;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SensorHandler extends AbstractHandler {

	private final InfluxDBClient influxDBClient;

	// 데이터를 읽어들임.
	protected void channelRead0(String msg) {
		Map<String, String> receiveData = parseData(msg);
		log.info("Parse Sensor : {} {} {} {}", receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataValue"), receiveData.get("dataTime"));
		addTSData(receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataValue"), receiveData.get("dataTime"));
	}

	private void addTSData(String server, String type, String value, String time) {
		try {
			long fieldValue = Long.parseLong(value);
			Point row = Point
					.measurement(server)
					.addTag("name", type)
					.addTag("generate_time", time)
					.addField("value", fieldValue)
					.time(Instant.now(), WritePrecision.NS);
			WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
			writeApi.writePoint("threeday", "semse",row);
//				log.info("fieldValue = {}",fieldValue);
		} catch (NumberFormatException e) {
			log.error("Failed to parse value {} as a Long. Exception message: {}", value, e.getMessage());
			// 예외 처리 로직 추가
		} catch (Exception e) {
			log.error("Unexpected error occurred while adding TS data. Exception message: {}", e.getMessage());
			// 예외 처리 로직 추가
		}
	}
}