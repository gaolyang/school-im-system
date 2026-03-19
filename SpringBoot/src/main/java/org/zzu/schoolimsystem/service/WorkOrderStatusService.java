package org.zzu.schoolimsystem.service;

public interface WorkOrderStatusService {
    /**
     * 根据工单编号更新工单状态
     *
     * @param orderNo 工单编号
     * @param status  目标状态
     * @param userId  当前登录用户ID（从token解析）
     * @return 1=成功 0=失败
     */
    Integer updateStatusByOrderNo(String orderNo, Integer status, Long userId);
}
