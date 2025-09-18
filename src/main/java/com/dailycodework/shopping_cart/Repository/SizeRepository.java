package com.dailycodework.shopping_cart.Repository;

import com.dailycodework.shopping_cart.Entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {
    boolean existsBySize(int size);
}
