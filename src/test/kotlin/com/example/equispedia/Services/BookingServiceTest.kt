package com.example.equispedia.Services

import com.example.equispedia.DTO.BookingRequest
import com.example.equispedia.Models.Booking
import com.example.equispedia.Models.Property
import com.example.equispedia.Models.User
import com.example.equispedia.Repository.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.util.Optional

class BookingServiceTest {

    private val bookingRepository: BookingRepository = mockk()
    private val userRepository: UserRepository = mockk()
    private val propertyRepository: PropertyRepository = mockk()
    private val roomTypeRepository: RoomTypeRepository = mockk()
    private val bookingItemRepository: BookingItemRepository = mockk()
    private val emailService: EmailService = mockk(relaxed = true)

    private val bookingService = BookingService(
        bookingRepository,
        userRepository,
        propertyRepository,
        roomTypeRepository,
        bookingItemRepository,
        emailService
    )

    @Test
    fun `createBooking should throw exception if user and default user not found`() {
        // Arrange
        val request = BookingRequest(
            userId = 1,
            propertyId = 1,
            checkIn = LocalDate.now(),
            checkOut = LocalDate.now().plusDays(2),
            totalPrice = 100.0.toBigDecimal(),
            items = emptyList()
        )
        every { userRepository.findById(request.userId) } returns Optional.empty()
        every { userRepository.findAll() } returns emptyList()

        // Act & Assert
        val exception = assertThrows(RuntimeException::class.java) {
            bookingService.createBooking(request)
        }
        assertEquals("No users found in database", exception.message)
    }

    @Test
    fun `createBooking should save booking successfully`() {
        // Arrange
        val request = BookingRequest(
            userId = 1,
            propertyId = 1,
            checkIn = LocalDate.now(),
            checkOut = LocalDate.now().plusDays(1),
            totalPrice = 150.0.toBigDecimal(),
            items = emptyList(),
            guestEmail = "guest@example.com",
            guestFirstName = "John"
        )

        val user = User(id = 1, email = "user@example.com", fullName = "User", passwordHash = "hash")
        val property = Property(
            id = 1,
            name = "Hotel",
            address = "123 St",
            propertyType = mockk(relaxed = true),
            region = mockk(relaxed = true),
            latitude = 0.0.toBigDecimal(),
            longitude = 0.0.toBigDecimal(),
            starRating = 5
        )
        
        every { userRepository.findById(request.userId) } returns Optional.of(user)
        every { propertyRepository.findById(request.propertyId) } returns Optional.of(property)
        
        val savedBooking = Booking(
            id = 1,
            user = user,
            property = property,
            checkIn = request.checkIn,
            checkOut = request.checkOut,
            totalPrice = request.totalPrice,
            status = com.example.equispedia.Models.BookingStatus.PAID
        )
        
        every { bookingRepository.save(any<Booking>()) } returns savedBooking

        // Act
        val response = bookingService.createBooking(request)

        // Assert
        assertEquals(savedBooking.id, response.id)
        assertEquals(savedBooking.totalPrice, response.totalPrice)
        assertEquals(com.example.equispedia.Models.BookingStatus.PAID, response.status)
        
        verify(exactly = 1) { bookingRepository.save(any<Booking>()) }
        verify(exactly = 1) { emailService.sendBookingConfirmation(
            toEmail = "guest@example.com",
            firstName = "John",
            booking = savedBooking,
            items = any(),
            propertyImageUrl = any()
        )}
    }
}
