package com.example.equispedia.Controllers

import com.example.equispedia.DTO.PropertySummaryResponse
import com.example.equispedia.DTO.UserCreateRequest
import com.example.equispedia.DTO.UserResponse
import com.example.equispedia.Services.BookingService
import com.example.equispedia.Services.UserService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.security.Principal

class UserControllerTest {

    private val userService: UserService = mockk()
    private val bookingService: BookingService = mockk()
    private val userController = UserController(userService, bookingService)

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

    @Test
    fun `toggleFavorite should return ok`() {
        every { userService.toggleFavorite(1, 2) } returns true

        val response = userController.toggleFavorite(1, 2)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(true, response.body?.get("isFavorite"))
    }

    @Test
    fun `getMyFavorites should return favorites`() {
        val res = listOf(PropertySummaryResponse(id = 1, name = "Hotel", propertyType = "Hotel", region = "City", starRating = 5, basePricePerNight = 100.0, mainImageUrl = null))
        every { userService.getMyFavorites("test@example.com") } returns res

        val principal = Principal { "test@example.com" }
        val response = userController.getMyFavorites(principal)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(res, response.body)
    }

    @Test
    fun `getMyBookings should return bookings`() {
        val res = listOf(mockk<com.example.equispedia.DTO.BookingResponse>(relaxed = true))
        every { bookingService.getMyBookings("test@example.com") } returns res

        val principal = Principal { "test@example.com" }
        val response = userController.getMyBookings(principal)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(res, response.body)
    }
}
