package com.example.equispedia.DTO

data class PropertyTypeRequest(
    val name: String
)

data class PropertyTypeResponse(
    val id: Int,
    val name: String
)
