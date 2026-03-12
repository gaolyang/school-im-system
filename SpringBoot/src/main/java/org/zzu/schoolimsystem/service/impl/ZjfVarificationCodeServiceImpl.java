package org.zzu.schoolimsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zzu.schoolimsystem.entity.ZjfVarificationCode;
import org.zzu.schoolimsystem.mapper.ZjfVarificationCodeMapper;
import org.zzu.schoolimsystem.service.ZjfVarificationCodeService;

import java.time.LocalDateTime;

@Service
public class ZjfVarificationCodeServiceImpl implements ZjfVarificationCodeService {

    private final ZjfVarificationCodeMapper mapper;

    public ZjfVarificationCodeServiceImpl(ZjfVarificationCodeMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer verifyCode(Long orderId, Integer code) {
        if (orderId == null || code == null) {
            return 0;
        }

        LambdaQueryWrapper<ZjfVarificationCode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ZjfVarificationCode::getOrderId, orderId)
                .eq(ZjfVarificationCode::getCode, code)
                .eq(ZjfVarificationCode::getValidity, ZjfVarificationCode.VALIDITY_PENDING)
                .orderByDesc(ZjfVarificationCode::getId)
                .last("limit 1");

        // 如果这个接口只处理现场验证码，可以打开下面这行
        // queryWrapper.eq(ZjfVarificationCode::getType, ZjfVarificationCode.TYPE_ONSITE);

        ZjfVarificationCode record = mapper.selectOne(queryWrapper);
        if (record == null) {
            return 0;
        }

        LambdaUpdateWrapper<ZjfVarificationCode> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(ZjfVarificationCode::getId, record.getId())
                .eq(ZjfVarificationCode::getValidity, ZjfVarificationCode.VALIDITY_PENDING)
                .set(ZjfVarificationCode::getValidity, ZjfVarificationCode.VALIDITY_VERIFIED)
                .set(ZjfVarificationCode::getValitime, LocalDateTime.now());

        int updated = mapper.update(null, updateWrapper);
        return updated > 0 ? 1 : 0;
    }
}
