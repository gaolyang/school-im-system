package org.zzu.schoolimsystem.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zzu.schoolimsystem.dto.VerifyRequest;
import org.zzu.schoolimsystem.dto.VerifyResponse;
import org.zzu.schoolimsystem.service.ZjfVarificationCodeService;

@RestController
@RequestMapping("/api")
public class VerificationController {

    private final ZjfVarificationCodeService verificationService;

    public VerificationController(ZjfVarificationCodeService verificationService) {
        this.verificationService = verificationService;
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyResponse> verify(@RequestBody VerifyRequest request) {
        Integer result = verificationService.verifyCode(request.getOrderId(), request.getCode());
        return ResponseEntity.ok(new VerifyResponse(result));
    }
}
