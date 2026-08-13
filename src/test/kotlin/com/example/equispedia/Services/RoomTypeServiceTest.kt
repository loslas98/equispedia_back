package com.example.equispedia.Services

import com.example.equispedia.DTO.RoomTypeRequest
import com.example.equispedia.Models.Property
import com.example.equispedia.Models.RoomType
import com.example.equispedia.Repository.AmenityRepository
import com.example.equispedia.Repository.PropertyRepository
import com.example.equispedia.Repository.RoomTypeRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.Optional

class RoomTypeServiceTest {

    private val roomTypeRepository: RoomTypeRepository = mockk()
    private val propertyRepository: PropertyRepository = mockk()
    private val amenityRepository: AmenityRepository = mockk()
    private val amenityService: AmenityService = mockk(relaxed = true)

    private val roomTypeService = RoomTypeService(
        roomTypeRepository, propertyRepository, amenityRepository, amenityService
    )

    @Test
    fun `createRoomType should save and return RoomTypeResponse`() {
        // Arrange
        val req = RoomTypeRequest(
            propertyId = 1,
            name = "Deluxe",
            basePricePerNight = BigDecimal("150.0"),
            maxOccupancyAdults = 2,
            maxOccupancyChildren = 1,
            sqMeters = 30,
            isRefundable = true,
            freeCancellationDays = 3,
            cancellationPolicyDetails = "Refundable up to 3 days",
            amenityIds = listOf()
        )

        val property = mockk<Property>(relaxed = true)
        every { property.id } returns 1

        val roomType = RoomType(
            id = 1,
            property = property,
            name = "Deluxe",
            basePricePerNight = req.basePricePerNight,
            maxOccupancyAdults = req.maxOccupancyAdults,
            maxOccupancyChildren = req.maxOccupancyChildren
        )

        every { propertyRepository.findById(1) } returns Optional.of(property)
        every { amenityRepository.findAllById(any<List<Int>>()) } returns emptyList()
        every { roomTypeRepository.save(any()) } returns roomType

        // Act
        val response = roomTypeService.createRoomType(req)

        // Assert
        assertEquals(1, response.id)
        assertEquals("Deluxe", response.name)
        verify { roomTypeRepository.save(any()) }
    }
}
