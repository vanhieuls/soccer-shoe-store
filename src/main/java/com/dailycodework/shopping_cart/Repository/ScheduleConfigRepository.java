package com.dailycodework.shopping_cart.Repository;

import com.dailycodework.shopping_cart.Entity.ScheduleConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleConfigRepository extends JpaRepository<ScheduleConfig,Long> {
    Optional<ScheduleConfig> findByTaskName(String taskName);
    boolean existsByTaskName(String taskName);
}
