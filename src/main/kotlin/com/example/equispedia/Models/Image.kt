package com.example.equispedia.Models

import jakarta.persistence.*

@Entity
@Table(name = "images")
data class Image(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    val property: Property,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_type_id")
    val roomType: RoomType? = null,

    val url: String,
    
    val altText: String? = null,
    
    val isMain: Boolean = false
)
