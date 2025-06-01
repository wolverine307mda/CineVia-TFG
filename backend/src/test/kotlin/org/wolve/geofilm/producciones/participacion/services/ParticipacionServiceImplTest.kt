package org.wolve.geofilm.producciones.participacion.services

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.kotlin.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.*
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionRequest
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionResponse
import org.wolve.geofilm.producciones.participacion.dto.ParticipacionResponseProfesional
import org.wolve.geofilm.producciones.participacion.exceptions.ParticipacionNotFoundException
import org.wolve.geofilm.producciones.participacion.exceptions.RelacionNotFoundException
import org.wolve.geofilm.producciones.participacion.mappers.ParticipacionMapper
import org.wolve.geofilm.producciones.participacion.models.Participacion
import org.wolve.geofilm.producciones.participacion.models.RolParticipacion
import org.wolve.geofilm.producciones.participacion.repository.IParticipacionRepository
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.produccion.service.IProduccionService
import org.wolve.geofilm.producciones.profesional.model.Profesional
import org.wolve.geofilm.producciones.profesional.service.IProfesionalService
import org.wolve.geofilm.utils.pagination.PaginationUtils
import java.util.*

@ExtendWith(MockitoExtension::class)
class ParticipacionServiceTest {

 @Mock private lateinit var participacionRepository: IParticipacionRepository
 @Mock private lateinit var participacionMapper: ParticipacionMapper
 @Mock private lateinit var produccionService: IProduccionService
 @Mock private lateinit var profesionalService: IProfesionalService

 private lateinit var service: ParticipacionServiceImpl

 @BeforeEach
 fun setUp() {
  service = ParticipacionServiceImpl(
   participacionRepository,
   participacionMapper,
   produccionService,
   profesionalService
  )
 }

 @Test
 fun `getParticipacionById lanza excepcion si no existe`() {
  whenever(participacionRepository.findById("notfound")).thenReturn(Optional.empty())
  assertThrows<ParticipacionNotFoundException> {
   service.getParticipacionById("notfound")
  }
 }

 @Test
 fun `updateParticipacion lanza excepcion si no existe`() {
  whenever(participacionRepository.findById("missing")).thenReturn(Optional.empty())
  assertThrows<ParticipacionNotFoundException> {
   service.updateParticipacion("missing", ParticipacionRequest("prod", "prof", RolParticipacion.ACTOR, "rol"))
  }
 }

 @Test
 fun `deleteParticipacion elimina si existe`() {
  whenever(participacionRepository.existsById("del1")).thenReturn(true)
  service.deleteParticipacion("del1")
  verify(participacionRepository).deleteById("del1")
 }

 @Test
 fun `deleteParticipacion lanza excepcion si no existe`() {
  whenever(participacionRepository.existsById("nope")).thenReturn(false)
  assertThrows<ParticipacionNotFoundException> {
   service.deleteParticipacion("nope")
  }
 }

 @Test
 fun `getByProfesional devuelve lista de respuestas`() {
  val prof = mock<Profesional>()
  val p1 = mock<Participacion>()
  val p2 = mock<Participacion>()
  val list = listOf(p1, p2)
  val resp1 = mock<ParticipacionResponseProfesional>()
  val resp2 = mock<ParticipacionResponseProfesional>()

  whenever(profesionalService.findEntityById("prof1")).thenReturn(prof)
  whenever(participacionRepository.findAllByProfesional(prof)).thenReturn(list)
  whenever(participacionMapper.toResponseProfesional(list)).thenReturn(listOf(resp1, resp2))

  val result = service.getByProfesional("prof1")
  assertEquals(2, result.size)
 }

 @Test
 fun `getByProfesional lanza excepcion si profesional no existe`() {
  whenever(profesionalService.findEntityById("profX")).thenReturn(null)
  assertThrows<RelacionNotFoundException> {
   service.getByProfesional("profX")
  }
 }

 @Test
 fun `getByProduccion devuelve resultados paginados`() {
  val prod = mock<Produccion>()
  val p1 = mock<Participacion>()
  val p2 = mock<Participacion>()
  val pageable = PageRequest.of(0, 10)
  val page = PageImpl(listOf(p1, p2), pageable, 2)
  val responsePage = PaginationUtils.toPaginatedResponse(page.map { mock<ParticipacionResponse>() })

  whenever(produccionService.findEntityById("prod1")).thenReturn(prod)
  whenever(participacionRepository.findByProduccion(eq(prod), any())).thenReturn(page)
  whenever(participacionMapper.toPaginatedResponse(page)).thenReturn(responsePage)

  val result = service.getByProduccion("prod1", 0, 10, listOf("id"), "asc")
  assertEquals(2, result.totalItems)
 }
}
