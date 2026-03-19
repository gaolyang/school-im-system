package org.zzu.schoolimsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.zzu.schoolimsystem.dto.ChatSendDTO;
import org.zzu.schoolimsystem.entity.EventChatRecord;
import org.zzu.schoolimsystem.service.ChatService;

/**
 * ClassName: ChatDebugController
 * Package: org.zzu.schoolimsystem.controller
 * Description:
 *
 * @Author gly
 * @Create 2026/3/19 11:19
 * @Version 1.0
 */
@RestController
@RequestMapping("/internal/test/chat")
@RequiredArgsConstructor
public class ChatDebugController {

    private final ChatService chatService;

    @PostMapping("/send/{orderId}")
    public EventChatRecord send(@PathVariable Long orderId,
                                @RequestBody ChatSendDTO dto) {
        return chatService.sendMessage(orderId, dto);
    }
}
