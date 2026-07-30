package com.example.demo.service;

import com.example.demo.entity.Resume;
import com.example.demo.repository.ResumeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeService {

    private final ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public List<Resume> getAllResumeData() {
        return resumeRepository.findAll();
    }

    public List<Resume> getByType(String type) {
        return resumeRepository.findByType(type);
    }

    public Resume getResumeById(Long id) {
        return resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found: " + id));
    }

    public Resume createResume(Resume resume) {
        return resumeRepository.save(resume);
    }

    public Resume updateResume(Long id, Resume updatedResume) {
        Resume resume = getResumeById(id);
        resume.setType(updatedResume.getType());
        resume.setTitle(updatedResume.getTitle());
        resume.setSubtitle(updatedResume.getSubtitle());
        resume.setPeriod(updatedResume.getPeriod());
        resume.setDescription(updatedResume.getDescription());
        resume.setCategory(updatedResume.getCategory());
        resume.setItems(updatedResume.getItems());
        return resumeRepository.save(resume);
    }

    public void deleteResume(Long id) {
        resumeRepository.deleteById(id);
    }
}
