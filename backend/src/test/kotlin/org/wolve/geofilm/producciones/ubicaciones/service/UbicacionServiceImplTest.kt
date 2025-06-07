package org.wolve.geofilm.producciones.ubicaciones.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.wolve.geofilm.producciones.produccion.service.IProduccionService
import org.wolve.geofilm.producciones.ubicaciones.dto.UbicacionFilterParams
import org.wolve.geofilm.producciones.ubicaciones.dto.UbicacionRequest
import org.wolve.geofilm.producciones.ubicaciones.dto.UbicacionResponse
import org.wolve.geofilm.producciones.ubicaciones.exceptions.UbicacionNotFoundException
import org.wolve.geofilm.producciones.ubicaciones.mapper.UbicacionMapper
import org.wolve.geofilm.producciones.ubicaciones.models.Ubicacion
import org.wolve.geofilm.producciones.ubicaciones.repository.UbicacionRepository
import org.wolve.geofilm.utils.pagination.PaginatedResponse
import org.wolve.geofilm.utils.pagination.PaginationUtils
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class UbicacionServiceTest {

 @Mock
 private lateinit var ubicacionRepository: UbicacionRepository

 @Mock
 private lateinit var ubicacionMapper: UbicacionMapper

 @Mock
 private lateinit var pruduccionesService: IProduccionService

 private lateinit var service: UbicacionServiceImpl

 @BeforeEach
 fun setUp() {
  service = UbicacionServiceImpl(ubicacionRepository, ubicacionMapper, pruduccionesService)
 }

 @Test
 fun getUbicacionById () {
  val id = "u1"
  val now = LocalDateTime.now()
  val entity = Ubicacion(id = id, nombre = "Lugar", latitud = 1.0, longitud = 2.0, createdAt = now, updatedAt = now)
  val dto = UbicacionResponse(id = id, nombre = "Lugar", latitud = 1.0, longitud = 2.0, createdAt = now, updatedAt = now)

  whenever(ubicacionRepository.findById(id)).thenReturn(java.util.Optional.of(entity))
  whenever(ubicacionMapper.toResponse(entity)).thenReturn(dto)

  val result = service.getUbicacionById(id)

  assertNotNull(result)
  assertEquals(dto, result)
  verify(ubicacionRepository).findById(id)
  verify(ubicacionMapper).toResponse(entity)
 }

 @Test
 fun getUbicacionByIdNotFoundId() {
  val id = "noExiste"
  whenever(ubicacionRepository.findById(id)).thenReturn(java.util.Optional.empty())

  val result = service.getUbicacionById(id)
  assertNull(result)
  verify(ubicacionRepository).findById(id)
 }

 @Test
 fun getAllUbicaciones() {
  val now = LocalDateTime.now()
  val e1 = Ubicacion(id = "u1", nombre = "A", latitud = 0.0, longitud = 0.0, createdAt = now, updatedAt = now)
  val e2 = Ubicacion(id = "u2", nombre = "B", latitud = 1.0, longitud = 1.0, createdAt = now, updatedAt = now)
  whenever(ubicacionRepository.findAll()).thenReturn(listOf(e1, e2))
  val dto1 = UbicacionResponse(id = "u1", nombre = "A", latitud = 0.0, longitud = 0.0, createdAt = now, updatedAt = now)
  val dto2 = UbicacionResponse(id = "u2", nombre = "B", latitud = 1.0, longitud = 1.0, createdAt = now, updatedAt = now)
  whenever(ubicacionMapper.toResponse(e1)).thenReturn(dto1)
  whenever(ubicacionMapper.toResponse(e2)).thenReturn(dto2)

  val result = service.getAllUbicaciones()

  assertEquals(2, result.size)
  assertTrue(result.containsAll(listOf(dto1, dto2)))
  verify(ubicacionRepository).findAll()
 }

 @Test
 fun createUbicacion() {
  val request = UbicacionRequest(nombre = "X", latitud = 5.0, longitud = 6.0)
  val now = LocalDateTime.now()
  val entityToSave = Ubicacion(nombre = "X", latitud = 5.0, longitud = 6.0, createdAt = now, updatedAt = now)
  val savedEntity = entityToSave.copy(id = "uNew")
  val dto = UbicacionResponse(id = "uNew", nombre = "X", latitud = 5.0, longitud = 6.0, createdAt = now, updatedAt = now)

  whenever(ubicacionMapper.toEntity(request)).thenReturn(entityToSave)
  whenever(ubicacionRepository.save(entityToSave)).thenReturn(savedEntity)
  whenever(ubicacionMapper.toResponse(savedEntity)).thenReturn(dto)

  val result = service.createUbicacion(request)

  assertEquals(dto, result)
  verify(ubicacionMapper).toEntity(request)
  verify(ubicacionRepository).save(entityToSave)
  verify(ubicacionMapper).toResponse(savedEntity)
 }

 @Test
 fun updateUbicacion() {
  val id = "uUpd"
  val request = UbicacionRequest(nombre = "Y", latitud = 7.0, longitud = 8.0)
  val now = LocalDateTime.now()
  val existing = Ubicacion(id = id, nombre = "Old", latitud = 1.0, longitud = 2.0, createdAt = now, updatedAt = now)
  val updated = existing.copy(nombre = "Y", latitud = 7.0, longitud = 8.0, updatedAt = now)
  val dto = UbicacionResponse(id = id, nombre = "Y", latitud = 7.0, longitud = 8.0, createdAt = existing.createdAt, updatedAt = now)

  whenever(ubicacionRepository.findById(id)).thenReturn(java.util.Optional.of(existing))
  whenever(ubicacionRepository.save(updated)).thenReturn(updated)
  whenever(ubicacionMapper.toResponse(updated)).thenReturn(dto)

  val result = service.updateUbicacion(id, request)

  assertNotNull(result)
  assertEquals(dto, result)
  verify(ubicacionRepository).findById(id)
  verify(ubicacionRepository).save(updated)
  verify(ubicacionMapper).toResponse(updated)
 }

 @Test
 fun updateUbicacionNotFounfId() {
  val id = "noExisteUpd"
  val request = UbicacionRequest(nombre = "Z", latitud = 9.0, longitud = 10.0)
  whenever(ubicacionRepository.findById(id)).thenReturn(java.util.Optional.empty())

  val result = service.updateUbicacion(id, request)
  assertNull(result)
  verify(ubicacionRepository).findById(id)
 }

 @Test
 fun deleteUbicacion() {
  val id = "uDel"

  val now = LocalDateTime.now()
  val entity = Ubicacion(
   id        = id,
   nombre    = "Found",
   latitud   = 2.2,
   longitud  = 3.3,
   createdAt = now,
   updatedAt = now
  )

  whenever(ubicacionRepository.findById(id)).thenReturn(Optional.of(entity))

  service.deleteUbicacion(id)

  verify(ubicacionRepository).deleteById(id)
 }

 @Test
 fun deleteUbicacionNotFound() {
  val id = "noExisteDel"
  whenever(ubicacionRepository.findById(id)).thenReturn(java.util.Optional.empty())

  assertThrows(UbicacionNotFoundException::class.java) {
   service.deleteUbicacion(id)
  }
  verify(ubicacionRepository).findById(id)
 }

 @Test
 fun findEntityById() {
  val id = "uFind"
  val now = LocalDateTime.now()
  val entity = Ubicacion(id = id, nombre = "Found", latitud = 2.2, longitud = 3.3, createdAt = now, updatedAt = now)
  whenever(ubicacionRepository.findById(id)).thenReturn(java.util.Optional.of(entity))

  val result = service.findEntityById(id)
  assertEquals(entity, result)
  verify(ubicacionRepository).findById(id)
 }

 @Test
 fun findEntityByIdNotFound() {
  val id = "noFind"
  whenever(ubicacionRepository.findById(id)).thenReturn(java.util.Optional.empty())

  assertThrows(UbicacionNotFoundException::class.java) {
   service.findEntityById(id)
  }
  verify(ubicacionRepository).findById(id)
 }

 @Test
 fun existsByIdReturnValor() {
  whenever(ubicacionRepository.existsById("uEx")).thenReturn(true)
  assertTrue(service.existsById("uEx"))
  verify(ubicacionRepository).existsById("uEx")
 }

 @Test
 fun filterUbicaciones() {
  val now = LocalDateTime.now()
  val e1 = Ubicacion(id = "u1", nombre = "Casa", latitud = 1.0, longitud = 1.0, createdAt = now, updatedAt = now)
  val e2 = Ubicacion(id = "u2", nombre = "Calle", latitud = 2.0, longitud = 2.0, createdAt = now, updatedAt = now)
  val e3 = Ubicacion(id = "u3", nombre = "Parque", latitud = 3.0, longitud = 3.0, createdAt = now, updatedAt = now)
  whenever(ubicacionRepository.findAll()).thenReturn(listOf(e1, e2, e3))

  val params = UbicacionFilterParams(nombre = "Ca")
  val page = 0
  val size = 2

  val dto1 = UbicacionResponse(id = "u1", nombre = "Casa", latitud = 1.0, longitud = 1.0, createdAt = now, updatedAt = now)
  val dto2 = UbicacionResponse(id = "u2", nombre = "Calle", latitud = 2.0, longitud = 2.0, createdAt = now, updatedAt = now)
  whenever(ubicacionMapper.toResponse(e1)).thenReturn(dto1)
  whenever(ubicacionMapper.toResponse(e2)).thenReturn(dto2)

  val result: PaginatedResponse<UbicacionResponse> = service.filterUbicaciones(params, page, size, emptyList(), "")

  assertEquals(2L, result.totalItems)
  assertEquals(1, result.totalPages)
  assertEquals(0, result.currentPage)
  assertEquals(2, result.pageSize)
  assertEquals(listOf(dto1, dto2), result.data)
 }

}
