package org.zzu.schoolimsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import org.zzu.schoolimsystem.dto.ChatMessageDTO;
import org.zzu.schoolimsystem.entity.ChatMessage;
import org.zzu.schoolimsystem.entity.EventChatRecord;
//import org.zzu.schoolimsystem.mapper.ChatMessageMapper;
import org.zzu.schoolimsystem.service.ChatService;
import org.zzu.schoolimsystem.service.FileStorageService;

import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: ChatController
 * Package: org.zzu.schoolimsystem.controller
 * Description:
 *
 * @Author gly
 * @Create 2026/2/11 23:11
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final FileStorageService fileStorageService;

    @GetMapping("/history/{orderId}")
    public List<EventChatRecord> getHistory(@PathVariable Long orderId) {
        return chatService.getHistory(orderId);
    }

    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return fileStorageService.upload(file);
    }

    @PostMapping("/read/{orderId}")
    public void markAsRead(@PathVariable Long orderId, @RequestParam String userId) {
        chatService.markAsRead(orderId, userId);
    }
}
