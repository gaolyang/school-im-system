package org.zzu.schoolimsystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.zzu.schoolimsystem.entity.WorkOrder;

public interface WorkOrderService {
    IPage<WorkOrder> getWorkOrderPage(Integer page, Integer pageSize);

    void finishWorkOrder(String orderNo);
}
