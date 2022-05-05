package com.mtshop.admin.user;

import com.mtshop.admin.FileUploadUtil;
import com.mtshop.common.entity.Role;
import com.mtshop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

/*
 * view -> controller -> service -> repository -> entity
 * - @Controller is a special case of @Component, use at presentation layer and only apply to class
 * - @GetMapping map to http get
 * - @PostMapping map to http post
 * - @PathVariable bound with uri
 * - Model is an object attached to each response.
 * -- contains the return information and the Template Engine will extract the information into html and send it to the user.
 * -- It can be understood as the Context of Thymeleaf and stores information as key-values.
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

//      User(null,...,true,...)
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle", "Create New User");

        return "user_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes,
                           @RequestParam("image") MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);

            User savedUser = userService.save(user);

            String uploadDir = "images/user-photos/" + savedUser.getId();

            FileUploadUtil.cleanDir(uploadDir); // remove ảnh cũ khi có ảnh mới
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            if (user.getPhotos().isEmpty())
                user.setPhotos(null);
            userService.save(user);
        }

        redirectAttributes.addFlashAttribute("message", "User đã được lưu thành công !");

        return "redirect:/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.get(id);
            List<Role> listRoles = userService.listRoles();

            model.addAttribute("user", user);
            model.addAttribute("listRoles", listRoles);
            model.addAttribute("pageTitle", "Edit User (ID: " + id + ")");

            return "user_form";
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            userService.delete(id);

            redirectAttributes.addFlashAttribute("message", "User " + id + " xoá thành công !");
        } catch (UserNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable(name = "id") Integer id,
                                          @PathVariable(name = "status") boolean enabled,
                                          RedirectAttributes redirectAttributes) {
        userService.updateUserEnabledStatus(id, enabled);

        String status = enabled ? "enabled" : "disabled";
        String message = "The user id " + id + " has been " + status;

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/users";
    }
}
