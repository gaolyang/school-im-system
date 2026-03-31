package org.zzu.schoolimsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import org.zzu.schoolimsystem.entity.WllEvent;
import org.zzu.schoolimsystem.mapper.WllEventMapper;
import org.zzu.schoolimsystem.service.WllEventService;

@Service
public class WllEventServiceImpl implements WllEventService {

    private final WllEventMapper wllEventMapper;

    public WllEventServiceImpl(WllEventMapper wllEventMapper) {
        this.wllEventMapper = wllEventMapper;
    }

    @Override
    public IPage<WllEvent> getEventPage(Integer page, Integer pageSize) {
        Page<WllEvent> pageObj = new Page<>(page, pageSize);

        LambdaQueryWrapper<WllEvent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WllEvent::getIsValid, 1)
                .orderByDesc(WllEvent::getCreatorTime);

        return wllEventMapper.selectPage(pageObj, queryWrapper);
    }
}
