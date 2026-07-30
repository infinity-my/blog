package com.example.demo.config;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PostRepository postRepository;
    private final ProjectRepository projectRepository;
    private final ResumeRepository resumeRepository;
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final SettingRepository settingRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(PostRepository postRepository, ProjectRepository projectRepository,
                           ResumeRepository resumeRepository, ActivityRepository activityRepository,
                           UserRepository userRepository, SettingRepository settingRepository,
                           PasswordEncoder passwordEncoder) {
        this.postRepository = postRepository;
        this.projectRepository = projectRepository;
        this.resumeRepository = resumeRepository;
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
        this.settingRepository = settingRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // 初始化管理员账号
        // ⚠️ WARNING: 默认密码 admin123 仅用于开发环境，生产部署前请务必修改！
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(new User("admin", passwordEncoder.encode("admin123"), "ADMIN"));
            System.out.println("✅ Default admin user created (admin/admin123)");
        }

        // 初始化简历密码设置
        // ⚠️ WARNING: 默认密码 123456 仅用于开发环境，生产部署前请务必修改！
        if (settingRepository.findByKey("resume_password").isEmpty()) {
            settingRepository.save(new Setting("resume_password", "123456", "简历访问密码"));
            System.out.println("✅ Default resume password set (123456)");
        }

        if (postRepository.count() > 0) return;

        // ========== Projects ==========
        projectRepository.save(new Project(
            "☕",
            "要抱抱咖啡 — 咖啡店微信小程序",
            "完整的咖啡店微信小程序系统，包含用户端点单、会员管理、门店排队，以及 Vue 3 + Element Plus 后台管理系统。支持 8 大商品分类、64 款商品，实现订单全流程管理与模拟支付。",
            "https://github.com/infinity-my/AutoFlow-My-m",
            "text-amber-700",
            1,
            List.of("微信小程序", "VUE 3", "NODE.JS", "EXPRESS", "SQLITE", "ELEMENT PLUS")
        ));

        // ========== Posts ==========
        postRepository.save(new Post(
            "coffee-shop-miniprogram",
            "要抱抱咖啡 — 咖啡店微信小程序开发记录",
            "2026-06-28",
            "完整的咖啡店微信小程序系统开发记录，包含用户端点单、会员管理、门店排队与 Vue 3 后台管理系统。支持 8 大商品分类、64 款商品，实现订单全流程管理与模拟支付。",
            "/images/coffee/banner.png",
            List.of("微信小程序", "Vue 3", "Node.js", "Express", "SQLite")
        ));

        // ========== Activities ==========
        activityRepository.save(new Activity("发布了项目", "text-amber-600", "「要抱抱咖啡」微信小程序系统", "2026-06-28 18:00", 1));

        System.out.println("✅ Seed data initialized successfully!");
    }
}
