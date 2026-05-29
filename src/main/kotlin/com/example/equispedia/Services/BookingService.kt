package com.example.equispedia.Services

import com.example.equispedia.DTO.*
import com.example.equispedia.Models.*
import com.example.equispedia.Repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookingService(
    private val bookingRepository: BookingRepository,
    private val userRepository: UserRepository,
    private val propertyRepository: PropertyRepository,
    private val roomTypeRepository: RoomTypeRepository
) {
    fun toResponse(booking: Booking): BookingResponse {
        return BookingResponse(
            id = booking.id,
            userId = booking.user.id,
            propertyId = booking.property.id,
            checkIn = booking.checkIn,
            checkOut = booking.checkOut,
            totalPrice = booking.totalPrice,
            status = booking.status,
            createdAt = booking.createdAt,
            items = listOf() // Simplified for now
        )
    }

    @Transactional
    fun createBooking(req: BookingRequest): BookingResponse {
        val user = userRepository.findById(req.userId).orElseThrow()
        val prop = propertyRepository.findById(req.propertyId).orElseThrow()
        
        val booking = Booking(
            user = user,
            property = prop,
            checkIn = req.checkIn,
            checkOut = req.checkOut,
            totalPrice = req.totalPrice
        )
        return toResponse(bookingRepository.save(booking))
    }
}
