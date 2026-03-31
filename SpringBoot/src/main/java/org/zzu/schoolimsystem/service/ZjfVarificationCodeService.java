package org.zzu.schoolimsystem.service;


//import
import org.zzu.schoolimsystem.dto.VerifyResponse;

public interface ZjfVarificationCodeService {
    /**
     * 校验 orderId 与 code 是否有效。
     *
     * @return 1 有效，0 无效
     */
//    Integer verifyCode(Long orderId, Integer code);

    VerifyResponse verifyCodeDetail(Long orderId, Integer code);
}
