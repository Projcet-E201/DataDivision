package com.example.data.kafka.consumer.global;

import com.example.data.sse.SseService;
import com.example.data.util.DataInfo;
import com.example.data.util.DataSet;
import com.example.data.util.StateValue;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.*;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractHandler {

	protected final ConcurrentMap<String, ConcurrentLinkedQueue<DataSet>> dataQueueMap = new ConcurrentHashMap<>();
	protected final ScheduledExecutorService dataDivisionScheduler = Executors.newScheduledThreadPool(1);

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

	protected void 	addTSData(String server, String type, String value, String time, int batchSize) {
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
			if (dataType.equals("MOTOR") && fieldValue > 250) {
				sseService.sendError("[" + time + "]" + server + "_" + dataType + "_" + type + "데이터 경고");
			} else if (dataType.equals("VACUUM") && fieldValue > 90) {
				sseService.sendError("[" + time + "]" + server + "_" + dataType + "_" + type + "데이터 경고");
			} else if (dataType.equals("AIR_IN_KPA") && fieldValue > 800) {
				sseService.sendError("[" + time + "]" + server + "_" + dataType + "_" + type + "데이터 경고");
			} else if (dataType.equals("AIR_OUT_KPA") && fieldValue > 800) {
				sseService.sendError("[" + time + "]" + server + "_" + dataType + "_" + type + "데이터 경고");
			} else if (dataType.equals("AIR_OUT_MPA") && fieldValue > 0.8) {
				sseService.sendError("[" + time + "]" + server + "_" + dataType + "_" + type + "데이터 경고");
			} else if (dataType.equals("WATER") && fieldValue > 3) {
				sseService.sendError("[" + time + "]" + server + "_" + dataType + "_" + type + "데이터 경고");
			} else if (dataType.equals("ABRASION") && fieldValue > 35) {
				sseService.sendError("[" + time + "]" + server + "_" + dataType + "_" + type + "데이터 경고");
			} else if (dataType.equals("LOAD") && fieldValue > 14) {
				sseService.sendError("[" + time + "]" + server + "_" + dataType + "_" + type + "데이터 경고");
			} else if (dataType.equals("VELOCITY") && fieldValue > 45000) {
				sseService.sendError("[" + time + "]" + server + "_" + dataType + "_" + type + "데이터 경고");
			}
			if (points.size() >= batchSize) {
				writeApi.writePoints("week", "semse", new ArrayList<>(points));
				points.clear();
				log.info("Saved server for data: {}", server);
			}

		} catch (NumberFormatException e) {
			log.error("Failed to parse value {} as a Long. Exception message: {}", value, e.getMessage());
			writeApi.close();
			sseService.sendError("[" + time + "]" + server + "> " + type + "> 데이터가 저장되고 있지 않습니다.");
			// 예외 처리 로직 추가
		} catch (Exception e) {
			log.error("Unexpected error occurred while adding TS data. Exception message: {}", e.getMessage());
			writeApi.close();
			sseService.sendError("[" + time + "]" + server + "> " + type + "> 데이터가 저장되고 있지 않습니다.");
			// 예외 처리 로직 추가
		}
	}

	protected void machineDivision(String server, String type, String dataValue, String time) {
		String[] parts = dataValue.split(":");
		String name = parts[0];

		// 서버 이름으로 클라이언트 객체를 가져옴
		String key_value = server + "_" + name;
		if (name.startsWith("string")) {
			StateValue.state_value.put(key_value, parts[1] + " " + time);
		} else {
			double value = Double.parseDouble(parts[1]);
			StateValue.state_value.put(key_value, value);
		}
	}
}
