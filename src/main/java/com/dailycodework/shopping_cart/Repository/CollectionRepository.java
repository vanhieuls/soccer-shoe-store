package com.dailycodework.shopping_cart.Repository;

import com.dailycodework.shopping_cart.Entity.Collections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<Collections, Long> {
    // Additional query methods can be defined here if needed
    boolean existsByName(String name);
    @Query("select c from Collections c where c.name like %?1%")
    Optional<List<Collections>> SearchByName(String name);
}
