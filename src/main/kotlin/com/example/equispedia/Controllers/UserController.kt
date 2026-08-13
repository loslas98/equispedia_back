package com.example.equispedia.Controllers

import com.example.equispedia.DTO.UserCreateRequest
import com.example.equispedia.DTO.UserResponse
import com.example.equispedia.Services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.security.Principal
import com.example.equispedia.DTO.BookingResponse
import com.example.equispedia.DTO.PropertySummaryResponse
import com.example.equispedia.Services.BookingService

@RestController
@RequestMapping("/api/users")
class UserController(
    private val userService: UserService,
    private val bookingService: BookingService
) {

    @PostMapping
    fun createUser(@RequestBody request: UserCreateRequest): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(userService.createUser(request))
    }

    @GetMapping("/{id}")
    fun getUser(@PathVariable id: Int): ResponseEntity<UserResponse> {
        val user = userService.getUser(id)
        return if (user != null) ResponseEntity.ok(user) else ResponseEntity.notFound().build()
    }

    @PostMapping("/{userId}/favorites/{propertyId}")
    fun toggleFavorite(@PathVariable userId: Int, @PathVariable propertyId: Int): ResponseEntity<Map<String, Boolean>> {
        val isFavorite = userService.toggleFavorite(userId, propertyId)
        return ResponseEntity.ok(mapOf("isFavorite" to isFavorite))
    }

    @GetMapping("/me/favorites")
    fun getMyFavorites(principal: Principal): ResponseEntity<List<PropertySummaryResponse>> {
        val favorites = userService.getMyFavorites(principal.name)
        return ResponseEntity.ok(favorites)
    }

    @GetMapping("/me/bookings")
    fun getMyBookings(principal: Principal): ResponseEntity<List<BookingResponse>> {
        val bookings = bookingService.getMyBookings(principal.name)
        return ResponseEntity.ok(bookings)
    }
}