package com.example.equispedia.Controllers

import com.example.equispedia.DTO.PointOfInterestRequest
import com.example.equispedia.DTO.PointOfInterestResponse
import com.example.equispedia.Services.PointOfInterestService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

import java.math.BigDecimal

class MiscControllersTest {

    private val pointOfInterestService: PointOfInterestService = mockk()
    private val controller = PointOfInterestController(pointOfInterestService)

    @Test
    fun `create should return ok`() {
        val req = PointOfInterestRequest("Park", BigDecimal.ZERO, BigDecimal.ZERO)
        val res = PointOfInterestResponse(1, "Park", BigDecimal.ZERO, BigDecimal.ZERO)
        every { pointOfInterestService.create(req) } returns res

        val response = controller.create(req)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(res, response.body)
    }

    @Test
    fun `getAll should return list`() {
        every { pointOfInterestService.getAll() } returns emptyList()

        val response = controller.getAll()

        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(0, response.body?.size)
    }
}
