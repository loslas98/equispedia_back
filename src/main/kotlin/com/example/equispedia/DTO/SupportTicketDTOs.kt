package com.example.equispedia.DTO

data class CreateTicketRequest(
    val fullName: String,
    val email: String,
    val subject: String,
    val message: String,
    val userId: Int? = null
)
