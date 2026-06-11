package com.example.equispedia.DTO

import java.math.BigDecimal

data class HotelSearchResult(
    val id: Int,
    val name: String,
    val region: RegionResponse,
    val propertyType: PropertyTypeResponse,
    val tags: List<TagResponse>,
    val amenities: List<AmenityResponse>,
    val starRating: Int?,
    val latitude: BigDecimal,
    val longitude: BigDecimal,
    val lowestPricePerNight: BigDecimal,
    val thumbnailUrl: String?,
    val petsAllowed: Boolean,
    val childrenAllowed: Boolean,
    val contactlessCheckIn: Boolean,
    val hasFreeCancellation: Boolean
)

data class HotelSearchRequest(
    val regionId: Int,
    val checkIn: java.time.LocalDate,
    val checkOut: java.time.LocalDate,
    val adults: Int,
    val children: Int = 0
)