package com.mtshop.admin.category.controller;

import com.mtshop.admin.category.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/categories/check_name")
    public String checkDuplicateEmail(@RequestParam(value = "id", required = false) Integer id, @RequestParam("name") String email) {
        return categoryService.isNameUnique(id, email) ? "OK" : "Duplicated";
    }
}
