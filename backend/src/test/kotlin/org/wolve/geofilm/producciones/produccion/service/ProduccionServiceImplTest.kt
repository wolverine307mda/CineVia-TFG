package org.wolve.geofilm.producciones.produccion.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.wolve.geofilm.producciones.produccion.dto.ProduccionCompletaResponse
import org.wolve.geofilm.producciones.produccion.dto.ProduccionRequest
import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import org.wolve.geofilm.producciones.produccion.exceptions.ProduccionNotFoundException
import org.wolve.geofilm.producciones.produccion.mapper.ProduccionMapper
import org.wolve.geofilm.producciones.produccion.models.Categoria
import org.wolve.geofilm.producciones.produccion.models.ClasificacionEdad
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.produccion.models.TipoProduccion
import org.wolve.geofilm.producciones.produccion.repository.ProduccionRepository
import org.wolve.geofilm.utils.pagination.PaginatedResponse
import java.util.*

@ExtendWith(MockitoExtension::class)
class ProduccionServiceImplTest {

 @Mock
 private lateinit var produccionRepository: ProduccionRepository

 @Mock
 private lateinit var produccionMapper: ProduccionMapper

 private lateinit var service: ProduccionServiceImpl

 @BeforeEach
 fun setUp() {
  service = ProduccionServiceImpl(produccionRepository, produccionMapper)
 }

 @Test
 fun `getProduccionById - existing id returns response`() {
  val id = "id1"
  val entity = mock(Produccion::class.java)
  val response = mock(ProduccionResponse::class.java)
  whenever(produccionRepository.findById(id)).thenReturn(Optional.of(entity))
  whenever(produccionMapper.toProduccionResponse(entity)).thenReturn(response)

  val result = service.getProduccionById(id)

  assertEquals(response, result)
  verify(produccionRepository).findById(id)
  verify(produccionMapper).toProduccionResponse(entity)
 }

 @Test
 fun `getProduccionById - missing id throws exception`() {
  val id = "missing"
  whenever(produccionRepository.findById(id)).thenReturn(Optional.empty())

  assertThrows(ProduccionNotFoundException::class.java) {
   service.getProduccionById(id)
  }
 }

 @Test
 fun `createProduccion - maps and saves correctly`() {
  val request = mock(ProduccionRequest::class.java)
  val entity = mock(Produccion::class.java)
  val saved = mock(Produccion::class.java)
  val response = mock(ProduccionResponse::class.java)
  whenever(produccionMapper.toProduccionEntity(request)).thenReturn(entity)
  whenever(produccionRepository.save(entity)).thenReturn(saved)
  whenever(produccionMapper.toProduccionResponse(saved)).thenReturn(response)

  val result = service.createProduccion(request)

  assertEquals(response, result)
  verify(produccionMapper).toProduccionEntity(request)
  verify(produccionRepository).save(entity)
  verify(produccionMapper).toProduccionResponse(saved)
 }

 @Test
 fun `updateProduccion - updates when exists`() {
  val id = "id2"
  val request = mock(ProduccionRequest::class.java)
  val existing = Produccion(
   id = id,
   titulo = "old",
   tipo = TipoProduccion.PELICULA,
   estreno = Date(),
   duracion = 100,
   sinopsis = "old",
   imagen = null,
   informacion = null,
   puntuacion = 5.0,
   categorias = mutableSetOf(Categoria.ACCION),
   clasificacionEdad = ClasificacionEdad.MAYORES_7
  )
  val updated = existing.copy(
   titulo = "new",
   duracion = 120,
   sinopsis = "new",
   puntuacion = 8.0,
   categorias = mutableSetOf(),
   clasificacionEdad = ClasificacionEdad.MAYORES_12
  )
  val response = mock(ProduccionResponse::class.java)
  whenever(produccionRepository.findById(id)).thenReturn(Optional.of(existing))
  whenever(produccionRepository.save(any<Produccion>())).thenReturn(updated)
  whenever(produccionMapper.toProduccionResponse(updated)).thenReturn(response)

  val result = service.updateProduccion(id, request)

  assertEquals(response, result)
  verify(produccionRepository).save(any<Produccion>())
  verify(produccionMapper).toProduccionResponse(updated)
 }

 @Test
 fun `deleteProduccion - deletes when exists`() {
  val id = "id3"
  whenever(produccionRepository.findById(id)).thenReturn(Optional.of(mock(Produccion::class.java)))

  service.deleteProduccion(id)

  verify(produccionRepository).deleteById(id)
 }

 @Test
 fun `filtrarProducciones - returns paginated with correct args`() {
  val page = 0; val size = 5
  val pageRequest: Pageable = PageRequest.of(page, size)
  val entity = mock(Produccion::class.java)
  val pageImpl = PageImpl(listOf(entity), pageRequest, 1)
  val paginatedResp: PaginatedResponse<*>? = mock(PaginatedResponse::class.java)
  whenever(produccionRepository.findAll(any(), eq(pageRequest))).thenReturn(pageImpl)

  val result = service.filtrarProducciones(
   titulo = null,
   tipo = null,
   estrenoDesde = null,
   estrenoHasta = null,
   categorias = null,
   clasificacionEdad = null,
   duracionMin = null,
   duracionMax = null,
   page = page,
   size = size,
   sortBy = listOf(),
   sortDirection = ""
  )

  assertEquals(paginatedResp, result)
  verify(produccionRepository).findAll(any(), eq(pageRequest))
  verify(produccionMapper).toPaginatedResponse(pageImpl)
 }

 @Test
 fun `getClasificacionesEdad returns all enums`() {
  val enums = ClasificacionEdad.values().toList()
  assertEquals(enums, service.getClasificacionesEdad())
 }

 @Test
 fun `getCategoriasDisponibles returns all enums`() {
  val enums = Categoria.values().toList()
  assertEquals(enums, service.getCategoriasDisponibles())
 }

 @Test
 fun `getTiposProduccion returns all enums`() {
  val enums = TipoProduccion.values().toList()
  assertEquals(enums, service.getTiposProduccion())
 }

 @Test
 fun `findProduccionCompletaById - maps when exists`() {
  val id = "full1"
  val fullEntity = mock(Produccion::class.java)
  val fullResponse = mock(ProduccionCompletaResponse::class.java)
  whenever(produccionRepository.findCompletaById(id)).thenReturn(fullEntity)
  whenever(produccionMapper.toCompletaResponse(fullEntity)).thenReturn(fullResponse)

  val result = service.findProduccionCompletaById(id)

  assertEquals(fullResponse, result)
 }

 @Test
 fun `countParticipacionesByProduccionId returns count`() {
  val id = "cnt1"
  whenever(produccionRepository.countParticipacionesById(id)).thenReturn(3L)
  assertEquals(3L, service.countParticipacionesByProduccionId(id))
 }
}