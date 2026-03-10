package org.zzu.schoolimsystem.entity;

/**
 * ClassName: ChatMessage
 * Package: org.zzu.schoolimsystem.entity
 * Description:
 *
 * @Author gly
 * @Create 2026/2/11 23:20
 * @Version 1.0
 */


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("chat_message") // 对应数据库表名
public class ChatMessage {

    @TableId(type = IdType.AUTO) // 对应主键自增
    private Long id;

    private Long orderId;     // 关联工单ID
    private Long senderId;    // 发送者ID
    private Long receiverId;  // 接收者ID

    private String content;   // 消息内容

    // 消息类型: 1-文字, 2-图片 (数据库里是 tinyint, 这里用 Integer 接收即可)
    private Integer msgType;

    // ✅ 新增：是否已读
    private Integer isRead; // 0-未读，1-已读

    private LocalDateTime createTime; // 创建时间
}
