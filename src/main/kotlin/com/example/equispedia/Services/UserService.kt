package com.example.equispedia.Services

import com.example.equispedia.DTO.*
import com.example.equispedia.Models.*
import com.example.equispedia.Repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val propertyRepository: PropertyRepository
) {
    fun toResponse(user: User) = UserResponse(
        id = user.id,
        fullName = user.fullName,
        email = user.email,
        isMember = user.isMember,
        loyaltyPoints = user.loyaltyPoints,
        createdAt = user.createdAt
    )

    fun createUser(req: UserCreateRequest): UserResponse {
        val user = User(
            fullName = req.fullName,
            email = req.email,
            passwordHash = req.passwordHash,
            isMember = req.isMember
        )
        return toResponse(userRepository.save(user))
    }

    fun getUser(id: Int): UserResponse? = userRepository.findById(id).map(::toResponse).orElse(null)

    @Transactional
    fun toggleFavorite(userId: Int, propertyId: Int): Boolean {
        val user = userRepository.findById(userId).orElseThrow { RuntimeException("User not found") }
        val property = propertyRepository.findById(propertyId).orElseThrow { RuntimeException("Property not found") }
        
        val isFavorite = user.favoriteProperties.contains(property)
        if (isFavorite) {
            user.favoriteProperties.remove(property)
        } else {
            user.favoriteProperties.add(property)
        }
        userRepository.save(user)
        return !isFavorite
    }

    @Transactional(readOnly = true)
    fun getMyFavorites(email: String): List<PropertySummaryResponse> {
        val user = userRepository.findByEmail(email) ?: throw RuntimeException("User not found")
        return user.favoriteProperties.map {
            PropertySummaryResponse(
                id = it.id,
                name = it.name,
                propertyType = it.propertyType.name,
                region = it.region.name,
                starRating = it.starRating ?: 0,
                basePricePerNight = it.roomTypes.minOfOrNull { rt -> rt.basePricePerNight }?.toDouble() ?: 0.0,
                mainImageUrl = it.images.firstOrNull { img -> img.isMain }?.url
            )
        }
    }
}
