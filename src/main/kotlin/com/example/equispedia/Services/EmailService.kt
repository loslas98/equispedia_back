package com.example.equispedia.Services

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender
) {
    fun sendSimpleEmail(to: String, subject: String, body: String) {
        try {
            val message = SimpleMailMessage().apply {
                setTo(to)
                setSubject(subject)
                setText(body)
                setFrom("no-reply@equispedia.com")
            }
            mailSender.send(message)
            println("Email sent successfully to $to")
        } catch (e: Exception) {
            System.err.println("Failed to send email to $to: ${e.message}")
        }
    }
}
