package com.example.equispedia.Models

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "support_tickets")
data class SupportTicket(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,
    
    val fullName: String,
    val email: String,
    val subject: String,
    
    @Column(columnDefinition = "TEXT")
    val message: String,
    
    @Column(name = "user_id")
    val userId: Int? = null,
    
    val status: String = "open",
    val createdAt: Instant = Instant.now()
)
