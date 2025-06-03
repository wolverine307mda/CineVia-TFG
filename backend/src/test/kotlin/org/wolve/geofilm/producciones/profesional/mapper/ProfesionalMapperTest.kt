package org.wolve.geofilm.producciones.profesional.mapper

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalRequest
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalResponse
import org.wolve.geofilm.producciones.profesional.model.Profesional
import org.wolve.geofilm.producciones.participacion.models.Participacion
import org.wolve.geofilm.utils.pagination.PaginationUtils.PaginatedResponse
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.util.*

class ProfesionalMapperTest {

 private lateinit var mapper: ProfesionalMapper

 @BeforeEach
 fun setUp() {
  mapper = ProfesionalMapper()
 }

 @Test
 fun toEntity() {
  val fechaNac = Date(1000000)
  val fechaIni = Date(2000000)
  val request = ProfesionalRequest(
   nombre = "Juan Perez",
   foto = "foto.jpg",
   fechaNacimiento = fechaNac,
   fechaInicio = fechaIni,
   lugarNacimiento = "Madrid",
   biografia = "Actor famoso"
  )

  val entity: Profesional = mapper.toEntity(request)

  assertNotNull(entity.id)
  assertEquals(request.nombre, entity.nombre)
  assertEquals(request.foto, entity.foto)
  assertEquals(request.fechaNacimiento, entity.fechaNacimiento)
  assertEquals(request.fechaInicio, entity.fechaInicio)
  assertEquals(request.lugarNacimiento, entity.lugarNacimiento)
  assertEquals(request.biografia, entity.biografia)
  assertTrue(entity.participaciones.isEmpty())
 }

 @Test
 fun toResponse() {
  val fechaNac = Date(1000000)
  val fechaIni = Date(2000000)
  val participacionesList = listOf(Participacion())
  val entity = Profesional(
   id = "prof1",
   nombre = "Ana Lopez",
   foto = "ana.png",
   fechaNacimiento = fechaNac,
   fechaInicio = fechaIni,
   lugarNacimiento = "Sevilla",
   biografia = "Directora",
   participaciones = participacionesList
  )

  val response: ProfesionalResponse = mapper.toResponse(entity)

  assertEquals(entity.id, response.id)
  assertEquals(entity.nombre, response.nombre)
  assertEquals(entity.foto, response.foto)
  assertEquals(entity.fechaNacimiento, response.fechaNacimiento)
  assertEquals(entity.fechaInicio, response.fechaInicio)
  assertEquals(entity.lugarNacimiento, response.lugarNacimiento)
  assertEquals(entity.biografia, response.biografia)
  assertEquals(participacionesList.size, response.participacionesCount)
 }

 @Test
 fun toPaginatedResponse() {
  val fecha = Date()
  val e1 = Profesional(
   id = "p1", nombre = "X", foto = null,
   fechaNacimiento = fecha, fechaInicio = fecha,
   lugarNacimiento = "", biografia = "", participaciones = emptyList()
  )
  val e2 = Profesional(
   id = "p2", nombre = "Y", foto = "y.png",
   fechaNacimiento = fecha, fechaInicio = fecha,
   lugarNacimiento = "", biografia = "Bio", participaciones = listOf(Participacion())
  )
  val pageReq = PageRequest.of(0, 2)
  val pageImpl = PageImpl(listOf(e1, e2), pageReq, 5)

  val paginated: PaginatedResponse<ProfesionalResponse> = mapper.toPaginatedResponse(pageImpl)

  assertEquals(5, paginated.totalItems)
  assertEquals(2, paginated.data.size)
  val resp1 = paginated.data[0]
  assertEquals(e1.id, resp1.id)
  val resp2 = paginated.data[1]
  assertEquals(e2.id, resp2.id)
  assertEquals(1, resp2.participacionesCount)
 }

 @Test
 fun toResponseSinParticipaciones() {
  val fecha = Date()
  val entity = Profesional(
   id = "p3", nombre = "Z", foto = null,
   fechaNacimiento = fecha, fechaInicio = fecha,
   lugarNacimiento = "", biografia = null, participaciones = emptyList()
  )

  val response = mapper.toResponse(entity)
  assertEquals(0, response.participacionesCount)
 }

}
