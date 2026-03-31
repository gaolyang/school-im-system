//package org.zzu.schoolimsystem.dto;
//
///**
// * ClassName: VerifyResponse
// * Package: org.zzu.schoolimsystem.dto
// * Description:
// *
// * @Author gly
// * @Create 2026/3/10 19:40
// * @Version 1.0
// */
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@AllArgsConstructor
//public class VerifyResponse {
//
//
////
//    private Integer code; // 1 成功，0 验证码过期
//}
package org.zzu.schoolimsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerifyResponse {
    /**
     * 1 = 验证成功
     * 0 = 验证码不存在或不匹配
     * 2 = 已验证
     * -1 = 验证码无效
     */
    private Integer verifyStatus;

    /**
     * 详细原因
     */
    private String reason;
}
