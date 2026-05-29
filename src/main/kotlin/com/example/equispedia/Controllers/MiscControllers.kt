package com.example.equispedia.Controllers

import com.example.equispedia.DTO.*
import com.example.equispedia.Services.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/points-of-interest")
class PointOfInterestController(private val service: PointOfInterestService) {
    @PostMapping
    fun create(@RequestBody req: PointOfInterestRequest) = ResponseEntity.ok(service.create(req))
    
    @GetMapping
    fun getAll() = ResponseEntity.ok(service.getAll())
}

@RestController
@RequestMapping("/api/images")
class ImageController(private val service: ImageService) {
    @PostMapping
    fun create(@RequestBody req: ImageRequest) = ResponseEntity.ok(service.createImage(req))
}

@RestController
@RequestMapping("/api/faqs")
class PropertyFAQController(private val service: PropertyFAQService) {
    @PostMapping
    fun create(@RequestBody req: PropertyFAQRequest) = ResponseEntity.ok(service.create(req))
}

@RestController
@RequestMapping("/api/reviews")
class ReviewController(private val service: ReviewService) {
    @PostMapping
    fun create(@RequestBody req: ReviewRequest) = ResponseEntity.ok(service.create(req))
}

@RestController
@RequestMapping("/api/amenities")
class AmenityController(private val service: AmenityService) {
    @PostMapping("/categories")
    fun createCategory(@RequestBody req: AmenityCategoryRequest) = ResponseEntity.ok(service.createCategory(req))
    
    @PostMapping
    fun createAmenity(@RequestBody req: AmenityRequest) = ResponseEntity.ok(service.createAmenity(req))
}

@RestController
@RequestMapping("/api/property-types")
class PropertyTypeController(private val service: PropertyTypeService) {
    @PostMapping
    fun create(@RequestBody req: PropertyTypeRequest) = ResponseEntity.ok(service.createType(req))

    @GetMapping
    fun getAll() = ResponseEntity.ok(service.getAllTypes())
}
