package com.example.equispedia.DTO

import com.example.equispedia.Models.*

fun Tag.toResponse() = TagResponse(this.id, this.name)

fun PropertyType.toResponse() = PropertyTypeResponse(this.id, this.name)

fun PaymentMethod.toResponse() = PaymentMethodResponse(this.id, this.name, this.iconUrl)

fun AmenityCategory.toResponse() = AmenityCategoryResponse(this.id, this.name)

fun Amenity.toResponse() = AmenityResponse(this.id, this.category.toResponse(), this.name, this.iconName)

fun Region.toResponse() = RegionResponse(this.id, this.name, this.type, this.parentRegion?.id)

fun Image.toResponse() = ImageResponse(this.id, this.property.id, this.roomType?.id, this.url, this.altText, this.isMain)

fun PropertyFAQ.toResponse() = PropertyFAQResponse(this.id, this.property.id, this.question, this.answer)

fun Review.toResponse() = ReviewResponse(this.id, this.property.id, this.user.id, this.ratingOverall, this.ratingCleanliness, this.ratingService, this.ratingFacilities, this.comment, this.travelerType, this.isVerified, this.createdAt)

fun RoomType.toResponse() = RoomTypeResponse(this.id, this.property.id, this.name, this.basePricePerNight, this.maxOccupancyAdults, this.maxOccupancyChildren, this.sqMeters, this.isRefundable, this.freeCancellationDays, this.cancellationPolicyDetails, this.amenities.map { it.toResponse() })

fun Property.toDetailResponse(include: String?): PropertyDetailResponse {
    val includes = include?.split(",")?.map { it.trim().lowercase() } ?: emptyList()
    
    return PropertyDetailResponse(
        id = this.id,
        name = this.name,
        propertyType = this.propertyType.toResponse(),
        region = this.region.toResponse(),
        address = this.address,
        latitude = this.latitude,
        longitude = this.longitude,
        starRating = this.starRating,
        description = this.description,
        checkInStartTime = this.checkInStartTime,
        checkInEndTime = this.checkInEndTime,
        checkOutTime = this.checkOutTime,
        minAgeCheckIn = this.minAgeCheckIn,
        contactlessCheckIn = this.contactlessCheckIn,
        petsAllowed = this.petsAllowed,
        childrenAllowed = this.childrenAllowed,
        importantInfo = this.importantInfo,
        tags = this.tags.map { it.toResponse() },
        amenities = this.amenities.map { it.toResponse() },
        paymentMethods = this.paymentMethods.map { it.toResponse() },
        rooms = if (includes.contains("rooms")) this.roomTypes.map { it.toResponse() } else null,
        faqs = if (includes.contains("faqs")) this.faqs.map { it.toResponse() } else null,
        reviews = if (includes.contains("reviews")) this.reviews.map { it.toResponse() } else null,
        images = if (includes.contains("images")) this.images.map { it.toResponse() } else null
    )
}

fun Property.toResponse(): PropertyResponse {
    return PropertyResponse(
        id = this.id,
        name = this.name,
        propertyType = this.propertyType.toResponse(),
        region = this.region.toResponse(),
        address = this.address,
        latitude = this.latitude,
        longitude = this.longitude,
        starRating = this.starRating,
        description = this.description,
        checkInStartTime = this.checkInStartTime,
        checkInEndTime = this.checkInEndTime,
        checkOutTime = this.checkOutTime,
        minAgeCheckIn = this.minAgeCheckIn,
        contactlessCheckIn = this.contactlessCheckIn,
        petsAllowed = this.petsAllowed,
        childrenAllowed = this.childrenAllowed,
        importantInfo = this.importantInfo,
        tags = this.tags.map { it.toResponse() },
        amenities = this.amenities.map { it.toResponse() },
        paymentMethods = this.paymentMethods.map { it.toResponse() },
        images = this.images.map { it.url },
        currentPrice = this.roomTypes.minByOrNull { it.basePricePerNight }?.basePricePerNight?.toPlainString()
    )
}
