package org.zzu.schoolimsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("zjf_varification_code") // 改为数据库真实表名
public class ZjfVarificationCode implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    // 映射数据库 event_number (bigint)
    @TableField("event_number")
    private Long eventNumber;

    // 数据库 code 是 int
    @TableField("code")
    private Integer code;

    // 如果需要保留类型字段
    @TableField("type")
    private String type;

    // 发送时间 begtime
    @TableField("begtime")
    private LocalDateTime begtime;

    // 验证时间 valitime
    @TableField("valitime")
    private LocalDateTime valitime;
}
