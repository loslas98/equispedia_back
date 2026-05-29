package com.example.equispedia.Models

import jakarta.persistence.*
import java.time.Instant

enum class TravelerType {
    SOLO, COUPLE, FAMILY, BUSINESS
}

@Entity
@Table(name = "reviews")
data class Review(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    val property: Property,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    val ratingOverall: Int,
    val ratingCleanliness: Int,
    val ratingService: Int,
    val ratingFacilities: Int,
    
    @Column(columnDefinition = "TEXT")
    val comment: String? = null,

    @Enumerated(EnumType.STRING)
    val travelerType: TravelerType,
    
    val isVerified: Boolean = true,
    
    val createdAt: Instant = Instant.now()
)
