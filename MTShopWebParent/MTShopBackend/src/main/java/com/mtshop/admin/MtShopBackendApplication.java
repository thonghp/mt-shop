package com.mtshop.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

/*
 * - container is ApplicationContext
 * - dependency is Bean, Beans managed inside ApplicationContext are all singleton
 * - initially run the function run() then it will detect the bean, the bean that matches will create the instance and
 * then put in the context
 */

@SpringBootApplication
@EntityScan({"com.mtshop.common.entity", "com.mtshop.admin.user"})
public class MtShopBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MtShopBackendApplication.class, args);
    }
}
