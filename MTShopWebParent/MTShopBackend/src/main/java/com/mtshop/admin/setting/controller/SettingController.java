package com.mtshop.admin.setting.controller;

import com.mtshop.admin.FileUploadUtil;
import com.mtshop.admin.setting.CurrencyRepository;
import com.mtshop.admin.setting.GeneralSettingBag;
import com.mtshop.admin.setting.SettingService;
import com.mtshop.common.entity.Brand;
import com.mtshop.common.entity.Currency;
import com.mtshop.common.entity.Setting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class SettingController {

    @Autowired
    private SettingService service;

    @Autowired
    private CurrencyRepository currencyRepo;


    @GetMapping("/settings")
    public String listAll(Model model) {
        List<Setting> listSettings = service.listAllSettings();
        List<Currency> listCurrencies = currencyRepo.findAllByOrderByNameAsc();

        model.addAttribute("listCurrencies", listCurrencies);

        for (Setting setting : listSettings) {
            model.addAttribute(setting.getKey(), setting.getValue());
        }

        return "settings/settings";
    }

    @PostMapping("/settings/save_general")
    public String saveGeneralSetting(@RequestParam("fileImage") MultipartFile multipartFile, HttpServletRequest request,
                                     RedirectAttributes redirectAttributes) throws IOException {
        GeneralSettingBag generalSettings = service.getGeneralSettings();

        saveSiteLogo(multipartFile, generalSettings);
        saveCurrencySymbol(request, generalSettings);
        updateSettingValueFromForm(request, generalSettings.list());
//            brandService.save(brand);

        redirectAttributes.addFlashAttribute("message", "Cài đặt chung đã được lưu thành công !");

        return "redirect:/settings";
    }

    private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag generalSettings) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String value = "images/site-logo/" + fileName;
            generalSettings.updateSiteLogo(value);

            String uploadDir = "images/site-logo/";
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
    }

    private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag generalSettings) {
        Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
        Optional<Currency> findByIdResult = currencyRepo.findById(currencyId);

        if (findByIdResult.isPresent()) {
            Currency currency = findByIdResult.get();
            generalSettings.updateCurrencySymbol(currency.getSymbol());
        }
    }

    private void updateSettingValueFromForm(HttpServletRequest request, List<Setting> listSettings) {
        for (Setting setting : listSettings) {
            String value = request.getParameter(setting.getKey());
            if (value != null)
                setting.setValue(value);
        }

        service.saveAll(listSettings);
    }
}