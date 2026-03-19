package org.zzu.schoolimsystem.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zzu.schoolimsystem.dto.WorkOrderStatusUpdateRequest;
import org.zzu.schoolimsystem.dto.WorkOrderStatusUpdateResponse;
import org.zzu.schoolimsystem.service.WorkOrderStatusService;
import org.zzu.schoolimsystem.Utils.JwtUtil;

@RestController
@RequestMapping("/api/work-order")
public class WorkOrderStatusController {

    private final WorkOrderStatusService workOrderStatusService;
    private final JwtUtil jwtUtil;

    public WorkOrderStatusController(WorkOrderStatusService workOrderStatusService,
                                     JwtUtil jwtUtil) {
        this.workOrderStatusService = workOrderStatusService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/status")
    public ResponseEntity<WorkOrderStatusUpdateResponse> updateStatus(
            @RequestBody WorkOrderStatusUpdateRequest request,
            HttpServletRequest httpServletRequest) {

        String token = httpServletRequest.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new WorkOrderStatusUpdateResponse(401, "未登录或token已过期"));
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            if (jwtUtil.isTokenExpired(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new WorkOrderStatusUpdateResponse(401, "token已过期"));
            }

            Long userId = jwtUtil.getUserIdFromToken(token);

            Integer result = workOrderStatusService.updateStatusByOrderNo(
                    request.getOrderNo(),
                    request.getStatus(),
                    userId
            );

            if (result == 1) {
                return ResponseEntity.ok(new WorkOrderStatusUpdateResponse(1, "工单状态更新成功"));
            }
            return ResponseEntity.ok(new WorkOrderStatusUpdateResponse(0, "工单状态更新失败"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new WorkOrderStatusUpdateResponse(401, "token无效"));
        }
    }
}
