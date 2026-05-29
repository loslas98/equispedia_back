package com.example.equispedia.Controllers

import com.example.equispedia.DTO.*
import com.example.equispedia.Services.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/regions")
class RegionController(private val service: RegionService) {
    @PostMapping
    fun create(@RequestBody req: RegionRequest) = ResponseEntity.ok(service.createRegion(req))

    @GetMapping
    fun getAll() = ResponseEntity.ok(service.getAllRegions())
}

@RestController
@RequestMapping("/api/tags")
class TagController(private val service: TagService) {
    @PostMapping
    fun create(@RequestBody req: TagRequest) = ResponseEntity.ok(service.createTag(req))

    @GetMapping
    fun getAll() = ResponseEntity.ok(service.getAllTags())
}

@RestController
@RequestMapping("/api/payment-methods")
class PaymentMethodController(private val service: PaymentMethodService) {
    @PostMapping
    fun create(@RequestBody req: PaymentMethodRequest) = ResponseEntity.ok(service.createPaymentMethod(req))

    @GetMapping
    fun getAll() = ResponseEntity.ok(service.getAllPaymentMethods())
}
