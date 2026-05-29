package com.example.equispedia.Repository

import com.example.equispedia.Models.Review
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ReviewRepository : JpaRepository<Review, Int>
