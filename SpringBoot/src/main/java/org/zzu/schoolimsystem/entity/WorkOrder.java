package org.zzu.schoolimsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("work_order")
public class WorkOrder {

    public static final String STATUS_WAIT_GRAB = "0";
    public static final String STATUS_ASSIGN_AUDIT = "1";
    public static final String STATUS_AUDIT_PASS = "2";
    public static final String STATUS_REJECT_WAIT_GRAB = "3";
    public static final String STATUS_HANDLING = "4";
    public static final String STATUS_HANDLE_DONE_WAIT_CONFIRM = "5";
    public static final String STATUS_WAIT_REVIEW = "6";
    public static final String STATUS_REVIEW_PASS = "7";
    public static final String STATUS_REVIEW_REJECT = "8";
    public static final String STATUS_CLOSED = "9";
    public static final String STATUS_VERIFYING = "10";



    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("event_no")
    private String eventNo;

    @TableField("order_no")
    private String orderNo;

    @TableField("event_title")
    private String eventTitle;

    @TableField("content")
    private String content;

    @TableField("event_level")
    private Integer eventLevel;

    @TableField("create_user_id")
    private String createUserId;

    @TableField("create_user_ip")
    private String createUserIp;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("grab_config_id")
    private Long grabConfigId;

    @TableField("current_status")
    private String currentStatus;

    @TableField("grab_end_time")
    private LocalDateTime grabEndTime;

    @TableField("audit_id")
    private String auditId;

    @TableField("audit_time")
    private LocalDateTime auditTime;

    @TableField("audit_ip")
    private String auditIp;

    @TableField("audit_note")
    private String auditNote;

    @TableField("verify_name")
    private String verifyName;

    @TableField("verify_tel")
    private String verifyTel;

    @TableField("verify_code")
    private String verifyCode;

    @TableField("verify_time")
    private LocalDateTime verifyTime;

    @TableField("grab_user_id")
    private String grabUserId;

    @TableField("grab_user_time")
    private LocalDateTime grabUserTime;

    @TableField("handle_content")
    private String handleContent;

    @TableField("handle_ip")
    private String handleIp;

    @TableField("handle_time")
    private LocalDateTime handleTime;

    @TableField("approve_id")
    private String approveId;

    @TableField("approve_note")
    private String approveNote;

    @TableField("approve_time")
    private LocalDateTime approveTime;

    /**
     * 如果你项目里没配 JSON TypeHandler，
     * 先用 String 最稳，先保证查询不报错。
     */
    @TableField("ext_info")
    private String extInfo;

    @TableField("is_valid")
    private Integer isValid;

    @TableField("sure_time")
    private LocalDateTime sureTime;
}
