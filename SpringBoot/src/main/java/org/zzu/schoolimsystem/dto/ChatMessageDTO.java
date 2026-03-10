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
public class ChatMessageDTO {
    // 前端只需要传这几个字段
    private Long senderId;    // 谁发的
    private Long receiverId;  // 发给谁
    private String content;   // 发了什么

    // 可选：如果是图片消息，前端可能还需要传个 msgType，默认是 1
    private Integer msgType = 1;
}
