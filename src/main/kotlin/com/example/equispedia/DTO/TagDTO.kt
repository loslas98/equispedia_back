package com.example.equispedia.DTO

data class TagRequest(
    val name: String
)

data class TagResponse(
    val id: Int,
    val name: String
)
