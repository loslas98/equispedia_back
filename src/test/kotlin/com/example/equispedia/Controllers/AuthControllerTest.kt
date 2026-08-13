package com.example.equispedia.Controllers

import com.example.equispedia.DTO.*
import com.example.equispedia.Services.AuthService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

class AuthControllerTest {

    private val authService: AuthService = mockk()
    private val authController = AuthController(authService)

    @Test
    fun `register should return ok`() {
        val req = RegisterRequest("test@test.com", "pass", "Test")
        val res = AuthResponse("token", UserInfoResponse("test@test.com", "Test", 1))
        every { authService.register(req) } returns res

        val response = authController.register(req)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(res, response.body)
    }
    
    @Test
    fun `register should handle exceptions`() {
        val req = RegisterRequest("test@test.com", "pass", "Test")
        every { authService.register(req) } throws Exception("Error")

        val response = authController.register(req)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
    }

    @Test
    fun `login should return ok`() {
        val req = LoginRequest("test@test.com", "pass")
        val res = AuthResponse("token", UserInfoResponse("test@test.com", "Test", 1))
        every { authService.login(req) } returns res

        val response = authController.login(req)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(res, response.body)
    }

    @Test
    fun `me should return ok`() {
        SecurityContextHolder.getContext().authentication = 
            UsernamePasswordAuthenticationToken("test@test.com", null)
            
        val res = UserInfoResponse("test@test.com", "Test", 1)
        every { authService.getMe("test@test.com") } returns res

        val response = authController.me()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(res, response.body)
    }
    
    @Test
    fun `googleLogin should return ok`() {
        val req = GoogleLoginRequest("token")
        val res = AuthResponse("token", UserInfoResponse("test@test.com", "Test", 1))
        every { authService.loginWithGoogle(req) } returns res
        
        val response = authController.googleLogin(req)
        
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(res, response.body)
    }
}
