package com.example.equispedia.Services

import com.example.equispedia.Models.Booking
import jakarta.mail.internet.MimeMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.scheduling.annotation.Async

@Service
class EmailService(private val mailSender: JavaMailSender) {

    @Async
    fun sendBookingConfirmation(
        toEmail: String,
        firstName: String?,
        booking: Booking
    ) {
        val name = firstName ?: booking.user.email ?: "Cliente"
        
        val subject = "Confirmación de Reserva - Equispedia"
        val body = """
            <!DOCTYPE html>
            <html lang="es">
            <head>
                <meta charset="UTF-8">
                <style>
                    body { font-family: Arial, sans-serif; color: #333; line-height: 1.6; }
                    .container { max-width: 600px; margin: 0 auto; padding: 20px; border: 1px solid #eaeaea; }
                    .header { background-color: #0b1c2d; padding: 20px; text-align: center; }
                    .header h1 { color: #ffffff; margin: 0; font-size: 24px; }
                    .content { padding: 20px; }
                    .title-green { color: #28a745; font-weight: bold; margin-top: 20px; text-transform: uppercase; font-size: 14px;}
                    .footer { font-size: 12px; color: #777; text-align: center; margin-top: 30px; border-top: 1px solid #eaeaea; padding-top: 20px;}
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>Equispedia</h1>
                    </div>
                    <div class="content">
                        <p>Hola <strong>${'$'}{name.uppercase()}</strong>,</p>
                        <p>Tu reserva ha sido confirmada y procesada con éxito. En este correo encontrarás toda la información necesaria sobre tu estadía.</p>
                        
                        <div class="title-green">DETALLES DE LA RESERVA:</div>
                        <p>
                            <strong>Propiedad:</strong> ${'$'}{booking.property.name}<br>
                            <strong>ID de Reserva:</strong> #${'$'}{booking.id}<br>
                            <strong>Check-in:</strong> ${'$'}{booking.checkIn}<br>
                            <strong>Check-out:</strong> ${'$'}{booking.checkOut}<br>
                            <strong>Monto Total Pagado:</strong> ${'$'}${'$'}{booking.totalPrice}
                        </p>

                        <div class="title-green">INFORMACIÓN IMPORTANTE:</div>
                        <p>
                            Para el check-in, por favor presenta tu identificación.<br>
                        </p>

                        <p>Si tienes alguna consulta, no dudes en responder a este correo.</p>
                        <p>¡Te esperamos!</p>
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
            
            helper.setFrom("no-reply@equispedia.online", "Equispedia")
            helper.setTo(toEmail)
            helper.setSubject(subject)
            helper.setText(body, true) // true indicates HTML

            mailSender.send(message)
            println("Email sent successfully to ${'$'}toEmail")
        } catch (e: Exception) {
            System.err.println("Failed to send email to ${'$'}toEmail: ${'$'}{e.message}")
            e.printStackTrace()
        }
    }
}
