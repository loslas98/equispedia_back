package com.example.equispedia.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtUtil {

    @Value("\${JWT_SECRET:super_secret_key_equispedia_1234567890_backend}")
    private lateinit var jwtSecretStr: String

    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtSecretStr.toByteArray(Charsets.UTF_8))
    }

    private val jwtExpirationMs = 86400000 // 24 hours

    fun generateToken(email: String): String {
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith(secretKey)
            .compact()
    }

    fun extractEmail(token: String): String {
        return getClaims(token).subject
    }

    fun validateToken(token: String): Boolean {
        try {
            getClaims(token)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    private fun getClaims(token: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .body
    }
}
