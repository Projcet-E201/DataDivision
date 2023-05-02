package com.example.data.kafka.data;

import com.example.data.kafka.data.global.AbstractHandler;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MachineStateHandler extends AbstractHandler {

    private final InfluxDBClient influxDBClient;

    protected void channelRead0(String msg) {
        Map<String, String> receiveData = parseData(msg);
        log.info("Parse MachineState : {} {} {} {}", receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataValue"), receiveData.get("dataTime"));
        addTSData(receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataValue"), receiveData.get("dataTime"));
    }

    private void addTSData(String server, String type, String value, String time) {
        String[] value_lst = value.split(",");
        for (int i = 0; i < value_lst.length; i++) {
            String[] result = value_lst[i].split(":");
            log.info(Arrays.toString(result));
            if (result[0].startsWith("string")) {
                try {
                    Point row = Point
                            .measurement(server)
                            .addTag("name", result[0])
                            .addTag("generate_time", time)
                            .addField("value_str", result[1])
                            .time(Instant.now(), WritePrecision.NS);
                    WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
                    writeApi.writePoint("day", "semse",row);
                } catch (NumberFormatException e) {
                    log.error("Failed to parse value {} as a Long. Exception message: {}", result[1], e.getMessage());
                    // 예외 처리 로직 추가
                } catch (Exception e) {
                    log.error("Unexpected error occurred while adding TS data. Exception message: {}", e.getMessage());
                    // 예외 처리 로직 추가
                }
            } else if (result[0].startsWith("double")) {
                try {
                    Double fieldValue = Double.parseDouble(result[1]);
                    Point row = Point
                            .measurement(server)
                            .addTag("name", result[0])
                            .addTag("generate_time", time)
                            .addField("value_double", fieldValue)
                            .time(Instant.now(), WritePrecision.NS);
                    WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
                    writeApi.writePoint("day", "semse",row);
                } catch (NumberFormatException e) {
                    log.error("Failed to parse value {} as a Long. Exception message: {}", result[1], e.getMessage());
                    influxDBClient.close();
                    // 예외 처리 로직 추가
                } catch (Exception e) {
                    log.error("Unexpected error occurred while adding TS data. Exception message: {}", e.getMessage());
                    influxDBClient.close();
                    // 예외 처리 로직 추가
                }
            } else {
                try {
                    int fieldValue = Integer.parseInt(result[1]);
                    Point row = Point
                            .measurement(server)
                            .addTag("name", result[0])
                            .addTag("generate_time", time)
                            .addField("value", fieldValue)
                            .time(Instant.now(), WritePrecision.NS);
                    WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();
                    writeApi.writePoint("day", "semse",row);
                } catch (NumberFormatException e) {
                    log.error("Failed to parse value {} as a Long. Exception message: {}", result[1], e.getMessage());
                    influxDBClient.close();
                    // 예외 처리 로직 추가
                } catch (Exception e) {
                    log.error("Unexpected error occurred while adding TS data. Exception message: {}", e.getMessage());
                    influxDBClient.close();
                    // 예외 처리 로직 추가
                }
            }
        }
    }

}
