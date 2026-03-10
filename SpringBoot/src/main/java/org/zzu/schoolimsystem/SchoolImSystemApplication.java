package org.zzu.schoolimsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



import org.mybatis.spring.annotation.MapperScan; // 1. 记得导入这个包
@SpringBootApplication
@MapperScan("org.zzu.schoolimsystem.mapper")
public class SchoolImSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchoolImSystemApplication.class, args);
    }

}
