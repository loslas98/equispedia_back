package com.example.equispedia.Services

import com.example.equispedia.DTO.*
import com.example.equispedia.Models.*
import com.example.equispedia.Repository.*
import org.springframework.stereotype.Service

@Service
class PointOfInterestService(private val repo: PointOfInterestRepository) {
    fun toResponse(poi: PointOfInterest) = PointOfInterestResponse(poi.id, poi.name, poi.latitude, poi.longitude)
    fun create(req: PointOfInterestRequest) = toResponse(repo.save(PointOfInterest(name = req.name, latitude = req.latitude, longitude = req.longitude)))
    fun getAll() = repo.findAll().map(::toResponse)
}

@Service
class ImageService(
    private val imageRepository: ImageRepository,
    private val propertyRepository: PropertyRepository,
    private val roomTypeRepository: RoomTypeRepository
) {
    fun toResponse(img: Image) = ImageResponse(img.id, img.property.id, img.roomType?.id, img.url, img.altText, img.isMain)
    
    fun createImage(req: ImageRequest): ImageResponse {
        val prop = propertyRepository.findById(req.propertyId).orElseThrow()
        val room = req.roomTypeId?.let { roomTypeRepository.findById(it).orElse(null) }
        val img = Image(property = prop, roomType = room, url = req.url, altText = req.altText, isMain = req.isMain)
        return toResponse(imageRepository.save(img))
    }
}

@Service
class PropertyFAQService(
    private val faqRepository: PropertyFAQRepository,
    private val propertyRepository: PropertyRepository
) {
    fun toResponse(faq: PropertyFAQ) = PropertyFAQResponse(faq.id, faq.property.id, faq.question, faq.answer)
    fun create(req: PropertyFAQRequest): PropertyFAQResponse {
        val prop = propertyRepository.findById(req.propertyId).orElseThrow()
        val faq = PropertyFAQ(property = prop, question = req.question, answer = req.answer)
        return toResponse(faqRepository.save(faq))
    }
}

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository,
    private val propertyRepository: PropertyRepository,
    private val userRepository: UserRepository
) {
    fun toResponse(rev: Review) = ReviewResponse(
        id = rev.id, propertyId = rev.property.id, userId = rev.user.id,
        ratingOverall = rev.ratingOverall, ratingCleanliness = rev.ratingCleanliness,
        ratingService = rev.ratingService, ratingFacilities = rev.ratingFacilities,
        comment = rev.comment, travelerType = rev.travelerType, isVerified = rev.isVerified,
        createdAt = rev.createdAt
    )
    fun create(req: ReviewRequest): ReviewResponse {
        val prop = propertyRepository.findById(req.propertyId).orElseThrow()
        val user = userRepository.findById(req.userId).orElseThrow()
        val rev = Review(
            property = prop, user = user, ratingOverall = req.ratingOverall,
            ratingCleanliness = req.ratingCleanliness, ratingService = req.ratingService,
            ratingFacilities = req.ratingFacilities, comment = req.comment,
            travelerType = req.travelerType
        )
        return toResponse(reviewRepository.save(rev))
    }
}
