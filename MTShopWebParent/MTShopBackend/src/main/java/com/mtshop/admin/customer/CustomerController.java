package com.mtshop.admin.customer;

import com.mtshop.common.entity.Customer;
import com.mtshop.common.exception.CustomerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CustomerController {

    private String defaultRedirectURL = "redirect:/customers/page/1?sortField=firstName&sortType=asc";

    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public String listFirstPage(Model model) {
        return listByPage(1, model, "firstName", "asc", null);
    }

    @GetMapping("/customers/page/{pageNumber}")
    public String listByPage(@PathVariable(name = "pageNumber") int pageNum, Model model,
                             @RequestParam("sortField") String sortField, @RequestParam("sortType") String sortType,
                             @RequestParam(value = "keyword", required = false) String keyword) {
        Page<Customer> page = customerService.listByPage(pageNum, sortField, sortType, keyword);
        List<Customer> listCustomers = page.getContent();

        long startElementOfPage = (pageNum - 1) * customerService.CUSTOMERS_PER_PAGE + 1;
        long endElementOfPage = startElementOfPage + customerService.CUSTOMERS_PER_PAGE - 1;

        if (endElementOfPage > page.getTotalElements()) {
            endElementOfPage = page.getTotalElements();
        }

        String reverseSortType = "desc".equals(sortType) ? "asc" : "desc";

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startElementOfPage);
        model.addAttribute("endCount", endElementOfPage);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listCustomers", listCustomers);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortType", sortType);
        model.addAttribute("reverseSortType", reverseSortType);
        model.addAttribute("keyword", keyword);
        model.addAttribute("moduleURL", "/customers");

        return "customers/customers";
    }

    @GetMapping("/customers/{id}/enabled/{status}")
    public String updateCustomerEnabledStatus(@PathVariable(name = "id") Integer id,
                                              @PathVariable(name = "status") boolean enabled,
                                              RedirectAttributes redirectAttributes) {
        customerService.updateCustomerEnabledStatus(id, enabled);

        String status = enabled ? "kích hoạt" : "vô hiệu hoá";
        String message = "Khách hàng có id là " + id + " đã được " + status;

        redirectAttributes.addFlashAttribute("message", message);

        return defaultRedirectURL;
    }

    @GetMapping("/customers/detail/{id}")
    public String viewCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            Customer customer = customerService.get(id);
            model.addAttribute("customer", customer);

            return "customers/customer_detail_modal";
        } catch (CustomerNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());

            return defaultRedirectURL;
        }
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable Integer id, RedirectAttributes ra) {
        try {
            customerService.delete(id);
            ra.addFlashAttribute("message", "Khách hàng có id là " + id +
                    " đã được xoá thành công.");

        } catch (CustomerNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
        }

        return defaultRedirectURL;
    }
}
