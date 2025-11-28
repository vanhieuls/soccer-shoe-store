package com.dailycodework.shopping_cart.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.engine.internal.Cascade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String code;
    BigDecimal discountAmount;
    boolean percentTage;
    int usageLimit;
    int usedCount;
    LocalDateTime startDate;
    LocalDateTime endDate;
    boolean active;
    BigDecimal minOrderAmount;      // ✅ Đơn hàng tối thiểu
    BigDecimal maxDiscountAmount; // ✅ Mức giảm tối đa (rất quan trọng)
     // Danh sách người dùng đã sử dụng voucher
    int pointRequired; // Số điểm cần thiết để sử dụng voucher
    @ManyToMany (cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "voucher_user",
            joinColumns = @JoinColumn(name = "voucher_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    Set<User> users; // Danh sách người dùng đã sử dụng voucher

}
//public ResponseEntity<Object> deleteRole(Long id) {
//2
//    if(roleRepository.findById(id).isPresent()){
//3
//        if(roleRepository.getOne(id).getUsers().size() == 0) {
//4
//            roleRepository.deleteById(id);
//5
//            if (roleRepository.findById(id).isPresent()) {
//6
//                return ResponseEntity.unprocessableEntity().body("Failed to delete the specified record");
//7
//            } else return ResponseEntity.ok().body("Successfully deleted specified record");
//8
//        } else return ResponseEntity.unprocessableEntity().body("Failed to delete,  Please delete the users associated with this role");
//9
//    } else
//10
//        return ResponseEntity.unprocessableEntity().body("No Records Found");
//11
//}