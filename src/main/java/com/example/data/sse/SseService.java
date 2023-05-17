package com.example.data.sse;

import com.example.data.util.StateValue;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {
    private final List<SseEmitter> errorEmitters = new CopyOnWriteArrayList<>();
    private final Map<String, List<SseEmitter>> infoEmitters = new HashMap<>();

    public SseEmitter subscribeToErrors() {
        SseEmitter emitter = new SseEmitter();
        this.errorEmitters.add(emitter);
        emitter.onCompletion(() -> this.errorEmitters.remove(emitter));
        emitter.onTimeout(() -> this.errorEmitters.remove(emitter));
        return emitter;
    }


    public SseEmitter subscribeToInfo(@PathVariable String variable_num) {
        SseEmitter emitter = new SseEmitter();

        // variable_num에 해당하는 List 가져오기. 없으면 새 List 생성
        List<SseEmitter> emitters = infoEmitters.computeIfAbsent(variable_num, k -> new CopyOnWriteArrayList<>());

        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

    public void sendError(String errorMessage) {
        sendEvent(this.errorEmitters, errorMessage);
    }


    private void sendEvent(List<SseEmitter> emitters, String message) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(message);
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
    }
    private void sendInfoEvent(List<SseEmitter> emitters, List<Map<String, Object>> message) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        emitters.forEach(emitter -> {
            try {
                emitter.send(message);
            } catch (IOException e) {
                deadEmitters.add(emitter);
            }
        });
        emitters.removeAll(deadEmitters);
    }
//    @Scheduled(fixedRate = 10000)
//    public void sendDataUpdates() {
//        for (Map.Entry<String, List<SseEmitter>> entry : infoEmitters.entrySet()) {
//            String variable_num = entry.getKey();
//            System.out.println("####################################################variable_num = " + variable_num);
//            List<SseEmitter> emitters = entry.getValue();
//
//            List<Map<String, Object>> data = generateData(variable_num); // 데이터를 생성하는 로직
//
//            sendInfoEvent(emitters, data);
//        }
//    }

    private List<Map<String, Object>> generateData(String client_num) {
        String client = "CLIENT" + client_num;
        System.out.println("client = " + client);
        List<Map<String, Object>> recordsList = new ArrayList<>();
        List<String> temp = new ArrayList<>();
        temp.add("boolean");
        temp.add("double");
        temp.add("int");
        temp.add("string");
        for (String type_value : temp) {
            Map<String, Object> recordMap = new HashMap<>();
            for (int i = 1; i < 11; i++) {
                String search_value = client + "_" + type_value + i;

                if (type_value.equals("string")) {
                    Map<String, Object> valuesMap = new HashMap<>();
                    String value = (String) StateValue.state_value.get(search_value);
                    if (value != null) {
                        String[] parts = value.split(" ");
                        valuesMap.put("time", parts[1]);
                        valuesMap.put("value", parts[0]);
                        recordMap.put(search_value, valuesMap);
                    }
                } else {
                    Object value = StateValue.state_value.get(search_value);
                    if (value != null) {
                        recordMap.put(search_value, value);
                    }
                }
            }
            recordsList.add(recordMap);
        }
        return recordsList;
    }

}
