package com.example.data.kafka.data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
	private static final int BATCH_SIZE = 30;
	private final List<Point> points = new ArrayList<>();

	private final WriteApi writeApi;

	protected void channelRead0(String msg) {
		Map<String, String> receiveData = parseData(msg);
//		log.info("Parse Sensor : {} {} {} {}", receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataValue"), receiveData.get("dataTime"));
		addTSData(receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataValue"), receiveData.get("dataTime"));
	}

	private void addTSData(String server, String type, String value, String time) {
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

			points.add(row);

			if (points.size() >= BATCH_SIZE) {
				writeApi.writePoints("week", "semse", new ArrayList<>(points));
				points.clear();
				log.info("Saved server for data: {}", server);
			}

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