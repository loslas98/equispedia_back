package com.example.equispedia.DTO

import java.math.BigDecimal
import com.example.equispedia.Models.TransportMode

data class PointOfInterestRequest(
    val name: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal
)

data class PointOfInterestResponse(
    val id: Int,
    val name: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal
)

data class PropertyPOIRequest(
    val poiId: Int,
    val distanceKm: BigDecimal,
    val transportMode: TransportMode,
    val timeMins: Int
)

data class PropertyPOIResponse(
    val poi: PointOfInterestResponse,
    val distanceKm: BigDecimal,
    val transportMode: TransportMode,
    val timeMins: Int
)
