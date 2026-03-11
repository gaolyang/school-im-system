package org.zzu.schoolimsystem.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zzu.schoolimsystem.dto.ChatMessageDTO;
import org.zzu.schoolimsystem.entity.ChatMessage;
import org.zzu.schoolimsystem.mapper.ChatMessageMapper;
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
public class ChatController {

    @Autowired
    private ChatMessageMapper messageMapper; // MyBatis-Plus Mapper

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // 用于手动推送WebSocket消息

    @MessageMapping("/send/{orderId}")
    public void sendMessage(@DestinationVariable Long orderId, ChatMessageDTO msgDTO) {
        ChatMessage message = new ChatMessage();



        message.setOrderId(orderId);
        message.setSenderId(msgDTO.getSenderId());
        message.setReceiverId(msgDTO.getReceiverId());
        message.setContent(msgDTO.getContent());
        message.setCreateTime(LocalDateTime.now());

        // ✅ 显式设置为 0 (未读)，防止数据库默认值或缓存问题
        message.setIsRead(0);

        messageMapper.insert(message);
        messagingTemplate.convertAndSend("/topic/order/" + orderId, message);
    }

    // 2. HTTP 接口：获取历史聊天记录
    @GetMapping("/history/{orderId}")
    public List<ChatMessage> getHistory(@PathVariable Long orderId) {
        return messageMapper.selectList(new QueryWrapper<ChatMessage>()
                .eq("order_id", orderId)
                .orderByAsc("create_time"));
    }

// 记得导入这些包


// ...

    @PostMapping("/upload")
    // ❌ 原来返回值是 String
    // ✅ 修改为 Map<String, Object> 以返回 JSON
    public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        System.out.println("上传图片");
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
        String projectPath = System.getProperty("user.dir");
        String savePath = projectPath + File.separator + "uploads" + File.separator;

        File saveDir = new File(savePath);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        file.transferTo(new File(savePath + fileName));
        String url = "http://localhost:8080/images/" + fileName;

        // === 修改重点：构建标准 JSON 返回 ===
        Map<String, Object> res = new HashMap<>();
        res.put("errno", 0); // 0 表示成功
        Map<String, String> data = new HashMap<>();
        data.put("url", url);
        res.put("data", data);

        return res; // 返回 { "errno": 0, "data": { "url": "..." } }
    }

// ... 保持其他引用不变

    // ✅ 修改：标记已读接口，增加 WebSocket 广播功能
    @PostMapping("/read/{orderId}")
    public void markAsRead(@PathVariable Long orderId, @RequestParam Long userId) {
        // 1. 更新数据库 (保持原逻辑)
        ChatMessage updateMsg = new ChatMessage();
        updateMsg.setIsRead(1);

        messageMapper.update(updateMsg, new UpdateWrapper<ChatMessage>()
                .eq("order_id", orderId)
                .eq("receiver_id", userId)
                .eq("is_read", 0));

        // 2. ✅ 新增：发送“已读回执”通知
        // 我们利用 msgType = 3 来代表“这是一条系统通知：已读状态更新”
        ChatMessage readReceipt = new ChatMessage();
        readReceipt.setOrderId(orderId);
        readReceipt.setSenderId(userId); // 谁读了消息
        readReceipt.setMsgType(3); // 3 代表已读回执
        readReceipt.setContent("READ_RECEIPT"); // 内容不重要
        readReceipt.setCreateTime(LocalDateTime.now());

        // 广播给房间里的人
        messagingTemplate.convertAndSend("/topic/order/" + orderId, readReceipt);
    }
}
