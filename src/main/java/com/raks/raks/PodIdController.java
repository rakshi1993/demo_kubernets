package com.raks.raks;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;

@RestController
public class PodIdController {

    @GetMapping("/pod")
    public String getPodName() {
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            return "Served by Pod: " + hostname;
        } catch (Exception e) {
            return "Pod name not available";
        }
    }
}
