package org.zzu.schoolimsystem.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.zzu.schoolimsystem.entity.WllEvent;

public interface WllEventService {
    IPage<WllEvent> getEventPage(Integer page, Integer pageSize);
}
