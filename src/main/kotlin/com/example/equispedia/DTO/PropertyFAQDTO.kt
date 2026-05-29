package com.example.equispedia.DTO

data class PropertyFAQRequest(
    val propertyId: Int,
    val question: String,
    val answer: String
)

data class PropertyFAQResponse(
    val id: Int,
    val propertyId: Int,
    val question: String,
    val answer: String
)
