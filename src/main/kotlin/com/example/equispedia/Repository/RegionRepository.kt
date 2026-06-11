package com.example.equispedia.Repository

import com.example.equispedia.Models.Region
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RegionRepository : JpaRepository<Region, Int>{

    @Query("""
        SELECT r FROM Region r
        WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :query, '%'))
        ORDER BY r.type, r.name
    """)
    fun searchByName(@Param("query") query: String): List<Region>

    fun findByParentRegion_IdIn(parentIds: Collection<Int>): List<Region>
}
