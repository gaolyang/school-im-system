package org.zzu.schoolimsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
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
    public Integer verifyCode(String eventnumber, String code) {
        if (eventnumber == null || code == null) {
            return 0;
        }

        Long eventNumber;
        Integer codeInt;
        try {
            eventNumber = Long.parseLong(eventnumber);
            codeInt = Integer.parseInt(code);
        } catch (NumberFormatException e) {
            return 0;
        }

        QueryWrapper<ZjfVarificationCode> qw = new QueryWrapper<>();
        // 使用数据库列名
        qw.eq("event_number", eventNumber)
                .eq("code", codeInt);

        // 如果你想检查是否已验证（valitime 非空 表示已验证），可以加条件：
        // qw.isNull("valitime"); // 仅未验证的记录
        Long count = mapper.selectCount(qw);
        return (count != null && count > 0) ? 1 : 0;
    }
}
