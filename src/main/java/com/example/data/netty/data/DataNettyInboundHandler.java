package com.example.data.netty.data;

import java.time.Instant;
import java.util.Map;
import java.util.ArrayDeque;
import java.util.Deque;
import com.example.data.netty.global.handler.AbstractNettyInboundHandler;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class DataNettyInboundHandler extends AbstractNettyInboundHandler {

	private final InfluxDBClient influxDBClient;

	// 데이터를 읽어들임.
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) {
		Map<String, String> receiveData = parseData(msg.toString(CharsetUtil.UTF_8));
//		log.info("Receive Data: {} {} {} {}", receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataValue"), receiveData.get("dataTime"));
		addTSData(receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataValue"), receiveData.get("dataTime"));
	}

	private void addTSData(String server, String type, String value, String time) {
		if (!type.startsWith("MACHINE_STATE")) {
			try {
				String bigName = typeTobigType(type);
				System.out.println("server = " + server);
				float fieldValue = Float.parseFloat(value);
				Point row = Point
						.measurement(bigName)
						.addTag("name", type)
						.addTag("generate_time", time)
						.addField("value", fieldValue)
						.time(Instant.now(), WritePrecision.NS);
				WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
				writeApi.writePoint(server, "semse",row);
//				log.info("fieldValue = {}",fieldValue);
			} catch (NumberFormatException e) {
				log.error("Failed to parse value {} as a Long. Exception message: {} {}",type, value, e.getMessage());
				// 예외 처리 로직 추가
			} catch (Exception e) {
				log.error("Unexpected error occurred while adding TS data. Exception message: {}", e.getMessage());
				// 예외 처리 로직 추가
			}
		} else {
			// Machine State
//			System.out.println("value = " + value);
			String[] value_lst = value.split(",");
			for (int i = 0; i < value_lst.length; i++) {
				String[] result = value_lst[i].split(":");
				String bigName = typeTobigType(result[0]);
//				log.info(Arrays.toString(result));
				if (result[0].startsWith("string")) {
					try {
						Point row = Point
								.measurement(server)
								.addTag("big_name", bigName)
								.addTag("name", result[0])
								.addTag("generate_time", time)
								.addField("value_str", result[1])
								.time(Instant.now(), WritePrecision.NS);
						WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
						writeApi.writePoint("day", "semse",row);
//						log.info("string = {} ",result[0]);
					} catch (NumberFormatException e) {
						log.error("Machine State String Failed to parse value {} as a Long. Exception message: {} {}", result[0], result[1], e.getMessage());
						// 예외 처리 로직 추가
					} catch (Exception e) {
						log.error("Machine State String Unexpected error occurred while adding TS data. Exception message: {}", e.getMessage());
						// 예외 처리 로직 추가
					}
				} else if (result[0].startsWith("double")) {
					try {
						Double fieldValue = Double.parseDouble(result[1]);
						Point row = Point
								.measurement(server)
								.addTag("big_name", bigName)
								.addTag("name", result[0])
								.addTag("generate_time", time)
								.addField("value_double", fieldValue)
								.time(Instant.now(), WritePrecision.NS);
						WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
						writeApi.writePoint("day", "semse",row);
//						log.info("double = {}",result[0]);
					} catch (NumberFormatException e) {
						log.error("Machine State Double Failed to parse value {} as a Long. Exception message: {}", result[1], e.getMessage());
						influxDBClient.close();
						// 예외 처리 로직 추가
					} catch (Exception e) {
						log.error("Machine State Double Unexpected error occurred while adding TS data. Exception message: {}", e.getMessage());
						influxDBClient.close();
						// 예외 처리 로직 추가
					}
				} else {
					try {
						int fieldValue = Integer.parseInt(result[1]);
						Point row = Point
								.measurement(server)
								.addTag("big_name", bigName)
								.addTag("name", result[0])
								.addTag("generate_time", time)
								.addField("value", fieldValue)
								.time(Instant.now(), WritePrecision.NS);
						WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
						writeApi.writePoint("day", "semse",row);
//						log.info("int || boolean = {}",result[1]);
					} catch (NumberFormatException e) {
						log.error("Machine State Integer Failed to parse value {} as a Long. Exception message: {}", result[1], e.getMessage());
						influxDBClient.close();
						// 예외 처리 로직 추가
					} catch (Exception e) {
						log.error("Machine State Integer Unexpected error occurred while adding TS data. Exception message: {}", e.getMessage());
						influxDBClient.close();
						// 예외 처리 로직 추가
					}
				}
			}
		}
	}

	private String typeTobigType(String type) {
		String bigType = type.replaceAll("\\d+", "");
		return bigType;
	}
}