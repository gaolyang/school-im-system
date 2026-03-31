package org.zzu.schoolimsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.zzu.schoolimsystem.entity.WorkOrder;
import org.zzu.schoolimsystem.mapper.WorkOrderMapper;
import org.zzu.schoolimsystem.service.SmsService;
import org.zzu.schoolimsystem.service.WorkOrderService;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

    private final WorkOrderMapper workOrderMapper;
    private final SmsService smsService;

    public WorkOrderServiceImpl(WorkOrderMapper workOrderMapper, SmsService smsService) {
        this.workOrderMapper = workOrderMapper;
        this.smsService = smsService;
    }

    @Override
    public IPage<WorkOrder> getWorkOrderPage(Integer page, Integer pageSize) {
        Page<WorkOrder> pageObj = new Page<>(page, pageSize);

        LambdaQueryWrapper<WorkOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WorkOrder::getIsValid, 1)
                .orderByDesc(WorkOrder::getCreateTime)
                .orderByDesc(WorkOrder::getId);

        return workOrderMapper.selectPage(pageObj, queryWrapper);
    }

    /**
     * 工单完成：
     * 1. 仅允许“处置中(4)”的工单执行完成操作
     * 2. 更新状态为“处置完成待确认(5)”
     * 3. 生成确认验证码
     * 4. 发送确认通知
     *
     * 当前采用强一致策略：
     * 状态更新和通知发送视为一个整体，
     * 只要通知发送失败，则抛出异常，整个事务回滚。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void finishWorkOrder(String orderNo) {
        // 1. 查询有效工单
        LambdaQueryWrapper<WorkOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WorkOrder::getOrderNo, orderNo)
                .eq(WorkOrder::getIsValid, 1);

        WorkOrder workOrder = workOrderMapper.selectOne(queryWrapper);

        if (workOrder == null) {
            throw new RuntimeException("工单不存在");
        }

        // 2. 只有“处置中(4)”状态才允许点击完成
        if (!WorkOrder.STATUS_HANDLING.equals(workOrder.getCurrentStatus())) {
            throw new RuntimeException("当前工单状态不允许完成操作");
        }

        // 3. 校验现场联系人手机号
        if (!StringUtils.hasText(workOrder.getVerifyTel())) {
            throw new RuntimeException("工单确认手机号为空，无法发送通知");
        }

        // 4. 生成验证码
        String code = generateCode();

        // 5. 更新工单状态和相关字段
        workOrder.setCurrentStatus(WorkOrder.STATUS_HANDLE_DONE_WAIT_CONFIRM); // 4 -> 5
        workOrder.setVerifyCode(code);
        workOrder.setHandleTime(LocalDateTime.now());

        int rows = workOrderMapper.updateById(workOrder);
        if (rows <= 0) {
            throw new RuntimeException("更新工单状态失败");
        }

        // 6. 发送通知
        // 只要这里失败，就抛异常，让整个事务回滚
        try {
            smsService.SimulatesendVerifySms(
                    workOrder.getVerifyTel(),
                    code,
                    workOrder.getOrderNo()
            );
        } catch (Exception e) {
            throw new RuntimeException("通知发送失败，事务已回滚", e);
        }
    }

    /**
     * 生成4位验证码
     */
    private String generateCode() {
        int code = 1000 + new Random().nextInt(9000);
        return String.valueOf(code);
    }
}
