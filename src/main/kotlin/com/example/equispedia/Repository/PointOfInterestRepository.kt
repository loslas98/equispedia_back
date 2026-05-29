package com.example.equispedia.Repository

import com.example.equispedia.Models.PointOfInterest
import com.example.equispedia.Models.PropertyPOI
import com.example.equispedia.Models.PropertyPOIId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PointOfInterestRepository : JpaRepository<PointOfInterest, Int>

@Repository
interface PropertyPOIRepository : JpaRepository<PropertyPOI, PropertyPOIId>
