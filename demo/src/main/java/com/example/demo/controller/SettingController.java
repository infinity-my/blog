package com.example.demo.controller;

import com.example.demo.service.SettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/settings")
public class SettingController {

    private final SettingService settingService;

    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @PostMapping("/verify-resume-password")
    public ResponseEntity<Map<String, Boolean>> verifyResumePassword(@RequestBody Map<String, String> body) {
        String inputPassword = body.get("password");
        String storedPassword = settingService.getSettingValue("resume_password", "123456");
        
        boolean isValid = storedPassword.equals(inputPassword);
        return ResponseEntity.ok(Map.of("valid", isValid));
    }
}
