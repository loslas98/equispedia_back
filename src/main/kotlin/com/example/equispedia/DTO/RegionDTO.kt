package com.example.equispedia.DTO

import com.example.equispedia.Models.RegionType

data class RegionRequest(
    val name: String,
    val type: RegionType,
    val parentRegionId: Int? = null
)

data class RegionResponse(
    val id: Int,
    val name: String,
    val type: RegionType,
    val parentRegionId: Int?
)
