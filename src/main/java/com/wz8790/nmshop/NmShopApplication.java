package com.wz8790.nmshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.wz8790.nmshop.dao")
public class NmShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(NmShopApplication.class, args);
    }

}
