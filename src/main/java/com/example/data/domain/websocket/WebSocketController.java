package com.example.data.domain.websocket;

import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class WebSocketController {
    private final SimpMessagingTemplate messagingTemplate;
    private final PageMessageService pageMessageService;


}
