package org.zzu.schoolimsystem.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * ClassName: FileStorageService
 * Package: org.zzu.schoolimsystem.service
 * Description:
 *
 * @Author gly
 * @Create 2026/3/12 17:47
 * @Version 1.0
 */
public interface FileStorageService {

    Map<String, Object> upload(MultipartFile file) throws IOException;
}
