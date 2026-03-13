package org.zzu.schoolimsystem.service;

import org.zzu.schoolimsystem.dto.ChatSendDTO;
import org.zzu.schoolimsystem.entity.EventChatRecord;

import java.util.List;

/**
 * ClassName: ChatService
 * Package: org.zzu.schoolimsystem.service
 * Description:
 *
 * @Author gly
 * @Create 2026/3/12 17:41
 * @Version 1.0
 */
public interface ChatService {

    EventChatRecord sendMessage(Long orderId, ChatSendDTO dto);

    List<EventChatRecord> getHistory(Long orderId);

    void markAsRead(Long orderId, String userId);
}
