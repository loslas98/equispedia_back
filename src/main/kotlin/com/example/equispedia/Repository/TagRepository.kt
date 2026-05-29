package com.example.equispedia.Repository

import com.example.equispedia.Models.Tag
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TagRepository : JpaRepository<Tag, Int>
