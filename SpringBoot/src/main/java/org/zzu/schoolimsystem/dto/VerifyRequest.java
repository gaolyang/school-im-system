package org.zzu.schoolimsystem.dto;

/**
 * ClassName: VerifyRequest
 * Package: org.zzu.schoolimsystem.dto
 * Description:
 *
 * @Author gly
 * @Create 2026/3/10 19:39
 * @Version 1.0
 */

import lombok.Data;

@Data
public class VerifyRequest {
    private String eventnumber ;  // 事件id
    private String code; // 验证码
}
