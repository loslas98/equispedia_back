package com.example.equispedia.Controllers

import com.example.equispedia.DTO.*
import com.example.equispedia.Services.PropertyService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDate

@RestController
@RequestMapping("/api/properties")
class PropertyController(private val propertyService: PropertyService) {

    @PostMapping
    fun createProperty(@RequestBody request: PropertyRequest): ResponseEntity<PropertyResponse> {
        return ResponseEntity.ok(propertyService.createProperty(request))
    }

    @Operation(
        summary = "Obtener una propiedad por su ID",
        description = "Retorna los detalles de la propiedad. Permite precargar relaciones dinámicamente mediante el parámetro 'include'."
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Propiedad obtenida exitosamente"),
        ApiResponse(responseCode = "404", description = "Propiedad no encontrada")
    ])
    @GetMapping("/{id}")
    fun getProperty(
        @Parameter(description = "ID único de la propiedad", required = true, example = "1")
        @PathVariable id: Int,

        @Parameter(
            description = "Relaciones separadas por comas a precargar en la respuesta. Opciones válidas: rooms, faqs, reviews, images",
            required = false,
            example = "rooms,faqs"
        )
        @RequestParam(required = false) include: String?
    ): ResponseEntity<Any> {
        return if (include != null) {
            val prop = propertyService.getPropertyWithIncludes(id, include)
            if (prop != null) ResponseEntity.ok(prop) else ResponseEntity.notFound().build()
        } else {
            val prop = propertyService.getProperty(id)
            if (prop != null) ResponseEntity.ok(prop) else ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    fun getAllProperties(): ResponseEntity<List<PropertyResponse>> {
        return ResponseEntity.ok(propertyService.getAllProperties())
    }

    @Operation(
        summary = "Verificar disponibilidad de habitaciones de una propiedad",
        description = "Retorna el estado de disponibilidad y el desglose de precios por habitación para un rango de fechas y cantidad de huéspedes."
    )
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Consulta realizada correctamente")
    ])
    @GetMapping("/{id}/availability")
    fun checkAvailability(
        @Parameter(description = "ID único de la propiedad", required = true, example = "1")
        @PathVariable id: Int,

        @Parameter(description = "Fecha de inicio (Check-in)", required = true, example = "2026-07-03")
        @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) checkIn: LocalDate,

        @Parameter(description = "Fecha de fin (Check-out)", required = true, example = "2026-07-06")
        @RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) checkOut: LocalDate,

        @Parameter(description = "Cantidad de huéspedes", required = true, example = "2")
        @RequestParam guests: Int
    ): ResponseEntity<PropertyAvailabilityResponse> {
        val response = propertyService.checkAvailability(id, checkIn, checkOut, guests)
        return ResponseEntity.ok(response)
    }
}
