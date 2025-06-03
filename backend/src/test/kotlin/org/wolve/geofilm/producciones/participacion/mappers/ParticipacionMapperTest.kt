package org.wolve.geofilm.producciones.participacion.mappers

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionRequest
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionResponse
import org.wolve.geofilm.producciones.participacion.models.Participacion
import org.wolve.geofilm.producciones.participacion.models.RolParticipacion
import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import org.wolve.geofilm.producciones.produccion.mapper.ProduccionMapper
import org.wolve.geofilm.producciones.produccion.models.*
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalResponse
import org.wolve.geofilm.producciones.profesional.mapper.ProfesionalMapper
import org.wolve.geofilm.producciones.profesional.model.Profesional
import java.time.LocalDateTime
import java.util.*

class ParticipacionMapperTest {

 private lateinit var profesionalMapper: ProfesionalMapper
 private lateinit var produccionMapper: ProduccionMapper
 private lateinit var mapper: ParticipacionMapper

 @BeforeEach
 fun setUp() {
  profesionalMapper = mock()
  produccionMapper = mock()
  mapper = ParticipacionMapper(profesionalMapper, produccionMapper)
 }

 @Test
 fun toParticipacionEntity() {
  val request = ParticipacionRequest(
   produccionId = "prod1",
   profesionalId = "prof1",
   rol = RolParticipacion.ACTOR,
   papel = "Heroe"
  )

  val prodEntity = Produccion(
   id = "prod1",
   titulo = "Alguna Película",
   tipo = TipoProduccion.PELICULA,
   estreno = Date(),
   duracion = 100,
   sinopsis = "Una sinopsis",
   imagen = null,
   informacion = null,
   puntuacion = 0.0,
   categorias = mutableSetOf(),
   clasificacionEdad = ClasificacionEdad.TODOS_LOS_PUBLICOS,
   participaciones = mutableSetOf(),
   rodajes = mutableSetOf(),
   saga = null
  )

  val profEntity = Profesional(
   id = "prof1",
   nombre = "Persona X",
   foto = null,
   fechaNacimiento = Date(),
   fechaInicio = Date(),
   lugarNacimiento = "Madrid",
   biografia = "Una bio",
   participaciones = listOf()
  )

  val entidad = mapper.toEntity(request, prodEntity, profEntity)

  assertNotNull(entidad.id)
  assertEquals(prodEntity, entidad.produccion)
  assertEquals(profEntity, entidad.profesional)
  assertEquals(RolParticipacion.ACTOR, entidad.rol)
  assertEquals("Heroe", entidad.papel)
  assertNotNull(entidad.createdAt)
  assertNotNull(entidad.updatedAt)
 }

 @Test
 fun toParticipacionResponse() {
  val profEntity = Profesional(
   id = "prof2",
   nombre = "Actor Y",
   foto = null,
   fechaNacimiento = Date(),
   fechaInicio = Date(),
   lugarNacimiento = "Ciudad Z",
   biografia = "Biografía de Y",
   participaciones = emptyList()
  )
  val profDto = ProfesionalResponse(
   id = "prof2",
   nombre = "Actor Y",
   foto = null,
   fechaNacimiento = profEntity.fechaNacimiento,
   fechaInicio = profEntity.fechaInicio,
   lugarNacimiento = "Ciudad Z",
   biografia = "Biografía de Y",
   participacionesCount = 5
  )
  whenever(profesionalMapper.toResponse(eq(profEntity))).thenReturn(profDto)

  val prodEntity = Produccion(
   id = "prod2",
   titulo = "Película W",
   tipo = TipoProduccion.PELICULA,
   estreno = Date(),
   duracion = 90,
   sinopsis = "",
   imagen = null,
   informacion = null,
   puntuacion = 8.0,
   categorias = mutableSetOf(),
   clasificacionEdad = ClasificacionEdad.MAYORES_12,
   participaciones = mutableSetOf(),
   rodajes = mutableSetOf(),
   saga = null
  )

  val fechaNow = Date()
  val participacionEntity = Participacion(
   id = "part1",
   produccion = prodEntity,
   profesional = profEntity,
   rol = RolParticipacion.DIRECTOR,
   papel = "Protagonista"
  )

  val response = mapper.toResponse(participacionEntity)

  assertEquals("part1", response.id)
  assertEquals(RolParticipacion.DIRECTOR, response.rol)
  assertEquals("Protagonista", response.papel)
  assertEquals(profDto, response.profesional)
  assertNotNull(response.createdAt)
  assertNotNull(response.updatedAt)
 }

 @Test
 fun toResponseProfesional () {
  val profEntity = Profesional(
   id = "pf2",
   nombre = "Actor A",
   foto = null,
   fechaNacimiento = Date(),
   fechaInicio = Date(),
   lugarNacimiento = "Barcelona",
   biografia = "Bio",
   participaciones = emptyList()
  )

  val prodEntity = Produccion(
   id = "pd2",
   titulo = "Serie B",
   tipo = TipoProduccion.SERIE,
   estreno = Date(),
   duracion = 45,
   sinopsis = "",
   imagen = null,
   informacion = null,
   puntuacion = 7.0,
   categorias = mutableSetOf(),
   clasificacionEdad = ClasificacionEdad.MAYORES_7,
   participaciones = mutableSetOf(),
   rodajes = mutableSetOf(),
   saga = null
  )

  val prodDto = ProduccionResponse(
   id = "pd2",
   titulo = "Serie B",
   sinopsis = "",
   estreno = prodEntity.estreno,
   duracion = 45,
   imagen = null,
   informacion = null,
   puntuacion = 7.0,
   categorias = emptySet(),
   tipo = TipoProduccion.SERIE,
   clasificacionEdad = ClasificacionEdad.MAYORES_7.valorNumerico
  )
  whenever(produccionMapper.toResponse(eq(prodEntity))).thenReturn(prodDto)

  val participacionEntity = Participacion(
   id = "partP",
   produccion = prodEntity,
   profesional = profEntity,
   rol = RolParticipacion.ACTOR,
   papel = null
  )

  val responseProf = mapper.toResponseProfesional(participacionEntity)

  assertEquals("partP", responseProf.id)
  assertEquals(RolParticipacion.ACTOR, responseProf.rol)
  assertNull(responseProf.papel)
  assertEquals(prodDto, responseProf.produccion)
  assertNotNull(responseProf.createdAt)
  assertNotNull(responseProf.updatedAt)
 }

 @Test
 fun toPaginatedResponse() {
  val prof = Profesional(
   id = "pX", nombre = "X", foto = null,
   fechaNacimiento = Date(), fechaInicio = Date(),
   lugarNacimiento = "", biografia = "", participaciones = emptyList()
  )
  val prod = Produccion(
   id = "pY", titulo = "Y", tipo = TipoProduccion.PELICULA,
   estreno = Date(), duracion = 60, sinopsis = "",
   imagen = null, informacion = null, puntuacion = 0.0,
   categorias = mutableSetOf(), clasificacionEdad = ClasificacionEdad.TODOS_LOS_PUBLICOS,
   participaciones = mutableSetOf(), rodajes = mutableSetOf(), saga = null
  )
  val e1 = Participacion(id = "r1", produccion = prod, profesional = prof, rol = RolParticipacion.ACTOR, papel = "Extra")
  val e2 = Participacion(id = "r2", produccion = prod, profesional = prof, rol = RolParticipacion.ACTOR, papel = "Reparto")

  val dto1 = ParticipacionResponse(
   id = "r1",
   rol = RolParticipacion.ACTOR,
   papel = "Extra",
   profesional = ProfesionalResponse(
    id = "pX", nombre = "X", foto = null,
    fechaNacimiento = Date(), fechaInicio = Date(),
    lugarNacimiento = "", biografia = "", participacionesCount = 0
   ),
   createdAt = e1.createdAt,
   updatedAt = e1.updatedAt
  )
  val dto2 = ParticipacionResponse(
   id = "r2",
   rol = RolParticipacion.ACTOR,
   papel = "Reparto",
   profesional = dto1.profesional,
   createdAt = e2.createdAt,
   updatedAt = e2.updatedAt
  )

  // Stub explícito del mapper
  whenever(profesionalMapper.toResponse(e1.profesional)).thenReturn(dto1.profesional)
  whenever(profesionalMapper.toResponse(e2.profesional)).thenReturn(dto2.profesional)

  val pageRequest = PageRequest.of(0, 2)
  val springPage = PageImpl(listOf(e1, e2), pageRequest, 5)

  val result = mapper.toPaginatedResponse(springPage)

  assertEquals(5, result.totalItems)
  assertEquals(2, result.data.size)
  assertEquals(dto1.profesional, result.data[0].profesional)
  assertEquals(dto2.profesional, result.data[1].profesional)
 }

}
