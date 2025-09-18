package com.dailycodework.shopping_cart.Repository;

import com.dailycodework.shopping_cart.DTO.Dto.PaymentDto;
import com.dailycodework.shopping_cart.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderCode(Long orderCode);
}
