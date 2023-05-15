package com.example.data.kafka.data;

import com.example.data.kafka.data.global.AbstractHandler;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MachineStateHandler extends AbstractHandler {

    private static final int BATCH_SIZE = 5;
    private final List<Point> points = new ArrayList<>();

    private final WriteApi writeApi;

    protected void channelRead0(String msg) {
        Map<String, String> receiveData = parseData(msg);
//        log.info("Parse MachineState : {} {} {}", receiveData.get("dataServer"), receiveData.get("dataType"),  receiveData.get("dataTime"));

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm:ss");
        String formattedDateTime = currentTime.format(formatter);
        log.info("generate time : {}, receive time : {} ", receiveData.get("dataTime"), formattedDateTime);

        addTSData(receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataValue"), receiveData.get("dataTime"));
    }

    private void addTSData(String server, String type, String value, String time) {
        long startTime = System.currentTimeMillis();

        String[] result = value.split(":");
        String bigName = type.replaceAll("[0-9]", "");
        try {
            Point row = Point
                .measurement(server)
                .addTag("big_name", bigName)
                .addTag("name", result[0])
                .addTag("generate_time", time)
                .time(Instant.now(), WritePrecision.NS);

            if (result[0].startsWith("string")) {
                row.addField("value_str", result[1]);
            } else if (result[0].startsWith("double")) {
                row.addField("value_double", Double.parseDouble(result[1]));
            } else {
                row.addField("value", Integer.parseInt(result[1]));
            }


            points.add(row);
            if (points.size() >= BATCH_SIZE) {
                writeApi.writePoints("day", "semse", new ArrayList<>(points));
                points.clear();
                log.info(server);
            }

        } catch (NumberFormatException e) {
            log.error("Machine State Failed to parse value {} as a Long. Exception message: {} {}", result[0], result[1], e.getMessage());
            writeApi.close();
            // 예외 처리 로직 추가
        } catch (Exception e) {
            log.error("Machine State Unexpected error occurred while adding TS data. Exception message: {}", e.getMessage());
            writeApi.close();
            // 예외 처리 로직 추가
        }
        long endTime = System.currentTimeMillis();
        log.info("{} {}, DB 저장 : {} ms", server, type, endTime - startTime);
    }
}