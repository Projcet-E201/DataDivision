package com.example.data.kafka.consumer.global;

import com.example.data.sse.SseService;
import com.example.data.util.DataInfo;
import com.example.data.util.DataSet;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractHandler {

	protected final ConcurrentMap<String, ConcurrentLinkedQueue<DataSet>> dataQueueMap = new ConcurrentHashMap<>();
	protected final ScheduledExecutorService dataDivisionScheduler = Executors.newScheduledThreadPool(2);
	protected Map<String, StringBuilder> dataMap = new ConcurrentHashMap<>();
	protected final List<Point> points = new ArrayList<>();

	protected final WriteApi writeApi;
	protected final SseService sseService;

	protected Map<String, String> parseData(String receivedData) {
		String[] dataParts = receivedData.split(" ");
		Map<String, String> dataMap = new HashMap<>();

		if (dataParts.length == 4) {
			dataMap.put("dataServer", dataParts[0]);
			dataMap.put("dataType", dataParts[1]);
			dataMap.put("dataValue", dataParts[2]);
			dataMap.put("dataTime", dataParts[3]);
		}

		else if (dataParts.length == 5) {
			dataMap.put("dataServer", dataParts[0]);
			dataMap.put("dataType", dataParts[1]);
			dataMap.put("dataValue", dataParts[2]);
			dataMap.put("dataIdentifier", dataParts[3]);
			dataMap.put("dataTime", dataParts[4]);
		}

		else{
			log.error("데이터 양식이 이상합니다.");
		}

		return dataMap;
	}

	protected void addTSData(String server, String type, String value, String time) {
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

			if (points.size() >= DataInfo.BATCH_SIZE) {
				writeApi.writePoints("week", "semse", new ArrayList<>(points));
				points.clear();
				log.info("Saved server for data: {}", server);
			}

		} catch (NumberFormatException e) {
			log.error("Failed to parse value {} as a Long. Exception message: {}", value, e.getMessage());
			writeApi.close();
			sseService.sendError(type + "에 데이터가 저장되고 있지 않습니다.");
			// 예외 처리 로직 추가
		} catch (Exception e) {
			log.error("Unexpected error occurred while adding TS data. Exception message: {}", e.getMessage());
			writeApi.close();
			// 예외 처리 로직 추가
		}
	}
}
