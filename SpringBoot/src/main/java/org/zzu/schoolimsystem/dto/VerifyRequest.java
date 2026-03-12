package org.zzu.schoolimsystem.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class VerifyRequest {
    /**
     * 新字段是 order_id。
     * 为了兼容旧前端，这里保留 eventnumber 别名。
     */
    @JsonAlias({"eventnumber", "orderId", "order_id"})
    private Long orderId;

    private Integer code;
}
