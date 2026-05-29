package com.example.equispedia.Repository

import com.example.equispedia.Models.PaymentMethod
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PaymentMethodRepository : JpaRepository<PaymentMethod, Int>
