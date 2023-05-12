package com.example.data.kafka.data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import com.example.data.kafka.data.global.AbstractHandler;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SensorHandler extends AbstractHandler {

	private final WriteApi writeApi;

	protected void channelRead0(String msg) {
		Map<String, String> receiveData = parseData(msg);
//		log.info("Parse Sensor : {} {} {} {}", receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataValue"), receiveData.get("dataTime"));

		LocalDateTime currentTime = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm:ss");
		String formattedDateTime = currentTime.format(formatter);
		log.info("[SENSOR] generate time : {}, receive time : {} ", receiveData.get("dataTime"), formattedDateTime);

		addTSData(receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataValue"), receiveData.get("dataTime"));
	}

	private void addTSData(String server, String type, String value, String time) {
		long startTime = System.currentTimeMillis();

		try {
			float fieldValue = Float.parseFloat(value);
			String dataType = type.replaceAll("[0-9]", "");
			Point row = Point
					.measurement(server)
					.addTag("name", type)
					.addTag("generate_time", time)
					.addTag("big_name",dataType)                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       
					.addField("value", fieldValue)
					.time(Instant.now(), WritePrecision.NS);
			writeApi.writePoint("week", "semse", row);

			long endTime = System.currentTimeMillis();
			log.info("{} {}, DB 저장 : {} ms", server, type, endTime - startTime);
		} catch (NumberFormatException e) {
			log.error("Failed to parse value {} as a Long. Exception message: {}", value, e.getMessage());
			writeApi.close();
			// 예외 처리 로직 추가
		} catch (Exception e) {
			log.error("Unexpected error occurred while adding TS data. Exception message: {}", e.getMessage());
			writeApi.close();
			// 예외 처리 로직 추가
		}
	}
}