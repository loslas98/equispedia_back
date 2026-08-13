package com.example.equispedia.Services

import com.example.equispedia.DTO.LoginRequest
import com.example.equispedia.DTO.RegisterRequest
import com.example.equispedia.Models.User
import com.example.equispedia.Repository.UserRepository
import com.example.equispedia.config.JwtUtil
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder

class AuthServiceTest {

    private val userRepository: UserRepository = mockk()
    private val passwordEncoder: PasswordEncoder = mockk()
    private val jwtUtil: JwtUtil = mockk()
    private val authService = AuthService(userRepository, passwordEncoder, jwtUtil)

    @Test
    fun `register should throw exception if email is taken`() {
        // Arrange
        val request = RegisterRequest("test@example.com", "password", "Test Name")
        every { userRepository.findByEmail(request.email) } returns mockk<User>()

        // Act & Assert
        val exception = assertThrows(IllegalArgumentException::class.java) {
            authService.register(request)
        }
        assertEquals("Email is already taken", exception.message)
    }

    @Test
    fun `register should create user and return token`() {
        // Arrange
        val request = RegisterRequest("new@example.com", "password", "New User")
        every { userRepository.findByEmail(request.email) } returns null
        every { passwordEncoder.encode(request.passwordHash) } returns "encodedPassword"
        
        val savedUser = User(
            id = 1, 
            email = request.email, 
            passwordHash = "encodedPassword", 
            fullName = request.fullName
        )
        every { userRepository.save(any<User>()) } returns savedUser
        every { jwtUtil.generateToken(savedUser.email) } returns "mockedToken"

        // Act
        val response = authService.register(request)

        // Assert
        assertEquals("mockedToken", response.token)
        assertEquals(request.email, response.user.email)
        verify(exactly = 1) { userRepository.save(any<User>()) }
    }

    @Test
    fun `login should throw exception if user not found`() {
        // Arrange
        val request = LoginRequest("unknown@example.com", "password")
        every { userRepository.findByEmail(request.email) } returns null

        // Act & Assert
        val exception = assertThrows(IllegalArgumentException::class.java) {
            authService.login(request)
        }
        assertEquals("Invalid credentials", exception.message)
    }

    @Test
    fun `login should throw exception if password mismatch`() {
        // Arrange
        val request = LoginRequest("user@example.com", "wrongpassword")
        val user = User(id = 1, email = request.email, passwordHash = "encoded", fullName = "User")
        every { userRepository.findByEmail(request.email) } returns user
        every { passwordEncoder.matches(request.passwordHash, user.passwordHash) } returns false

        // Act & Assert
        val exception = assertThrows(IllegalArgumentException::class.java) {
            authService.login(request)
        }
        assertEquals("Invalid credentials", exception.message)
    }
    @Test
    fun `getMe should return user info`() {
        val user = User(id = 1, email = "test@test.com", passwordHash = "encoded", fullName = "Test User")
        every { userRepository.findByEmail("test@test.com") } returns user

        val response = authService.getMe("test@test.com")

        assertEquals("test@test.com", response.email)
        assertEquals("Test User", response.fullName)
    }

    @Test
    fun `getMe should throw exception if user not found`() {
        every { userRepository.findByEmail("notfound@test.com") } returns null

        val exception = assertThrows(IllegalArgumentException::class.java) {
            authService.getMe("notfound@test.com")
        }
        assertEquals("User not found", exception.message)
    }
}
