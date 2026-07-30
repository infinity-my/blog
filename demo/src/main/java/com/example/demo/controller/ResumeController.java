package com.example.demo.controller;

import com.example.demo.entity.Resume;
import com.example.demo.service.ResumeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @GetMapping
    public List<Resume> getAllResumeData() {
        return resumeService.getAllResumeData();
    }

    @GetMapping("/type/{type}")
    public List<Resume> getByType(@PathVariable String type) {
        return resumeService.getByType(type);
    }
}
