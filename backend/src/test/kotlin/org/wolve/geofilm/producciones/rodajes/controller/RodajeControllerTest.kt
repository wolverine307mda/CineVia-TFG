package org.wolve.geofilm.producciones.rodajes.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.wolve.geofilm.auth.services.jwt.JwtAuthenticationFilter
import org.wolve.geofilm.auth.services.jwt.JwtService
import org.wolve.geofilm.producciones.produccion.controller.ProduccionController
import java.time.LocalDateTime
import org.wolve.geofilm.producciones.rodajes.dto.request.RodajeRequest
import org.wolve.geofilm.producciones.rodajes.dto.response.RodajeResponse
import org.wolve.geofilm.producciones.rodajes.service.IRodajeService
import org.wolve.geofilm.producciones.ubicaciones.dto.UbicacionResponse
import org.wolve.geofilm.users.repositories.UsuarioRepository
import org.wolve.geofilm.utils.storage.images.FirebaseStorageService
import javax.sql.DataSource

@ActiveProfiles("test")
@WebMvcTest(
 controllers = [RodajeController::class],
 excludeAutoConfiguration = [
  SecurityFilterAutoConfiguration::class,
  DataSourceAutoConfiguration::class,
  HibernateJpaAutoConfiguration::class,
  FlywayAutoConfiguration::class
 ]
)
@AutoConfigureMockMvc(addFilters = false)
class RodajeControllerTest {

 @MockkBean private lateinit var jwtService: JwtService
 @MockkBean private lateinit var usuarioRepository: UsuarioRepository
 @MockkBean private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter
 @MockkBean private lateinit var dataSource: DataSource
 @MockkBean private lateinit var flyway: Flyway
 @MockkBean private lateinit var firebaseStorageService: FirebaseStorageService

 @Autowired
 lateinit var mockMvc: MockMvc

 @Autowired
 lateinit var objectMapper: ObjectMapper

 @MockkBean
 lateinit var rodajeService: IRodajeService

 @BeforeEach
 fun setup() {
  every { flyway.migrate() } returns mockk()
 }

 private val sampleUbicacion = UbicacionResponse(
  id = "ubic-1",
  nombre = "Plaza Mayor",
  latitud = 40.4168,
  longitud = -3.7038,
  createdAt = LocalDateTime.now(),
  updatedAt = LocalDateTime.now()
 )

 private val sampleResponse = RodajeResponse(
  id = "rod-1",
  notas = "Notas de prueba",
  imagenes = listOf("img1.jpg", "img2.jpg"),
  createdAt = LocalDateTime.now(),
  updatedAt = LocalDateTime.now(),
  ubicacion = sampleUbicacion
 )

 private val sampleRequest = RodajeRequest(
  produccionId = "prod-1",
  ubicacionId = "ubic-1",
  notas = "Notas de prueba",
  imagenes = listOf("img1.jpg", "img2.jpg")
 )

 @Test
 fun `should return rodaje by id`() {
  every { rodajeService.findById("rod-1") } returns sampleResponse

  mockMvc.perform(get("/api/rodajes/{id}", "rod-1"))
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.id").value("rod-1"))
   .andExpect(jsonPath("$.notas").value("Notas de prueba"))
 }

 @Test
 fun `should create rodaje`() {
  every { rodajeService.create(sampleRequest) } returns sampleResponse

  mockMvc.perform(
   post("/api/rodajes")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(sampleRequest))
  )
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.id").value("rod-1"))
 }

 @Test
 fun `should update rodaje`() {
  every { rodajeService.update("rod-1", sampleRequest) } returns sampleResponse

  mockMvc.perform(
   put("/api/rodajes/{id}", "rod-1")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(sampleRequest))
  )
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.id").value("rod-1"))
 }

 @Test
 fun `should delete rodaje`() {
  justRun { rodajeService.delete("rod-1") }

  mockMvc.perform(delete("/api/rodajes/{id}", "rod-1"))
   .andExpect(status().isNoContent)
 }

 @Test
 fun `should return rodajes by produccion`() {
  every { rodajeService.findByProduccion("prod-1", 0, 10) } returns mapOf(
   "data" to listOf(sampleResponse),
   "totalItems" to 1,
   "totalPages" to 1
  )

  mockMvc.perform(
   get("/api/rodajes/produccion/{produccionId}", "prod-1")
    .param("page", "0")
    .param("size", "10")
  )
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.totalItems").value(1))
   .andExpect(jsonPath("$.data[0].id").value("rod-1"))
 }
}

/**
 * Versión mínima del DTO de respuesta de Ubicacion para evitar dependencias en el módulo de ubicaciones.
 */
data class UbicacionResponse(
 val id: String,
 val nombre: String,
 val direccion: String,
 val latitud: Double,
 val longitud: Double
)
