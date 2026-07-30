package com.example.demo.service;

import com.example.demo.entity.Activity;
import com.example.demo.repository.ActivityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public List<Activity> getAllActivities() {
        return activityRepository.findAllByOrderBySortOrderDesc();
    }

    public Activity getActivityById(Long id) {
        return activityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Activity not found: " + id));
    }

    public Activity createActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    public Activity updateActivity(Long id, Activity updatedActivity) {
        Activity activity = getActivityById(id);
        activity.setAction(updatedActivity.getAction());
        activity.setActionColor(updatedActivity.getActionColor());
        activity.setTitle(updatedActivity.getTitle());
        activity.setDate(updatedActivity.getDate());
        activity.setSortOrder(updatedActivity.getSortOrder());
        return activityRepository.save(activity);
    }

    public void deleteActivity(Long id) {
        activityRepository.deleteById(id);
    }
}
