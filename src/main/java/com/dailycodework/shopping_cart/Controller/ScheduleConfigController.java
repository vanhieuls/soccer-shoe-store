package com.dailycodework.shopping_cart.Controller;

import com.dailycodework.shopping_cart.DTO.Request.ScheduleConfigRequest;
import com.dailycodework.shopping_cart.DTO.Response.ApiResponse;
import com.dailycodework.shopping_cart.Entity.ScheduleConfig;
import com.dailycodework.shopping_cart.Exception.AppException;
import com.dailycodework.shopping_cart.Exception.ErrorCode;
import com.dailycodework.shopping_cart.Service.Interface.IScheduleConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedule-config")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Schedule Config")
public class ScheduleConfigController {
    IScheduleConfig scheduleConfigService;

    @GetMapping()
    public ApiResponse<ScheduleConfig> getScheduleConfig(@RequestParam String taskName) {
        return ApiResponse.<ScheduleConfig>builder()
                .code(200)
                .message("Get cron expression successfully")
                .result(scheduleConfigService.getCronExpression(taskName))
                .build();
    }

    @PutMapping()
    public ApiResponse<Void> updateScheduleConfig(@RequestParam String taskName,@RequestParam String cronExpression) {
        scheduleConfigService.updateCronExpression(taskName, cronExpression);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Update cron expression successfully")
                .build();
    }

    @PostMapping()
    public ApiResponse<ScheduleConfig> createScheduleConfig(@RequestBody ScheduleConfigRequest request) {
        return ApiResponse.<ScheduleConfig>builder()
                .code(200)
                .message("Create schedule config successfully")
                .result(scheduleConfigService.createScheduleConfig(request))
                .build();
    }

    @DeleteMapping()
    public ApiResponse<Void> deleteScheduleConfig(@PathVariable Long id) {
        scheduleConfigService.deleteScheduleConfig(id);
        return ApiResponse.<Void>builder()
                .code(200)
                .message("Delete schedule config successfully")
                .build();
    }
}
