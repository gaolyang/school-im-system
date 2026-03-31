package org.zzu.schoolimsystem.controller;

/**
 * ClassName: STRNDARD
 * Package: org.zzu.schoolimsystem.controller
 * Description:
 *
 * @Author gly
 * @Create 2026/3/22 19:52
 * @Version 1.0
 */
public class STRNDARD {
}


/*
* @GetMapping("/list")
public Result<PageResult<WorkOrder>> list(
        @RequestParam(defaultValue = "1") Integer page,
        @RequestParam(defaultValue = "10") Integer page_size) {

    List<WorkOrder> list = workOrderService.selectList(page, page_size);
    Long total = workOrderService.countTotal();

    Integer totalPages = (int) Math.ceil((double) total / page_size);

    Pagination pagination = new Pagination(page, page_size, total, totalPages);
    PageResult<WorkOrder> pageResult = new PageResult<>(list, pagination);

    return Result.success(pageResult);
}
* */
