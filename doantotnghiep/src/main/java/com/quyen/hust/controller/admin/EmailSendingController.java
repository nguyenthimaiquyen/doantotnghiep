package com.quyen.hust.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/mailSending")
public class EmailSendingController {


    @GetMapping
    public String getMailSendingManagementPage(Model model) {
        return "admin/mail-sending/manage-mail-sending";
    }
}
