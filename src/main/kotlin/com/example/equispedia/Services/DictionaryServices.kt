package com.example.equispedia.Services

import com.example.equispedia.DTO.*
import com.example.equispedia.Models.*
import com.example.equispedia.Repository.*
import org.springframework.stereotype.Service

@Service
class TagService(private val tagRepository: TagRepository) {
    fun toResponse(tag: Tag) = TagResponse(tag.id, tag.name)
    fun createTag(req: TagRequest) = toResponse(tagRepository.save(Tag(name = req.name)))
    fun getAllTags() = tagRepository.findAll().map(::toResponse)
}

@Service
class PropertyTypeService(private val repo: PropertyTypeRepository) {
    fun toResponse(pt: PropertyType) = PropertyTypeResponse(pt.id, pt.name)
    fun createType(req: PropertyTypeRequest) = toResponse(repo.save(PropertyType(name = req.name)))
    fun getAllTypes() = repo.findAll().map(::toResponse)
}

@Service
class PaymentMethodService(private val repo: PaymentMethodRepository) {
    fun toResponse(pm: PaymentMethod) = PaymentMethodResponse(pm.id, pm.name, pm.iconUrl)
    fun createPaymentMethod(req: PaymentMethodRequest) = toResponse(repo.save(PaymentMethod(name = req.name, iconUrl = req.iconUrl)))
    fun getAllPaymentMethods() = repo.findAll().map(::toResponse)
}
