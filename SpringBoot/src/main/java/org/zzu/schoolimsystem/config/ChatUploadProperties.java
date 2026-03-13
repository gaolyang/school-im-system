package org.zzu.schoolimsystem.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * ClassName: ChatUploadProperties
 * Package: org.zzu.schoolimsystem.config
 * Description:
 *
 * @Author gly
 * @Create 2026/3/12 18:01
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "chat.upload")
public class ChatUploadProperties {
    private String dir;
    private String accessUrlPrefix;
}
