package com.example.equispedia.Services

import com.example.equispedia.DTO.*
import com.example.equispedia.Models.*
import com.example.equispedia.Repository.*
import org.springframework.stereotype.Service

@Service
class RoomTypeService(
    private val roomTypeRepository: RoomTypeRepository,
    private val propertyRepository: PropertyRepository,
    private val amenityRepository: AmenityRepository,
    private val amenityService: AmenityService
) {
    fun toResponse(room: RoomType): RoomTypeResponse = RoomTypeResponse(
        id = room.id,
        propertyId = room.property.id,
        name = room.name,
        basePricePerNight = room.basePricePerNight,
        maxOccupancyAdults = room.maxOccupancyAdults,
        maxOccupancyChildren = room.maxOccupancyChildren,
        sqMeters = room.sqMeters,
        isRefundable = room.isRefundable,
        freeCancellationDays = room.freeCancellationDays,
        cancellationPolicyDetails = room.cancellationPolicyDetails,
        amenities = room.amenities.map(amenityService::toAmenityResponse)
    )

    fun createRoomType(req: RoomTypeRequest): RoomTypeResponse {
        val prop = propertyRepository.findById(req.propertyId).orElseThrow()
        val amenities = amenityRepository.findAllById(req.amenityIds).toMutableSet()
        
        val roomType = RoomType(
            property = prop,
            name = req.name,
            basePricePerNight = req.basePricePerNight,
            maxOccupancyAdults = req.maxOccupancyAdults,
            maxOccupancyChildren = req.maxOccupancyChildren,
            sqMeters = req.sqMeters,
            isRefundable = req.isRefundable,
            freeCancellationDays = req.freeCancellationDays,
            cancellationPolicyDetails = req.cancellationPolicyDetails
        )
        roomType.amenities.addAll(amenities)
        
        return toResponse(roomTypeRepository.save(roomType))
    }
    
    fun getByProperty(propertyId: Int) = roomTypeRepository.findAll().filter { it.property.id == propertyId }.map(::toResponse)
}

@Service
class BedTypeService(private val bedTypeRepository: BedTypeRepository) {
    fun toResponse(bed: BedType) = BedTypeResponse(bed.id, bed.name)
    fun createBedType(req: BedTypeRequest) = toResponse(bedTypeRepository.save(BedType(name = req.name)))
    fun getAll() = bedTypeRepository.findAll().map(::toResponse)
}

@Service
class RoomInventoryService(
    private val inventoryRepository: RoomInventoryRepository,
    private val roomTypeRepository: RoomTypeRepository
) {
    fun toResponse(inv: RoomInventory) = RoomInventoryResponse(
        id = inv.id,
        roomTypeId = inv.roomType.id,
        date = inv.date,
        roomsAvailable = inv.roomsAvailable,
        priceModifier = inv.priceModifier,
        discountPercentage = inv.discountPercentage
    )

    fun createInventory(req: RoomInventoryRequest): RoomInventoryResponse {
        val roomType = roomTypeRepository.findById(req.roomTypeId).orElseThrow()
        val inv = RoomInventory(
            roomType = roomType,
            date = req.date,
            roomsAvailable = req.roomsAvailable,
            priceModifier = req.priceModifier,
            discountPercentage = req.discountPercentage
        )
        return toResponse(inventoryRepository.save(inv))
    }
}
