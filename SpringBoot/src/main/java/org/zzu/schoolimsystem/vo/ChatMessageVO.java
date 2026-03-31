package org.zzu.schoolimsystem.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatMessageVO {

    private Long id;

    private Long orderId;

    private String senderId;

    /**
     * 发送人用户名 / 昵称
     */
    private String senderName;

    private String receiverId;

    private String senderAvatar;

    /**
     * 接收人用户名 / 昵称，可选
     */
    private String receiverName;

    /**
     * 1-文本 2-图片 3-文件 4-语音
     */
    private Integer contentType;

    /**
     * 文本内容 or 图片URL
     */
    private String content;

    private LocalDateTime sendTime;

    private Integer isRead;

    private String extra;
}
