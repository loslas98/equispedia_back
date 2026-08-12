package com.example.equispedia.Models

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test

class UserTest {

    @Test
    fun `should create User with correct fields`() {
        // Arrange
        val expectedEmail = "test@example.com"
        val expectedName = "Test User"
        val expectedPasswordHash = "hash"

        // Act
        val user = User(
            id = 1,
            email = expectedEmail,
            fullName = expectedName,
            passwordHash = expectedPasswordHash
        )

        // Assert
        assertEquals(1, user.id)
        assertEquals(expectedEmail, user.email)
        assertEquals(expectedName, user.fullName)
        assertEquals(expectedPasswordHash, user.passwordHash)
        assertFalse(user.isMember)
        assertEquals(0, user.loyaltyPoints)
    }
}
