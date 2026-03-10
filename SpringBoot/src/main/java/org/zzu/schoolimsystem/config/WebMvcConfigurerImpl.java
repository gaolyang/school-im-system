package org.zzu.schoolimsystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry; // 1. 导入 CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;

@Configuration
public class WebMvcConfigurerImpl implements WebMvcConfigurer {

    // === 1. 配置静态资源映射 (图片预览) ===
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectPath = System.getProperty("user.dir");
        String uploadPath = "file:" + projectPath + File.separator + "uploads" + File.separator;

        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }

    // === 2. 新增：配置全局跨域 (解决上传和历史记录失败的问题) ===
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 允许所有接口
                .allowedOriginPatterns("*") // 允许所有来源 (Vue前端)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // 允许的方法
                .allowCredentials(true) // 允许携带 Cookie/凭证
                .maxAge(3600);
    }
}
