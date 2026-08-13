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

    @Test
    fun `createProperty should save and return property response`() {
        // Arrange
        val req = com.example.equispedia.DTO.PropertyRequest(
            name = "New Hotel", propertyTypeId = 1, regionId = 1,
            address = "Address", latitude = java.math.BigDecimal.ZERO,
            longitude = java.math.BigDecimal.ZERO, tagIds = listOf(),
            amenityIds = listOf(), paymentMethodIds = listOf()
        )
        val type = com.example.equispedia.Models.PropertyType(id = 1, name = "Hotel")
        val region = com.example.equispedia.Models.Region(id = 1, name = "City", type = com.example.equispedia.Models.RegionType.CITY)
        
        every { propertyTypeRepository.findById(1) } returns Optional.of(type)
        every { regionRepository.findById(1) } returns Optional.of(region)
        every { tagRepository.findAllById(any<List<Int>>()) } returns listOf()
        every { amenityRepository.findAllById(any<List<Int>>()) } returns listOf()
        every { paymentMethodRepository.findAllById(any<List<Int>>()) } returns listOf()
        
        val savedProp = Property(id = 1, name = "New Hotel", address = "Address", propertyType = type, region = region, latitude = java.math.BigDecimal.ZERO, longitude = java.math.BigDecimal.ZERO)
        every { propertyRepository.save(any()) } returns savedProp

        // Act
        val response = propertyService.createProperty(req)

        // Assert
        assertEquals(1, response.id)
        assertEquals("New Hotel", response.name)
    }

    @Test
    fun `checkAvailability should return true if any room is available`() {
        val checkIn = java.time.LocalDate.now()
        val checkOut = checkIn.plusDays(1)
        val roomType = com.example.equispedia.Models.RoomType(id = 1, property = mockk(relaxed = true), name = "Standard", maxOccupancyAdults = 2, maxOccupancyChildren = 1, basePricePerNight = java.math.BigDecimal("100.0"))
        
        every { roomInventoryRepository.findByPropertyIdAndDateBetween(1, checkIn, checkOut) } returns listOf(
            com.example.equispedia.Models.RoomInventory(id = 1, roomType = roomType, date = checkIn, roomsAvailable = 1)
        )
        every { roomTypeRepository.findByPropertyId(1) } returns listOf(roomType)

        val response = propertyService.checkAvailability(1, checkIn, checkOut, 2)

        assertEquals(true, response.isAvailable)
        assertEquals(1, response.rooms.size)
        assertEquals(true, response.rooms[0].isAvailable)
    }
}
