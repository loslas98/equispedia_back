package com.example.equispedia.Controllers

import com.example.equispedia.DTO.BookingRequest
import com.example.equispedia.DTO.BookingResponse
import com.example.equispedia.Models.BookingStatus
import com.example.equispedia.Services.BookingService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.time.LocalDate

class BookingControllerTest {

    private val bookingService: BookingService = mockk()
    private val bookingController = BookingController(bookingService)

    @Test
    fun `createBooking should return ok and booking response`() {
        val request = BookingRequest(
            userId = 1,
            propertyId = 1,
            checkIn = LocalDate.now(),
            checkOut = LocalDate.now().plusDays(2),
            totalPrice = 150.0.toBigDecimal(),
            items = emptyList()
        )
        
        val expectedResponse = BookingResponse(
            id = 1,
            userId = 1,
            propertyId = 1,
            propertyName = "Mock Property",
            propertyImageUrl = "mock_image_url.jpg",
            checkIn = request.checkIn,
            checkOut = request.checkOut,
            totalPrice = request.totalPrice,
            status = BookingStatus.PENDING,
            createdAt = java.time.Instant.now(),
            items = emptyList()
        )

        every { bookingService.createBooking(request) } returns expectedResponse

        val response = bookingController.createBooking(request)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(expectedResponse, response.body)
    }
}
