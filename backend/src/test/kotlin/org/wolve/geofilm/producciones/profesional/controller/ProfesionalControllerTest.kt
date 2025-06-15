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
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.wolve.geofilm.auth.services.jwt.JwtAuthenticationFilter
import org.wolve.geofilm.auth.services.jwt.JwtService
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalRequest
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalResponse
import org.wolve.geofilm.producciones.profesional.service.IProfesionalService
import org.wolve.geofilm.users.repositories.UsuarioRepository
import org.wolve.geofilm.utils.pagination.PaginationUtils
import org.wolve.geofilm.utils.storage.images.FirebaseStorageService
import java.text.SimpleDateFormat
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

 private val dateFormatter = SimpleDateFormat("yyyy-MM-dd")


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
 }

 @Test
 fun createdProfesional() {
  val req = ProfesionalRequest("C", "img.png", "1990-12-12", "1990-12-12", "City", "BioC")
  val saved = ProfesionalResponse("p3", "C", "img.png", dateFormatter.parse(req.fechaNacimiento), dateFormatter.parse(req.fechaInicio), "City", "BioC", 0)
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
  val req = ProfesionalRequest("D", null, "1990-12-12", "1990-12-12", "Town", "BioD")
  val resp = ProfesionalResponse(id, "D", null, dateFormatter.parse(req.fechaNacimiento), dateFormatter.parse(req.fechaInicio), "Town", "BioD", 1)
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
  val req = ProfesionalRequest("X", null, "1990-12-12", "1990-12-12", "", "")
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

 @Test
 fun getProfesionalByIdNotFound_retorna404() {
  val id = "inexistente"
  every { profesionalService.getProfesionalById(id) } returns null

  mockMvc.perform(get("/api/profesionales/{id}", id))
   .andExpect(status().isNotFound)
 }

 @Test
 fun searchByNombre_retornaLista() {
  val nombre = "Demo"
  val data = listOf(ProfesionalResponse("pX", "Demo", null, Date(), Date(), "", null, 0))
  val paginated = PaginationUtils.PaginatedResponse(
   data = data, totalItems = 1, totalPages = 1, currentPage = 0, pageSize = 10
  )
  every {
   profesionalService.searchByNombre(nombre, 0, 10, listOf("nombre"), "asc")
  } returns paginated

  mockMvc.perform(
   get("/api/profesionales/buscar")
    .param("nombre", nombre)
    .param("page", "0")
    .param("size", "10")
  )
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.data.length()").value(1))
   .andExpect(jsonPath("$.data[0].id").value("pX"))
 }

 @Test
 fun createProfesional_errorInterno_retorna500() {
  val req = ProfesionalRequest("X", null, "2020-01-01", "2020-01-01", "L", "B")
  every { profesionalService.createProfesional(any()) } throws RuntimeException("Error")

  mockMvc.perform(
   post("/api/profesionales")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(req))
  )
   .andExpect(status().isInternalServerError)
 }

 @Test
 fun updateProfesional_errorInterno_retorna500() {
  val id = "pY"
  val req = ProfesionalRequest("Y", null, "2020-01-01", "2020-01-01", "L", "B")
  every { profesionalService.updateProfesional(eq(id), any()) } throws RuntimeException("Error")

  mockMvc.perform(
   put("/api/profesionales/{id}", id)
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(req))
  )
   .andExpect(status().isInternalServerError)
 }

 @Test
 fun filtrarProfesionales_retornaLista() {
  val data = listOf(ProfesionalResponse("pF", "F", null, Date(), Date(), "L", null, 0))
  val paginated = PaginationUtils.PaginatedResponse(
   data = data, totalItems = 1, totalPages = 1, currentPage = 1, pageSize = 5
  )
  every {
   profesionalService.filtrarProfesionales(
    nombre = "F",
    fechaNacimientoDesde = "1990-01-01",
    fechaNacimientoHasta = "2000-12-31",
    fechaInicioDesde = "2010-01-01",
    fechaInicioHasta = "2020-12-31",
    lugarNacimiento = "L",
    page = 1,
    size = 5,
    sortBy = listOf("nombre"),
    sortDirection = "asc"
   )
  } returns paginated

  mockMvc.perform(
   get("/api/profesionales/filtrar")
    .param("nombre", "F")
    .param("fechaNacimientoDesde", "1990-01-01")
    .param("fechaNacimientoHasta", "2000-12-31")
    .param("fechaInicioDesde", "2010-01-01")
    .param("fechaInicioHasta", "2020-12-31")
    .param("lugarNacimiento", "L")
    .param("page", "1")
    .param("size", "5")
    .param("sortBy", "nombre")
    .param("sortDirection", "asc")
  )
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.currentPage").value(1))
   .andExpect(jsonPath("$.data[0].id").value("pF"))
 }

 @Test
 fun uploadTempProfesionalImage_sinOldUrl_subeYretornaUrl() {
  val fileBytes = "test".toByteArray()
  val multipartFile = MockMultipartFile("file", "img.png", MediaType.IMAGE_PNG_VALUE, fileBytes)
  every { firebaseStorageService.uploadImage(multipartFile, "profesionales", any()) } returns "http://nueva-url"

  mockMvc.perform(
   multipart("/api/profesionales/upload-image-temp")
    .file(multipartFile)
    .with { request -> request.method = "POST"; request }
  )
   .andExpect(status().isOk)
   .andExpect(content().string("http://nueva-url"))
 }

 @Test
 fun uploadTempProfesionalImage_conOldUrlFirebase_eliminaYSobe() {
  val oldUrl = "https://storage-download.googleapis.com/movietrip-e3a91.appspot.com/antigua/path/img.png"
  val fileBytes = "test".toByteArray()
  val multipartFile = MockMultipartFile("file", "img.png", MediaType.IMAGE_PNG_VALUE, fileBytes)
  every { firebaseStorageService.deleteImage("antigua/path/img.png") } returns true
  every { firebaseStorageService.uploadImage(multipartFile, "profesionales", any()) } returns "http://url-nueva"

  mockMvc.perform(
   multipart("/api/profesionales/upload-image-temp")
    .file(multipartFile)
    .param("oldUrl", oldUrl)
    .with { request -> request.method = "POST"; request }
  )
   .andExpect(status().isOk)
   .andExpect(content().string("http://url-nueva"))
 }

 @Test
 fun uploadTempProfesionalImage_conOldUrlNoFirebase_ignoraEliminacion() {
  val oldUrl = "https://otro-bucket.com/imagen.png"
  val fileBytes = "data".toByteArray()
  val multipartFile = MockMultipartFile("file", "img.png", MediaType.IMAGE_PNG_VALUE, fileBytes)
  every { firebaseStorageService.uploadImage(multipartFile, "profesionales", any()) } returns "http://url-final"

  mockMvc.perform(
   multipart("/api/profesionales/upload-image-temp")
    .file(multipartFile)
    .param("oldUrl", oldUrl)
    .with { request -> request.method = "POST"; request }
  )
   .andExpect(status().isOk)
   .andExpect(content().string("http://url-final"))
 }

}