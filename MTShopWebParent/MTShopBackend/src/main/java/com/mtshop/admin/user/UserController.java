package com.mtshop.admin.user;

import com.mtshop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/*
 * view -> controller -> service -> repository -> entity
 * @Controller is a special case of @Component, use at presentation layer and only apply to class
 * @GetMapping map to http get
 * Model is used to pass data between view and controller, It acts as a data container of the application
 */

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users") // http://localhost:8080/MTShopAdmin/users
    public String listAll(Model model) {
        List<User> listUsers = userService.listAll();
        model.addAttribute("listUsers", listUsers); // = request.setAttribute() of HttpServletRequest

        return "users"; // map file users.html
    }
}
