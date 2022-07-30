package com.mtshop.customer.controller;

import com.mtshop.Utility;
import com.mtshop.common.entity.Customer;
import com.mtshop.common.exception.CustomerNotFoundException;
import com.mtshop.customer.CustomerService;
import com.mtshop.setting.EmailSettingBag;
import com.mtshop.setting.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SettingService settingService;

    @GetMapping("/forgot_password")
    public String showRequestForm() {
        return "customer/forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processRequestForm(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        try {
            String token = customerService.updateResetPasswordToken(email);
            String link = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(link, email);

            model.addAttribute("message", "Chúng tôi đã gửi một liên kết đặt lại mật khẩu đến email của bạn."
                    + " Làm ơn kiểm tra.");
        } catch (CustomerNotFoundException e) {
            model.addAttribute("error", e.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Không thể gửi email");
        }

        return "customer/forgot_password_form";
    }

    private void sendEmail(String link, String email)
            throws UnsupportedEncodingException, MessagingException {
        EmailSettingBag emailSettings = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettings);

        String toAddress = email;
        String subject = "Đây là liên kết để đặt lại mật khẩu của bạn";

        String content = "<p>Xin chao,</p>"
                + "<p>Ban da yêu cau dat lai mat khau cua mình.</p>"
                + "Nhap vao liên ket bên duoi de thay doi mat khau cua ban:</p>"
                + "<p><a href=\"" + link + "\">Thay doi mat khau cua toi</a></p>"
                + "<br>"
                + "<p>Bo qua email này neu ban nho mat khau cua mình, "
                + "hoac ban chua thuc hien yêu cau.</p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);

        helper.setText(content, true);
        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetForm(@Param("token") String token, Model model) {
        Customer customer = customerService.getByResetPasswordToken(token);
        if (customer != null) {
            model.addAttribute("token", token);
        } else {
            model.addAttribute("pageTitle", "Invalid Token");
            model.addAttribute("message", "Invalid Token");
            return "message";
        }

        return "customer/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetForm(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        try {
            customerService.updatePassword(token, password);

            model.addAttribute("pageTitle", "Đặt lại mật khẩu của bạn");
            model.addAttribute("title", "Đặt lại mật khẩu của bạn");
            model.addAttribute("message", "Bạn đã thay đổi thành công mật khẩu của bạn.");

        } catch (CustomerNotFoundException e) {
            model.addAttribute("pageTitle", "Invalid Token");
            model.addAttribute("message", e.getMessage());
        }

        return "message";
    }
}
