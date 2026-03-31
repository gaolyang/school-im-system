package org.zzu.schoolimsystem.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zzu.schoolimsystem.service.FileStorageService;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatUploadController {

    private final FileStorageService fileStorageService;

    /**
     * 聊天图片上传
     */
    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return fileStorageService.upload(file);
    }
}
