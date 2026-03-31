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

@Service
@RequiredArgsConstructor
public class LocalFileStorageServiceImpl implements FileStorageService {

    private final ChatUploadProperties properties;

    @Override
    public Map<String, Object> upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String suffix = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String fileName = UUID.randomUUID().toString().replace("-", "") + suffix;

//        // 保存到项目运行目录下的 uploads 文件夹
//        String projectPath = System.getProperty("user.dir");
//        File saveDir = new File(projectPath, properties.getDir());

        File saveDir = new File(properties.getDir());

        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }

        File target = new File(saveDir, fileName);
        file.transferTo(target);

        // 返回浏览器可访问的路径
        String url = properties.getAccessUrlPrefix() + fileName;

        Map<String, Object> res = new HashMap<>();
        res.put("errno", 0);

        Map<String, String> data = new HashMap<>();
        data.put("url", url);
        res.put("data", data);

        return res;
    }
}
