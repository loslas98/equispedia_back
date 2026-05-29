package com.example.equispedia.Controllers

import com.example.equispedia.DTO.*
import com.example.equispedia.Services.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/room-types")
class RoomTypeController(private val service: RoomTypeService) {
    @PostMapping
    fun create(@RequestBody req: RoomTypeRequest) = ResponseEntity.ok(service.createRoomType(req))

    @GetMapping("/property/{propertyId}")
    fun getByProperty(@PathVariable propertyId: Int) = ResponseEntity.ok(service.getByProperty(propertyId))
}

@RestController
@RequestMapping("/api/bed-types")
class BedTypeController(private val service: BedTypeService) {
    @PostMapping
    fun create(@RequestBody req: BedTypeRequest) = ResponseEntity.ok(service.createBedType(req))

    @GetMapping
    fun getAll() = ResponseEntity.ok(service.getAll())
}

@RestController
@RequestMapping("/api/room-inventory")
class RoomInventoryController(private val service: RoomInventoryService) {
    @PostMapping
    fun create(@RequestBody req: RoomInventoryRequest) = ResponseEntity.ok(service.createInventory(req))
}
