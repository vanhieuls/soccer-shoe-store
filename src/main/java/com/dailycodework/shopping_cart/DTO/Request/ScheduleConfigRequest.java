package com.dailycodework.shopping_cart.DTO.Request;

import lombok.Builder;

@Builder
public record ScheduleConfigRequest ( String taskName, String cronExpression){

}
