package com.example.equispedia.Services

import com.example.equispedia.DTO.*
import com.example.equispedia.Models.*
import com.example.equispedia.Repository.*
import org.springframework.stereotype.Service

@Service
class RegionService(
    private val regionRepository: RegionRepository
) {
    fun toResponse(region: Region): RegionResponse = RegionResponse(
        id = region.id,
        name = region.name,
        type = region.type,
        parentRegionId = region.parentRegion?.id
    )

    fun createRegion(req: RegionRequest): RegionResponse {
        val parent = req.parentRegionId?.let { regionRepository.findById(it).orElse(null) }
        val region = Region(
            name = req.name,
            type = req.type,
            parentRegion = parent
        )
        return toResponse(regionRepository.save(region))
    }

    fun getAllRegions() = regionRepository.findAll().map(::toResponse)
}
