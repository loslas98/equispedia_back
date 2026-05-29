package com.example.equispedia.Repository

import com.example.equispedia.Models.PropertyFAQ
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PropertyFAQRepository : JpaRepository<PropertyFAQ, Int>
