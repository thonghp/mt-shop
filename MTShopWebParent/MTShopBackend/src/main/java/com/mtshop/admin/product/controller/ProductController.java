package com.mtshop.admin.product.controller;

import com.mtshop.admin.FileUploadUtil;
import com.mtshop.admin.brand.BrandNotFoundException;
import com.mtshop.admin.brand.BrandService;
import com.mtshop.admin.category.CategoryService;
import com.mtshop.admin.product.ProductNotFoundException;
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
                              @RequestParam(value = "fileImage", required = false) MultipartFile mainImageMultipart,
                              @RequestParam(value = "extraImage", required = false) MultipartFile[] extraImageMultiparts) throws IOException {
        setMainImageName(product, mainImageMultipart);
        setExtraImageName(product, extraImageMultiparts);

        Product savedProduct = productService.save(product);

        saveUploadedImages(mainImageMultipart, extraImageMultiparts, savedProduct);

        redirectAttributes.addFlashAttribute("message", "Sản phẩm đã được lưu thành công !");

        return "redirect:/products";
    }

    private void saveUploadedImages(MultipartFile mainImageMultipart, MultipartFile[] extraImageMultiparts,
                                    Product savedProduct) throws IOException {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
            String uploadDir = "images/product-images/" + savedProduct.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, mainImageMultipart);
        }

        if (extraImageMultiparts.length > 0) {
            String uploadDir = "images/product-images/" + savedProduct.getId() + "/extras";

            for (MultipartFile multipartFile : extraImageMultiparts) {
                if (multipartFile.isEmpty()) continue;

                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
            }
        }
    }

    private void setExtraImageName(Product product, MultipartFile[] extraImageMultiparts) {
        if (extraImageMultiparts.length > 0) {
            for (MultipartFile multipartFile : extraImageMultiparts) {
                if (!multipartFile.isEmpty()) {
                    String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                    product.addExtraImage(fileName);
                }
            }
        }
    }

    private void setMainImageName(Product product, MultipartFile mainImageMultipart) {
        if (!mainImageMultipart.isEmpty()) {
            String fileName = StringUtils.cleanPath(mainImageMultipart.getOriginalFilename());
            product.setMainImage(fileName);
        }
    }

    @GetMapping("/products/edit/{id}")
    public String editProduct(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.get(id);
            List<Product> listProducts = productService.listAll();

            model.addAttribute("brand", product);
            model.addAttribute("listProducts", listProducts);
            model.addAttribute("pageTitle", "Sửa nhãn hiệu (ID: " + id + ")");

            return "products/product_form";
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/products";
        }
    }

    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            productService.delete(id);

            String producImagesDir = "images/product-images/" + id;
            String productExtraImagesDir = "images/product-images/" + id + "/extras";
            FileUploadUtil.removeDir(productExtraImagesDir);
            FileUploadUtil.removeDir(producImagesDir);

            redirectAttributes.addFlashAttribute("message", "Nhãn hiệu có id  " + id +
                    " được xoá thành công !");
        } catch (ProductNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/products";
    }

    @GetMapping("/products/{id}/enabled/{status}")
    public String updateProductEnabledStatus(@PathVariable(name = "id") Integer id,
                                             @PathVariable(name = "status") boolean enabled,
                                             RedirectAttributes redirectAttributes) {
        productService.updateProductEnabledStatus(id, enabled);

        String status = enabled ? "kích hoạt" : "vô hiệu hoá";
        String message = "Sản phẩm có id là " + id + " đã được " + status;

        redirectAttributes.addFlashAttribute("message", message);

        return "redirect:/products";
    }
}
