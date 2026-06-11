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
    val paymentMethods: List<PaymentMethodResponse>,
    val images: List<String>,
    val currentPrice: String?
)

@com.fasterxml.jackson.annotation.JsonInclude(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL)
data class PropertyDetailResponse(
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
    val paymentMethods: List<PaymentMethodResponse>,

    // Dynamic relationship fields
    val rooms: List<RoomTypeResponse>? = null,
    val faqs: List<PropertyFAQResponse>? = null,
    val reviews: List<ReviewResponse>? = null,
    val images: List<ImageResponse>? = null
)

data class RoomAvailabilityResponse(
    val roomTypeId: Int,
    val name: String,
    val isAvailable: Boolean,
    val totalPrice: BigDecimal,
    val pricePerNightAverage: BigDecimal
)

data class PropertyAvailabilityResponse(
    val propertyId: Int,
    val isAvailable: Boolean,
    val checkIn: java.time.LocalDate,
    val checkOut: java.time.LocalDate,
    val rooms: List<RoomAvailabilityResponse>
)

