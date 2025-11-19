package com.raks.raks;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SuccessController {

    @GetMapping("/success")
    public String successMessage() {
        return "success";
    }
}
