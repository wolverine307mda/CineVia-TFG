package org.wolve.geofilm.producciones.profesional.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
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
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalRequest
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalResponse
import org.wolve.geofilm.producciones.profesional.service.IProfesionalService
import org.wolve.geofilm.users.repositories.UsuarioRepository
import org.wolve.geofilm.utils.pagination.PaginationUtils
import org.wolve.geofilm.utils.storage.images.FirebaseStorageService
import java.util.*
import javax.sql.DataSource

@ActiveProfiles("test")
@WebMvcTest(
 controllers = [ProfesionalController::class],
 excludeAutoConfiguration = [
  SecurityFilterAutoConfiguration::class,
  DataSourceAutoConfiguration::class,
  HibernateJpaAutoConfiguration::class,
  FlywayAutoConfiguration::class
 ]
)
@AutoConfigureMockMvc(addFilters = false)
class ProfesionalControllerTest(
 @Autowired val mockMvc: MockMvc,
 @Autowired val objectMapper: ObjectMapper
) {

 @MockkBean private lateinit var jwtService: JwtService
 @MockkBean private lateinit var usuarioRepository: UsuarioRepository
 @MockkBean private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter
 @MockkBean private lateinit var dataSource: DataSource
 @MockkBean private lateinit var flyway: Flyway
 @MockkBean private lateinit var firebaseStorageService: FirebaseStorageService

 @BeforeEach
 fun setup() {
  every { flyway.migrate() } returns mockk()
 }

 @MockkBean
 private lateinit var profesionalService: IProfesionalService

 @Test
 fun getprofesionales() {
  val data = listOf(ProfesionalResponse("p1", "A", null, Date(), Date(), "", null, 0))
  val paginatedResponse = PaginationUtils.PaginatedResponse(
   data = data,
   totalItems = 1,
   totalPages = 1,
   currentPage = 0,
   pageSize = 10
  )

  every {
   profesionalService.getAllProfesionales(any(), any(), any(), any())
  } returns paginatedResponse

  mockMvc.perform(
   get("/api/profesionales")
    .param("page", "0")
    .param("size", "10")
    .accept(MediaType.APPLICATION_JSON)
  )
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.data.length()").value(data.size))
   .andExpect(jsonPath("$.data[0].id").value("p1"))
 }


 @Test
 fun getProfesionalById() {
  val id = "p2"
  val response = ProfesionalResponse(id, "B", null, Date(), Date(), "", "Bio", 2)
  every{profesionalService.getProfesionalById(id)}.returns(response)

  mockMvc.perform(get("/api/profesionales/{id}", id)
   .accept(MediaType.APPLICATION_JSON))
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.id").value(id))
   .andExpect(jsonPath("$.nombre").value("B"))
 }

 @Test
 fun getProfesionalIdNotFound() {
  val id = "none"
  every{profesionalService.getProfesionalById(id)}.throws(NoSuchElementException())

  mockMvc.perform(get("/api/profesionales/{id}", id))
   .andExpect(status().isNotFound)
 }

 @Test
 fun createdProfesional() {
  val req = ProfesionalRequest("C", "img.png", Date(), Date(), "City", "BioC")
  val saved = ProfesionalResponse("p3", "C", "img.png", req.fechaNacimiento, req.fechaInicio, "City", "BioC", 0)
  every {profesionalService.createProfesional(any())}.returns(saved)

  mockMvc.perform(post("/api/profesionales")
   .contentType(MediaType.APPLICATION_JSON)
   .content(objectMapper.writeValueAsString(req)))
   .andExpect(status().isCreated)
   .andExpect(header().string("Location", "/api/profesionales/p3"))
   .andExpect(jsonPath("$.id").value("p3"))
 }

 @Test
 fun crearProfesionalInvalido() {
  mockMvc.perform(post("/api/profesionales")
   .contentType(MediaType.APPLICATION_JSON)
   .content(  "{\"nombre\": \"C\", \"imagen\": \"img.png\", \"fechaNacimiento\": \"2020-01-01\", \"fechaInicio\": \"2020-01-01\", \"ciudad\": \"City\", \"biografia\": \"BioC\"}") )
   .andExpect(status().isBadRequest)
 }

 @Test
 fun actualizarProfesional() {
  val id = "p4"
  val req = ProfesionalRequest("D", null, Date(), Date(), "Town", "BioD")
  val resp = ProfesionalResponse(id, "D", null, req.fechaNacimiento, req.fechaInicio, "Town", "BioD", 1)
  every{profesionalService.updateProfesional(eq(id), any())}.returns(resp)

  mockMvc.perform(put("/api/profesionales/{id}", id)
   .contentType(MediaType.APPLICATION_JSON)
   .content(objectMapper.writeValueAsString(req)))
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.id").value(id))
 }

 @Test
 fun updateProfesionalNotFound() {
  val id = "none"
  val req = ProfesionalRequest("X", null, Date(), Date(), "", "")
  every{profesionalService.updateProfesional(eq(id), any())}.throws(NoSuchElementException())

  mockMvc.perform(put("/api/profesionales/{id}", id)
   .contentType(MediaType.APPLICATION_JSON)
   .content(objectMapper.writeValueAsString(req)))
   .andExpect(status().isNotFound)
 }

 @Test
 fun deleteProfesional() {
  val id = "p5"
  every{profesionalService.deleteProfesional(id)}.returns(Unit)

  mockMvc.perform(delete("/api/profesionales/{id}", id))
   .andExpect(status().isNoContent)
 }

 @Test
 fun deleteNotFound() {
  val id = "none"
  every{profesionalService.deleteProfesional(id)}.throws(NoSuchElementException())

  mockMvc.perform(delete("/api/profesionales/{id}", id))
   .andExpect(status().isNotFound)
 }

 @Test
 fun getProfesionalesFilterByNombre() {
  val data = listOf(ProfesionalResponse("p6", "Filter", null, Date(), Date(), "", null, 0))
  val paginatedResponse = PaginationUtils.PaginatedResponse(
   data = data,
   totalItems = 1,
   totalPages = 1,
   currentPage = 0,
   pageSize = 5
  )

  every {
   profesionalService.getAllProfesionales(
    page = 0,
    size = 5,
    sortBy = any(),
    sortDirection = any(),
   )
  } returns paginatedResponse

  mockMvc.perform(
   get("/api/profesionales")
    .param("nombre", "Filter")
    .param("page", "0")
    .param("size", "5")
    .accept(MediaType.APPLICATION_JSON)
  )
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.data.length()").value(1))
   .andExpect(jsonPath("$.data[0].id").value("p6"))
   .andExpect(jsonPath("$.data[0].nombre").value("Filter"))
 }

 @Test
 fun deleteLanzaError() {
  val id = "error"
  every{profesionalService.deleteProfesional(id)}.throws(RuntimeException("DB error"))

  mockMvc.perform(delete("/api/profesionales/{id}", id))
   .andExpect(status().isInternalServerError)
 }

}
