package com.example.equispedia.Controllers

import com.example.equispedia.DTO.HotelSearchRequest
import com.example.equispedia.DTO.HotelSearchResult
import com.example.equispedia.DTO.PropertyAvailabilityResponse
import com.example.equispedia.DTO.PropertyDetailResponse
import com.example.equispedia.DTO.PropertyRequest
import com.example.equispedia.DTO.PropertyResponse
import com.example.equispedia.Services.PropertyService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
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

    @Test
    fun `createProperty should return ok`() {
        val req = mockk<PropertyRequest>(relaxed = true)
        val res = mockk<PropertyResponse>(relaxed = true)
        every { propertyService.createProperty(req) } returns res
        
        val response = propertyController.createProperty(req)
        
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(res, response.body)
    }

    @Test
    fun `getProperty should return property`() {
        val res = mockk<PropertyResponse>(relaxed = true)
        every { propertyService.getProperty(1) } returns res
        
        val response = propertyController.getProperty(1, null)
        
        assertEquals(res, response)
    }
    
    @Test
    fun `getProperty with include should return detailed property`() {
        val res = mockk<PropertyDetailResponse>(relaxed = true)
        every { propertyService.getPropertyWithIncludes(1, "rooms") } returns res
        
        val response = propertyController.getProperty(1, "rooms")
        
        assertEquals(res, response)
    }

    @Test
    fun `getAllProperties should return list`() {
        val res = listOf(mockk<PropertyResponse>(relaxed = true))
        every { propertyService.getAllProperties() } returns res
        
        val response = propertyController.getAllProperties()
        
        assertEquals(1, response.size)
        assertEquals(res, response)
    }

    @Test
    fun `checkAvailability should return response`() {
        val res = mockk<PropertyAvailabilityResponse>(relaxed = true)
        every { propertyService.checkAvailability(1, any(), any(), 2) } returns res
        
        val response = propertyController.checkAvailability(1, "2024-01-01", "2024-01-05", 2)
        
        assertEquals(res, response)
    }
}
