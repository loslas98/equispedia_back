package com.example.equispedia.DTO

data class LoginRequest(
    val email: String,
    val passwordHash: String // We will use this field for password input to match frontend/models (even if it's plain text in the request)
)

data class RegisterRequest(
    val email: String,
    val passwordHash: String,
    val fullName: String
)

data class AuthResponse(
    val token: String,
    val user: UserInfoResponse
)

data class UserInfoResponse(
    val email: String,
    val fullName: String,
    val id: Int
)

data class GoogleLoginRequest(
    val token: String
)
