package com.mtshop.admin.order;

import com.mtshop.admin.brand.BrandService;
import com.mtshop.admin.setting.SettingService;
import com.mtshop.common.entity.order.Order;
import com.mtshop.common.entity.setting.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private SettingService settingService;

    @GetMapping("/orders")
    public String listFirstPage(Model model, HttpServletRequest request) {
        return listByPage(1, model, "orderTime", "desc", null, request);
    }

    @GetMapping("/orders/page/{pageNum}")
    public String listByPage(@PathVariable(name = "pageNumber") int pageNum, Model model,
                             @RequestParam(value = "sortField") String sortField,
                             @RequestParam(value = "sortType") String sortType,
                             @RequestParam(value = "keyword", required = false) String keyword,
                             HttpServletRequest request) {

        Page<Order> page = orderService.listByPage(pageNum, sortField, sortType, keyword);
        List<Order> listOrders = page.getContent();

        long startElementOfPage = (pageNum - 1) * OrderService.ORDERS_PER_PAGE + 1;
        long endElementOfPage = startElementOfPage + OrderService.ORDERS_PER_PAGE - 1;

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
        model.addAttribute("listOrders", listOrders);
        model.addAttribute("moduleURL", "/orders");

        loadCurrencySetting(request);

        return "orders/orders";
    }

    private void loadCurrencySetting(HttpServletRequest request) {
        List<Setting> currencySettings = settingService.getCurrencySettings();

        for (Setting setting : currencySettings) {
            request.setAttribute(setting.getKey(), setting.getValue());
        }
    }

    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetails(@PathVariable("id") Integer id, Model model,
                                   RedirectAttributes ra, HttpServletRequest request) {
        try {
            Order order = orderService.get(id);
            loadCurrencySetting(request);
            model.addAttribute("order", order);

            return "orders/order_details_modal";
        } catch (OrderNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());

            return "redirect:/orders";
        }

    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            orderService.delete(id);

            ra.addFlashAttribute("message", "Đơn hàng có id là " + id + " đã được xoá.");
        } catch (OrderNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/orders";
    }

}
