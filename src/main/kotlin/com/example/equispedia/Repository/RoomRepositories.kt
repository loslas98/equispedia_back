package com.example.equispedia.Repository

import com.example.equispedia.Models.BedType
import com.example.equispedia.Models.RoomBed
import com.example.equispedia.Models.RoomBedId
import com.example.equispedia.Models.RoomType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDate

@Repository
interface RoomTypeRepository : JpaRepository<RoomType, Int> {

    @Query("""
        SELECT COUNT(rt) > 0 FROM RoomType rt
        WHERE rt.property.id = :propertyId
        AND rt.maxOccupancyAdults >= :adults
        AND rt.maxOccupancyChildren >= :children
        AND NOT EXISTS (
            SELECT ri FROM RoomInventory ri
            WHERE ri.roomType = rt
            AND ri.date >= :checkIn AND ri.date < :checkOut
            AND ri.roomsAvailable <= (
                SELECT COUNT(bi) FROM BookingItem bi
                JOIN bi.booking b
                WHERE bi.roomType = rt
                AND b.status != 'CANCELLED'
                AND b.checkIn <= ri.date AND b.checkOut > ri.date
            )
        )
    """)
    fun hasAvailability(
        @Param("propertyId") propertyId: Int,
        @Param("adults") adults: Int,
        @Param("children") children: Int,
        @Param("checkIn") checkIn: LocalDate,
        @Param("checkOut") checkOut: LocalDate
    ): Boolean

    @Query("""
        SELECT MIN(rt.basePricePerNight) FROM RoomType rt
        WHERE rt.property.id = :propertyId
        AND rt.maxOccupancyAdults >= :adults
        AND rt.maxOccupancyChildren >= :children
        AND NOT EXISTS (
            SELECT ri FROM RoomInventory ri
            WHERE ri.roomType = rt
            AND ri.date >= :checkIn AND ri.date < :checkOut
            AND ri.roomsAvailable <= (
                SELECT COUNT(bi) FROM BookingItem bi
                JOIN bi.booking b
                WHERE bi.roomType = rt
                AND b.status != 'CANCELLED'
                AND b.checkIn <= ri.date AND b.checkOut > ri.date
            )
        )
    """)
    fun findLowestAvailablePrice(
        @Param("propertyId") propertyId: Int,
        @Param("adults") adults: Int,
        @Param("children") children: Int,
        @Param("checkIn") checkIn: LocalDate,
        @Param("checkOut") checkOut: LocalDate
    ): BigDecimal?

    @Query("""
        SELECT COUNT(rt) > 0 FROM RoomType rt
        WHERE rt.property.id = :propertyId
        AND rt.isRefundable = true
        AND rt.maxOccupancyAdults >= :adults
        AND rt.maxOccupancyChildren >= :children
        AND NOT EXISTS (
            SELECT ri FROM RoomInventory ri
            WHERE ri.roomType = rt
            AND ri.date >= :checkIn AND ri.date < :checkOut
            AND ri.roomsAvailable <= (
                SELECT COUNT(bi) FROM BookingItem bi
                JOIN bi.booking b
                WHERE bi.roomType = rt
                AND b.status != 'CANCELLED'
                AND b.checkIn <= ri.date AND b.checkOut > ri.date
            )
        )
    """)
    fun hasRefundableAvailability(
        @Param("propertyId") propertyId: Int,
        @Param("adults") adults: Int,
        @Param("children") children: Int,
        @Param("checkIn") checkIn: LocalDate,
        @Param("checkOut") checkOut: LocalDate
    ): Boolean
}

@Repository
interface BedTypeRepository : JpaRepository<BedType, Int>

@Repository
interface RoomBedRepository : JpaRepository<RoomBed, RoomBedId>