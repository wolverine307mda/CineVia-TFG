package org.wolve.geofilm.producciones.saga.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.wolve.geofilm.producciones.produccion.exceptions.ProduccionNotFoundException
import org.wolve.geofilm.producciones.produccion.models.ClasificacionEdad
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.produccion.models.TipoProduccion
import org.wolve.geofilm.producciones.produccion.repository.ProduccionRepository
import org.wolve.geofilm.producciones.saga.dto.SagaFilterParams
import org.wolve.geofilm.producciones.saga.dto.SagaRequest
import org.wolve.geofilm.producciones.saga.dto.SagaResponse
import org.wolve.geofilm.producciones.saga.exception.SagaNotFoundException
import org.wolve.geofilm.producciones.saga.exceptions.SagaValidationException
import org.wolve.geofilm.producciones.saga.mapper.SagaMapper
import org.wolve.geofilm.producciones.saga.model.Saga
import org.wolve.geofilm.producciones.saga.repository.SagaRepository
import java.util.*

@ExtendWith(MockitoExtension::class)
class SagaServiceTest {

 @Mock
 private lateinit var sagaRepository: SagaRepository

 @Mock
 private lateinit var produccionRepository: ProduccionRepository

 @Mock
 private lateinit var sagaMapper: SagaMapper

 private lateinit var service: SagaServiceImpl

 @BeforeEach
 fun setUp() {
  service = SagaServiceImpl(sagaRepository, produccionRepository, sagaMapper)
 }

 @Test
 fun getSagaById() {
  val id = "saga1"
  val entity = Saga(
   id = id,
   nombre = "Mi Saga",
   descripcion = "Desc",
   isAcabada = false,
   fechaInicio = Date(1622505600000L),
   fechaFin = null,
   imagen = null,
   producciones = mutableListOf()
  )
  val dto = SagaResponse(
   id = id,
   nombre = "Mi Saga",
   descripcion = "Desc",
   fechaInicio = entity.fechaInicio.toString(),
   fechaFin = null,
   imagen = null,
   isAcabada = false,
   producciones = emptyList()
  )

  whenever(sagaRepository.findById(id)).thenReturn(Optional.of(entity))
  whenever(sagaMapper.toSagaResponse(entity)).thenReturn(dto)

  val result = service.getSagaById(id)

  assertNotNull(result)
  assertEquals(dto, result)
  verify(sagaRepository).findById(id)
  verify(sagaMapper).toSagaResponse(entity)
 }

 @Test
 fun getSagaByIdNotFound() {
  val id = "noExiste"
  whenever(sagaRepository.findById(id)).thenReturn(Optional.empty())

  val result = service.getSagaById(id)
  assertNull(result)
  verify(sagaRepository).findById(id)
  verify(sagaMapper, never()).toSagaResponse(any())
 }

 @Test
 fun getAllSagas() {
  val e1 = Saga(id = "s1", nombre = "A", descripcion = "x", isAcabada = true, fechaInicio = Date(), fechaFin = null, imagen = null, producciones = mutableListOf())
  val e2 = Saga(id = "s2", nombre = "B", descripcion = "y", isAcabada = false, fechaInicio = Date(), fechaFin = null, imagen = null, producciones = mutableListOf())
  whenever(sagaRepository.findAll()).thenReturn(listOf(e1, e2))

  val dto1 = SagaResponse(id = "s1", nombre = "A", descripcion = "x", fechaInicio = e1.fechaInicio.toString(), fechaFin = null, imagen = null, isAcabada = true, producciones = emptyList())
  val dto2 = SagaResponse(id = "s2", nombre = "B", descripcion = "y", fechaInicio = e2.fechaInicio.toString(), fechaFin = null, imagen = null, isAcabada = false, producciones = emptyList())
  whenever(sagaMapper.toSagaResponse(e1)).thenReturn(dto1)
  whenever(sagaMapper.toSagaResponse(e2)).thenReturn(dto2)

  val result = service.getAllSagas()

  assertEquals(2, result.size)
  assertTrue(result.containsAll(listOf(dto1, dto2)))
  verify(sagaRepository).findAll()
  verify(sagaMapper).toSagaResponse(e1)
  verify(sagaMapper).toSagaResponse(e2)
 }

 @Test
 fun createSaga() {
  val request = SagaRequest(
   nombre = "Nueva Saga",
   descripcion = "Desc",
   isAcabada = false,
   fechaInicio = Date(1625097600000L),
   fechaFin = null,
   imagen = "img.png"
  )
  val newEntity = Saga(
   nombre = request.nombre,
   descripcion = request.descripcion,
   isAcabada = request.isAcabada,
   fechaInicio = request.fechaInicio,
   fechaFin = request.fechaFin,
   imagen = request.imagen,
   producciones = mutableListOf()
  )
  val savedEntity = newEntity.copy(id = "sagaNuevo")

  val dto = SagaResponse(
   id = "sagaNuevo",
   nombre = "Nueva Saga",
   descripcion = "Desc",
   fechaInicio = savedEntity.fechaInicio.toString(),
   fechaFin = null,
   imagen = "img.png",
   isAcabada = false,
   producciones = emptyList()
  )

  whenever(sagaMapper.toSagaEntity(request)).thenReturn(newEntity)
  whenever(sagaRepository.save(newEntity)).thenReturn(savedEntity)
  whenever(sagaMapper.toSagaResponse(savedEntity)).thenReturn(dto)
  whenever(sagaRepository.existsByNombre(request.nombre)).thenReturn(false)

  val result = service.createSaga(request)

  assertEquals(dto, result)
  verify(sagaRepository).existsByNombre(request.nombre)
  verify(sagaMapper).toSagaEntity(request)
  verify(sagaRepository).save(newEntity)
  verify(sagaMapper).toSagaResponse(savedEntity)
 }

 @Test
 fun createSagaVacia() {
  val request = SagaRequest(
   nombre = "   ",  // en blanco
   descripcion = "Desc",
   isAcabada = false,
   fechaInicio = Date(),
   fechaFin = null,
   imagen = null
  )
  assertThrows(SagaValidationException::class.java) {
   service.createSaga(request)
  }
  verify(sagaRepository, never()).save(any())
  verify(sagaMapper, never()).toSagaEntity(any())
 }

 @Test
 fun createSagaMismoNombre() {
  val request = SagaRequest(
   nombre = "Duplicada",
   descripcion = "Desc",
   isAcabada = false,
   fechaInicio = Date(),
   fechaFin = null,
   imagen = null
  )
  whenever(sagaRepository.existsByNombre(request.nombre)).thenReturn(true)

  assertThrows(SagaValidationException::class.java) {
   service.createSaga(request)
  }
  verify(sagaRepository).existsByNombre(request.nombre)
  verify(sagaRepository, never()).save(any())
 }

 @Test
 fun updateSaga() {
  val id = "sagaX"
  val existing = Saga(
   id = id, nombre = "Antigua", descripcion = "DescAntigua", isAcabada = false,
   fechaInicio = Date(1622505600000L), fechaFin = null, imagen = null, producciones = mutableListOf()
  )
  val request = SagaRequest(
   nombre = "Actualizada", descripcion = "DescNueva", isAcabada = true,
   fechaInicio = Date(1625097600000L), fechaFin = Date(1627776000000L), imagen = "url.png"
  )
  val updatedEntity = existing.copy(
   nombre = request.nombre, descripcion = request.descripcion,
   isAcabada = request.isAcabada, fechaInicio = request.fechaInicio,
   fechaFin = request.fechaFin, imagen = request.imagen
  )
  val dto = SagaResponse(
   id = id, nombre = "Actualizada", descripcion = "DescNueva",
   fechaInicio = updatedEntity.fechaInicio.toString(),
   fechaFin = updatedEntity.fechaFin.toString(),
   imagen = "url.png", isAcabada = true, producciones = emptyList()
  )

  whenever(sagaRepository.findById(id)).thenReturn(Optional.of(existing))
  whenever(sagaRepository.existsByNombre(request.nombre)).thenReturn(false)
  whenever(sagaRepository.save(any())).thenReturn(updatedEntity)
  whenever(sagaMapper.toSagaResponse(updatedEntity)).thenReturn(dto)

  val result = service.updateSaga(id, request)

  assertNotNull(result)
  assertEquals(dto, result)
  verify(sagaRepository, times(2)).findById(id)
  verify(sagaRepository).save(any())
  verify(sagaMapper).toSagaResponse(updatedEntity)
 }

 @Test
 fun updateSagaNotFound() {
  val id = "noExiste"
  val request = SagaRequest(
   nombre = "Nombre",
   descripcion = "Desc",
   isAcabada = false,
   fechaInicio = Date(),
   fechaFin = null,
   imagen = null
  )

  whenever(sagaRepository.findById(id)).thenReturn(Optional.empty())

  val exception = assertThrows<SagaNotFoundException> {
   service.updateSaga(id, request)
  }

  assertEquals(id, exception.message)
  verify(sagaRepository).findById(id)
  verify(sagaRepository, never()).save(any())
 }

 @Test
 fun updateSagaANombreExistente() {
  val id = "saga1"
  val existing = Saga(
   id = id,
   nombre = "Original",
   descripcion = "Desc",
   isAcabada = false,
   fechaInicio = Date(),
   fechaFin = null,
   imagen = null,
   producciones = mutableListOf()
  )
  val request = SagaRequest(
   nombre = "OtraSaga",
   descripcion = "Desc2",
   isAcabada = true,
   fechaInicio = Date(),
   fechaFin = null,
   imagen = null
  )
  whenever(sagaRepository.findById(id)).thenReturn(Optional.of(existing))
  whenever(sagaRepository.existsByNombre(request.nombre)).thenReturn(true)

  assertThrows(SagaValidationException::class.java) {
   service.updateSaga(id, request)
  }
  verify(sagaRepository).findById(id)
  verify(sagaRepository, never()).save(any())
 }

 @Test
 fun actualizarImagenSaga() {
  val id = "sagaImg"
  val existing = Saga(
   id = id,
   nombre = "ConImagen",
   descripcion = "Desc",
   isAcabada = false,
   fechaInicio = Date(),
   fechaFin = null,
   imagen = "old.png",
   producciones = mutableListOf()
  )
  val newUrl = "nueva.png"
  val updated = existing.copy(imagen = newUrl)
  val dto = SagaResponse(
   id = id,
   nombre = "ConImagen",
   descripcion = "Desc",
   fechaInicio = existing.fechaInicio.toString(),
   fechaFin = null,
   imagen = newUrl,
   isAcabada = false,
   producciones = emptyList()
  )

  whenever(sagaRepository.findById(id)).thenReturn(Optional.of(existing))
  whenever(sagaRepository.save(updated)).thenReturn(updated)
  whenever(sagaMapper.toSagaResponse(updated)).thenReturn(dto)

  val result = service.actualizarImagenSaga(id, newUrl)

  assertEquals(dto, result)
  verify(sagaRepository).findById(id)
  verify(sagaRepository).save(updated)
  verify(sagaMapper).toSagaResponse(updated)
 }

 @Test
 fun filterSagas() {
  val page = 0
  val size = 2
  val params = SagaFilterParams(
   nombre = null, isAcabada = null,
   fechaInicioDesde = null, fechaInicioHasta = null,
   tieneImagen = null
  )
  val s1 = Saga("s1", "Alpha", "", false, Date(1622505600000L), null, null, mutableListOf())
  val s2 = Saga("s2", "Beta", "", true, Date(1625097600000L), null, null, mutableListOf())
  val s3 = Saga("s3", "Gamma", "", false, Date(1620009600000L), null, null, mutableListOf())
  whenever(sagaRepository.findAll()).thenReturn(listOf(s1, s2, s3))

  val dtoS1 = SagaResponse("s1", "Alpha", "", s1.fechaInicio.toString(), null, null, false, emptyList())
  val dtoS2 = SagaResponse("s2", "Beta", "", s2.fechaInicio.toString(), null, null, true, emptyList())
  // No hace falta mockear dtoS3 porque no entra en la paginación
  whenever(sagaMapper.toSagaResponse(s1)).thenReturn(dtoS1)
  whenever(sagaMapper.toSagaResponse(s2)).thenReturn(dtoS2)

  val result = service.filterSagas(params, page, size, listOf(), "")

  assertEquals(3L, result.totalItems)
  assertEquals(2, result.data.size)
  assertEquals(dtoS2, result.data[0])
  assertEquals(dtoS1, result.data[1])
 }

 @Test
 fun actualizarImagenSagaNotFound() {
  val id = "noExisteImg"
  whenever(sagaRepository.findById(id)).thenReturn(Optional.empty())

  assertThrows(SagaNotFoundException::class.java) {
   service.actualizarImagenSaga(id, "url.png")
  }
  verify(sagaRepository).findById(id)
  verify(sagaRepository, never()).save(any())
 }

 @Test
 fun deleteSagaNotFound() {
  val id = "noExisteDel"
  // findByIdOrNull internamente hace findById().orElse(null), así que usamos esto
  whenever(sagaRepository.findById(id)).thenReturn(Optional.empty())

  assertThrows(SagaNotFoundException::class.java) {
   service.deleteSaga(id)
  }

  verify(sagaRepository).findById(id)
  verify(produccionRepository, never()).findAllBySagaId(any())
  verify(sagaRepository, never()).delete(any())
 }

 @Test
 fun agregarProduccionASaga() {
  val sagaId = "s1"
  val prodId = "p1"
  val sagaEntity = Saga(
   id = sagaId,
   nombre = "S1",
   descripcion = "D1",
   isAcabada = false,
   fechaInicio = Date(),
   fechaFin = null,
   imagen = null,
   producciones = mutableListOf()
  )
  val prodEntity = Produccion(
   id = prodId,
   titulo = "P1",
   tipo = TipoProduccion.PELICULA,
   estreno = Date(),
   duracion = 90,
   sinopsis = "",
   imagen = null,
   informacion = null,
   puntuacion = 0.0,
   categorias = mutableSetOf(),
   clasificacionEdad = ClasificacionEdad.MAYORES_12
  )

  whenever(sagaRepository.findById(sagaId)).thenReturn(Optional.of(sagaEntity))
  whenever(produccionRepository.findById(prodId)).thenReturn(Optional.of(prodEntity))
  whenever(produccionRepository.save(prodEntity)).thenReturn(prodEntity)

  service.agregarProduccionASaga(sagaId, prodId)

  assertEquals(sagaEntity, prodEntity.saga)
  verify(sagaRepository).findById(sagaId)
  verify(produccionRepository).findById(prodId)
  verify(produccionRepository).save(prodEntity)
 }

 @Test
 fun agregarProduccionASagaNotFound() {
  val sagaId = "noS1"
  val prodId = "p1"
  whenever(sagaRepository.findById(sagaId)).thenReturn(Optional.empty())

  assertThrows(SagaNotFoundException::class.java) {
   service.agregarProduccionASaga(sagaId, prodId)
  }
  verify(sagaRepository).findById(sagaId)
  verify(produccionRepository, never()).findById(anyString())
 }

 @Test
 fun agregarProduccionASagaProductionNotFound`() {
  val sagaId = "s1"
  val prodId = "noP"
  val sagaEntity = Saga(
   id = sagaId,
   nombre = "S1",
   descripcion = "D1",
   isAcabada = false,
   fechaInicio = Date(),
   fechaFin = null,
   imagen = null,
   producciones = mutableListOf()
  )
  whenever(sagaRepository.findById(sagaId)).thenReturn(Optional.of(sagaEntity))
  whenever(produccionRepository.findById(prodId)).thenReturn(Optional.empty())

  assertThrows(ProduccionNotFoundException::class.java) {
   service.agregarProduccionASaga(sagaId, prodId)
  }
  verify(sagaRepository).findById(sagaId)
  verify(produccionRepository).findById(prodId)
 }

 @Test
 fun eliminarProduccionDeSaga() {
  val prodId = "p4"
  val sagaEntity = Saga(
   id = "sA",
   nombre = "S A",
   descripcion = "D",
   isAcabada = false,
   fechaInicio = Date(),
   fechaFin = null,
   imagen = null,
   producciones = mutableListOf()
  )
  val prodEntity = Produccion(
   id = prodId,
   titulo = "P4",
   tipo = TipoProduccion.PELICULA,
   estreno = Date(),
   duracion = 80,
   sinopsis = "",
   imagen = null,
   informacion = null,
   puntuacion = 0.0,
   categorias = mutableSetOf(),
   clasificacionEdad = ClasificacionEdad.MAYORES_12
  ).apply { saga = sagaEntity }

  whenever(produccionRepository.findById(prodId)).thenReturn(Optional.of(prodEntity))
  whenever(produccionRepository.save(prodEntity)).thenReturn(prodEntity)

  service.eliminarProduccionDeSaga(prodId)

  assertNull(prodEntity.saga)
  verify(produccionRepository).findById(prodId)
  verify(produccionRepository).save(prodEntity)
 }

 @Test
 fun eliminarProduccionDeSagaNotFound() {
  val prodId = "noP4"
  whenever(produccionRepository.findById(prodId)).thenReturn(Optional.empty())

  assertThrows(ProduccionNotFoundException::class.java) {
   service.eliminarProduccionDeSaga(prodId)
  }
  verify(produccionRepository).findById(prodId)
  verify(produccionRepository, never()).save(any())
 }

 @Test
 fun eliminarProduccionDeSagaIllegalState() {
  val prodId = "p5"
  val prodEntity = Produccion(
   id = prodId,
   titulo = "P5",
   tipo = TipoProduccion.PELICULA,
   estreno = Date(),
   duracion = 60,
   sinopsis = "",
   imagen = null,
   informacion = null,
   puntuacion = 0.0,
   categorias = mutableSetOf(),
   clasificacionEdad = ClasificacionEdad.MAYORES_12
  )
  whenever(produccionRepository.findById(prodId)).thenReturn(Optional.of(prodEntity))

  assertThrows(IllegalStateException::class.java) {
   service.eliminarProduccionDeSaga(prodId)
  }
  verify(produccionRepository).findById(prodId)
  verify(produccionRepository, never()).save(any())
 }

 @Test
 fun getAllSagasPaginated() {
  val page = 0
  val size = 2
  val pageRequest = PageRequest.of(page, size)
  val entity1 = Saga("s1", "A", "x", false, Date(), null, null, mutableListOf())
  val entity2 = Saga("s2", "B", "y", true, Date(), null, null, mutableListOf())
  val springPage = PageImpl(listOf(entity1, entity2), pageRequest, 5L)

  whenever(sagaRepository.findAll(any<Pageable>())).thenReturn(springPage)

  val dto1 = SagaResponse("s1", "A", "x", entity1.fechaInicio.toString(), null, null, false, emptyList())
  val dto2 = SagaResponse("s2", "B", "y", entity2.fechaInicio.toString(), null, null, true, emptyList())
  whenever(sagaMapper.toSagaResponse(entity1)).thenReturn(dto1)
  whenever(sagaMapper.toSagaResponse(entity2)).thenReturn(dto2)

  val result = service.getAllSagasPaginated(page, size, emptyList(), "")

  assertEquals(5L, result.totalItems)
  assertEquals(2, result.data.size)
  assertEquals(dto1, result.data[0])
  assertEquals(dto2, result.data[1])
  verify(sagaRepository).findAll(any<Pageable>())
 }

 @Test
 fun updateSagaNotFoundId() {
  val id = "noExiste"
  val request = SagaRequest(
   nombre = "Nombre",
   descripcion = "Desc",
   isAcabada = false,
   fechaInicio = Date(),
   fechaFin = null,
   imagen = null
  )

  whenever(sagaRepository.findById(id)).thenReturn(Optional.empty())

  val exception = assertThrows(SagaNotFoundException::class.java) {
   service.updateSaga(id, request)
  }

  assertEquals(id, exception.message)
  verify(sagaRepository).findById(id)
  verify(sagaRepository, never()).save(any())
 }

 @Test
 fun deleteSagaEliminaYQuitaProducciones() {
  val id = "sagaDel"
  val existing = Saga(
   id = id,
   nombre = "ParaBorrar",
   descripcion = "Desc",
   isAcabada = false,
   fechaInicio = Date(),
   fechaFin = null,
   imagen = null,
   producciones = mutableListOf()
  )

  whenever(sagaRepository.findById(id)).thenReturn(Optional.of(existing))
  whenever(produccionRepository.findAllBySagaId(id)).thenReturn(emptyList())

  service.deleteSaga(id)

  verify(sagaRepository).findById(id)
  verify(produccionRepository).findAllBySagaId(id)
  verify(sagaRepository).delete(existing)
 }

 @Test
 fun filterSagasConParametros() {
  val page = 0
  val size = 5
  val now = Date(1625097600000L)
  val s1 = Saga(id="s1", nombre="Alpha", descripcion="", isAcabada = false, fechaInicio = Date(1622505600000L), fechaFin = now, imagen = "img1", producciones = mutableListOf())
  val s2 = Saga(id="s2", nombre="Beta",  descripcion="", isAcabada = true, fechaInicio = Date(1625097600000L), fechaFin = null, imagen = null, producciones = mutableListOf())
  val s3 = Saga(id="s3", nombre="AlphaX",descripcion="", isAcabada = false, fechaInicio = Date(1627689600000L), fechaFin = null, imagen = "img2", producciones = mutableListOf())

  whenever(sagaRepository.findAll()).thenReturn(listOf(s1, s2, s3))

  val params = SagaFilterParams(
   nombre = "Alpha",
   isAcabada = false,
   fechaInicioDesde = Date(1622000000000L),
   fechaInicioHasta = Date(1628000000000L),
   tieneImagen = true
  )

  val dto1 = SagaResponse("s1","Alpha","",s1.fechaInicio.toString(), s1.fechaFin.toString(), "img1", false, emptyList())
  val dto3 = SagaResponse("s3","AlphaX","",s3.fechaInicio.toString(), null, "img2", false, emptyList())
  whenever(sagaMapper.toSagaResponse(eq(s1))).thenReturn(dto1)
  whenever(sagaMapper.toSagaResponse(eq(s3))).thenReturn(dto3)

  val result = service.filterSagas(params, page, size, listOf("nombre"), "asc")

  assertEquals(2L, result.totalItems)
  assertEquals(dto1, result.data[0])
  assertEquals(dto3, result.data[1])
 }

 @Test
 fun existsByIdReturValor() {
  whenever(sagaRepository.existsById("xyz")).thenReturn(true)
  assertTrue(service.existsById("xyz"))
  verify(sagaRepository).existsById("xyz")
 }

 @Test
 fun existsByNombreReturnValor() {
  whenever(sagaRepository.existsByNombre("MiNombre")).thenReturn(false)
  assertFalse(service.existsByNombre("MiNombre"))
  verify(sagaRepository).existsByNombre("MiNombre")
 }
}
