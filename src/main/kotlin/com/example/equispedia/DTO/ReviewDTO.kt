package com.example.equispedia.DTO

import com.example.equispedia.Models.TravelerType
import java.time.Instant

data class ReviewRequest(
    val propertyId: Int,
    val userId: Int,
    val ratingOverall: Int,
    val ratingCleanliness: Int,
    val ratingService: Int,
    val ratingFacilities: Int,
    val comment: String? = null,
    val travelerType: TravelerType
)

data class ReviewResponse(
    val id: Int,
    val propertyId: Int,
    val userId: Int,
    val ratingOverall: Int,
    val ratingCleanliness: Int,
    val ratingService: Int,
    val ratingFacilities: Int,
    val comment: String?,
    val travelerType: TravelerType,
    val isVerified: Boolean,
    val createdAt: Instant
)
