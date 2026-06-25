package com.example.equispedia.Services

import com.example.equispedia.DTO.*
import com.example.equispedia.Models.*
import com.example.equispedia.Repository.*
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PropertyService(
    private val propertyRepository: PropertyRepository,
    private val propertyTypeRepository: PropertyTypeRepository,
    private val regionRepository: RegionRepository,
    private val tagRepository: TagRepository,
    private val amenityRepository: AmenityRepository,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val roomTypeRepository: RoomTypeRepository,
    private val roomInventoryRepository: RoomInventoryRepository
) {

    @Transactional
    fun createProperty(req: PropertyRequest): PropertyResponse {
        val type = propertyTypeRepository.findById(req.propertyTypeId).orElseThrow()
        val region = regionRepository.findById(req.regionId).orElseThrow()
        
        val tags = tagRepository.findAllById(req.tagIds).toMutableSet()
        val amenities = amenityRepository.findAllById(req.amenityIds).toMutableSet()
        val pms = paymentMethodRepository.findAllById(req.paymentMethodIds).toMutableSet()

        val prop = Property(
            name = req.name,
            propertyType = type,
            region = region,
            address = req.address,
            latitude = req.latitude,
            longitude = req.longitude,
            starRating = req.starRating,
            description = req.description,
            checkInStartTime = req.checkInStartTime,
            checkInEndTime = req.checkInEndTime,
            checkOutTime = req.checkOutTime,
            minAgeCheckIn = req.minAgeCheckIn,
            contactlessCheckIn = req.contactlessCheckIn,
            petsAllowed = req.petsAllowed,
            childrenAllowed = req.childrenAllowed,
            importantInfo = req.importantInfo
        )
        prop.tags.addAll(tags)
        prop.amenities.addAll(amenities)
        prop.paymentMethods.addAll(pms)

        return propertyRepository.save(prop).toResponse()
    }

    @Transactional(readOnly = true)
    fun getProperty(id: Int) = propertyRepository.findById(id).map { it.toResponse() }.orElse(null)
    
    @Transactional(readOnly = true)
    fun getPropertyWithIncludes(id: Int, include: String?): PropertyDetailResponse? {
        val propOpt = propertyRepository.findById(id)
        if (propOpt.isEmpty) return null
        return propOpt.get().toDetailResponse(include)
    }
    
    @Transactional(readOnly = true)
    fun getAllProperties() = propertyRepository.findAll().map { it.toResponse() }

    @Transactional(readOnly = true)
    fun searchHotels(req: HotelSearchRequest): List<HotelSearchResult> {
            val candidates = propertyRepository.findCandidatesByRegionAndCapacity(
                req.regionId, req.adults, req.children
            )

            return candidates.mapNotNull { property ->
                val available = roomTypeRepository.hasAvailability(
                    property.id, req.adults, req.children, req.checkIn, req.checkOut
                )

                if (!available) return@mapNotNull null

                val lowestPrice = roomTypeRepository.findLowestAvailablePrice(
                    property.id, req.adults, req.children, req.checkIn, req.checkOut
                ) ?: return@mapNotNull null

                val hasFreeCancellation = roomTypeRepository.hasRefundableAvailability(
                    property.id, req.adults, req.children, req.checkIn, req.checkOut
                )

                HotelSearchResult(
                    id = property.id,
                    name = property.name,
                    region = property.region.toResponse(),
                    propertyType = property.propertyType.toResponse(),
                    tags = property.tags.map { it.toResponse() },
                    amenities = property.amenities.map { it.toResponse() },
                    starRating = property.starRating,
                    latitude = property.latitude,
                    longitude = property.longitude,
                    lowestPricePerNight = lowestPrice,
                    thumbnailUrl = property.images.firstOrNull()?.url,
                    petsAllowed = property.petsAllowed,
                    childrenAllowed = property.childrenAllowed,
                    contactlessCheckIn = property.contactlessCheckIn,
                    hasFreeCancellation = hasFreeCancellation
                )
            }
    }

    fun checkAvailability(
        propertyId: Int,
        checkIn: java.time.LocalDate,
        checkOut: java.time.LocalDate,
        guests: Int
    ): PropertyAvailabilityResponse {
        val totalNights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut).toInt()
        val inventories = roomInventoryRepository.findByPropertyIdAndDateBetween(propertyId, checkIn, checkOut)
        
        // Group inventories by roomType ID
        val inventoriesByRoom = inventories.groupBy { it.roomType.id }
        
        // Let's get all room types associated with the property to ensure we report on ALL of them
        val allRoomTypes = roomTypeRepository.findByPropertyId(propertyId)
        
        val roomAvailabilityResponses = allRoomTypes.map { roomType ->
            val roomInventories = inventoriesByRoom[roomType.id] ?: emptyList()
            
            // Check capacity
            val capacityFits = (roomType.maxOccupancyAdults + roomType.maxOccupancyChildren) >= guests
            
            // Check availability for every night in the range
            val isAvailable = capacityFits &&
                    roomInventories.size == totalNights &&
                    roomInventories.all { it.roomsAvailable > 0 }
            
            // Calculate total price and price modifier
            var totalPrice = java.math.BigDecimal.ZERO
            if (isAvailable) {
                for (inv in roomInventories) {
                    val base = roomType.basePricePerNight
                    val modifier = inv.priceModifier ?: java.math.BigDecimal.ONE
                    val discount = inv.discountPercentage ?: java.math.BigDecimal.ZERO
                    val nightPrice = base.multiply(modifier).multiply(java.math.BigDecimal.ONE.subtract(discount))
                    totalPrice = totalPrice.add(nightPrice)
                }
            } else {
                totalPrice = roomType.basePricePerNight.multiply(java.math.BigDecimal(totalNights))
            }
            
            val avgPrice = if (totalNights > 0) {
                totalPrice.divide(java.math.BigDecimal(totalNights), 2, java.math.RoundingMode.HALF_UP)
            } else {
                roomType.basePricePerNight
            }
            
            RoomAvailabilityResponse(
                roomTypeId = roomType.id,
                name = roomType.name,
                isAvailable = isAvailable,
                totalPrice = totalPrice.setScale(2, java.math.RoundingMode.HALF_UP),
                pricePerNightAverage = avgPrice
            )
        }
        
        val anyRoomAvailable = roomAvailabilityResponses.any { it.isAvailable }
        
        return PropertyAvailabilityResponse(
            propertyId = propertyId,
            isAvailable = anyRoomAvailable,
            checkIn = checkIn,
            checkOut = checkOut,
            rooms = roomAvailabilityResponses
        )
    }
}
