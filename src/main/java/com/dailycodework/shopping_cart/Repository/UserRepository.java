package com.dailycodework.shopping_cart.Repository;

import com.dailycodework.shopping_cart.Entity.User;
import com.dailycodework.shopping_cart.Entity.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail (String email);
    boolean existsByUsername (String username);
    Optional<User> findByUsername (String username);
    Optional<User> findByEmail (String email);
    Optional<User> findByResetPasswordToken (String resetPasswordToken);
    Optional<List<Voucher>> findVouchersById(Long userId);
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.cart")
    Page<User> findAllWithCart(Pageable pageable);
    @Query("SELECT u FROM User u WHERE u.username LIKE %:keyword% OR u.email LIKE %:keyword%")
    Optional<Page<User>> searchByUsernameOrEmail(@Param("keyword") String keyword, Pageable pageable);
}
