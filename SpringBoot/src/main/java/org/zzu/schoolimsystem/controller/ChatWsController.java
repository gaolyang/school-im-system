package org.zzu.schoolimsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.zzu.schoolimsystem.dto.ChatSendDTO;
import org.zzu.schoolimsystem.service.ChatService;

/**
 * ClassName: ChatWsController
 * Package: org.zzu.schoolimsystem.controller
 * Description:
 *
 * @Author gly
 * @Create 2026/3/12 17:59
 * @Version 1.0
 */
@Controller
@RequiredArgsConstructor
public class ChatWsController {

    private final ChatService chatService;

    @MessageMapping("/send/{orderId}")
    public void sendMessage(@DestinationVariable Long orderId, ChatSendDTO dto) {
        chatService.sendMessage(orderId, dto);
    }
}
