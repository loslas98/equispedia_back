package com.example.equispedia.Models

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    
    val fullName: String,
    
    @Column(unique = true)
    val email: String,
    
    val passwordHash: String,
    
    val isMember: Boolean = false,
    
    val loyaltyPoints: Int = 0,
    
    val createdAt: Instant = Instant.now()
) {
    @ManyToMany
    @JoinTable(
        name = "user_favorites",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "property_id")]
    )
    val favoriteProperties: MutableSet<Property> = mutableSetOf()
}
