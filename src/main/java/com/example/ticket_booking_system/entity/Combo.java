package com.example.ticket_booking_system.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "tblCombo")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Combo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long comboID;

    @Column(nullable = false, unique = true, columnDefinition = "NVARCHAR(MAX)")
    private String name; // Tên combo, vd: "Combo Bắp Nước Lớn"

    @Column(nullable = false)
    private Float unitPrice;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description; // Mô tả, vd: "1 Bắp rang + 1 Nước ngọt"

    @Column
    private String image; // URL hình ảnh của combo

    @Column(nullable = false)
    @Builder.Default
    private boolean isActive = true; // Chỉ hiển thị các combo đang còn bán
}
