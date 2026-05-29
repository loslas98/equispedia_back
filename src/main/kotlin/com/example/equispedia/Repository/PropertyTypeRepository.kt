package com.example.equispedia.Repository

import com.example.equispedia.Models.PropertyType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PropertyTypeRepository : JpaRepository<PropertyType, Int>
