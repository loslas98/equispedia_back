package com.example.equispedia.Services

import com.example.equispedia.DTO.CreateTicketRequest
import com.example.equispedia.Models.SupportTicket
import com.example.equispedia.Repository.SupportTicketRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SupportTicketService(
    private val repository: SupportTicketRepository,
    private val emailService: EmailService
) {
    @Transactional
    fun createTicket(request: CreateTicketRequest): SupportTicket {
        val ticket = repository.save(
            SupportTicket(
                fullName = request.fullName,
                email = request.email,
                subject = request.subject,
                message = request.message,
                userId = request.userId
            )
        )
        
        // Notificar al equipo de soporte
        emailService.sendSimpleEmail(
            to = "soporte@equispedia.com",
            subject = "Nuevo Ticket de Soporte #${ticket.id}: ${ticket.subject}",
            body = "De: ${ticket.fullName} (${ticket.email})\n\nMensaje:\n${ticket.message}"
        )
        
        // Confirmar recepción al usuario
        emailService.sendSimpleEmail(
            to = ticket.email,
            subject = "Confirmación de Ticket #${ticket.id} - Equispedia",
            body = "Hola ${ticket.fullName},\n\nHemos recibido tu consulta y asignado el ticket #${ticket.id}. Un agente se pondrá en contacto contigo a la brevedad.\n\nDetalle de tu mensaje:\n${ticket.message}"
        )
        
        return ticket
    }
}
