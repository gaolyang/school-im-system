package org.zzu.schoolimsystem.dto;

import lombok.Data;

@Data
public class WorkOrderStatusUpdateRequest {
    /** 工单编号，例如 WO-20260310-002 */
    private String orderNo;

    /** 目标状态，例如 5 */
    private Integer status;
}
