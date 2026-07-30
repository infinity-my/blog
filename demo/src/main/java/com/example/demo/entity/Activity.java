package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action;

    private String actionColor;

    private String title;

    private String date;

    private int sortOrder;

    public Activity() {}

    public Activity(String action, String actionColor, String title, String date, int sortOrder) {
        this.action = action;
        this.actionColor = actionColor;
        this.title = title;
        this.date = date;
        this.sortOrder = sortOrder;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getActionColor() { return actionColor; }
    public void setActionColor(String actionColor) { this.actionColor = actionColor; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
