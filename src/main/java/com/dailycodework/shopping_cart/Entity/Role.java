package com.dailycodework.shopping_cart.Entity;

import com.dailycodework.shopping_cart.Enum.Roles;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity
public class Role {
    @Id
    @Enumerated(EnumType.STRING)
    Roles name;
    String description;
//    @ManyToMany
//    Set<Permission> permissions = new HashSet<>();
}
