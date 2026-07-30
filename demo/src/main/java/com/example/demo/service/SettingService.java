package com.example.demo.service;

import com.example.demo.entity.Setting;
import com.example.demo.repository.SettingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SettingService {

    private final SettingRepository settingRepository;

    public SettingService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    public Optional<Setting> getSetting(String key) {
        return settingRepository.findByKey(key);
    }

    public String getSettingValue(String key, String defaultValue) {
        return settingRepository.findByKey(key)
                .map(Setting::getValue)
                .orElse(defaultValue);
    }

    public Setting saveSetting(Setting setting) {
        Optional<Setting> existing = settingRepository.findByKey(setting.getKey());
        if (existing.isPresent()) {
            Setting s = existing.get();
            s.setValue(setting.getValue());
            s.setDescription(setting.getDescription());
            return settingRepository.save(s);
        }
        return settingRepository.save(setting);
    }

    public Setting updateResumePassword(String newPassword) {
        Setting setting = new Setting("resume_password", newPassword, "简历访问密码");
        return saveSetting(setting);
    }
}
