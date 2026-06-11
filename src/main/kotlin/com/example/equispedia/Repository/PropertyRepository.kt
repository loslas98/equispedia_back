package com.example.equispedia.Repository

import com.example.equispedia.Models.Property
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

@Repository
interface PropertyRepository : JpaRepository<Property, Int>{
    @Query("""
        SELECT DISTINCT p FROM Property p
        JOIN RoomType rt ON rt.property = p
        WHERE (p.region.id = :regionId OR p.region.parentRegion.id = :regionId)
        AND rt.maxOccupancyAdults >= :adults
        AND rt.maxOccupancyChildren >= :children
    """)
    fun findCandidatesByRegionAndCapacity(
        @Param("regionId") regionId: Int,
        @Param("adults") adults: Int,
        @Param("children") children: Int
    ): List<Property>
}
