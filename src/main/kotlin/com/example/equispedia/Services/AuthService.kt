package com.example.equispedia.Services

import com.example.equispedia.DTO.AuthResponse
import com.example.equispedia.DTO.LoginRequest
import com.example.equispedia.DTO.RegisterRequest
import com.example.equispedia.DTO.UserInfoResponse
import com.example.equispedia.DTO.GoogleLoginRequest
import com.example.equispedia.Models.User
import com.example.equispedia.Repository.UserRepository
import com.example.equispedia.config.JwtUtil
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.util.UUID

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

    fun loginWithGoogle(request: GoogleLoginRequest): AuthResponse {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.setBearerAuth(request.token)
        val entity = HttpEntity<Unit>(headers)
        
        val response = try {
            restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v3/userinfo",
                HttpMethod.GET,
                entity,
                Map::class.java
            )
        } catch (e: Exception) {
            throw IllegalArgumentException("Invalid Google token")
        }

        val body = response.body ?: throw IllegalArgumentException("Invalid Google token")
        val email = body["email"] as? String ?: throw IllegalArgumentException("Email not found in Google profile")
        val name = body["name"] as? String ?: "Google User"

        // Upsert user
        var user = userRepository.findByEmail(email)
        if (user == null) {
            val newUser = User(
                email = email,
                passwordHash = passwordEncoder.encode(UUID.randomUUID().toString())!!, // Dummy password
                fullName = name
            )
            user = userRepository.save(newUser)
        }

        val token = jwtUtil.generateToken(user.email)

        return AuthResponse(
            token = token,
            user = UserInfoResponse(user.email, user.fullName, user.id)
        )
    }
}
