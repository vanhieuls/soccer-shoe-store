package com.dailycodework.shopping_cart.Entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Blob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String fileName;
    String fileType;
    @Lob
    Blob image;
    String downloadUrl;
    @ManyToOne
    @JoinColumn(name="product_id")
    Product product;

}
