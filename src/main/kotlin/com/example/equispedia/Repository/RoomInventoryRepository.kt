package com.example.equispedia.Repository

import com.example.equispedia.Models.RoomInventory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoomInventoryRepository : JpaRepository<RoomInventory, Int>
