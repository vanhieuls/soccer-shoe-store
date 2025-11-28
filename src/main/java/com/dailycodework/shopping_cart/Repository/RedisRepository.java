package com.dailycodework.shopping_cart.Repository;

import com.dailycodework.shopping_cart.Entity.RedisToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository extends CrudRepository<RedisToken, String> {

}
