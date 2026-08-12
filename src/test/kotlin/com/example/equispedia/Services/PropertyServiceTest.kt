package com.example.equispedia.Services

import com.example.equispedia.Models.Property
import com.example.equispedia.Repository.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.util.Optional

class PropertyServiceTest {

    private val propertyRepository: PropertyRepository = mockk()
    private val propertyTypeRepository: PropertyTypeRepository = mockk()
    private val regionRepository: RegionRepository = mockk()
    private val tagRepository: TagRepository = mockk()
    private val amenityRepository: AmenityRepository = mockk()
    private val paymentMethodRepository: PaymentMethodRepository = mockk()
    private val roomTypeRepository: RoomTypeRepository = mockk()
    private val roomInventoryRepository: RoomInventoryRepository = mockk()

    private val propertyService = PropertyService(
        propertyRepository,
        propertyTypeRepository,
        regionRepository,
        tagRepository,
        amenityRepository,
        paymentMethodRepository,
        roomTypeRepository,
        roomInventoryRepository
    )

    @Test
    fun `getProperty should return null if not found`() {
        // Arrange
        val propertyId = 99
        every { propertyRepository.findById(propertyId) } returns Optional.empty()

        // Act
        val response = propertyService.getProperty(propertyId)

        // Assert
        assertNull(response)
    }

    @Test
    fun `getProperty should return PropertyResponse if found`() {
        // Arrange
        val propertyId = 1
        val mockProperty = Property(
            id = propertyId,
            name = "Test Hotel",
            address = "123 Main St",
            latitude = 0.0.toBigDecimal(),
            longitude = 0.0.toBigDecimal(),
            starRating = 4,
            region = mockk(relaxed = true),
            propertyType = mockk(relaxed = true)
        )
        
        every { propertyRepository.findById(propertyId) } returns Optional.of(mockProperty)

        // Act
        val response = propertyService.getProperty(propertyId)

        // Assert
        assertEquals(propertyId, response?.id)
        assertEquals("Test Hotel", response?.name)
        assertEquals(4, response?.starRating)
    }
}
