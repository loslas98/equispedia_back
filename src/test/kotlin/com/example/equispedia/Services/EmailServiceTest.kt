package com.example.equispedia.Services

import com.example.equispedia.Models.Booking
import com.example.equispedia.Models.Property
import com.example.equispedia.Models.User
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.mail.internet.MimeMessage
import org.junit.jupiter.api.Test
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import java.time.LocalDate

class EmailServiceTest {

    private val mailSender: JavaMailSender = mockk()
    private val emailService = EmailService(mailSender)

    @Test
    fun `sendSimpleEmail should send email successfully`() {
        // Arrange
        val to = "test@example.com"
        val subject = "Test Subject"
        val body = "Test Body"

        // Mock mailSender to just accept the send method
        every { mailSender.send(any<SimpleMailMessage>()) } returns Unit

        // Act
        emailService.sendSimpleEmail(to, subject, body)

        // Assert
        verify(exactly = 1) { 
            mailSender.send(withArg<SimpleMailMessage> {
                assert(it.to?.contains(to) == true)
                assert(it.subject == subject)
                assert(it.text == body)
            }) 
        }
    }

    @Test
    fun `sendBookingConfirmation should send HTML email successfully`() {
        // Arrange
        val toEmail = "client@example.com"
        val mimeMessage = mockk<MimeMessage>(relaxed = true)
        
        every { mailSender.createMimeMessage() } returns mimeMessage
        every { mailSender.send(any<MimeMessage>()) } returns Unit

        val mockUser = User(
            id = 1,
            email = toEmail,
            passwordHash = "hash",
            fullName = "Test User"
        )
        
        val mockProperty = Property(
            id = 1,
            name = "Test Hotel",
            address = "123 Test St",
            propertyType = mockk(relaxed = true),
            region = mockk(relaxed = true),
            latitude = 0.0.toBigDecimal(),
            longitude = 0.0.toBigDecimal(),
            starRating = 5
        )
        
        val mockBooking = Booking(
            id = 1,
            user = mockUser,
            property = mockProperty,
            checkIn = LocalDate.now(),
            checkOut = LocalDate.now().plusDays(2),
            totalPrice = 200.0.toBigDecimal(),
            status = com.example.equispedia.Models.BookingStatus.CONFIRMED
        )

        // Act
        emailService.sendBookingConfirmation(
            toEmail = toEmail,
            firstName = "Client",
            booking = mockBooking,
            items = emptyList()
        )

        // Assert
        verify(exactly = 1) { mailSender.createMimeMessage() }
        verify(exactly = 1) { mailSender.send(mimeMessage) }
    }
}
