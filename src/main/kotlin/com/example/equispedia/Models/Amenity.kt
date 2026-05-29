package com.example.equispedia.Models

import jakarta.persistence.*

@Entity
@Table(name = "amenity_categories")
data class AmenityCategory(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val name: String
)

@Entity
@Table(name = "amenities")
data class Amenity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    val category: AmenityCategory,

    val name: String,
    
    val iconName: String? = null
)
