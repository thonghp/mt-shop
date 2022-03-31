package com.mtshop.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.mtshop.common.entity", "com.mtshop.admin.user"})
public class MtShopBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MtShopBackendApplication.class, args);
    }

}
