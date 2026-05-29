package com.example.equispedia.Models

import jakarta.persistence.*
import java.io.Serializable
import java.math.BigDecimal

@Entity
@Table(name = "points_of_interest")
data class PointOfInterest(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val name: String,
    
    val latitude: BigDecimal,
    val longitude: BigDecimal
)

enum class TransportMode {
    WALKING, DRIVING
}

@Embeddable
data class PropertyPOIId(
    @Column(name = "property_id")
    val propertyId: Int = 0,
    
    @Column(name = "poi_id")
    val poiId: Int = 0
) : Serializable

@Entity
@Table(name = "property_poi")
data class PropertyPOI(
    @EmbeddedId
    val id: PropertyPOIId = PropertyPOIId(),

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("propertyId")
    @JoinColumn(name = "property_id")
    val property: Property,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("poiId")
    @JoinColumn(name = "poi_id")
    val poi: PointOfInterest,

    val distanceKm: BigDecimal,

    @Enumerated(EnumType.STRING)
    val transportMode: TransportMode,

    val timeMins: Int
)
