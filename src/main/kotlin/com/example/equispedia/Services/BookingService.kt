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
    private val roomTypeRepository: RoomTypeRepository,
    private val bookingItemRepository: BookingItemRepository,
    private val emailService: EmailService
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
        val user = userRepository.findById(req.userId).orElseGet {
            userRepository.findAll().firstOrNull() ?: throw RuntimeException("No users found in database")
        }
        val prop = propertyRepository.findById(req.propertyId).orElseGet {
            propertyRepository.findAll().firstOrNull() ?: throw RuntimeException("No properties found in database")
        }
        
        val booking = Booking(
            user = user,
            property = prop,
            checkIn = req.checkIn,
            checkOut = req.checkOut,
            totalPrice = req.totalPrice,
            status = BookingStatus.PAID
        )
        val savedBooking = bookingRepository.save(booking)

        // Guardar los items
        val savedItems = req.items.mapNotNull { itemReq ->
            val roomType = roomTypeRepository.findById(itemReq.roomTypeId).orElseGet {
                roomTypeRepository.findAll().firstOrNull()
            }
            if (roomType != null) {
                val bookingItem = BookingItem(
                    booking = savedBooking,
                    roomType = roomType,
                    guestsCount = itemReq.guestsCount
                )
                bookingItemRepository.save(bookingItem)
            } else null
        }

        // Send email if guestEmail is provided
        req.guestEmail?.let { email ->
            val propertyImageUrl = prop.images.firstOrNull()?.url
            
            emailService.sendBookingConfirmation(
                toEmail = email,
                firstName = req.guestFirstName,
                booking = savedBooking,
                items = savedItems,
                propertyImageUrl = propertyImageUrl
            )
        }

        return toResponse(savedBooking).copy(
            items = savedItems.map { 
                BookingItemResponse(it.id, it.roomType.id, it.guestsCount) 
            }
        )
    }
}
