package com.example.equispedia.Models

import jakarta.persistence.*

@Entity
@Table(name = "tags")
data class Tag(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val name: String
)
