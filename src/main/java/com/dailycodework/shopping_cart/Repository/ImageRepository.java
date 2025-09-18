package com.dailycodework.shopping_cart.Repository;

import com.dailycodework.shopping_cart.Entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image,Long> {

}
