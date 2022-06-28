package com.mtshop.product;

import com.mtshop.category.CategoryService;
import com.mtshop.common.entity.Category;
import com.mtshop.common.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping("/c/{category_alias}")
    public String viewCategoryFirstPage(@PathVariable("category_alias") String alias, Model model) {
        return viewCategoryByPage(alias, 1, model);
    }

    @GetMapping("/c/{category_alias}/page/{pageNumber}")
    public String viewCategoryByPage(@PathVariable("category_alias") String alias, @PathVariable("pageNumber") int pageNum,
                                     Model model) {
        Category category = categoryService.getCategory(alias);

        if (category == null)
            return "error/404";

        List<Category> listCategoryParents = categoryService.getCategoryParents(category);
        Page<Product> pageProducts = productService.listByCategory(pageNum, category.getId());
        List<Product> listProducts = pageProducts.getContent();

        long startElementOfPage = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
        long endElementOfPage = startElementOfPage + ProductService.PRODUCTS_PER_PAGE - 1;

        if (endElementOfPage > pageProducts.getTotalElements()) {
            endElementOfPage = pageProducts.getTotalElements();
        }

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", pageProducts.getTotalPages());
        model.addAttribute("totalItems", pageProducts.getTotalElements());
        model.addAttribute("startCount", startElementOfPage);
        model.addAttribute("endCount", endElementOfPage);
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("pageTitle", category.getName());
        model.addAttribute("listCategoryParents", listCategoryParents);

        return "products_by_category";
    }
}
