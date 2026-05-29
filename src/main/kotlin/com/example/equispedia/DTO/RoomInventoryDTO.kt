package com.example.equispedia.DTO

import java.math.BigDecimal
import java.time.LocalDate

data class RoomInventoryRequest(
    val roomTypeId: Int,
    val date: LocalDate,
    val roomsAvailable: Int,
    val priceModifier: BigDecimal? = null,
    val discountPercentage: BigDecimal? = null
)

data class RoomInventoryResponse(
    val id: Int,
    val roomTypeId: Int,
    val date: LocalDate,
    val roomsAvailable: Int,
    val priceModifier: BigDecimal?,
    val discountPercentage: BigDecimal?
)
