package com.example.equispedia.Controllers

import com.example.equispedia.DTO.BookingRequest
import com.example.equispedia.DTO.BookingResponse
import com.example.equispedia.Services.BookingService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/bookings")
class BookingController(private val bookingService: BookingService) {

    @PostMapping
    fun createBooking(@RequestBody request: BookingRequest): ResponseEntity<BookingResponse> {
        return ResponseEntity.ok(bookingService.createBooking(request))
    }
}
