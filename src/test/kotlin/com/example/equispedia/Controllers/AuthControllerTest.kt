package com.example.equispedia.Controllers

import com.example.equispedia.DTO.AuthResponse
import com.example.equispedia.DTO.LoginRequest
import com.example.equispedia.DTO.RegisterRequest
import com.example.equispedia.Services.AuthService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class AuthControllerTest {

    private val authService: AuthService = mockk()
    private val authController = AuthController(authService)

    @Test
    fun `register should return ok when service succeeds`() {
        val request = RegisterRequest("test@example.com", "password", "Test User")
        val userInfo = com.example.equispedia.DTO.UserInfoResponse("test@example.com", "Test User", 1)
        val expectedResponse = AuthResponse("token123", userInfo)
        
        every { authService.register(request) } returns expectedResponse

        val response = authController.register(request)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(expectedResponse, response.body)
    }

    @Test
    fun `login should return ok when credentials are correct`() {
        val request = LoginRequest("test@example.com", "password")
        val userInfo = com.example.equispedia.DTO.UserInfoResponse("test@example.com", "Test User", 1)
        val expectedResponse = AuthResponse("token123", userInfo)
        
        every { authService.login(request) } returns expectedResponse

        val response = authController.login(request)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(expectedResponse, response.body)
    }

    @Test
    fun `login should return bad request when service throws exception`() {
        val request = LoginRequest("test@example.com", "wrongpassword")
        
        every { authService.login(request) } throws Exception("Invalid credentials")

        val response = authController.login(request)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        val body = response.body as Map<*, *>
        assertEquals("Invalid credentials", body["error"])
    }
}
