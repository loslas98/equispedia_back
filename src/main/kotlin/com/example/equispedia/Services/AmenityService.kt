package com.example.equispedia.Services

import com.example.equispedia.DTO.*
import com.example.equispedia.Models.*
import com.example.equispedia.Repository.*
import org.springframework.stereotype.Service

@Service
class AmenityService(
    private val categoryRepository: AmenityCategoryRepository,
    private val amenityRepository: AmenityRepository
) {
    fun toCategoryResponse(cat: AmenityCategory) = AmenityCategoryResponse(cat.id, cat.name)
    fun toAmenityResponse(amenity: Amenity) = AmenityResponse(
        id = amenity.id,
        category = toCategoryResponse(amenity.category),
        name = amenity.name,
        iconName = amenity.iconName
    )

    fun createCategory(req: AmenityCategoryRequest) = toCategoryResponse(categoryRepository.save(AmenityCategory(name = req.name)))
    
    fun createAmenity(req: AmenityRequest): AmenityResponse {
        val cat = categoryRepository.findById(req.categoryId).orElseThrow()
        val amenity = Amenity(category = cat, name = req.name, iconName = req.iconName)
        return toAmenityResponse(amenityRepository.save(amenity))
    }
}
