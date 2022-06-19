package com.mtshop.admin.category.controller;

import com.mtshop.admin.category.CategoryService;
import com.mtshop.admin.category.export.CategoryCSVExporter;
import com.mtshop.admin.user.UserNotFoundException;
import com.mtshop.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String listFirstPage(Model model) {
        return listByPage(1, model, "id", "asc", null);
    }

    @GetMapping("/categories/page/{pageNumber}")
    public String listByPage(@PathVariable(name = "pageNumber") int pageNum, Model model,
                             @RequestParam("sortField") String sortField, @RequestParam("sortType") String sortType,
                             @RequestParam(value = "keyword", required = false) String keyword) {
        Page<Category> page = categoryService.listByPage(pageNum, sortField, sortType, keyword);
        List<Category> listCategories = page.getContent();

        long startElementOfPage = (pageNum - 1) * categoryService.CATEGORY_PER_PAGE + 1;
        long endElementOfPage = startElementOfPage + categoryService.CATEGORY_PER_PAGE - 1;

        if (endElementOfPage > page.getTotalElements()) {
            endElementOfPage = page.getTotalElements();
        }

        String reverseSortType = "desc".equals(sortType) ? "asc" : "desc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startElementOfPage);
        model.addAttribute("endCount", endElementOfPage);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortType", sortType);
        model.addAttribute("reverseSortType", reverseSortType);
        model.addAttribute("keyword", keyword);

        return "categories/categories";
    }

    @GetMapping("/categories/new")
    public String newCategory(Model model) {
        Category category = new Category();
        category.setEnabled(true);
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        model.addAttribute("category", category);
        model.addAttribute("pageTitle", "Tạo thể loại mới");
        model.addAttribute("listCategories", listCategories);

        return "categories/category_form.html";
    }

    @PostMapping("/categories/save")
    public String saveUser(Category category, RedirectAttributes redirectAttributes) {

        categoryService.save(category);

        redirectAttributes.addFlashAttribute("message", "Thể loại đã được lưu thành công !");

        return "redirect:/categories";
    }

//    @GetMapping("/users/edit/{id}")
//    public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
//        try {
//            User user = categoryService.get(id);
//            List<Role> listRoles = categoryService.listRoles();
//
//            model.addAttribute("user", user);
//            model.addAttribute("listRoles", listRoles);
//            model.addAttribute("pageTitle", "Sửa người dùng (ID: " + id + ")");
//
//            return "users/user_form";
//        } catch (UserNotFoundException e) {
//            redirectAttributes.addFlashAttribute("message", e.getMessage());
//
//            return "redirect:/users";
//        }
//    }

//    @GetMapping("/users/delete/{id}")
//    public String deleteUser(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
//        try {
//            categoryService.delete(id);
//
//            redirectAttributes.addFlashAttribute("message", "User " + id + " xoá thành công !");
//        } catch (UserNotFoundException e) {
//            redirectAttributes.addFlashAttribute("message", e.getMessage());
//        }
//
//        return "redirect:/categories";
//    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable(name = "id") Integer id,
                                          @PathVariable(name = "status") boolean enabled,
                                          RedirectAttributes redirectAttributes) {
        categoryService.updateCategoryEnabledStatus(id, enabled);

        String status = enabled ? "kích hoạt" : "vô hiệu hoá";
        String message = "User " + id + " đã được " + status;

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/categories";
    }

    @GetMapping("/categories/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        List<Category> listCategories = categoryService.listAll();
        CategoryCSVExporter exporter = new CategoryCSVExporter();
        exporter.export(listCategories, response);
    }
}
