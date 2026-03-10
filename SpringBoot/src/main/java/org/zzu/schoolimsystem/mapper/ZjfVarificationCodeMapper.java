package org.zzu.schoolimsystem.mapper;

//package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.example.entity.ZjfVarificationCode;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.zzu.schoolimsystem.entity.ZjfVarificationCode;

/**
 * 验证码验证表 Mapper
 */
@Repository
public interface ZjfVarificationCodeMapper extends BaseMapper<ZjfVarificationCode> {

    /**
     * 验证eventno和code是否有效
     * @param eventno 事件号
     * @param code 验证码
     * @return 存在返回1，不存在返回0
     */
    Integer verifyCode(@Param("eventno") String eventno, @Param("code") String code);
}
