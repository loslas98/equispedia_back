package com.example.equispedia.Controllers

import com.example.equispedia.DTO.*
import com.example.equispedia.Services.PropertyService
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller
import java.time.LocalDate

@Controller
class PropertyController(private val propertyService: PropertyService) {

    @org.springframework.web.bind.annotation.PostMapping("/api/properties")
    @org.springframework.web.bind.annotation.ResponseBody
    fun createProperty(@org.springframework.web.bind.annotation.RequestBody request: PropertyRequest): org.springframework.http.ResponseEntity<PropertyResponse> {
        return org.springframework.http.ResponseEntity.ok(propertyService.createProperty(request))
    }

    @QueryMapping
    fun searchHotels(
        @Argument regionId: Int,
        @Argument checkIn: String,
        @Argument checkOut: String,
        @Argument adults: Int,
        @Argument children: Int?
    ): List<HotelSearchResult> {
        val request = HotelSearchRequest(
            regionId = regionId,
            checkIn = LocalDate.parse(checkIn),
            checkOut = LocalDate.parse(checkOut),
            adults = adults,
            children = children ?: 0
        )
        return propertyService.searchHotels(request)
    }

    @QueryMapping
    fun getProperty(
        @Argument id: Int,
        @Argument include: String?
    ): Any? {
        return if (include != null) {
            propertyService.getPropertyWithIncludes(id, include)
        } else {
            propertyService.getProperty(id)
        }
    }

    @QueryMapping
    fun getAllProperties(): List<PropertyResponse> {
        return propertyService.getAllProperties()
    }

    @QueryMapping
    fun checkAvailability(
        @Argument id: Int,
        @Argument checkIn: String,
        @Argument checkOut: String,
        @Argument guests: Int
    ): PropertyAvailabilityResponse {
        return propertyService.checkAvailability(
            id,
            LocalDate.parse(checkIn),
            LocalDate.parse(checkOut),
            guests
        )
    }
}
