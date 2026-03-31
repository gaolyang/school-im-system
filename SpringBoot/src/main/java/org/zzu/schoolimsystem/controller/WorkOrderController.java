package org.zzu.schoolimsystem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.*;
import org.zzu.schoolimsystem.common.PageResult;
import org.zzu.schoolimsystem.common.Pagination;
import org.zzu.schoolimsystem.common.Result;
import org.zzu.schoolimsystem.entity.WorkOrder;
import org.zzu.schoolimsystem.service.WorkOrderService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workOrder")
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    public WorkOrderController(WorkOrderService workOrderService) {
        this.workOrderService = workOrderService;
    }

    @GetMapping("/list")
    public Result list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize) {

        IPage<WorkOrder> result = workOrderService.getWorkOrderPage(page, pageSize);

        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", result.getCurrent());
        pagination.put("page_size", result.getSize());
        pagination.put("total", result.getTotal());
        pagination.put("pages", result.getPages());
        pagination.put("list", result.getRecords());

        Map<String, Object> data = new HashMap<>();
//        data.put("code", 1);
//        data.put("msg", "success");
//        data.put("data", data);
        data.put("list", result.getRecords());
        data.put("pagination", pagination);

        String msg= "查询成功";

        return Result.success(msg,data);
    }



    @PutMapping("/finish/{orderNo}")
    public Result finish(@PathVariable String orderNo) {
        workOrderService.finishWorkOrder(orderNo);
        return Result.success("工单已完成，短信已发送");
    }



//    @GetMapping("/list1")
//    public Result<PageResult<WorkOrder>> list(
//            @RequestParam(defaultValue = "1") Integer page,
//            @RequestParam(defaultValue = "10") Integer page_size) {
//
///*        List<WorkOrder> list = workOrderService.selectList(page, page_size); 暂时没有实现
//        Long total = workOrderService.countTotal();*/
//
//        Integer totalPages = (int) Math.ceil((double) total / page_size);
//
//        Pagination pagination = new Pagination(page, page_size, total, totalPages);
//        PageResult<WorkOrder> pageResult = new PageResult<>(list, pagination);
//
//        return Result.error(202,"信息丢了");
//    }
}
