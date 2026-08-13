package com.example.equispedia.chat

import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import ai.koog.prompt.executor.clients.google.GoogleLLMClient
import ai.koog.prompt.executor.clients.google.GoogleModels
import ai.koog.prompt.executor.llms.MultiLLMPromptExecutor
import com.example.equispedia.DTO.HotelSearchRequest
import com.example.equispedia.Services.*
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@Service
class KoogChatbotService(
    @Value("\${GOOGLE_API_KEY:}") private val googleApiKey: String,
    private val propertyService: PropertyService,
    private val regionService: RegionService,
    private val roomTypeService: RoomTypeService,
    private val amenityService: AmenityService,
    private val paymentMethodService: PaymentMethodService,
    private val pointOfInterestService: PointOfInterestService,
    private val userService: UserService,
    private val authService: AuthService
) {
    fun chat(
        message: String,
        history: List<Map<String, String>> = emptyList(),
        authenticatedEmail: String? = null
    ): String {
        require(googleApiKey.isNotBlank()) { "GOOGLE_API_KEY no está configurada" }

        val conversation = buildString {
            history.takeLast(MAX_HISTORY_MESSAGES).forEach { item ->
                val content = item["content"]?.trim().orEmpty()
                if (content.isNotEmpty()) {
                    val speaker = if (item["role"] == "user") "Usuario" else "Asistente"
                    appendLine("$speaker: $content")
                }
            }
            append("Usuario: ${message.trim()}")
        }

        val tools = EquispediaTools(
            propertyService = propertyService,
            regionService = regionService,
            roomTypeService = roomTypeService,
            amenityService = amenityService,
            paymentMethodService = paymentMethodService,
            pointOfInterestService = pointOfInterestService,
            userService = userService,
            authService = authService,
            authenticatedEmail = authenticatedEmail,
            mutationsConfirmed = hasExplicitConfirmation(message)
        )

        val agent = AIAgent(
            promptExecutor = MultiLLMPromptExecutor(GoogleLLMClient(googleApiKey)),
            llmModel = GoogleModels.Gemini3_Flash_Preview,
            toolRegistry = ToolRegistry { tools(tools) },
            systemPrompt = SYSTEM_PROMPT
        )

        return runBlocking { agent.run(conversation) }
    }

    private fun hasExplicitConfirmation(message: String): Boolean {
        val normalized = message.lowercase()
        return CONFIRMATION_PHRASES.any(normalized::contains)
    }

    private companion object {
        const val MAX_HISTORY_MESSAGES = 20
        val CONFIRMATION_PHRASES = listOf("confirmo", "sí, crear", "si, crear", "sí, agrégalo", "si, agregalo")
        val SYSTEM_PROMPT = """
            Eres Yuko, el asistente virtual de Equispedia. Preséntate como Yuko cuando corresponda.
            Responde en español de forma clara, concisa y amable.
            Usa el historial solo como contexto. Para cualquier dato de Equispedia usa las herramientas;
            nunca inventes hoteles, regiones, disponibilidad, precios, servicios, políticas o favoritos.
            Para buscar hoteles con fechas, primero resuelve la región y después usa buscar_hoteles.
            Si faltan fechas, huéspedes u otro dato obligatorio, solicítalo antes de llamar la herramienta.
            Antes de cambiar favoritos, resume la acción y pide confirmación explícita.
            Solo intenta la acción cuando el último mensaje del usuario sea una confirmación clara.
            No puedes crear reservas, procesar pagos, iniciar sesión ni enviar correos arbitrarios.
            Escribe texto simple: no uses Markdown, asteriscos, encabezados ni tablas.
            Para enumeraciones usa líneas breves que comiencen con el carácter •.
        """.trimIndent()
    }
}

class EquispediaTools(
    private val propertyService: PropertyService,
    private val regionService: RegionService,
    private val roomTypeService: RoomTypeService,
    private val amenityService: AmenityService,
    private val paymentMethodService: PaymentMethodService,
    private val pointOfInterestService: PointOfInterestService,
    private val userService: UserService,
    private val authService: AuthService,
    private val authenticatedEmail: String?,
    private val mutationsConfirmed: Boolean
) : ToolSet {
    @Tool("buscar_regiones")
    @LLMDescription("Busca regiones reales registradas en Equispedia y devuelve sus identificadores")
    fun searchRegions(@LLMDescription("Nombre parcial de ciudad o región") query: String): String =
        runTool("buscar_regiones") {
            regionService.searchRegions(query).take(10).joinToString("\n") {
                "id=${it.id} | nombre=${it.name} | tipo=${it.type}"
            }.ifBlank { "No se encontraron regiones para: $query" }
        }

    @Tool("buscar_alojamientos")
    @LLMDescription("Busca alojamientos por texto cuando todavía no se requieren fechas ni disponibilidad")
    fun searchProperties(@LLMDescription("Ciudad, nombre, tipo, comodidad o característica") query: String): String =
        runTool("buscar_alojamientos") {
            val terms = query.lowercase().split(Regex("\\s+")).filter { it.length >= 3 }
            val properties = propertyService.getAllProperties()
            properties.filter { property ->
                if (terms.isEmpty()) return@filter true
                val searchable = listOf(
                    property.name, property.region.name, property.propertyType.name, property.address,
                    property.description.orEmpty(), property.tags.joinToString(" ") { it.name },
                    property.amenities.joinToString(" ") { it.name }
                ).joinToString(" ").lowercase()
                terms.any(searchable::contains)
            }.ifEmpty { properties }.take(MAX_RESULTS).joinToString("\n", transform = ::propertyLine)
                .ifBlank { "No hay alojamientos registrados." }
        }

    @Tool("buscar_hoteles")
    @LLMDescription("Busca hoteles con disponibilidad y precio real para región, fechas y cantidad de huéspedes")
    fun searchHotels(
        @LLMDescription("Identificador de región obtenido con buscar_regiones") regionId: Int,
        @LLMDescription("Fecha de entrada ISO YYYY-MM-DD") checkIn: String,
        @LLMDescription("Fecha de salida ISO YYYY-MM-DD") checkOut: String,
        @LLMDescription("Cantidad de adultos, mínimo 1") adults: Int,
        @LLMDescription("Cantidad de niños, puede ser 0") children: Int
    ): String = runTool("buscar_hoteles") {
        val start = parseDate(checkIn)
        val end = parseDate(checkOut)
        require(end.isAfter(start)) { "La fecha de salida debe ser posterior a la entrada." }
        require(adults >= 1 && children >= 0) { "La cantidad de huéspedes no es válida." }
        propertyService.searchHotels(HotelSearchRequest(regionId, start, end, adults, children))
            .take(MAX_RESULTS).joinToString("\n") {
                "id=${it.id} | nombre=${it.name} | región=${it.region.name} | estrellas=${it.starRating ?: "sin dato"} | " +
                    "precio_noche=${it.lowestPricePerNight} | cancelación_gratis=${yesNo(it.hasFreeCancellation)} | " +
                    "mascotas=${yesNo(it.petsAllowed)} | niños=${yesNo(it.childrenAllowed)}"
            }.ifBlank { "No se encontraron hoteles disponibles para esos criterios." }
    }

    @Tool("consultar_disponibilidad")
    @LLMDescription("Consulta habitaciones disponibles y precio total real de un alojamiento")
    fun checkAvailability(
        @LLMDescription("Identificador del alojamiento") propertyId: Int,
        @LLMDescription("Fecha de entrada ISO YYYY-MM-DD") checkIn: String,
        @LLMDescription("Fecha de salida ISO YYYY-MM-DD") checkOut: String,
        @LLMDescription("Cantidad total de huéspedes") guests: Int
    ): String = runTool("consultar_disponibilidad") {
        require(guests >= 1) { "Debe existir al menos un huésped." }
        val response = propertyService.checkAvailability(propertyId, parseDate(checkIn), parseDate(checkOut), guests)
        buildString {
            appendLine("alojamiento_id=${response.propertyId} | disponible=${yesNo(response.isAvailable)}")
            response.rooms.forEach {
                appendLine("habitación=${it.name} | disponible=${yesNo(it.isAvailable)} | total=${it.totalPrice} | promedio_noche=${it.pricePerNightAverage}")
            }
        }.trim()
    }

    @Tool("obtener_detalles_alojamiento")
    @LLMDescription("Obtiene políticas, servicios y datos reales de un alojamiento por identificador")
    fun propertyDetails(@LLMDescription("Identificador del alojamiento") propertyId: Int): String =
        runTool("obtener_detalles_alojamiento") {
            val p = propertyService.getProperty(propertyId) ?: return@runTool "No existe el alojamiento $propertyId."
            listOf(
                propertyLine(p), "dirección=${p.address}", "check_in=${p.checkInStartTime ?: "sin dato"}",
                "check_out=${p.checkOutTime ?: "sin dato"}", "edad_mínima=${p.minAgeCheckIn ?: "sin dato"}",
                "check_in_sin_contacto=${yesNo(p.contactlessCheckIn)}", "información=${p.importantInfo.orEmpty()}",
                "servicios=${p.amenities.joinToString(", ") { it.name }}",
                "medios_pago=${p.paymentMethods.joinToString(", ") { it.name }}"
            ).joinToString("\n")
        }

    @Tool("obtener_habitaciones")
    @LLMDescription("Lista tipos de habitación, capacidad, precio base y cancelación de un alojamiento")
    fun rooms(@LLMDescription("Identificador del alojamiento") propertyId: Int): String =
        runTool("obtener_habitaciones") {
            roomTypeService.getByProperty(propertyId).joinToString("\n") {
                "id=${it.id} | nombre=${it.name} | precio_base=${it.basePricePerNight} | adultos=${it.maxOccupancyAdults} | " +
                    "niños=${it.maxOccupancyChildren} | reembolsable=${yesNo(it.isRefundable)} | servicios=${it.amenities.joinToString(", ") { a -> a.name }}"
            }.ifBlank { "No hay habitaciones registradas para ese alojamiento." }
        }

    @Tool("consultar_catalogos")
    @LLMDescription("Consulta servicios y métodos de pago disponibles en la plataforma")
    fun catalogs(@LLMDescription("Usa servicios o metodos_pago") category: String): String =
        runTool("consultar_catalogos") {
            when (category.lowercase()) {
                "servicios", "amenities" -> amenityService.getAllAmenities().joinToString("\n") { "id=${it.id} | nombre=${it.name}" }
                "metodos_pago", "métodos_pago", "pagos" -> paymentMethodService.getAllPaymentMethods().joinToString("\n") { "id=${it.id} | nombre=${it.name}" }
                else -> "Categoría no válida. Usa servicios o metodos_pago."
            }
        }

    @Tool("consultar_puntos_interes")
    @LLMDescription("Lista puntos de interés registrados con sus coordenadas")
    fun pointsOfInterest(): String = runTool("consultar_puntos_interes") {
        pointOfInterestService.getAll().take(MAX_RESULTS).joinToString("\n") {
            "id=${it.id} | nombre=${it.name} | latitud=${it.latitude} | longitud=${it.longitude}"
        }.ifBlank { "No hay puntos de interés registrados." }
    }

    @Tool("ver_favoritos")
    @LLMDescription("Muestra los alojamientos favoritos del usuario autenticado")
    fun favorites(): String = runTool("ver_favoritos") {
        val email = requireAuthenticatedEmail()
        userService.getMyFavorites(email).joinToString("\n") {
            "id=${it.id} | nombre=${it.name} | región=${it.region} | estrellas=${it.starRating} | precio_base=${it.basePricePerNight}"
        }.ifBlank { "El usuario no tiene alojamientos favoritos." }
    }

    @Tool("cambiar_favorito")
    @LLMDescription("Agrega o elimina un alojamiento de favoritos solo después de confirmación explícita")
    fun toggleFavorite(@LLMDescription("Identificador del alojamiento confirmado por el usuario") propertyId: Int): String =
        runTool("cambiar_favorito") {
            requireConfirmedMutation()
            val user = authService.getMe(requireAuthenticatedEmail())
            val isFavorite = userService.toggleFavorite(user.id, propertyId)
            if (isFavorite) "Alojamiento agregado a favoritos." else "Alojamiento eliminado de favoritos."
        }

    private fun requireAuthenticatedEmail(): String =
        authenticatedEmail ?: throw IllegalStateException("El usuario debe iniciar sesión para realizar esta acción.")

    private fun requireConfirmedMutation() {
        check(mutationsConfirmed) { "La acción requiere confirmación explícita en el último mensaje del usuario." }
    }

    private fun parseDate(value: String): LocalDate =
        runCatching { LocalDate.parse(value) }.getOrElse { throw IllegalArgumentException("Fecha inválida: $value. Usa YYYY-MM-DD.") }

    private fun propertyLine(p: com.example.equispedia.DTO.PropertyResponse): String =
        "id=${p.id} | nombre=${p.name} | región=${p.region.name} | tipo=${p.propertyType.name} | " +
            "estrellas=${p.starRating ?: "sin dato"} | precio=${p.currentPrice ?: "sin dato"} | mascotas=${yesNo(p.petsAllowed)} | niños=${yesNo(p.childrenAllowed)}"

    private fun yesNo(value: Boolean) = if (value) "sí" else "no"

    private inline fun runTool(name: String, block: () -> String): String {
        logger.info("Koog ejecutó {}", name)
        return runCatching(block).getOrElse { "No fue posible ejecutar $name: ${it.message}" }
    }

    private companion object {
        val logger = LoggerFactory.getLogger(EquispediaTools::class.java)
        const val MAX_RESULTS = 15
    }
}

data class ChatMessageRequest(
    val message: String,
    val history: List<Map<String, String>> = emptyList()
)

data class ChatMessageResponse(val reply: String)

@RestController
@RequestMapping("/api/chat")
class ChatController(private val chatbotService: KoogChatbotService) {
    @PostMapping
    fun sendMessage(
        @RequestBody request: ChatMessageRequest,
        authentication: Authentication?
    ): ResponseEntity<ChatMessageResponse> {
        if (request.message.isBlank()) {
            return ResponseEntity.badRequest().body(ChatMessageResponse("El mensaje no puede estar vacío."))
        }

        return try {
            val email = authentication?.takeIf { it.isAuthenticated }?.name
            ResponseEntity.ok(ChatMessageResponse(chatbotService.chat(request.message, request.history, email)))
        } catch (_: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ChatMessageResponse("El asistente no está configurado temporalmente."))
        } catch (_: Exception) {
            ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(ChatMessageResponse("No fue posible consultar al asistente en este momento."))
        }
    }
}
