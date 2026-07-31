package com.example.equispedia.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class CorsConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**") // Aplica a todos los endpoints
            .allowedOrigins("http://localhost:5173", "https://equispedia.online", "https://www.equispedia.online") // Origen de tu frontend local
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos permitidos
            .allowedHeaders("*") // Permite cualquier cabecera
            .allowCredentials(true) // Permite el envío de cookies o credenciales
    }
}
