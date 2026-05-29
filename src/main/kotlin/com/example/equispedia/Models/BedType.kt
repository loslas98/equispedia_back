package com.example.equispedia.Models

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "bed_types")
data class BedType(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val name: String
)

@Embeddable
data class RoomBedId(
    @Column(name = "room_type_id")
    val roomTypeId: Int = 0,
    
    @Column(name = "bed_type_id")
    val bedTypeId: Int = 0
) : Serializable

@Entity
@Table(name = "room_beds")
data class RoomBed(
    @EmbeddedId
    val id: RoomBedId = RoomBedId(),

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("roomTypeId")
    @JoinColumn(name = "room_type_id")
    val roomType: RoomType,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bedTypeId")
    @JoinColumn(name = "bed_type_id")
    val bedType: BedType,

    val count: Int
)
