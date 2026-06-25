package com.example.equispedia.Services

import com.example.equispedia.DTO.AuthResponse
import com.example.equispedia.DTO.LoginRequest
import com.example.equispedia.DTO.RegisterRequest
import com.example.equispedia.DTO.UserInfoResponse
import com.example.equispedia.Models.User
import com.example.equispedia.Repository.UserRepository
import com.example.equispedia.config.JwtUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    fun register(request: RegisterRequest): AuthResponse {
        if (userRepository.findByEmail(request.email) != null) {
            throw IllegalArgumentException("Email is already taken")
        }

        val user = User(
            email = request.email,
            passwordHash = passwordEncoder.encode(request.passwordHash)!!, // Encrypt plain password
            fullName = request.fullName
        )
        
        val savedUser = userRepository.save(user)
        val token = jwtUtil.generateToken(savedUser.email)

        return AuthResponse(
            token = token,
            user = UserInfoResponse(savedUser.email, savedUser.fullName, savedUser.id)
        )
    }

    fun login(request: LoginRequest): AuthResponse {
        val user = userRepository.findByEmail(request.email)
            ?: throw IllegalArgumentException("Invalid credentials")

        if (!passwordEncoder.matches(request.passwordHash, user.passwordHash)) {
            throw IllegalArgumentException("Invalid credentials")
        }

        val token = jwtUtil.generateToken(user.email)
        
        return AuthResponse(
            token = token,
            user = UserInfoResponse(user.email, user.fullName, user.id)
        )
    }

    fun getMe(email: String): UserInfoResponse {
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User not found")
            
        return UserInfoResponse(user.email, user.fullName, user.id)
    }
}
