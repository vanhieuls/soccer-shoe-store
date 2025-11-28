package com.dailycodework.shopping_cart.Entity;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Builder
@RedisHash("RedisHas")
public class RedisToken {
    @Id
    String jwtId;
    //caì đặt thời gian sống theo ngày (mặc định là giây)
    @TimeToLive(unit = TimeUnit.MILLISECONDS)
    Long expiredTime;
}
