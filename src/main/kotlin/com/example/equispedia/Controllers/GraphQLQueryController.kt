package com.example.equispedia.Controllers

import com.example.equispedia.DTO.*
import com.example.equispedia.Services.*
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class GraphQLQueryController(
    private val regionService: RegionService,
    private val tagService: TagService,
    private val propertyTypeService: PropertyTypeService,
    private val amenityService: AmenityService
) {

    @QueryMapping
    fun searchRegions(@Argument query: String): List<RegionResponse> {
        return regionService.searchRegions(query)
    }

    @QueryMapping
    fun getAllTags(): List<TagResponse> {
        return tagService.getAllTags()
    }

    @QueryMapping
    fun getAllTypes(): List<PropertyTypeResponse> {
        return propertyTypeService.getAllTypes()
    }

    @QueryMapping
    fun getAllAmenities(): List<AmenityResponse> {
        return amenityService.getAllAmenities()
    }
}
