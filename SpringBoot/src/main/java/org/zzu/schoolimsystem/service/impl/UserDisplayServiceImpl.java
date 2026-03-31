package org.zzu.schoolimsystem.service.impl;

import org.springframework.stereotype.Service;
import org.zzu.schoolimsystem.service.UserDisplayService;

@Service
public class UserDisplayServiceImpl implements UserDisplayService {

    @Override
    public String getDisplayNameByUserId(String userId) {
        // TODO:
        // 这里替换成你自己的实际查询逻辑：
        // 1. 查管理员表
        // 2. 查专家表
        // 3. 查用户表
        // 查不到就兜底返回 userId
        return "用户" + userId;
    }
}
