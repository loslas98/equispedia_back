package com.example.equispedia.Controllers

import com.example.equispedia.DTO.HotelSearchRequest
import com.example.equispedia.DTO.HotelSearchResult
import com.example.equispedia.Services.PropertyService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class PropertyControllerTest {

    private val propertyService: PropertyService = mockk()
    private val propertyController = PropertyController(propertyService)

    @Test
    fun `searchHotels should return results`() {
        val result = HotelSearchResult(
            id = 1,
            name = "Test Hotel",
            region = mockk(relaxed = true),
            propertyType = mockk(relaxed = true),
            tags = emptyList(),
            amenities = emptyList(),
            starRating = 5,
            latitude = 0.0.toBigDecimal(),
            longitude = 0.0.toBigDecimal(),
            lowestPricePerNight = 100.0.toBigDecimal(),
            thumbnailUrl = "img.jpg",
            petsAllowed = true,
            childrenAllowed = true,
            contactlessCheckIn = false,
            hasFreeCancellation = true
        )
        
        every { propertyService.searchHotels(any<HotelSearchRequest>()) } returns listOf(result)

        val results = propertyController.searchHotels(1, "2024-01-01", "2024-01-05", 2, 0)

        assertEquals(1, results.size)
        assertEquals("Test Hotel", results[0].name)
    }
}
