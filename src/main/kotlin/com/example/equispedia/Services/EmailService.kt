package com.example.equispedia.Services

import com.example.equispedia.Models.Booking
import com.example.equispedia.Models.BookingItem
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.time.temporal.ChronoUnit

import org.slf4j.LoggerFactory

@Service
class EmailService(
    private val mailSender: JavaMailSender,
    @org.springframework.beans.factory.annotation.Value("\${spring.mail.username}")
    private val fromEmail: String
) {

    private val log = LoggerFactory.getLogger(EmailService::class.java)

    @Async
    fun sendBookingConfirmation(
        toEmail: String,
        firstName: String?,
        booking: Booking,
        items: List<BookingItem> = emptyList(),
        propertyImageUrl: String? = null
    ) {
        val name = firstName ?: booking.user.email ?: "Cliente"
        val subject = "Confirmación de Reserva - Equispedia"
        
        // El logo proporcionado por el usuario (Nota: Gmail bloquea imágenes HTTP. Lo ideal es usar HTTPS)
        val logoUrl = "https://i.ibb.co/dsztK1wr/Screenshot-5.png"
        
        val propImageHtml = if (propertyImageUrl != null) {
            """<div style="text-align: center; margin-bottom: 20px;">
                <img src="$propertyImageUrl" alt="${booking.property.name}" style="width: 100%; max-height: 300px; object-fit: cover; border-radius: 12px; box-shadow: 0 4px 10px rgba(0,0,0,0.1);">
               </div>"""
        } else ""

        val nights = ChronoUnit.DAYS.between(booking.checkIn, booking.checkOut).coerceAtLeast(1)
        val taxes = booking.totalPrice.toDouble() * 0.15 / 1.15
        val basePrice = booking.totalPrice.toDouble() - taxes

        val roomsHtml = if (items.isNotEmpty()) {
            items.joinToString("<hr style='border: 0; border-top: 1px dashed #e2e8f0; margin: 15px 0;'>") { item ->
                """
                <div style="margin-bottom: 5px;"><strong>${item.roomType.name}</strong></div>
                <div style="font-size: 13px; color: #718096; margin-bottom: 2px;">${item.roomType.maxOccupancyAdults} Adults, ${item.roomType.maxOccupancyChildren} Children</div>
                <div style="font-size: 13px; color: #718096;">1 room x $nights nights</div>
                """
            }
        } else {
            "<div style='font-size: 13px; color: #718096;'>1 room x $nights nights</div>"
        }

        val starsStr = "★".repeat(booking.property.starRating ?: 0)

        val body = """
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; color: #2d3748; line-height: 1.6; background-color: #f7fafc; margin: 0; padding: 20px 0; }
                    .container { max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.05); }
                    .header { background-color: #0b1c2d; padding: 30px 20px; text-align: center; border-bottom: 4px solid #28a745; }
                    .header img { max-width: 200px; height: auto; }
                    .content { padding: 40px 30px; }
                    h2 { color: #0b1c2d; margin-top: 0; font-size: 22px; font-weight: 600; }
                    .greeting { font-size: 18px; color: #4a5568; margin-bottom: 25px; }
                    .details-box { background-color: #f8f9fa; border: 1px solid #e2e8f0; border-radius: 12px; padding: 25px; margin-top: 25px; margin-bottom: 25px; }
                    .title-green { color: #28a745; font-weight: 700; text-transform: uppercase; font-size: 13px; letter-spacing: 1px; margin-bottom: 15px; display: block; border-bottom: 1px solid #e2e8f0; padding-bottom: 8px; }
                    .detail-row { margin-bottom: 10px; font-size: 15px; }
                    .detail-row strong { color: #2d3748; display: inline-block; width: 140px; }
                    .price-row { display: flex; justify-content: space-between; font-size: 14px; margin-bottom: 8px; color: #4a5568; }
                    .total-price { font-size: 18px; color: #28a745; font-weight: bold; margin-top: 15px; border-top: 1px solid #e2e8f0; padding-top: 15px; display: flex; justify-content: space-between; }
                    .footer { font-size: 13px; color: #718096; text-align: center; margin-top: 10px; background-color: #f8f9fa; padding: 25px 20px; border-top: 1px solid #e2e8f0; }
                    .prop-title { font-size: 18px; font-weight: bold; margin-bottom: 5px; color: #0b1c2d; }
                    .prop-stars { color: #f6ad55; font-size: 16px; margin-bottom: 2px; }
                    .prop-address { font-size: 13px; color: #718096; margin-bottom: 15px; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <img src="$logoUrl" alt="Equispedia Logo">
                    </div>
                    <div class="content">
                        <div class="greeting">¡Hola <strong>${name.uppercase()}</strong>!</div>
                        <h2>Tu reserva está confirmada 🎉</h2>
                        <p>Hemos procesado tu pago con éxito y todo está listo para tu estadía. A continuación, encontrarás los detalles de tu reserva.</p>
                        
                        $propImageHtml
                        
                        <div class="details-box">
                            <span class="title-green">Detalles de la Propiedad</span>
                            <div class="prop-stars">$starsStr</div>
                            <div class="prop-title">${booking.property.name}</div>
                            <div class="prop-address">📍 ${booking.property.address}</div>
                            
                            <div class="detail-row" style="margin-top: 15px;"><strong>Reserva #:</strong> ${booking.id}</div>
                            <div class="detail-row"><strong>Check-in:</strong> ${booking.checkIn}</div>
                            <div class="detail-row" style="margin-bottom: 20px;"><strong>Check-out:</strong> ${booking.checkOut}</div>
                            
                            <span class="title-green" style="margin-top: 25px;">Detalles de la Habitación</span>
                            $roomsHtml
                            
                            <span class="title-green" style="margin-top: 25px;">Detalle del Precio</span>
                            <div class="price-row">
                                <span>1 room x $nights nights</span>
                                <span>$${String.format("%.2f", basePrice)}</span>
                            </div>
                            <div class="price-row">
                                <span>Taxes and fees (15%)</span>
                                <span>$${String.format("%.2f", taxes)}</span>
                            </div>
                            <div class="total-price">
                                <span>Total Pagado:</span>
                                <span>$${String.format("%.2f", booking.totalPrice)}</span>
                            </div>
                        </div>

                        <span class="title-green" style="border:none;">Información Importante</span>
                        <p style="font-size: 14px; color: #4a5568;">
                            • Por favor, presenta una identificación válida al momento del check-in.<br>
                            • Si tienes alguna consulta especial, no dudes en contactarnos respondiendo a este correo.
                        </p>
                        
                        <p style="margin-top: 30px; font-weight: bold; color: #0b1c2d;">¡Te esperamos pronto!</p>
                    </div>
                    <div class="footer">
                        Este es un mensaje automático generado por Equispedia. Por favor, no respondas a este correo.
                    </div>
                </div>
            </body>
            </html>
        """.trimIndent()

        try {
            val message: MimeMessage = mailSender.createMimeMessage()
            val helper = MimeMessageHelper(message, true, "UTF-8")
            
            helper.setFrom(fromEmail, "Equispedia")
            helper.setTo(toEmail)
            helper.setSubject(subject)
            helper.setText(body, true) // true indicates HTML

            mailSender.send(message)
            log.info("Email sent successfully to $toEmail")
        } catch (e: Exception) {
            log.error("Failed to send email to $toEmail", e)
        }
    }

    fun sendSimpleEmail(to: String, subject: String, body: String) {
        try {
            val message = SimpleMailMessage().apply {
                setTo(to)
                setSubject(subject)
                setText(body)
                setFrom(fromEmail)
            }
            mailSender.send(message)
            log.info("Email sent successfully to $to")
        } catch (e: Exception) {
            log.error("Failed to send email to $to: ${e.message}", e)
        }
    }
}
