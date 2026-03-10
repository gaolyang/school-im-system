package org.zzu.schoolimsystem.config;

/**
 * ClassName: SecurityConfig
 * Package: org.zzu.schoolimsystem.config
 * Description:
 *
 * @Author gly
 * @Create 2026/2/12 10:52
 * @Version 1.0
 */
//package org.zzu.schoolimsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // 1. 关闭 CSRF 保护，允许 POST 请求
                .authorizeHttpRequests()
                .anyRequest().permitAll(); // 2. (开发阶段) 允许所有接口直接访问，不需要登录
        return http.build();
    }
}
