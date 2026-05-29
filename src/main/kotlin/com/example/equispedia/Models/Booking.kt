package com.example.equispedia.Models

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

enum class BookingStatus {
    PENDING, CONFIRMED, CANCELLED, PAID
}

@Entity
@Table(name = "bookings")
data class Booking(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    val property: Property,

    val checkIn: LocalDate,
    val checkOut: LocalDate,

    val totalPrice: BigDecimal,

    @Enumerated(EnumType.STRING)
    val status: BookingStatus = BookingStatus.PENDING,

    val createdAt: Instant = Instant.now()
)

@Entity
@Table(name = "booking_items")
data class BookingItem(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    val booking: Booking,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id")
    val roomType: RoomType,

    val guestsCount: Int
)
