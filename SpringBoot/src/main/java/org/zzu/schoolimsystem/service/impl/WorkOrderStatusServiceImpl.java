package org.zzu.schoolimsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zzu.schoolimsystem.entity.WorkOrder;
import org.zzu.schoolimsystem.mapper.WorkOrderMapper;
import org.zzu.schoolimsystem.service.WorkOrderStatusService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class WorkOrderStatusServiceImpl implements WorkOrderStatusService {

    /**
     * 这个接口统一处理以下几类状态：
     * 4 = 处置中（处置未解决，回退继续处置）
     * 5 = 处置完成待确认
     * 8 = 复审驳回，回滚处置中
     * 9 = 复核通过，工单关闭
     */
    private static final Set<Integer> ALLOWED_STATUS =
            new HashSet<>(Arrays.asList(
                    WorkOrder.STATUS_HANDLING,
                    WorkOrder.STATUS_HANDLE_FINISHED_PENDING_CONFIRM,
                    WorkOrder.STATUS_REVIEW_REJECTED_BACK_TO_HANDLING,
                    WorkOrder.STATUS_CLOSED
            ));

    private final WorkOrderMapper workOrderMapper;

    public WorkOrderStatusServiceImpl(WorkOrderMapper workOrderMapper) {
        this.workOrderMapper = workOrderMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateStatusByOrderNo(String orderNo, Integer status, Long userId) {
        if (orderNo == null || orderNo.trim().isEmpty() || status == null) {
            return 0;
        }

        if (!ALLOWED_STATUS.contains(status)) {
            return 0;
        }

        LocalDateTime now = LocalDateTime.now();

        LambdaUpdateWrapper<WorkOrder> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(WorkOrder::getOrderNo, orderNo)
                .eq(WorkOrder::getIsValid, 1)
                .set(WorkOrder::getCurrentStatus, status);

        // 根据不同状态补充业务字段
        if (WorkOrder.STATUS_HANDLE_FINISHED_PENDING_CONFIRM.equals(status)) {
            // 处置完成
            updateWrapper.set(WorkOrder::getHandleTime, now);
        }

        if (WorkOrder.STATUS_REVIEW_REJECTED_BACK_TO_HANDLING.equals(status)
                || WorkOrder.STATUS_CLOSED.equals(status)) {
            // 复审类操作：记录复审时间、复审人
            updateWrapper.set(WorkOrder::getApproveTime, now)
                    .set(WorkOrder::getApproveId, userId);
        }

        int updated = workOrderMapper.update(null, updateWrapper);
        return updated > 0 ? 1 : 0;
    }
}
