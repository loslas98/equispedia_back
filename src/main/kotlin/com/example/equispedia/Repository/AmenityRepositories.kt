package com.example.equispedia.Repository

import com.example.equispedia.Models.Amenity
import com.example.equispedia.Models.AmenityCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AmenityCategoryRepository : JpaRepository<AmenityCategory, Int>

@Repository
interface AmenityRepository : JpaRepository<Amenity, Int>
