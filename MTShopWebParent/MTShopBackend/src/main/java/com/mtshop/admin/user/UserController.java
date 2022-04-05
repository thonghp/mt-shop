package com.mtshop.admin.user;

import com.mtshop.common.entity.Role;
import com.mtshop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/*
 * view -> controller -> service -> repository -> entity
 * @Controller is a special case of @Component, use at presentation layer and only apply to class
 * @GetMapping map to http get
 * Model is an object that stores information and is used by Template Engine to generate webpage. It can be understood
 * as the Context of Thymeleaf
 * Model stores information as key-values.
 * addFlashAttribute() stores the attribute in the flashmap, persists internally in the user session, and is deleted
 * when a redirect is requested, Multiple object types can be stored inside
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

    @GetMapping("/users/new")
    public String newUser(Model model) {
        User user = new User();
        List<Role> listRoles = userService.listRoles();

        user.setEnabled(true);

        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);

        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes) {
        userService.save(user);

        redirectAttributes.addFlashAttribute("message","The user has bean saved successfully");

        return "redirect:/users";
    }
}
