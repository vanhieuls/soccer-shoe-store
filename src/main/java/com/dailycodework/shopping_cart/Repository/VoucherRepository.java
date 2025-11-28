package com.dailycodework.shopping_cart.Repository;

import com.dailycodework.shopping_cart.Entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher,Long> {
    Optional<Voucher> findByCode (String code);

}
