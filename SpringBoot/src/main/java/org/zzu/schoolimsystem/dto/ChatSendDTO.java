package org.zzu.schoolimsystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatSendDTO {

    @NotBlank(message = "发送者不能为空")
    private String senderId;

    @NotBlank(message = "接收者不能为空")
    private String receiverId;

    @NotBlank(message = "消息内容不能为空")
    private String content;

    /**
     * 1文本 2图片 3文件 4语音
     */
    private Integer contentType = 1;

    /**
     * 扩展字段，后续可以存 JSON
     */
    private String extra;
}
