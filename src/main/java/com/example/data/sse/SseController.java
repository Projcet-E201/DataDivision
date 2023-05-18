package com.example.data.sse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class SseController {

    private final SseService sseService;

    public SseController(SseService sseService) {
        this.sseService = sseService;
    }

    // log 정보 보내기
    @GetMapping(value = "/subscribe/error", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeToErrors() {
        return sseService.subscribeToErrors();
    }

    // state 정보 보내기
    @GetMapping(value = "/subscribe/{client_num}/info", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeToInfo(@PathVariable String client_num) {
        return sseService.subscribeToInfo(client_num);
    }
    public void someMethodThatMightThrowAnError() {
        try {
            // ...
            throw new Exception("An error occurred.");
        } catch (Exception e) {
            sseService.sendError(e.getMessage());
        }
    }
}
