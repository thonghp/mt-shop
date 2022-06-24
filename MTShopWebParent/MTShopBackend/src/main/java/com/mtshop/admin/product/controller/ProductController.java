package com.mtshop.admin.product.controller;

import com.mtshop.admin.FileUploadUtil;
import com.mtshop.admin.brand.BrandNotFoundException;
import com.mtshop.admin.brand.BrandService;
import com.mtshop.admin.category.CategoryService;
import com.mtshop.admin.product.ProductService;
import com.mtshop.common.entity.Brand;
import com.mtshop.common.entity.Category;
import com.mtshop.common.entity.Product;
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

import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @GetMapping("/products")
    public String listFirstPage(Model model) {
        List<Product> listProducts = productService.listAll();
        model.addAttribute("listProducts", listProducts);

//        return listByPage(1, model, "name", "asc", null);
        return "products/products";
    }


//    @Autowired
//    private CategoryService categoryService;
//
//    @GetMapping("/brands")
//    public String listFirstPage(Model model) {
//        return listByPage(1, model, "name", "asc", null);
//    }
//
//    @GetMapping("/brands/page/{pageNumber}")
//    public String listByPage(@PathVariable(name = "pageNumber") int pageNum, Model model,
//                             @RequestParam(value = "sortField") String sortField,
//                             @RequestParam(value = "sortType") String sortType,
//                             @RequestParam(value = "keyword", required = false) String keyword) {
//        Page<Brand> page = brandService.listByPage(pageNum, sortField, sortType, keyword);
//        List<Brand> listBrands = page.getContent();
//
//        long startElementOfPage = (pageNum - 1) * BrandService.BRAND_PER_PAGE + 1;
//        long endElementOfPage = startElementOfPage + BrandService.BRAND_PER_PAGE - 1;
//
//        if (endElementOfPage > page.getTotalElements()) {
//            endElementOfPage = page.getTotalElements();
//        }
//
//        String reverseSortType = sortType.equals("asc") ? "desc" : "asc";
//
//        model.addAttribute("currentPage", pageNum);
//        model.addAttribute("totalPages", page.getTotalPages());
//        model.addAttribute("totalItems", page.getTotalElements());
//        model.addAttribute("startCount", startElementOfPage);
//        model.addAttribute("endCount", endElementOfPage);
//        model.addAttribute("sortField", sortField);
//        model.addAttribute("sortType", sortType);
//        model.addAttribute("reverseSortType", reverseSortType);
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("listBrands", listBrands);
//
//        return "brands/brands";
//    }

    @GetMapping("/products/new")
    public String newProduct(Model model) {
        List<Brand> brands = brandService.listAll();

        Product product = new Product();
        product.setEnabled(true);
        product.setInStock(true);

        model.addAttribute("product", product);
        model.addAttribute("listBrands", brands);
        model.addAttribute("pageTitle", "Tạo sản phẩm mới");

        return "products/product_form";
    }

    @PostMapping("/products/save")
    public String saveProduct(Product product, RedirectAttributes redirectAttributes,
                              @RequestParam(value = "fileImage", required = false) MultipartFile multipartFile) throws IOException {
//        if (!multipartFile.isEmpty()) {
//            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//            brand.setLogo(fileName);
//
//            Brand savedBrand = brandService.save(brand);
//
//            String uploadDir = "images/brand-images/" + savedBrand.getId();
//
//            FileUploadUtil.cleanDir(uploadDir);
//            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
//        } else {
//            brandService.save(brand);
//        }

        redirectAttributes.addFlashAttribute("message", "Nhãn hiệu đã được lưu thành công !");

        return "redirect:/products";
    }

//    @GetMapping("/brands/edit/{id}")
//    public String editBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
//        try {
//            Brand brand = brandService.get(id);
//            List<Category> listCategories = categoryService.listCategoriesUsedInForm();
//
//            model.addAttribute("brand", brand);
//            model.addAttribute("listCategories", listCategories);
//            model.addAttribute("pageTitle", "Sửa nhãn hiệu (ID: " + id + ")");
//
//            return "brands/brand_form";
//        } catch (BrandNotFoundException e) {
//            redirectAttributes.addFlashAttribute("message", e.getMessage());
//
//            return "redirect:/brands";
//        }
//    }
//
//    @GetMapping("/brands/delete/{id}")
//    public String deleteBrand(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
//        try {
//            brandService.delete(id);
//            String brandDir = "images/brand-images/" + id;
//            FileUploadUtil.removeDir(brandDir);
//
//            redirectAttributes.addFlashAttribute("message", "Nhãn hiệu có id  " + id +
//                    " được xoá thành công !");
//        } catch (BrandNotFoundException e) {
//            redirectAttributes.addFlashAttribute("message", e.getMessage());
//        }
//
//        return "redirect:/brands";
//    }
}
