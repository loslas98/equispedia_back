package com.example.equispedia.Controllers

import com.example.equispedia.DTO.*
import com.example.equispedia.Services.PropertyService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/properties")
class PropertyController(private val propertyService: PropertyService) {

    @PostMapping
    fun createProperty(@RequestBody request: PropertyRequest): ResponseEntity<PropertyResponse> {
        return ResponseEntity.ok(propertyService.createProperty(request))
    }

    @GetMapping("/{id}")
    fun getProperty(@PathVariable id: Int): ResponseEntity<PropertyResponse> {
        val prop = propertyService.getProperty(id)
        return if (prop != null) ResponseEntity.ok(prop) else ResponseEntity.notFound().build()
    }

    @GetMapping
    fun getAllProperties(): ResponseEntity<List<PropertyResponse>> {
        return ResponseEntity.ok(propertyService.getAllProperties())
    }
    @GetMapping("/search")
    fun searchHotels(
        @RequestParam regionId: Int,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) checkIn: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) checkOut: LocalDate,
        @RequestParam adults: Int,
        @RequestParam(defaultValue = "0") children: Int
    ): ResponseEntity<List<HotelSearchResult>> {
        val request = HotelSearchRequest(
            regionId = regionId,
            checkIn = checkIn,
            checkOut = checkOut,
            adults = adults,
            children = children
        )
        return ResponseEntity.ok(propertyService.searchHotels(request))
    }
}
