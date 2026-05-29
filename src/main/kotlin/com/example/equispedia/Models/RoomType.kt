package com.example.equispedia.Models

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "room_types")
data class RoomType(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    val property: Property,

    val name: String,

    val basePricePerNight: BigDecimal,
    
    val maxOccupancyAdults: Int,
    val maxOccupancyChildren: Int,
    
    val sqMeters: Int? = null,

    val isRefundable: Boolean = true,
    
    val freeCancellationDays: Int? = null,
    
    @Column(columnDefinition = "TEXT")
    val cancellationPolicyDetails: String? = null
) {
    @ManyToMany
    @JoinTable(
        name = "room_amenities",
        joinColumns = [JoinColumn(name = "room_type_id")],
        inverseJoinColumns = [JoinColumn(name = "amenity_id")]
    )
    val amenities: MutableSet<Amenity> = mutableSetOf()
}
