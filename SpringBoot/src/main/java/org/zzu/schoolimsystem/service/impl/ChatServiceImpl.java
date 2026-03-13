package org.zzu.schoolimsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.zzu.schoolimsystem.dto.ChatSendDTO;
import org.zzu.schoolimsystem.entity.EventChatRecord;
import org.zzu.schoolimsystem.mapper.EventChatRecordMapper;
import org.zzu.schoolimsystem.service.ChatService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: ChatServiceImpl
 * Package: org.zzu.schoolimsystem.service.impl
 * Description:
 *
 * @Author gly
 * @Create 2026/3/12 17:47
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final EventChatRecordMapper chatRecordMapper;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public EventChatRecord sendMessage(Long orderId, ChatSendDTO dto) {
        EventChatRecord record = new EventChatRecord();
        record.setOrderId(orderId);
        record.setSenderId(dto.getSenderId());
        record.setReceiverId(dto.getReceiverId());
        record.setContent(dto.getContent());
        record.setContentType(dto.getContentType() == null ? 1 : dto.getContentType());
        record.setExtra(dto.getExtra());
        record.setSendTime(LocalDateTime.now());
        record.setIsRead(0);

        chatRecordMapper.insert(record);

        // 保持原来的 topic 不变
        messagingTemplate.convertAndSend("/topic/order/" + orderId, record);

        return record;
    }

    @Override
    public List<EventChatRecord> getHistory(Long orderId) {
        return chatRecordMapper.selectList(
                new QueryWrapper<EventChatRecord>()
                        .eq("order_id", orderId)
                        .orderByAsc("send_time")
        );
    }

    @Override
    public void markAsRead(Long orderId, String userId) {
        EventChatRecord update = new EventChatRecord();
        update.setIsRead(1);

        chatRecordMapper.update(update,
                new UpdateWrapper<EventChatRecord>()
                        .eq("order_id", orderId)
                        .eq("receiver_id", userId)
                        .eq("is_read", 0)
        );

        // 不再伪装成聊天消息，单独发“已读通知”
        Map<String, Object> receipt = new HashMap<>();
        receipt.put("eventType", "READ_RECEIPT");
        receipt.put("orderId", orderId);
        receipt.put("userId", userId);
        receipt.put("readTime", LocalDateTime.now());

        messagingTemplate.convertAndSend("/topic/order/" + orderId + "/receipt", receipt);
    }
}
