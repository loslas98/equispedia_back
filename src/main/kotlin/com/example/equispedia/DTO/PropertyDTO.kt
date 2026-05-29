package com.example.equispedia.DTO

import java.math.BigDecimal
import java.time.LocalTime

data class PropertyRequest(
    val name: String,
    val propertyTypeId: Int,
    val regionId: Int,
    val address: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal,
    val starRating: Int? = null,
    val description: String? = null,
    val checkInStartTime: LocalTime? = null,
    val checkInEndTime: LocalTime? = null,
    val checkOutTime: LocalTime? = null,
    val minAgeCheckIn: Int? = null,
    val contactlessCheckIn: Boolean = false,
    val petsAllowed: Boolean = false,
    val childrenAllowed: Boolean = true,
    val importantInfo: String? = null,
    val tagIds: List<Int> = emptyList(),
    val amenityIds: List<Int> = emptyList(),
    val paymentMethodIds: List<Int> = emptyList()
)

data class PropertyResponse(
    val id: Int,
    val name: String,
    val propertyType: PropertyTypeResponse,
    val region: RegionResponse,
    val address: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal,
    val starRating: Int?,
    val description: String?,
    val checkInStartTime: LocalTime?,
    val checkInEndTime: LocalTime?,
    val checkOutTime: LocalTime?,
    val minAgeCheckIn: Int?,
    val contactlessCheckIn: Boolean,
    val petsAllowed: Boolean,
    val childrenAllowed: Boolean,
    val importantInfo: String?,
    val tags: List<TagResponse>,
    val amenities: List<AmenityResponse>,
    val paymentMethods: List<PaymentMethodResponse>
)
