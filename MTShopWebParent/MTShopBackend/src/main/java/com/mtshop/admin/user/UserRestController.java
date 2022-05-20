package com.mtshop.admin.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/*
 * @RestController returns data as JSON
 * @Param bind the name in the query
 */
@RestController
public class UserRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/users/check_email")
    public String checkDuplicateEmail(@RequestParam(value = "id", required = false) Integer id, @RequestParam("email") String email) {
        return userService.isEmailUnique(id, email) ? "OK" : "Duplicated";
    }
}
