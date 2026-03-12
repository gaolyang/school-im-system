package org.zzu.schoolimsystem.dto;

/**
 * ClassName: ChatMessageDTO
 * Package: org.zzu.schoolimsystem.dto
 * Description:
 *
 * @Author gly
 * @Create 2026/2/11 23:22
 * @Version 1.0
 */
//package org.zzu.schoolimsystem.dto;

import lombok.Data;

@Data
public class ChatSendDTO {
    private String senderId;
    private String receiverId;
    private String content;
    private Integer contentType = 1;
    private String extra;
}
