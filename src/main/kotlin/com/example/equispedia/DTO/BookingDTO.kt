package com.example.equispedia.DTO

import com.example.equispedia.Models.BookingStatus
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

data class BookingItemRequest(
    val roomTypeId: Int,
    val guestsCount: Int
)

data class BookingRequest(
    val userId: Int,
    val propertyId: Int,
    val checkIn: LocalDate,
    val checkOut: LocalDate,
    val totalPrice: BigDecimal,
    val items: List<BookingItemRequest>
)

data class BookingItemResponse(
    val id: Int,
    val roomTypeId: Int,
    val guestsCount: Int
)

data class BookingResponse(
    val id: Int,
    val userId: Int,
    val propertyId: Int,
    val checkIn: LocalDate,
    val checkOut: LocalDate,
    val totalPrice: BigDecimal,
    val status: BookingStatus,
    val createdAt: Instant,
    val items: List<BookingItemResponse>
)
