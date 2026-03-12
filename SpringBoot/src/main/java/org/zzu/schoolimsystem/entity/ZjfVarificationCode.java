package org.zzu.schoolimsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("zjf_varification_code")
public class ZjfVarificationCode implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String VALIDITY_PENDING = "0";
    public static final String VALIDITY_VERIFIED = "1";
    public static final String VALIDITY_INVALID = "-1";
    public static final String TYPE_ONSITE = "a";

    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("order_id")
    private Long orderId;

    @TableField("code")
    private Integer code;

    @TableField("type")
    private String type;

    @TableField("begtime")
    private LocalDateTime begtime;

    @TableField("valitime")
    private LocalDateTime valitime;

    @TableField("validity")
    private String validity;
}
