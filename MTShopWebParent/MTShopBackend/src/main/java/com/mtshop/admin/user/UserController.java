package com.mtshop.admin.user;

import com.mtshop.admin.FileUploadUtil;
import com.mtshop.admin.user.export.UserCSVExporter;
import com.mtshop.admin.user.export.UserExcelExporter;
import com.mtshop.admin.user.export.UserPDFExporter;
import com.mtshop.common.entity.Role;
import com.mtshop.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
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
    public String listFirstPage(Model model) {
        return listByPage(1, model, "id", "asc", null);
    }

    @GetMapping("/users/page/{pageNumber}")
    public String listByPage(@PathVariable(name = "pageNumber") int pageNum, Model model,
                             @RequestParam("sortField") String sortField, @RequestParam("sortType") String sortType,
                             @RequestParam(value = "keyword", required = false) String keyword) {
        Page<User> page = userService.listByPage(pageNum, sortField, sortType, keyword);
        List<User> listUsers = page.getContent();

        long startElementOfPage = (pageNum - 1) * UserService.USER_PER_PAGE + 1;
        long endElementOfPage = startElementOfPage + UserService.USER_PER_PAGE - 1;

        if (endElementOfPage > page.getTotalElements()) {
            endElementOfPage = page.getTotalElements();
        }

        String reverseSortType = "desc".equals(sortType) ? "asc" : "desc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startElementOfPage);
        model.addAttribute("endCount", endElementOfPage);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listUsers", listUsers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortType", sortType);
        model.addAttribute("reverseSortType", reverseSortType);
        model.addAttribute("keyword", keyword);

        return "users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        User user = new User();
        List<Role> listRoles = userService.listRoles();

        user.setEnabled(true);

//      User(null,...,true,...)
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle", "Tạo người dùng mới");

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

        return getRedirectURLToAffectedUser(user);
    }

    private String getRedirectURLToAffectedUser(User user) {
        String firstPartOfEmail = user.getEmail().split("@")[0];
        return "redirect:/users/page/1?sortField=id&sortType=asc&keyword=" + firstPartOfEmail;
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.get(id);
            List<Role> listRoles = userService.listRoles();

            model.addAttribute("user", user);
            model.addAttribute("listRoles", listRoles);
            model.addAttribute("pageTitle", "Sửa người dùng (ID: " + id + ")");

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

        String status = enabled ? "kích hoạt" : "vô hiệu hoá";
        String message = "User " + id + " đã được " + status;

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/users";
    }

    @GetMapping("/users/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<User> listUsers = userService.listAll();
        UserCSVExporter exporter = new UserCSVExporter();
        exporter.export(listUsers, response);
    }

    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        List<User> listUsers = userService.listAll();
        UserExcelExporter exporter = new UserExcelExporter();
        exporter.export(listUsers, response);
    }

    @GetMapping("/users/export/pdf")
    public void exportToPDF(HttpServletResponse response) throws IOException {
        List<User> listUsers = userService.listAll();
        UserPDFExporter exporter = new UserPDFExporter();
        exporter.export(listUsers, response);
    }
}
