package org.zzu.schoolimsystem.service;

import org.zzu.schoolimsystem.dto.ChatSendDTO;
import org.zzu.schoolimsystem.vo.ChatMessageVO;

import java.util.List;

public interface ChatService {

    /**
     * 查询工单聊天历史
     */
    List<ChatMessageVO> getHistory(Long orderId);

    /**
     * 发送消息并持久化
     */
    ChatMessageVO sendMessage(Long orderId, ChatSendDTO dto);

    /**
     * 标记已读
     */
    void markAsRead(Long orderId, String receiverId);
}
