package com.dailycodework.shopping_cart.Repository;

import com.dailycodework.shopping_cart.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
