package com.example.equispedia.DTO

import java.math.BigDecimal

data class RoomTypeRequest(
    val propertyId: Int,
    val name: String,
    val basePricePerNight: BigDecimal,
    val maxOccupancyAdults: Int,
    val maxOccupancyChildren: Int,
    val sqMeters: Int? = null,
    val isRefundable: Boolean = true,
    val freeCancellationDays: Int? = null,
    val cancellationPolicyDetails: String? = null,
    val amenityIds: List<Int> = emptyList()
)

data class RoomTypeResponse(
    val id: Int,
    val propertyId: Int,
    val name: String,
    val basePricePerNight: BigDecimal,
    val maxOccupancyAdults: Int,
    val maxOccupancyChildren: Int,
    val sqMeters: Int?,
    val isRefundable: Boolean,
    val freeCancellationDays: Int?,
    val cancellationPolicyDetails: String?,
    val amenities: List<AmenityResponse>
)
