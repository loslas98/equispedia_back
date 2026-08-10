package com.example.equispedia.Controllers

import com.example.equispedia.DTO.CreateTicketRequest
import com.example.equispedia.Services.SupportTicketService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/support/tickets")
class SupportTicketController(
    private val service: SupportTicketService
) {
    @PostMapping
    fun createTicket(@RequestBody request: CreateTicketRequest): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(service.createTicket(request))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }
}
