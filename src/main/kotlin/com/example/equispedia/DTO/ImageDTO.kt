package com.example.equispedia.DTO

data class ImageRequest(
    val propertyId: Int,
    val roomTypeId: Int? = null,
    val url: String,
    val altText: String? = null,
    val isMain: Boolean = false
)

data class ImageResponse(
    val id: Int,
    val propertyId: Int,
    val roomTypeId: Int?,
    val url: String,
    val altText: String?,
    val isMain: Boolean
)
