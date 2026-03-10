package org.zzu.schoolimsystem.service;

/**
 * ClassName: ZjfVarificationCodeService
 * Package: org.zzu.schoolimsystem.service
 * Description:
 *
 * @Author gly
 * @Create 2026/3/10 19:40
 * @Version 1.0
 */

public interface ZjfVarificationCodeService {
    /**
     * 校验 eventno 与 code 是否有效
     * @return 1 有效，0 无效
     */
    Integer verifyCode(String eventno, String code);
}

