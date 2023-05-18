package com.example.data.kafka.data;

import com.example.data.kafka.data.global.AbstractHandler;
import com.example.data.util.StateValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MachineStateHandler extends AbstractHandler {

    protected void channelRead0(String msg) {
        Map<String, String> receiveData = parseData(msg);
//        log.info("Parse MachineState : {} {} {}", receiveData.get("dataServer"), receiveData.get("dataType"),  receiveData.get("dataTime"));

        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd/HH:mm:ss");
        String formattedDateTime = currentTime.format(formatter);
//        log.info("generate time : {}, receive time : {} ", receiveData.get("dataTime"), formattedDateTime);

        addTSData(receiveData.get("dataServer"), receiveData.get("dataType"), receiveData.get("dataValue"), receiveData.get("dataTime"));
    }

    private void addTSData(String server, String type, String dataValue, String time) {
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