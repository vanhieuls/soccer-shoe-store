package com.dailycodework.shopping_cart.Repository;

import com.dailycodework.shopping_cart.DTO.Dto.AddressDto;
import com.dailycodework.shopping_cart.Entity.Address;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    // Additional query methods can be defined here if needed
    Optional<List<Address>> findByUserId(Long userId);

}
