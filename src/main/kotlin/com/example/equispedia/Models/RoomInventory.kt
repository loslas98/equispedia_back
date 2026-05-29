package com.example.equispedia.Models

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "room_inventory")
data class RoomInventory(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id")
    val roomType: RoomType,

    val date: LocalDate,

    val roomsAvailable: Int,

    val priceModifier: BigDecimal? = null,

    val discountPercentage: BigDecimal? = null
)
