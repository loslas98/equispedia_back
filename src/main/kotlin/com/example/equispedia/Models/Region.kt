package com.example.equispedia.Models

import jakarta.persistence.*

@Entity
@Table(name = "regions")
data class Region(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val name: String,

    @Enumerated(EnumType.STRING)
    val type: RegionType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_region_id")
    val parentRegion: Region? = null
)
