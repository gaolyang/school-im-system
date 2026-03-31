package org.zzu.schoolimsystem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * ClassName: SysUser
 * Package: org.zzu.schoolimsystem.entity
 * Description:
 *
 * @Author gly
 * @Create 2026/3/29 18:42
 * @Version 1.0
 */
@Data
@TableName("sys_user")
public class SysUser {
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @TableField("username")
    private String username;

    @TableField("nickname")
    private String nickname;

    @TableField("avatar")
    private String avatar;
    //  暂时保留这么多字段 其余字段 暂时不添加
}
