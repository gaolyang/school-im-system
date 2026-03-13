package org.zzu.schoolimsystem.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("event_chat_records")
public class EventChatRecord {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("order_id")
    private Long orderId;

    @TableField("sender_id")
    private String senderId;

    @TableField("receiver_id")
    private String receiverId;

    @TableField("content_type")
    private Integer contentType;   // 1文本 2图片 3文件 4语音

    @TableField("content")
    private String content;

    @TableField("send_time")
    private LocalDateTime sendTime;

    @TableField("is_read")
    private Integer isRead;

    @TableField("extra")
    private String extra;          // 先用 String 接 json，后续再升级成 JacksonTypeHandler
}
