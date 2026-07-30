package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resumes")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // "education", "skill", "project", "publication", "interest"

    private String title;

    private String subtitle;

    private String period;

    @Column(length = 1000)
    private String description;

    private String category;

    @ElementCollection
    @CollectionTable(name = "resume_items", joinColumns = @JoinColumn(name = "resume_id"))
    @Column(name = "item")
    private List<String> items = new ArrayList<>();

    public Resume() {}

    public Resume(String type, String title, String subtitle, String period, String description, String category, List<String> items) {
        this.type = type;
        this.title = title;
        this.subtitle = subtitle;
        this.period = period;
        this.description = description;
        this.category = category;
        this.items = items;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getSubtitle() { return subtitle; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }

    public String getPeriod() { return period; }
    public void setPeriod(String period) { this.period = period; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public List<String> getItems() { return items; }
    public void setItems(List<String> items) { this.items = items; }
}
