package org.zzu.schoolimsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkOrderStatusUpdateResponse {
    /** 1=成功，0=失败，401=未登录或token无效 */
    private Integer code;
    private String message;
}
