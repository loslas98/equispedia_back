package com.example.equispedia.Controllers

import com.example.equispedia.DTO.AuthResponse
import com.example.equispedia.DTO.LoginRequest
import com.example.equispedia.DTO.RegisterRequest
import com.example.equispedia.DTO.UserInfoResponse
import com.example.equispedia.Services.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = ["*"]) // Allow React frontend
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(authService.register(request))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(authService.login(request))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @GetMapping("/me")
    fun me(): ResponseEntity<Any> {
        // Since this endpoint is protected by our JwtAuthenticationFilter,
        // we are guaranteed to have the email in the security context.
        val email = SecurityContextHolder.getContext().authentication.principal as String
        
        return try {
            ResponseEntity.ok(authService.getMe(email))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }
}
