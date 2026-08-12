package com.example.equispedia.Models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class PropertyTest {

    @Test
    fun `should create property correctly`() {
        val type = PropertyType(id = 1, name = "Hotel")
        val region = Region(id = 1, name = "Santiago", type = RegionType.CITY)

        val property = Property(
            id = 1,
            name = "Test Property",
            propertyType = type,
            region = region,
            address = "123 Main St",
            latitude = 0.0.toBigDecimal(),
            longitude = 0.0.toBigDecimal()
        )

        assertEquals(1, property.id)
        assertEquals("Test Property", property.name)
        assertNotNull(property.tags)
        assertNotNull(property.amenities)
        assertNotNull(property.paymentMethods)
        assertNotNull(property.images)
        assertNotNull(property.roomTypes)
        assertNotNull(property.faqs)
        assertNotNull(property.reviews)
    }
}
