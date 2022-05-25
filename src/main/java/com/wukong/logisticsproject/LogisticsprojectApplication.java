package com.wukong.logisticsproject;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.wukong.logisticsproject.mapper")
public class LogisticsprojectApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogisticsprojectApplication.class, args);
        System.out.println("启动成功--------------------------------------------");
    }

}
