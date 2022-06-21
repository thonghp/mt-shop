package com.mtshop.admin.category.controller;

import com.mtshop.admin.category.CategoryNotFoundException;
import com.mtshop.admin.category.CategoryPageInfo;
import com.mtshop.admin.category.CategoryService;
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

import java.util.List;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categories")
    public String listFirstPage(@RequestParam(value = "sortType", required = false) String sortType, Model model) {
        return listByPage(1, model, sortType);
    }

    @GetMapping("/categories/page/{pageNumber}")
    public String listByPage(@PathVariable(name = "pageNumber") int pageNum, Model model,
                             @RequestParam(value = "sortType", required = false) String sortType) {
        if (sortType == null || sortType.isEmpty()) {
            sortType = "asc";
        }

        CategoryPageInfo pageInfo = new CategoryPageInfo();
        List<Category> listCategories = categoryService.listByPage(sortType, pageNum, pageInfo);

        String reverseSortType = sortType.equals("asc") ? "desc" : "asc";

        model.addAttribute("totalPages", pageInfo.getTotalPages());
        model.addAttribute("totalItems", pageInfo.getTotalElements());
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortField", "name");
        model.addAttribute("sortType", sortType);

        model.addAttribute("listCategories", listCategories);
        model.addAttribute("reverseSortType", reverseSortType);

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

    @GetMapping("/categories/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Category category = categoryService.get(id);
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();

            model.addAttribute("category", category);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("pageTitle", "Sửa thể loại (ID: " + id + ")");

            return "categories/category_form";
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/categories";
        }
    }

    @GetMapping("/categories/delete/{id}")
    public String deleteCategory(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.delete(id);

            redirectAttributes.addFlashAttribute("message", "Thể loại có id  " + id +
                    " được xoá thành công !");
        } catch (CategoryNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/categories";
    }

    @GetMapping("/categories/{id}/enabled/{status}")
    public String updateUserEnabledStatus(@PathVariable(name = "id") Integer id,
                                          @PathVariable(name = "status") boolean enabled,
                                          RedirectAttributes redirectAttributes) {
        categoryService.updateCategoryEnabledStatus(id, enabled);

        String status = enabled ? "kích hoạt" : "vô hiệu hoá";
        String message = "Thể loại có id là " + id + " đã được " + status;

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/categories";
    }

//    @GetMapping("/categories/export/csv")
//    public void exportToCSV(HttpServletResponse response) throws IOException {
//        List<Category> listCategories = categoryService.listAll();
//        CategoryCSVExporter exporter = new CategoryCSVExporter();
//        exporter.export(listCategories, response);
//    }
}
