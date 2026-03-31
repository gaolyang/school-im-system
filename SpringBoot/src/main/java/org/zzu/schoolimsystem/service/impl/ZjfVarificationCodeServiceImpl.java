////package org.zzu.schoolimsystem.service.impl;
////
////import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
////import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
////import org.springframework.stereotype.Service;
////import org.springframework.transaction.annotation.Transactional;
////import org.zzu.schoolimsystem.entity.ZjfVarificationCode;
////import org.zzu.schoolimsystem.mapper.ZjfVarificationCodeMapper;
////import org.zzu.schoolimsystem.service.ZjfVarificationCodeService;
////
////import java.time.LocalDateTime;
////
////@Service
////public class ZjfVarificationCodeServiceImpl implements ZjfVarificationCodeService {
////
////    private final ZjfVarificationCodeMapper mapper;
////
////    public ZjfVarificationCodeServiceImpl(ZjfVarificationCodeMapper mapper) {
////        this.mapper = mapper;
////    }
////
////    @Override
////    @Transactional(rollbackFor = Exception.class)
////    public Integer verifyCode(Long orderId, Integer code) {
////        if (orderId == null || code == null) {
////            return 0;
////        }
////
////        LambdaQueryWrapper<ZjfVarificationCode> queryWrapper = new LambdaQueryWrapper<>();
////        queryWrapper.eq(ZjfVarificationCode::getOrderId, orderId)
////                .eq(ZjfVarificationCode::getCode, code)
////                .eq(ZjfVarificationCode::getValidity, ZjfVarificationCode.VALIDITY_PENDING)
////                .orderByDesc(ZjfVarificationCode::getId)
////                .last("limit 1");
////
////        // 如果这个接口只处理现场验证码，可以打开下面这行
////        // queryWrapper.eq(ZjfVarificationCode::getType, ZjfVarificationCode.TYPE_ONSITE);
////
////        ZjfVarificationCode record = mapper.selectOne(queryWrapper);
////        if (record == null) {
////            return 0;
////        }
////
////        LambdaUpdateWrapper<ZjfVarificationCode> updateWrapper = new LambdaUpdateWrapper<>();
////        updateWrapper.eq(ZjfVarificationCode::getId, record.getId())
////                .eq(ZjfVarificationCode::getValidity, ZjfVarificationCode.VALIDITY_PENDING)
////                .set(ZjfVarificationCode::getValidity, ZjfVarificationCode.VALIDITY_VERIFIED)
////                .set(ZjfVarificationCode::getValitime, LocalDateTime.now());
////
////        int updated = mapper.update(null, updateWrapper);
////        return updated > 0 ? 1 : 0;
////    }
////}
//
//


package org.zzu.schoolimsystem.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zzu.schoolimsystem.entity.WorkOrder;
import org.zzu.schoolimsystem.dto.VerifyResponse;
import org.zzu.schoolimsystem.entity.ZjfVarificationCode;
import org.zzu.schoolimsystem.mapper.WorkOrderMapper;
import org.zzu.schoolimsystem.mapper.ZjfVarificationCodeMapper;
import org.zzu.schoolimsystem.service.ZjfVarificationCodeService;

import java.time.LocalDateTime;



//返回固定的码 还没修改好

@Service
public class ZjfVarificationCodeServiceImpl implements ZjfVarificationCodeService {

    private final ZjfVarificationCodeMapper mapper;
    private final WorkOrderMapper workOrderMapper;

    public ZjfVarificationCodeServiceImpl(ZjfVarificationCodeMapper mapper,
                                          WorkOrderMapper workOrderMapper) {
        this.mapper = mapper;
        this.workOrderMapper = workOrderMapper;
    }

//    // 完整性约束
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Integer verifyCode(Long orderId, Integer code) {
//        if (orderId == null || code == null) {
//            return 0;
//        }
//
//
//        // 获取请求体
//        LambdaQueryWrapper<ZjfVarificationCode> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(ZjfVarificationCode::getOrderId, orderId)
//                .eq(ZjfVarificationCode::getCode, code)
//                .eq(ZjfVarificationCode::getValidity, ZjfVarificationCode.VALIDITY_NotRequest)
//                .orderByDesc(ZjfVarificationCode::getId)
//                .last("limit 1");
//
//        //
//
//
//        ZjfVarificationCode record = mapper.selectOne(queryWrapper);
//        if (record == null) {
//            return 0;      //代表请求的验证码找不到
//        }
//
//        LocalDateTime now = LocalDateTime.now();
//// 更新验证码     对应
//        LambdaUpdateWrapper<ZjfVarificationCode> codeUpdateWrapper = new LambdaUpdateWrapper<>();
//        codeUpdateWrapper.eq(ZjfVarificationCode::getId, record.getId())
//                .eq(ZjfVarificationCode::getValidity, ZjfVarificationCode.VALIDITY_NotRequest)
//                .set(ZjfVarificationCode::getValidity, ZjfVarificationCode.VALIDITY_AlreadyRequest)
//                .set(ZjfVarificationCode::getValitime, now);
//
//        int codeUpdated = mapper.update(null, codeUpdateWrapper);
//        if (codeUpdated <= 0) {
//            return 0;
//        }
////同步修改 work_oder 表中的数据
//        LambdaUpdateWrapper<WorkOrder> workOrderUpdateWrapper = new LambdaUpdateWrapper<>();
//        workOrderUpdateWrapper.eq(WorkOrder::getId, orderId)
//                .eq(WorkOrder::getIsValid, 1)
//                .set(WorkOrder::getCurrentStatus, WorkOrder.STATUS_HANDLING)
//                .set(WorkOrder::getVerifyTime, now);
//
//        int workOrderUpdated = workOrderMapper.update(null, workOrderUpdateWrapper);
//        if (workOrderUpdated <= 0) {
//            throw new RuntimeException("工单状态更新失败");
//        }
//
//        return 1;
//    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public VerifyResponse verifyCodeDetail(Long orderId, Integer code) {
        if (orderId == null || code == null) {
            return new VerifyResponse(0, "参数错误");
        }

        LambdaQueryWrapper<ZjfVarificationCode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ZjfVarificationCode::getOrderId, orderId)
                .eq(ZjfVarificationCode::getCode, code)
                .orderByDesc(ZjfVarificationCode::getId)
                .last("limit 1");

        ZjfVarificationCode record = mapper.selectOne(queryWrapper);

        // 1. 查不到
        if (record == null) {
            return new VerifyResponse(0, "验证码不存在或验证码错误");
        }

        // 2. 已验证
        if ("1".equals(record.getValidity())) {
            return new VerifyResponse(2, "验证码已验证，请勿重复提交");
        }

        // 3. 无效
        if ("-1".equals(record.getValidity())) {
            return new VerifyResponse(-1, "验证码无效");
        }

        // 4. 未验证，允许通过
        if ("0".equals(record.getValidity())) {
            LocalDateTime now = LocalDateTime.now();

            LambdaUpdateWrapper<ZjfVarificationCode> codeUpdateWrapper = new LambdaUpdateWrapper<>();
            codeUpdateWrapper.eq(ZjfVarificationCode::getId, record.getId())
                    .eq(ZjfVarificationCode::getValidity, "0")
                    .set(ZjfVarificationCode::getValidity, "1")
                    .set(ZjfVarificationCode::getValitime, now);

            int codeUpdated = mapper.update(null, codeUpdateWrapper);
            if (codeUpdated <= 0) {
                return new VerifyResponse(0, "验证码状态更新失败");
            }

            LambdaUpdateWrapper<WorkOrder> workOrderUpdateWrapper = new LambdaUpdateWrapper<>();
            workOrderUpdateWrapper.eq(WorkOrder::getId, orderId)
                    .eq(WorkOrder::getIsValid, 1)
                    .set(WorkOrder::getCurrentStatus, WorkOrder.STATUS_HANDLING)
                    .set(WorkOrder::getVerifyTime, now);

            int workOrderUpdated = workOrderMapper.update(null, workOrderUpdateWrapper);
            if (workOrderUpdated <= 0) {
                throw new RuntimeException("工单状态更新失败");
            }

            return new VerifyResponse(1, "验证码验证成功");
        }

        return new VerifyResponse(0, "未知验证码状态");
    }
}






























//
//package org.zzu.schoolimsystem.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.zzu.schoolimsystem.entity.WorkOrder;
//import org.zzu.schoolimsystem.entity.ZjfVarificationCode;
//import org.zzu.schoolimsystem.mapper.WorkOrderMapper;
//import org.zzu.schoolimsystem.mapper.ZjfVarificationCodeMapper;
//import org.zzu.schoolimsystem.service.ZjfVarificationCodeService;
//
//import java.time.LocalDateTime;
//
//@Service
//public class ZjfVarificationCodeServiceImpl implements ZjfVarificationCodeService {
//
//    private final ZjfVarificationCodeMapper mapper;
//    private final WorkOrderMapper workOrderMapper;
//
//    public ZjfVarificationCodeServiceImpl(ZjfVarificationCodeMapper mapper,
//                                          WorkOrderMapper workOrderMapper) {
//        this.mapper = mapper;
//        this.workOrderMapper = workOrderMapper;
//    }
//
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public Integer verifyCode(Long orderId, Integer code) {
//        if (orderId == null || code == null) {
//            return 0;
//        }
//
//        LambdaQueryWrapper<ZjfVarificationCode> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(ZjfVarificationCode::getOrderId, orderId)
//                .eq(ZjfVarificationCode::getCode, code)
//                .eq(ZjfVarificationCode::getValidity, ZjfVarificationCode.VALIDITY_PENDING)
//                .orderByDesc(ZjfVarificationCode::getId)
//                .last("limit 1");
//
//        // 如果这个接口只处理现场验证码，可以打开下面这行
//        // queryWrapper.eq(ZjfVarificationCode::getType, ZjfVarificationCode.TYPE_ONSITE);
//
//        ZjfVarificationCode record = mapper.selectOne(queryWrapper);
//        if (record == null) {
//            return 0;
//        }
//
//        LambdaUpdateWrapper<ZjfVarificationCode> updateWrapper = new LambdaUpdateWrapper<>();
//        updateWrapper.eq(ZjfVarificationCode::getId, record.getId())
//                .eq(ZjfVarificationCode::getValidity, ZjfVarificationCode.VALIDITY_PENDING)
//                .set(ZjfVarificationCode::getValidity, ZjfVarificationCode.VALIDITY_VERIFIED)
//                .set(ZjfVarificationCode::getValitime, LocalDateTime.now());
//
//        int updated = mapper.update(null, updateWrapper);
//        if (updated <= 0) {
//            return 0;
//        }
//
//        // 验证成功后，把工单状态改成 4
//        LambdaUpdateWrapper<WorkOrder> workOrderUpdateWrapper = new LambdaUpdateWrapper<>();
//        workOrderUpdateWrapper.eq(WorkOrder::getId, orderId)
//                .set(WorkOrder::getCurrentStatus, 4);
//
//        int workOrderUpdated = workOrderMapper.update(null, workOrderUpdateWrapper);
//        if (workOrderUpdated <= 0) {
//            throw new RuntimeException("验证码已核销，但工单状态更新失败，事务已回滚");
//        }
//
//        return 1;
//    }
//}
//
