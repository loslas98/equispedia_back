package com.example.equispedia.DTO

data class AmenityCategoryRequest(
    val name: String
)

data class AmenityCategoryResponse(
    val id: Int,
    val name: String
)

data class AmenityRequest(
    val categoryId: Int,
    val name: String,
    val iconName: String? = null
)

data class AmenityResponse(
    val id: Int,
    val category: AmenityCategoryResponse,
    val name: String,
    val iconName: String?
)
