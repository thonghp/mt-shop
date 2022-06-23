package com.mtshop.admin.brand.controller;

import com.mtshop.admin.FileUploadUtil;
import com.mtshop.admin.brand.BrandNotFoundException;
import com.mtshop.admin.brand.BrandPageInfo;
import com.mtshop.admin.brand.BrandService;
import com.mtshop.admin.brand.export.BrandCSVExporter;
import com.mtshop.admin.category.CategoryService;
import com.mtshop.common.entity.Brand;
import com.mtshop.common.entity.Category;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
public class BrandController {

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/brands")
    public String listFirstPage(@RequestParam(value = "sortType", required = false) String sortType, Model model) {
//        return listByPage(1, model, sortType, null);
        List<Brand> listBrands = brandService.listByPage(sortType, 1, new BrandPageInfo(), null);
        model.addAttribute("listBrands", listBrands);

        return "brands/brands";
    }

//    @GetMapping("/brands/page/{pageNumber}")
//    public String listByPage(@PathVariable(name = "pageNumber") int pageNum, Model model,
//                             @RequestParam(value = "sortType", required = false) String sortType,
//                             @RequestParam(value = "keyword", required = false) String keyword) {
//        if (sortType == null || sortType.isEmpty()) {
//            sortType = "asc";
//        }
//
//        BrandPageInfo pageInfo = new BrandPageInfo();
//        List<Category> listBrands = brandService.listByPage(sortType, pageNum, pageInfo, keyword);
//
//        long startElementOfPage = (pageNum - 1) * BrandService.ROOT_CATEGORIES_PER_PAGE + 1;
//        long endElementOfPage = startElementOfPage + BrandService.ROOT_CATEGORIES_PER_PAGE - 1;
//
//        if (endElementOfPage > pageInfo.getTotalElements()) {
//            endElementOfPage = pageInfo.getTotalElements();
//        }
//
//        String reverseSortType = sortType.equals("asc") ? "desc" : "asc";
//
//        model.addAttribute("totalPages", pageInfo.getTotalPages());
//        model.addAttribute("totalItems", pageInfo.getTotalElements());
//        model.addAttribute("currentPage", pageNum);
//        model.addAttribute("sortField", "name");
//        model.addAttribute("sortType", sortType);
//        model.addAttribute("keyword", keyword);
//        model.addAttribute("startCount", startElementOfPage);
//        model.addAttribute("endCount", endElementOfPage);
//        model.addAttribute("listBrands", listBrands);
//        model.addAttribute("reverseSortType", reverseSortType);
//
//        return "brands/brands";
//    }

    @GetMapping("/brands/new")
    public String newBrand(Model model) {
        Category category = new Category();
        category.setEnabled(true);
        List<Category> listCategories = categoryService.listCategoriesUsedInForm();

        model.addAttribute("brand", new Brand());
        model.addAttribute("pageTitle", "Tạo nhãn hiệu mới");
        model.addAttribute("listCategories", listCategories);

        return "brands/brand_form";
    }

    @PostMapping("/brands/save")
    public String saveBrand(Brand brand, RedirectAttributes redirectAttributes,
                           @RequestParam(value = "fileImage", required = false) MultipartFile multipartFile) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setLogo(fileName);

            Brand savedBrand = brandService.save(brand);

            String uploadDir = "images/brand-images/" + savedBrand.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        } else {
            brandService.save(brand);
        }

        redirectAttributes.addFlashAttribute("message", "Nhãn hiệu đã được lưu thành công !");

        return "redirect:/brands";
    }

    @GetMapping("/brands/edit/{id}")
    public String editBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Brand brand = brandService.get(id);
            List<Category> listCategories = categoryService.listCategoriesUsedInForm();

            model.addAttribute("brand", brand);
            model.addAttribute("listCategories", listCategories);
            model.addAttribute("pageTitle", "Sửa nhãn hiệu (ID: " + id + ")");

            return "brands/brand_form";
        } catch (BrandNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());

            return "redirect:/brands";
        }
    }

    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            brandService.delete(id);
            String brandDir = "images/brand-images/" + id;
            FileUploadUtil.removeDir(brandDir);

            redirectAttributes.addFlashAttribute("message", "Nhãn hiệu có id  " + id +
                    " được xoá thành công !");
        } catch (BrandNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/brands";
    }

//    @GetMapping("/brands/{id}/enabled/{status}")
//    public String updateCategoryEnabledStatus(@PathVariable(name = "id") Integer id,
//                                              @PathVariable(name = "status") boolean enabled,
//                                              RedirectAttributes redirectAttributes) {
//        brandService.updateCategoryEnabledStatus(id, enabled);
//
//        String status = enabled ? "kích hoạt" : "vô hiệu hoá";
//        String message = "Thể loại có id là " + id + " đã được " + status;
//
//        redirectAttributes.addFlashAttribute("message", message);
//
//        return "redirect:/brands";
//    }

//    @GetMapping("/brands/export/csv")
//    public void exportToCSV(HttpServletResponse response) throws IOException {
//        List<Category> listBrands = brandService.listBrandsUsedInForm();
//        BrandCSVExporter exporter = new BrandCSVExporter();
//        exporter.export(listBrands, response);
//    }
}
