package com.example.equispedia.Models

import jakarta.persistence.*

@Entity
@Table(name = "property_types")
data class PropertyType(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val name: String
)
