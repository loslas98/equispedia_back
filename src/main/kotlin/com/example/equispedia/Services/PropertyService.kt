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
    private val tagService: TagService,
    private val amenityService: AmenityService,
    private val paymentMethodService: PaymentMethodService,
    private val roomTypeRepository: RoomTypeRepository
) {
    fun toResponse(prop: Property): PropertyResponse {
        return PropertyResponse(
            id = prop.id,
            name = prop.name,
            propertyType = PropertyTypeResponse(prop.propertyType.id, prop.propertyType.name),
            region = RegionResponse(prop.region.id, prop.region.name, prop.region.type, prop.region.parentRegion?.id),
            address = prop.address,
            latitude = prop.latitude,
            longitude = prop.longitude,
            starRating = prop.starRating,
            description = prop.description,
            checkInStartTime = prop.checkInStartTime,
            checkInEndTime = prop.checkInEndTime,
            checkOutTime = prop.checkOutTime,
            minAgeCheckIn = prop.minAgeCheckIn,
            contactlessCheckIn = prop.contactlessCheckIn,
            petsAllowed = prop.petsAllowed,
            childrenAllowed = prop.childrenAllowed,
            importantInfo = prop.importantInfo,
            tags = prop.tags.map(tagService::toResponse),
            amenities = prop.amenities.map(amenityService::toAmenityResponse),
            paymentMethods = prop.paymentMethods.map(paymentMethodService::toResponse)
        )
    }

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

        return toResponse(propertyRepository.save(prop))
    }

    fun getProperty(id: Int) = propertyRepository.findById(id).map(::toResponse).orElse(null)
    fun getAllProperties() = propertyRepository.findAll().map(::toResponse)

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
                    region = RegionResponse(
                        property.region.id,
                        property.region.name,
                        property.region.type,
                        property.region.parentRegion?.id
                    ),
                    propertyType = PropertyTypeResponse(
                        property.propertyType.id,
                        property.propertyType.name
                    ),
                    tags = property.tags.map(tagService::toResponse),
                    amenities = property.amenities.map(amenityService::toAmenityResponse),
                    starRating = property.starRating,
                    latitude = property.latitude,
                    longitude = property.longitude,
                    lowestPricePerNight = lowestPrice,
                    thumbnailUrl = null,  // pendiente: ver punto 6
                    petsAllowed = property.petsAllowed,
                    childrenAllowed = property.childrenAllowed,
                    contactlessCheckIn = property.contactlessCheckIn,
                    hasFreeCancellation = hasFreeCancellation
                )
            }
    }
}
