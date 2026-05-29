package com.example.equispedia.Controllers

import com.example.equispedia.DTO.UserCreateRequest
import com.example.equispedia.DTO.UserResponse
import com.example.equispedia.Services.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/users")
class UserController(private val userService: UserService) {

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
}