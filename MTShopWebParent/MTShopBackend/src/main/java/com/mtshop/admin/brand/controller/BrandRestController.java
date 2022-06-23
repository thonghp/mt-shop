package com.mtshop.admin.brand.controller;

import com.mtshop.admin.brand.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BrandRestController {

    @Autowired
    private BrandService categoryService;

//    @PostMapping("/brands/check_unique")
//    public String checkUnique(@RequestParam(value = "id", required = false) Integer id,
//                              @RequestParam("name") String name,
//                              @RequestParam("alias") String alias) {
//        return categoryService.checkUnique(id, name, alias);
//    }
}
