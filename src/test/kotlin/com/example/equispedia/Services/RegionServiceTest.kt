package com.example.equispedia.Services

import com.example.equispedia.DTO.RegionRequest
import com.example.equispedia.Models.Region
import com.example.equispedia.Models.RegionType
import com.example.equispedia.Repository.RegionRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.Optional

class RegionServiceTest {

    private val regionRepository: RegionRepository = mockk()
    private val regionService = RegionService(regionRepository)

    @Test
    fun `createRegion should save and return region`() {
        val req = RegionRequest(name = "Andes", type = RegionType.CITY, parentRegionId = null)
        val region = Region(id = 1, name = "Andes", type = RegionType.CITY, parentRegion = null)
        
        every { regionRepository.save(any()) } returns region
        every { regionRepository.findById(any()) } returns Optional.empty()

        val result = regionService.createRegion(req)

        assertEquals(1, result.id)
        assertEquals("Andes", result.name)
        verify { regionRepository.save(any()) }
    }

    @Test
    fun `getAllRegions should return list of regions`() {
        val region = Region(id = 1, name = "Andes", type = RegionType.CITY, parentRegion = null)
        every { regionRepository.findAll() } returns listOf(region)

        val result = regionService.getAllRegions()

        assertEquals(1, result.size)
        assertEquals("Andes", result[0].name)
    }

    @Test
    fun `searchRegions should return empty if query is blank`() {
        val result = regionService.searchRegions("   ")
        assertEquals(0, result.size)
    }

    @Test
    fun `searchRegions should return matches and children correctly sorted`() {
        val parent = Region(id = 1, name = "Chile", type = RegionType.COUNTRY, parentRegion = null)
        val child = Region(id = 2, name = "Santiago", type = RegionType.CITY, parentRegion = parent)
        
        every { regionRepository.searchByName("Sant") } returns listOf(child)
        every { regionRepository.findByParentRegion_IdIn(setOf(2)) } returns emptyList()

        val result = regionService.searchRegions("Sant")

        assertEquals(1, result.size)
        assertEquals(2, result[0].id)
    }
}
