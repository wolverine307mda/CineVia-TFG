/*package org.wolve.geofilm.producciones.profesional.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
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
import org.wolve.geofilm.producciones.produccion.controller.ProduccionController
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalRequest
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalResponse
import org.wolve.geofilm.producciones.profesional.service.IProfesionalService
import java.util.*

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
@WebMvcTest(controllers = [ProfesionalController::class])
class ProfesionalControllerTest(@Autowired val mockMvc: MockMvc,
                                @Autowired val objectMapper: ObjectMapper) {

 @MockkBean
 private lateinit var profesionalService: IProfesionalService

 @Test
 fun `GET profesionales returns paginated list`() {
  val resp = listOf(ProfesionalResponse("p1", "A", null, Date(), Date(), "", null, 0))
  // Assuming controller maps GET /api/profesionales?page=0&size=10
  whenever(profesionalService.getAll(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(resp)

  mockMvc.perform(get("/api/profesionales")
   .param("page", "0").param("size", "10")
   .accept(MediaType.APPLICATION_JSON))
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.length()").value(resp.size))
   .andExpect(jsonPath("$[0].id").value("p1"))
 }

 @Test
 fun `GET profesional by id returns 200`() {
  val id = "p2"
  val response = ProfesionalResponse(id, "B", null, Date(), Date(), "", "Bio", 2)
  whenever(profesionalService.getById(id)).thenReturn(response)

  mockMvc.perform(get("/api/profesionales/{id}", id)
   .accept(MediaType.APPLICATION_JSON))
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.id").value(id))
   .andExpect(jsonPath("$.nombre").value("B"))
 }

 @Test
 fun `GET profesional by missing id returns 404`() {
  val id = "none"
  whenever(profesionalService.getById(id)).thenThrow(NoSuchElementException())

  mockMvc.perform(get("/api/profesionales/{id}", id))
   .andExpect(status().isNotFound)
 }

 @Test
 fun `POST profesional returns created`() {
  val req = ProfesionalRequest("C", "img.png", Date(), Date(), "City", "BioC")
  val saved = ProfesionalResponse("p3", "C", "img.png", req.fechaNacimiento, req.fechaInicio, "City", "BioC", 0)
  whenever(profesionalService.create(any())).thenReturn(saved)

  mockMvc.perform(post("/api/profesionales")
   .contentType(MediaType.APPLICATION_JSON)
   .content(objectMapper.writeValueAsString(req)))
   .andExpect(status().isCreated)
   .andExpect(header().string("Location", "/api/profesionales/p3"))
   .andExpect(jsonPath("$.id").value("p3"))
 }

 @Test
 fun `POST profesional with invalid body returns bad request`() {
  mockMvc.perform(post("/api/profesionales")
   .contentType(MediaType.APPLICATION_JSON)
   .content("{ invalid json }") )
   .andExpect(status().isBadRequest)
 }

 @Test
 fun `PUT profesional returns ok`() {
  val id = "p4"
  val req = ProfesionalRequest("D", null, Date(), Date(), "Town", "BioD")
  val resp = ProfesionalResponse(id, "D", null, req.fechaNacimiento, req.fechaInicio, "Town", "BioD", 1)
  whenever(profesionalService.update(eq(id), any())).thenReturn(resp)

  mockMvc.perform(put("/api/profesionales/{id}", id)
   .contentType(MediaType.APPLICATION_JSON)
   .content(objectMapper.writeValueAsString(req)))
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.id").value(id))
 }

 @Test
 fun `PUT missing profesional returns 404`() {
  val id = "none"
  val req = ProfesionalRequest("X", null, Date(), Date(), "", "")
  whenever(profesionalService.update(eq(id), any())).thenThrow(NoSuchElementException())

  mockMvc.perform(put("/api/profesionales/{id}", id)
   .contentType(MediaType.APPLICATION_JSON)
   .content(objectMapper.writeValueAsString(req)))
   .andExpect(status().isNotFound)
 }

 @Test
 fun `DELETE profesional returns no content`() {
  val id = "p5"
  whenever(profesionalService.delete(id)).thenReturn(Unit)

  mockMvc.perform(delete("/api/profesionales/{id}", id))
   .andExpect(status().isNoContent)
 }

 @Test
 fun `DELETE missing profesional returns not found`() {
  val id = "none"
  whenever(profesionalService.delete(id)).thenThrow(NoSuchElementException())

  mockMvc.perform(delete("/api/profesionales/{id}", id))
   .andExpect(status().isNotFound)
 }

 @Test
 fun `GET profesionales nombre filter returns filtered list`() {
  val resp = listOf(ProfesionalResponse("p6", "Filter", null, Date(), Date(), "", null, 0))
  whenever(profesionalService.getAll(eq("Filter"), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(resp)

  mockMvc.perform(get("/api/profesionales")
   .param("nombre", "Filter")
   .param("page", "0").param("size", "5")
   .accept(MediaType.APPLICATION_JSON))
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.length()").value(1))
 }

 @Test
 fun `DELETE profesional service throws exception returns 500`() {
  val id = "error"
  whenever(profesionalService.delete(id)).thenThrow(RuntimeException("DB error"))

  mockMvc.perform(delete("/api/profesionales/{id}", id))
   .andExpect(status().isInternalServerError)
 }

}
*/