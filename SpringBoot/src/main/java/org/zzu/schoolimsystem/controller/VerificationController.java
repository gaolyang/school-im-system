package org.zzu.schoolimsystem.controller;

/**
 * ClassName: VerificationController
 * Package: org.zzu.schoolimsystem.controller
 * Description:
 *
 * @Author gly
 * @Create 2026/3/10 19:44
 * @Version 1.0
 */

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        Integer result = verificationService.verifyCode(request.getEventnumber(), request.getCode());
        return ResponseEntity.ok(new VerifyResponse(result));
    }

}
