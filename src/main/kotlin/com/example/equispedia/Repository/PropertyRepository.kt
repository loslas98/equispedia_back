package com.example.equispedia.Repository

import com.example.equispedia.Models.Property
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PropertyRepository : JpaRepository<Property, Int>
