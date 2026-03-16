package org.zzu.schoolimsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("work_order")
public class WorkOrder implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final Integer STATUS_PENDING_GRAB = 0;
    public static final Integer STATUS_DISPATCH_AUDIT_PENDING = 1;
    public static final Integer STATUS_AUDIT_APPROVED = 2;
    public static final Integer STATUS_REJECTED_PENDING_GRAB = 3;
    public static final Integer STATUS_HANDLING = 4;
    public static final Integer STATUS_HANDLE_FINISHED_PENDING_CONFIRM = 5;
    public static final Integer STATUS_CONFIRM_APPROVED_PENDING_REVIEW = 6;
    public static final Integer STATUS_REVIEW_APPROVED = 7;
    public static final Integer STATUS_REVIEW_REJECTED_BACK_TO_HANDLING = 8;
    public static final Integer STATUS_CLOSED = 9;
    public static final Integer STATUS_ONSITE_VERIFYING = 10;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("event_id")
    private Long eventId;

    @TableField("order_no")
    private String orderNo;

    @TableField("title")
    private String title;

    @TableField("content")
    private String content;

    @TableField("event_level")
    private Integer eventLevel;

    @TableField("create_user_id")
    private Long createUserId;

    @TableField("create_user_ip")
    private String createUserIp;

    @TableField("create_time")
    private LocalDateTime createTime;

    @TableField("grab_config_id")
    private Long grabConfigId;

    @TableField("current_status")
    private Integer currentStatus;

    @TableField("grab_endtime")
    private LocalDateTime grabEndtime;

    @TableField("audit_id")
    private Long auditId;

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
    private Long grabUserId;

    @TableField("grab_user_time")
    private LocalDateTime grabUserTime;

    @TableField("handle_content")
    private String handleContent;

    @TableField("handle_ip")
    private String handleIp;

    @TableField("handle_time")
    private LocalDateTime handleTime;

    @TableField("approve_id")
    private Long approveId;

    @TableField("approve_note")
    private String approveNote;

    @TableField("approve_time")
    private LocalDateTime approveTime;

    @TableField("ext_info")
    private String extInfo;

    @TableField("is_valid")
    private Integer isValid;
}
