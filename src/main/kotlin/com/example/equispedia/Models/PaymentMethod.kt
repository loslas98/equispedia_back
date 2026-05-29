package com.example.equispedia.Models

import jakarta.persistence.*

@Entity
@Table(name = "payment_methods")
data class PaymentMethod(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    val name: String,
    
    val iconUrl: String? = null
)
