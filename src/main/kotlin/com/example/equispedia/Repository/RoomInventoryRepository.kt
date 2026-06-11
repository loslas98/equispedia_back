package com.example.equispedia.Repository

import com.example.equispedia.Models.RoomInventory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import org.springframework.data.jpa.repository.Query
import java.time.LocalDate

@Repository
interface RoomInventoryRepository : JpaRepository<RoomInventory, Int> {
    @Query("SELECT ri FROM RoomInventory ri WHERE ri.roomType.property.id = :propertyId AND ri.date >= :startDate AND ri.date < :endDate")
    fun findByPropertyIdAndDateBetween(propertyId: Int, startDate: LocalDate, endDate: LocalDate): List<RoomInventory>
}
