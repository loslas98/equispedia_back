package com.example.equispedia.Models

import jakarta.persistence.*

@Entity
@Table(name = "property_faqs")
data class PropertyFAQ(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    val property: Property,

    val question: String,
    
    @Column(columnDefinition = "TEXT")
    val answer: String
)
