package org.zzu.schoolimsystem.service;

public interface UserDisplayService {

    /**
     * 根据用户ID获取展示名
     */
    String getDisplayNameByUserId(String userId);
}
