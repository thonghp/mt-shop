package com.mtshop.admin.shippingrate;

import com.mtshop.common.entity.Country;
import com.mtshop.common.entity.ShippingRate;
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
public class ShippingRateController {

    @Autowired
    private ShippingRateService service;

    @GetMapping("/shipping_rates")
    public String listFirstPage(Model model) {
        return listByPage(1, model, "country", "asc", null);
    }

    @GetMapping("/shipping_rates/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNumber") int pageNum, Model model,
                             @RequestParam(value = "sortField") String sortField,
                             @RequestParam(value = "sortType") String sortType,
                             @RequestParam(value = "keyword", required = false) String keyword) {

        Page<ShippingRate> page = service.listByPage(pageNum, sortField, sortType, keyword);
        List<ShippingRate> shippingRates = page.getContent();

        long startElementOfPage = (pageNum - 1) * ShippingRateService.RATES_PER_PAGE + 1;
        long endElementOfPage = startElementOfPage + ShippingRateService.RATES_PER_PAGE - 1;

        if (endElementOfPage > page.getTotalElements()) {
            endElementOfPage = page.getTotalElements();
        }

        String reverseSortType = sortType.equals("asc") ? "desc" : "asc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("startCount", startElementOfPage);
        model.addAttribute("endCount", endElementOfPage);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortType", sortType);
        model.addAttribute("reverseSortType", reverseSortType);
        model.addAttribute("keyword", keyword);
        model.addAttribute("shippingRates", shippingRates);
        model.addAttribute("moduleURL", "/shipping_rates");

        return "shipping_rates/shipping_rates";
    }

    @GetMapping("/shipping_rates/new")
    public String newRate(Model model) {
        List<Country> listCountries = service.listAllCountries();

        model.addAttribute("rate", new ShippingRate());
        model.addAttribute("listCountries", listCountries);
        model.addAttribute("pageTitle", "Tạo mới phí vận chuyển");

        return "shipping_rates/shipping_rate_form";
    }

    @PostMapping("/shipping_rates/save")
    public String saveRate(ShippingRate rate, RedirectAttributes ra) {
        try {
            service.save(rate);
            ra.addFlashAttribute("message", "Phí vận chuyển đã được lưu thành công.");
        } catch (ShippingRateAlreadyExistsException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/shipping_rates";
    }

    @GetMapping("/shipping_rates/edit/{id}")
    public String editRate(@PathVariable(name = "id") Integer id,
                           Model model, RedirectAttributes ra) {
        try {
            ShippingRate rate = service.get(id);
            List<Country> listCountries = service.listAllCountries();

            model.addAttribute("listCountries", listCountries);
            model.addAttribute("rate", rate);
            model.addAttribute("pageTitle", "Chỉnh sửa (ID: " + id + ")");

            return "shipping_rates/shipping_rate_form";
        } catch (ShippingRateNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return "redirect:/shipping_rates";
        }
    }

    @GetMapping("/shipping_rates/cod/{id}/enabled/{supported}")
    public String updateCODSupport(@PathVariable(name = "id") Integer id,
                                   @PathVariable(name = "supported") Boolean supported,
                                   Model model, RedirectAttributes ra) {
        try {
            service.updateCODSupport(id, supported);
            ra.addFlashAttribute("message", "Hỗ trợ COD cho phí vận chuyển có id là " + id +
                    " đã được cập nhật.");
        } catch (ShippingRateNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/shipping_rates";
    }

    @GetMapping("/shipping_rates/delete/{id}")
    public String deleteRate(@PathVariable(name = "id") Integer id,
                             Model model, RedirectAttributes ra) {
        try {
            service.delete(id);
            ra.addFlashAttribute("message", "Phí vận chuyển có id là " + id +
                    " đã được xoá thành công.");
        } catch (ShippingRateNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/shipping_rates";
    }
}
