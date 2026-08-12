package com.example.equispedia.Controllers

import com.example.equispedia.DTO.UserCreateRequest
import com.example.equispedia.DTO.UserResponse
import com.example.equispedia.Services.UserService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.security.Principal

class UserControllerTest {

    private val userService: UserService = mockk()
    private val userController = UserController(userService)

    @Test
    fun `createUser should return user response`() {
        val req = UserCreateRequest("Test User", "test@example.com", "hash", true)
        val res = UserResponse(1, "Test User", "test@example.com", true, 0, java.time.Instant.now())
        every { userService.createUser(req) } returns res

        val response = userController.createUser(req)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(res, response.body)
    }

    @Test
    fun `getUser should return ok if found`() {
        val res = UserResponse(1, "Test User", "test@example.com", true, 0, java.time.Instant.now())
        every { userService.getUser(1) } returns res

        val response = userController.getUser(1)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(res, response.body)
    }

    @Test
    fun `getUser should return not found if null`() {
        every { userService.getUser(1) } returns null

        val response = userController.getUser(1)

        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
    }
}
