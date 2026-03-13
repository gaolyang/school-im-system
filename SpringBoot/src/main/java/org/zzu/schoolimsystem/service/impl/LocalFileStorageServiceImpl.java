package org.zzu.schoolimsystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zzu.schoolimsystem.config.ChatUploadProperties;
import org.zzu.schoolimsystem.service.FileStorageService;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * ClassName: LocalFileStorageServiceImpl
 * Package: org.zzu.schoolimsystem.service.impl
 * Description:
 *
 * @Author gly
 * @Create 2026/3/12 18:00
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class LocalFileStorageServiceImpl implements FileStorageService {

    private final ChatUploadProperties properties;

    @Override
    public Map<String, Object> upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID() + "_" + originalFilename;

        File saveDir = new File(properties.getDir());
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        File target = new File(saveDir, fileName);
        file.transferTo(target);

        String url = properties.getAccessUrlPrefix() + fileName;

        Map<String, Object> res = new HashMap<>();
        res.put("errno", 0);

        Map<String, String> data = new HashMap<>();
        data.put("url", url);
        res.put("data", data);

        return res;
    }
}
