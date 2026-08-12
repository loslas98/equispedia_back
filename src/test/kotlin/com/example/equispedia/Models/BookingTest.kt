package com.example.equispedia.Models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDate

class BookingTest {

    @Test
    fun `should create booking correctly`() {
        val user = User(id = 1, email = "test@example.com", passwordHash = "hash", fullName = "Test User")
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

        val checkInDate = LocalDate.of(2024, 1, 1)
        val checkOutDate = LocalDate.of(2024, 1, 5)

        val booking = Booking(
            id = 10,
            user = user,
            property = property,
            checkIn = checkInDate,
            checkOut = checkOutDate,
            totalPrice = 500.0.toBigDecimal(),
            status = BookingStatus.CONFIRMED
        )

        assertEquals(10, booking.id)
        assertEquals(user, booking.user)
        assertEquals(property, booking.property)
        assertEquals(checkInDate, booking.checkIn)
        assertEquals(checkOutDate, booking.checkOut)
        assertEquals(BookingStatus.CONFIRMED, booking.status)
        assertEquals(500.0.toBigDecimal(), booking.totalPrice)
    }
}
