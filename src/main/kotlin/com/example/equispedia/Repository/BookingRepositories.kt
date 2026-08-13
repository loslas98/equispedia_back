package com.example.equispedia.Repository

import com.example.equispedia.Models.Booking
import com.example.equispedia.Models.BookingItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookingRepository : JpaRepository<Booking, Int> {
    fun findByUserId(userId: Int): List<Booking>
}

@Repository
interface BookingItemRepository : JpaRepository<BookingItem, Int> {
    fun findByBookingId(bookingId: Int): List<BookingItem>
}
