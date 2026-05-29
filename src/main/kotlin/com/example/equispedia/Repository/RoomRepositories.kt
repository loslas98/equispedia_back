package com.example.equispedia.Repository

import com.example.equispedia.Models.BedType
import com.example.equispedia.Models.RoomBed
import com.example.equispedia.Models.RoomBedId
import com.example.equispedia.Models.RoomType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoomTypeRepository : JpaRepository<RoomType, Int>

@Repository
interface BedTypeRepository : JpaRepository<BedType, Int>

@Repository
interface RoomBedRepository : JpaRepository<RoomBed, RoomBedId>
