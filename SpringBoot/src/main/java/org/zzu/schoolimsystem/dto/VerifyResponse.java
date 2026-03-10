package org.zzu.schoolimsystem.dto;

/**
 * ClassName: VerifyResponse
 * Package: org.zzu.schoolimsystem.dto
 * Description:
 *
 * @Author gly
 * @Create 2026/3/10 19:40
 * @Version 1.0
 */

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyResponse {
    private Integer code; // 1 成功，0 失败
}
