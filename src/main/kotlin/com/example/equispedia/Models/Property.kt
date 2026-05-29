package com.example.equispedia.Models

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalTime

@Entity
@Table(name = "properties")
data class Property(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val name: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_type_id")
    val propertyType: PropertyType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    val region: Region,

    @Column(columnDefinition = "TEXT")
    val address: String,

    val latitude: BigDecimal,
    val longitude: BigDecimal,

    val starRating: Int? = null,

    @Column(columnDefinition = "TEXT")
    val description: String? = null,

    val checkInStartTime: LocalTime? = null,
    val checkInEndTime: LocalTime? = null,
    val checkOutTime: LocalTime? = null,
    
    val minAgeCheckIn: Int? = null,
    val contactlessCheckIn: Boolean = false,
    
    val petsAllowed: Boolean = false,
    val childrenAllowed: Boolean = true,
    
    @Column(columnDefinition = "TEXT")
    val importantInfo: String? = null
) {
    @ManyToMany
    @JoinTable(
        name = "property_tags",
        joinColumns = [JoinColumn(name = "property_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")]
    )
    val tags: MutableSet<Tag> = mutableSetOf()

    @ManyToMany
    @JoinTable(
        name = "property_amenities",
        joinColumns = [JoinColumn(name = "property_id")],
        inverseJoinColumns = [JoinColumn(name = "amenity_id")]
    )
    val amenities: MutableSet<Amenity> = mutableSetOf()

    @ManyToMany
    @JoinTable(
        name = "property_payment_methods",
        joinColumns = [JoinColumn(name = "property_id")],
        inverseJoinColumns = [JoinColumn(name = "payment_method_id")]
    )
    val paymentMethods: MutableSet<PaymentMethod> = mutableSetOf()
}
