package com.example.equispedia.Repository

import com.example.equispedia.Models.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : JpaRepository<Image, Int>
