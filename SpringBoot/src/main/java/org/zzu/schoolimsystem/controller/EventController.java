//package org.zzu.schoolimsystem.controller;
//
//import com.baomidou.mybatisplus.core.metadata.IPage;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.zzu.schoolimsystem.entity.WllEvent;
//import org.zzu.schoolimsystem.service.WllEventService;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/event")
//public class EventController {
//
//    private final WllEventService wllEventService;
//
//    public EventController(WllEventService wllEventService) {
//        this.wllEventService = wllEventService;
//    }
//
//    @GetMapping("/list")
//    public Map<String, Object> list(
//            @RequestParam(defaultValue = "1") Integer page,
//            @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize) {
//
//        IPage<WllEvent> result = wllEventService.getEventPage(page, pageSize);
//
//        Map<String, Object> data = new HashMap<>();
//        data.put("page", result.getCurrent());
//        data.put("page_size", result.getSize());
//        data.put("total", result.getTotal());
//        data.put("pages", result.getPages());
//        data.put("list", result.getRecords());
//
//        Map<String, Object> resp = new HashMap<>();
//        resp.put("code", 1);
//        resp.put("msg", "success");
//        resp.put("data", data);
//
//        return resp;
//    }
//}


package org.zzu.schoolimsystem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.zzu.schoolimsystem.common.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zzu.schoolimsystem.entity.WllEvent;
import org.zzu.schoolimsystem.service.WllEventService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/event")
public class EventController {

    private final WllEventService wllEventService;

    public EventController(WllEventService wllEventService) {
        this.wllEventService = wllEventService;
    }

    @GetMapping("/list")
    public Result list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize) {

        IPage<WllEvent> result = wllEventService.getEventPage(page, pageSize);

        Map<String, Object> pagination = new HashMap<>();
        pagination.put("page", result.getCurrent());
        pagination.put("page_size", result.getSize());
        pagination.put("total", result.getTotal());
        pagination.put("total_pages", result.getPages());

        Map<String, Object> data = new HashMap<>();
        data.put("list", result.getRecords());
        data.put("pagination", pagination);

        return Result.success(data);
    }
}
