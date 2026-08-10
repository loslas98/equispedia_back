package com.example.equispedia.Repository

import com.example.equispedia.Models.SupportTicket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SupportTicketRepository : JpaRepository<SupportTicket, Int>
