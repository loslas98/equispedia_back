package com.example.equispedia.Services

import com.example.equispedia.DTO.UserCreateRequest
import com.example.equispedia.Models.User
import com.example.equispedia.Repository.PropertyRepository
import com.example.equispedia.Repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.util.Optional

class UserServiceTest {

    private val userRepository: UserRepository = mockk()
    private val propertyRepository: PropertyRepository = mockk()
    private val userService = UserService(userRepository, propertyRepository)

    @Test
    fun `createUser should save and return user response`() {
        val req = UserCreateRequest(fullName = "Test User", email = "test@example.com", passwordHash = "hash", isMember = true)
        val user = User(id = 1, fullName = req.fullName, email = req.email, passwordHash = req.passwordHash, isMember = req.isMember)
        
        every { userRepository.save(any()) } returns user

        val result = userService.createUser(req)

        assertNotNull(result)
        assertEquals(1, result.id)
        assertEquals("Test User", result.fullName)
        verify(exactly = 1) { userRepository.save(any()) }
    }

    @Test
    fun `getUser should return user response if found`() {
        val user = User(id = 1, fullName = "Test User", email = "test@example.com", passwordHash = "hash")
        every { userRepository.findById(1) } returns Optional.of(user)

        val result = userService.getUser(1)

        assertNotNull(result)
        assertEquals("test@example.com", result?.email)
    }

    @Test
    fun `getUser should return null if not found`() {
        every { userRepository.findById(1) } returns Optional.empty()

        val result = userService.getUser(1)

        assertEquals(null, result)
    }
}
