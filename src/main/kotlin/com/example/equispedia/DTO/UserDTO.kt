package com.example.equispedia.DTO

import java.time.Instant

data class UserCreateRequest(
    val fullName: String,
    val email: String,
    val passwordHash: String,
    val isMember: Boolean = false
)

data class UserResponse(
    val id: Int,
    val fullName: String,
    val email: String,
    val isMember: Boolean,
    val loyaltyPoints: Int,
    val createdAt: Instant
)
