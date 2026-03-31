package org.zzu.schoolimsystem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.zzu.schoolimsystem.dto.ChatSendDTO;
import org.zzu.schoolimsystem.service.ChatService;
import org.zzu.schoolimsystem.vo.ChatMessageVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class EventChatRecordController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 查询某个工单的聊天历史
     */
    @GetMapping("/history/{orderId}")
    public Map<String, Object> getHistory(@PathVariable Long orderId) {
        List<ChatMessageVO> list = chatService.getHistory(orderId);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "查询成功");
        result.put("data", list);
        return result;
    }

    /**
     * HTTP 发送消息（调试功能的完整性）
     */
    @PostMapping("/send/{orderId}")
    public Map<String, Object> sendMessage(
            @PathVariable Long orderId,
            @Valid @RequestBody ChatSendDTO dto
    ) {
        ChatMessageVO record = chatService.sendMessage(orderId, dto);

        // 工单维度群聊广播
        messagingTemplate.convertAndSend("/topic/order/" + orderId, record);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "发送成功");
        result.put("data", record);
        return result;
    }

    /**
     * websocket 发送消息
     * 前端 publish 到 /app/send/{orderId}
     */
    @MessageMapping("/send/{orderId}")
    public void wsSendMessage(
            @DestinationVariable Long orderId,
            ChatSendDTO dto
    ) {

        //发送消息 并持久化
        ChatMessageVO record = chatService.sendMessage(orderId, dto);

        // 广播给当前工单下所有订阅者
        messagingTemplate.convertAndSend("/topic/order/" + orderId, record);
    }

    /**
     * 工单消息标记已读
     */
    @PutMapping("/read/{orderId}")
    public Map<String, Object> markRead(
            @PathVariable Long orderId,
            @RequestParam String receiverId
    ) {
        chatService.markAsRead(orderId, receiverId);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "已读更新成功");
        return result;
    }
}
