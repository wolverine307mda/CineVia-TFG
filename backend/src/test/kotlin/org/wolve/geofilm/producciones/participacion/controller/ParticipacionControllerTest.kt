package org.wolve.geofilm.producciones.participacion.controller

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.wolve.geofilm.auth.services.jwt.JwtAuthenticationFilter
import org.wolve.geofilm.auth.services.jwt.JwtService
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionRequest
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionResponse
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionResponseProfesional
import org.wolve.geofilm.producciones.participacion.models.RolParticipacion
import org.wolve.geofilm.producciones.participacion.services.IParticipacionService
import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import org.wolve.geofilm.producciones.produccion.models.Categoria
import org.wolve.geofilm.producciones.produccion.models.TipoProduccion
import org.wolve.geofilm.producciones.produccion.service.IProduccionService
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalResponse
import org.wolve.geofilm.users.repositories.UsuarioRepository
import org.wolve.geofilm.utils.pagination.PaginationUtils
import org.wolve.geofilm.utils.storage.images.FirebaseStorageService
import java.time.LocalDateTime
import java.util.*
import javax.sql.DataSource

@ActiveProfiles("test")
@WebMvcTest(
 controllers = [ParticipacionController::class],
 excludeAutoConfiguration = [
  SecurityFilterAutoConfiguration::class,
  DataSourceAutoConfiguration::class,
  HibernateJpaAutoConfiguration::class,
  FlywayAutoConfiguration::class
 ]
)
@AutoConfigureMockMvc(addFilters = false)
class ParticipacionControllerTest{

 @Autowired private lateinit var mockMvc: MockMvc
 @Autowired private lateinit var objectMapper: ObjectMapper

 @MockkBean private lateinit var jwtService: JwtService
 @MockkBean private lateinit var usuarioRepository: UsuarioRepository
 @MockkBean private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter
 @MockkBean private lateinit var dataSource: DataSource
 @MockkBean private lateinit var flyway: Flyway

 @MockkBean
 private lateinit var participacionService: IParticipacionService


 @BeforeEach
 fun setup() {
  every { flyway.migrate() } returns mockk()
 }

 @Test
 fun createParticipacion() {
  val req = ParticipacionRequest(
   produccionId = "prod1",
   profesionalId = "prof1",
   rol = RolParticipacion.ACTOR,
   papel = "Protagonista"
  )
  val response = ParticipacionResponse(
   id = "p1",
   profesional = ProfesionalResponse(
     id = "idprof",
    nombre = "nombre",
    foto = "url.jpg",
    fechaNacimiento = Date(100),
    fechaInicio = Date(),
    lugarNacimiento = "Bélgica",
    participacionesCount = 2,
    biografia = ""
   ),
   rol = RolParticipacion.ACTOR,
   papel = "Protagonista",
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now()
  )
  every { participacionService.createParticipacion(req) } returns response

  mockMvc.perform(
   post("/api/participaciones")
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(req))
  )
   .andExpect(status().isCreated)
   .andExpect(jsonPath("$.id").value("p1"))
   .andExpect(jsonPath("$.rol").value("ACTOR"))
 }

 @Test
 fun getParticipacionById() {
  val response = ParticipacionResponse(
   id = "p1",
   profesional = ProfesionalResponse(
    id = "idprof",
    nombre = "nombre",
    foto = "url.jpg",
    fechaNacimiento = Date(100),
    fechaInicio = Date(),
    lugarNacimiento = "Bélgica",
    participacionesCount = 2,
    biografia = ""
   ),
   rol = RolParticipacion.ACTOR,
   papel = "Protagonista",
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now()
  )
  every { participacionService.getParticipacionById("p1") } returns response

  mockMvc.perform(get("/api/participaciones/{id}", "p1"))
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.id").value("p1"))
   .andExpect(jsonPath("$.profesional.id").value("idprof"))
 }

 @Test
 fun getAllParticipaciones() {
  val data = listOf(
   ParticipacionResponse(
    id = "p1",
    profesional = ProfesionalResponse(
     id = "idprof",
     nombre = "nombre",
     foto = "url.jpg",
     fechaNacimiento = Date(100),
     fechaInicio = Date(),
     lugarNacimiento = "Bélgica",
     participacionesCount = 2,
     biografia = ""
    ),
    rol = RolParticipacion.ACTOR,
    papel = "Protagonista",
    createdAt = LocalDateTime.now(),
    updatedAt = LocalDateTime.now()
   )
  )
  val paginatedResponse = PaginationUtils.PaginatedResponse(
   data = data,
   totalItems = 1L,
   totalPages = 1,
   currentPage = 0,
   pageSize = 10
  )
  every {
   participacionService.getAllParticipaciones(0, 10, listOf("createdAt"), "desc")
  } returns paginatedResponse

  mockMvc.perform(
   get("/api/participaciones")
    .param("page", "0")
    .param("size", "10")
    .param("sortBy", "createdAt")
    .param("sortDirection", "desc")
  )
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.data[0].id").value("p1"))
 }

 @Test
 fun getByProduccion() {
  val prodId = "prod4"
  val data = listOf(
   ParticipacionResponse(
    id = "p1",
    profesional = ProfesionalResponse(
     id = "idprof",
     nombre = "nombre",
     foto = "url.jpg",
     fechaNacimiento = Date(100),
     fechaInicio = Date(),
     lugarNacimiento = "Bélgica",
     participacionesCount = 2,
     biografia = ""
    ),
    rol = RolParticipacion.ACTOR,
    papel = "Protagonista",
    createdAt = LocalDateTime.now(),
    updatedAt = LocalDateTime.now()
   )
  )
  val paginatedResponse = PaginationUtils.PaginatedResponse(
   data = data,
   totalItems = 1L,
   totalPages = 1,
   currentPage = 0,
   pageSize = 10
  )
  every {
   participacionService.getByProduccion(prodId, 0, 10, listOf("createdAt"), "desc")
  } returns paginatedResponse

  mockMvc.perform(
   get("/api/participaciones/produccion/{produccionId}", prodId)
    .param("page", "0")
    .param("size", "10")
    .param("sortBy", "createdAt")
    .param("sortDirection", "desc")
  )
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.data[0].id").value("p1"))
 }

 @Test
 fun getByProfesional() {
  val profId = "prof5"
  val data = listOf(
   ParticipacionResponseProfesional(
    id = "p1",
    produccion = ProduccionResponse(
     id = "idprof",
     titulo = "nombre",
     tipo = TipoProduccion.PELICULA,
     estreno = Date(),
     imagen = "",
     sinopsis = "",
     puntuacion = 2.2,
     duracion = 120,
     informacion = "",
     categorias = setOf(Categoria.DRAMA),
     clasificacionEdad = 0
    ),
    rol = RolParticipacion.ACTOR,
    papel = "Protagonista",
    createdAt = LocalDateTime.now(),
    updatedAt = LocalDateTime.now()
   )
  )
  every { participacionService.getByProfesional(profId) } returns data

  mockMvc.perform(get("/api/participaciones/profesional/{profesionalId}", profId))
   .andExpect(status().isOk)
   .andExpect(jsonPath("$[0].produccion.id").value("idprof"))
   .andExpect(jsonPath("$[0].rol").value("ACTOR"))
 }

 @Test
 fun updateParticipacion() {
  val id = "p6"
  val req = ParticipacionRequest(
   produccionId = "prod6",
   profesionalId = "prof6",
   rol = RolParticipacion.ACTOR,
   papel = "Lead"
  )
  val response = ParticipacionResponse(
   id = id,
   profesional = ProfesionalResponse(
    id = "idprof",
    nombre = "nombre",
    foto = "url.jpg",
    fechaNacimiento = Date(100),
    fechaInicio = Date(),
    lugarNacimiento = "Bélgica",
    participacionesCount = 2,
    biografia = ""
   ),
   rol = RolParticipacion.ACTOR,
   papel = "Lead",
   createdAt = LocalDateTime.now(),
   updatedAt = LocalDateTime.now()
  )
  every { participacionService.updateParticipacion(id, req) } returns response

  mockMvc.perform(
   put("/api/participaciones/{id}", id)
    .contentType(MediaType.APPLICATION_JSON)
    .content(objectMapper.writeValueAsString(req))
  )
   .andExpect(status().isOk)
   .andExpect(jsonPath("$.id").value(id))
   .andExpect(jsonPath("$.rol").value("ACTOR"))
 }


 @Test
 fun deleteParticipacion() {
  val id = "p7"
  justRun { participacionService.deleteParticipacion(id) }

  mockMvc.perform(delete("/api/participaciones/{id}", id))
   .andExpect(status().isNoContent)
 }
}
