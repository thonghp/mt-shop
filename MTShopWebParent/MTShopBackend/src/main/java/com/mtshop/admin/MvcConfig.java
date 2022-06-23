package com.mtshop.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        User
        String userDirName = "images/user-photos";
        Path userPhotosDir = Paths.get(userDirName);

        String userPhotosPath = userPhotosDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/" + userDirName + "/**").addResourceLocations("file:/" + userPhotosPath + "/");

//        Category
        String categoryDirName = "images/category-images";
        Path categoryImageDir = Paths.get(categoryDirName);

        String categoryImagePath = categoryImageDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/" + categoryDirName + "/**").addResourceLocations("file:/" + categoryImagePath + "/");
    }
}
