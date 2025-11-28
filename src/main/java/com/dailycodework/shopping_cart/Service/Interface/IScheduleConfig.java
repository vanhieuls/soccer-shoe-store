package com.dailycodework.shopping_cart.Service.Interface;

import com.dailycodework.shopping_cart.DTO.Request.ScheduleConfigRequest;
import com.dailycodework.shopping_cart.Entity.ScheduleConfig;

public interface IScheduleConfig {
    ScheduleConfig getCronExpression(String taskName);
    void updateCronExpression(String taskName, String cronExpression);
    ScheduleConfig createScheduleConfig(ScheduleConfigRequest request);
    void deleteScheduleConfig(Long id);
}
