package com.example.equispedia.DTO

data class PaymentMethodRequest(
    val name: String,
    val iconUrl: String? = null
)

data class PaymentMethodResponse(
    val id: Int,
    val name: String,
    val iconUrl: String?
)
