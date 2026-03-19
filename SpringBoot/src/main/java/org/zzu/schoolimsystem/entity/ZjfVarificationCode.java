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
//


    //  暂时设为几个固定值  作为  状态码
    public static final String VALIDITY_NotRequest = "0";   //还没验证
    public static final String VALIDITY_AlreadyRequest = "1"; //已经验证
    public static final String VALIDITY_Explire = "-1";  // 失效了
    public static final String TYPE_ONSITE = "a";      // 验证的 类型     a本地

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
