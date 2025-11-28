package com.dailycodework.shopping_cart.Service.ImpInterface;

import com.dailycodework.shopping_cart.DTO.Request.ScheduleConfigRequest;
import com.dailycodework.shopping_cart.Entity.ScheduleConfig;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Repository.ScheduleConfigRepository;
import com.dailycodework.shopping_cart.Service.Interface.IScheduleConfig;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ImpScheduleConfig implements IScheduleConfig {
    ScheduleConfigRepository scheduleConfigRepository;
    ImpOrder order;
    ThreadPoolTaskScheduler taskScheduler;
//    Nếu không có Nonfinal sẽ Lỗi "Cannot assign a value to final variable 'scheduledTask'" vì biến
//    scheduledTask được khai báo là final trong lớp ImpScheduleConfig, và khi sử dụng từ khóa final,
//    bạn không thể thay đổi giá trị của biến sau khi nó đã được gán một giá trị ban đầu.
    @NonFinal
    ScheduledFuture<?> scheduledTask;

    @Override
    public ScheduleConfig getCronExpression(String taskName) {
        return scheduleConfigRepository.findByTaskName(taskName).orElseThrow(()->new AppException(ErrorCode.SCHEDULE_CONFIG_NOT_EXIST));
    }

    @Override
    public void updateCronExpression(String taskName, String cronExpression) {
        ScheduleConfig scheduleConfig = scheduleConfigRepository.findByTaskName(taskName).orElseThrow(()->new AppException(ErrorCode.SCHEDULE_CONFIG_NOT_EXIST));
        scheduleConfig.setCronExpression(cronExpression);
        scheduleConfigRepository.save(scheduleConfig);
        restartScheduledTask(cronExpression);
    }
    private void restartScheduledTask(String cronExpression){
        if(scheduledTask != null){
            scheduledTask.cancel(false);
        }
        scheduledTask = taskScheduler.schedule(order::processPendingOrders, new CronTrigger(cronExpression));
    }
    @Override
    public ScheduleConfig createScheduleConfig(ScheduleConfigRequest request) {
        if(scheduleConfigRepository.existsByTaskName(request.taskName())) {
            throw new AppException(ErrorCode.SCHEDULE_CONFIG_EXISTED);
        }
        ScheduleConfig scheduleConfig = new ScheduleConfig();
        scheduleConfig.setTaskName(request.taskName());
        scheduleConfig.setCronExpression(request.cronExpression());
        return scheduleConfigRepository.save(scheduleConfig);
    }

    @Override
    public void deleteScheduleConfig(Long id) {
        ScheduleConfig scheduleConfig = scheduleConfigRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.SCHEDULE_CONFIG_NOT_EXIST));
        scheduleConfigRepository.deleteById(scheduleConfig.getId());
    }
}
