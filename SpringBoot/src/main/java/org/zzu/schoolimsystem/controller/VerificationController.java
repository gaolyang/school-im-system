package org.zzu.schoolimsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zzu.schoolimsystem.common.Result;
import org.zzu.schoolimsystem.dto.VerifyRequest;
import org.zzu.schoolimsystem.dto.VerifyResponse;
import org.zzu.schoolimsystem.service.ZjfVarificationCodeService;

//@RestController
//@RequestMapping("/api")
//public class VerificationController {
//
//    private final ZjfVarificationCodeService verificationService;
//
//    public VerificationController(ZjfVarificationCodeService verificationService) {
//        this.verificationService = verificationService;
//    }
//
//    @PostMapping("/verify")
//    public ResponseEntity<VerifyResponse> verify(@RequestBody VerifyRequest request) {
//        Integer result = verificationService.verifyCode(request.getOrderId(), request.getCode());//订单号 和对应的 验证码
//        return ResponseEntity.ok(new VerifyResponse(result));
//    }
//
//    @PostMapping("/verify_code")
//    public ResponseEntity<VerifyResponse> verify2(@RequestBody VerifyRequest request) {
//        Integer result = verificationService.verifyCode(request.getOrderId(), request.getCode());//订单号 和对应的 验证码
//        // 错误统一返回四百
//
//        return ResponseEntity.ok(new VerifyResponse(result));
//    }
//}


@RestController
@RequestMapping("/api")
public class VerificationController {

    private final ZjfVarificationCodeService verificationService;

    public VerificationController(ZjfVarificationCodeService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/verify")
    public Result<VerifyResponse> verify(@RequestBody VerifyRequest request) {
        if (request.getOrderId() == null || request.getCode() == null) {
            return Result.error(400, "参数错误：orderId 或 code 不能为空");
        }

        VerifyResponse response = verificationService.verifyCodeDetail(
                request.getOrderId(),
                request.getCode()
        );

        if (response.getVerifyStatus() == 1) {
            return Result.success("验证码校验成功", response);
        }

        return Result.error(400, response.getReason());
    }
}
