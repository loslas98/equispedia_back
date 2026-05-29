package com.example.equispedia.Repository

import com.example.equispedia.Models.Region
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RegionRepository : JpaRepository<Region, Int>
