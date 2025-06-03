package org.wolve.geofilm.producciones.rodajes.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.never
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.wolve.geofilm.producciones.produccion.exceptions.ProduccionNotFoundException
import org.wolve.geofilm.producciones.produccion.models.Categoria
import org.wolve.geofilm.producciones.produccion.models.ClasificacionEdad
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.produccion.models.TipoProduccion
import org.wolve.geofilm.producciones.produccion.repository.ProduccionRepository
import org.wolve.geofilm.producciones.rodajes.dto.request.RodajeRequest
import org.wolve.geofilm.producciones.rodajes.dto.response.RodajeResponse
import org.wolve.geofilm.producciones.rodajes.mapper.RodajeMapper
import org.wolve.geofilm.producciones.rodajes.models.Rodaje
import org.wolve.geofilm.producciones.rodajes.repository.RodajeRepository
import org.wolve.geofilm.producciones.ubicaciones.dto.UbicacionResponse
import org.wolve.geofilm.producciones.ubicaciones.exceptions.UbicacionNotFoundException
import org.wolve.geofilm.producciones.ubicaciones.mapper.UbicacionMapper
import org.wolve.geofilm.producciones.ubicaciones.models.Ubicacion
import org.wolve.geofilm.producciones.ubicaciones.repository.UbicacionRepository
import java.time.LocalDateTime
import java.util.*

@ExtendWith(MockitoExtension::class)
class RodajeServiceTest {

 @Mock
 private lateinit var rodajeRepository: RodajeRepository

 @Mock
 private lateinit var produccionRepository: ProduccionRepository

 @Mock
 private lateinit var ubicacionRepository: UbicacionRepository

 @Mock
 private lateinit var ubicacionMapper: UbicacionMapper

 @Mock
 private lateinit var rodajeMapper: RodajeMapper

 private lateinit var service: RodajeServiceImpl

 @BeforeEach
 fun setUp() {
  service = RodajeServiceImpl(
   rodajeRepository,
   produccionRepository,
   ubicacionRepository,
   rodajeMapper
  )
 }

 @Test
 fun createRodaje() {
  val prodId = "prod1"
  val locId = "loc1"
  val request = RodajeRequest(
   produccionId = prodId,
   ubicacionId = locId,
   notas = "Notas X",
   imagenes = listOf("img1.png", "img2.jpg")
  )

  val now = LocalDateTime.of(2023, 6, 1, 10, 0)
  val produccion = Produccion(
   id = prodId,
   titulo = "Test",
   tipo = TipoProduccion.SERIE,
   estreno = Date(),
   duracion = 100,
   sinopsis = "s",
   imagen = "i",
   informacion = "inf",
   puntuacion = 6.0,
   categorias = mutableSetOf(Categoria.DRAMA),
   clasificacionEdad = ClasificacionEdad.MAYORES_12
  )
  val ubicacion = Ubicacion(
   id = locId,
   nombre = "Lugar A",
   latitud = 1.0,
   longitud = 2.0,
   createdAt = now,
   updatedAt = now
  )

  val rodajeToSave = Rodaje(
   produccion = produccion,
   ubicacion = ubicacion,
   notas = "Notas X",
   imagenes = mutableListOf("img1.png", "img2.jpg"),
   createdAt = now,
   updatedAt = now
  )
  val savedRodaje = rodajeToSave.copy(id = "r1")

  val ubicacionResponse = UbicacionResponse(
   id = locId,
   nombre = "Lugar A",
   latitud = 1.0,
   longitud = 2.0,
   createdAt = now,
   updatedAt = now
  )

  val dto = RodajeResponse(
   id = "r1",
   notas = "Notas X",
   imagenes = listOf("img1.png", "img2.jpg"),
   createdAt = now,
   updatedAt = now,
   ubicacion = ubicacionResponse
  )

  whenever(produccionRepository.findById(prodId)).thenReturn(Optional.of(produccion))
  whenever(ubicacionRepository.findById(locId)).thenReturn(Optional.of(ubicacion))
  whenever(rodajeRepository.save(rodajeToSave)).thenReturn(savedRodaje)
  whenever(ubicacionMapper.toResponse(ubicacion)).thenReturn(ubicacionResponse)
  whenever(rodajeMapper.toResponse(savedRodaje)).thenReturn(dto)

  val result: RodajeResponse = service.create(request)

  assertEquals(dto, result)
  verify(produccionRepository).findById(prodId)
  verify(ubicacionRepository).findById(locId)
  verify(rodajeRepository).save(rodajeToSave)
  verify(ubicacionMapper).toResponse(ubicacion)
  verify(rodajeMapper).toResponse(savedRodaje)
 }

 @Test
 fun findByProduccion() {
  val prodId = "prodX"
  val page = 0
  val size = 2
  val pageable: Pageable = PageRequest.of(page, size)
  val now = LocalDateTime.of(2023, 7, 1, 12, 0)

  val ubicacion = Ubicacion(
   id = "loc1",
   nombre = "L1",
   latitud = 0.0,
   longitud = 0.0,
   createdAt = now,
   updatedAt = now
  )

  val r1 = Rodaje(
   id = "r1",
   produccion = Produccion(
    id = "1",
    titulo = "Test",
    tipo = TipoProduccion.SERIE,
    estreno = Date(),
    duracion = 100,
    sinopsis = "s",
    imagen = "i",
    informacion = "inf",
    puntuacion = 6.0,
    categorias = mutableSetOf(Categoria.DRAMA),
    clasificacionEdad = ClasificacionEdad.MAYORES_12
   ),
   ubicacion = ubicacion,
   notas = "N1",
   imagenes = mutableListOf("i1.jpg"),
   createdAt = now,
   updatedAt = now
  )

  val r2 = r1.copy(id = "r2")

  val ubicacionResponse = UbicacionResponse(
   id = "loc1",
   nombre = "L1",
   latitud = 0.0,
   longitud = 0.0,
   createdAt = now,
   updatedAt = now
  )

  val dto1 = RodajeResponse(
   id = "r1",
   notas = "N1",
   imagenes = listOf("i1.jpg"),
   createdAt = now,
   updatedAt = now,
   ubicacion = ubicacionResponse
  )
  val dto2 = dto1.copy(id = "r2")

  val springPage = PageImpl(listOf(r1, r2), pageable, 5)

  whenever(rodajeRepository.findAllByProduccionId(prodId, pageable)).thenReturn(springPage)
  whenever(ubicacionMapper.toResponse(r1.ubicacion)).thenReturn(ubicacionResponse)
  whenever(ubicacionMapper.toResponse(r2.ubicacion)).thenReturn(ubicacionResponse)
  whenever(rodajeMapper.toResponse(r1)).thenReturn(dto1)
  whenever(rodajeMapper.toResponse(r2)).thenReturn(dto2)

  val result: Map<String, Any> = service.findByProduccion(prodId, page, size)

  val data = result["data"] as List<*>
  val totalItems = result["totalItems"] as Long
  val totalPages = result["totalPages"] as Int

  assertEquals(5L, totalItems)
  assertEquals(3, totalPages)
  assertEquals(2, data.size)
  assertEquals(dto1, data[0])
  assertEquals(dto2, data[1])
  verify(rodajeRepository).findAllByProduccionId(prodId, pageable)
 }

 @Test
 fun createRodajeNotFound() {
  val prodId = "noProd"
  val locId = "loc1"
  val request = RodajeRequest(
   produccionId = prodId,
   ubicacionId = locId,
   notas = null,
   imagenes = emptyList()
  )
  whenever(produccionRepository.findById(prodId)).thenReturn(Optional.empty())

  assertThrows(ProduccionNotFoundException::class.java) {
   service.create(request)
  }
  verify(produccionRepository).findById(prodId)
  verify(ubicacionRepository, never()).findById(any())
  verify(rodajeRepository, never()).save(any())
 }

 @Test
 fun createRodajeSinUbicacion() {
  val prodId = "prod1"
  val locId = "noLoc"
  val request = RodajeRequest(
   produccionId = prodId,
   ubicacionId = locId,
   notas = "Notas",
   imagenes = listOf("img.png")
  )
  whenever(produccionRepository.findById(prodId)).thenReturn(Optional.of(mock()))
  whenever(ubicacionRepository.findById(locId)).thenReturn(Optional.empty())

  assertThrows(UbicacionNotFoundException::class.java) {
   service.create(request)
  }
  verify(produccionRepository).findById(prodId)
  verify(ubicacionRepository).findById(locId)
  verify(rodajeRepository, never()).save(any())
 }

 @Test
 fun findByProduccionSinRodajes() {
  val prodId = "prodY"
  val page = 1
  val size = 3
  val pageable: Pageable = PageRequest.of(page, size)
  val springPage = PageImpl<Rodaje>(emptyList(), pageable, 0)
  whenever(rodajeRepository.findAllByProduccionId(prodId, pageable)).thenReturn(springPage)

  val result: Map<String, Any> = service.findByProduccion(prodId, page, size)
  val data = result["data"] as List<*>
  val totalItems = result["totalItems"] as Long
  val totalPages = result["totalPages"] as Int

  assertEquals(0L, totalItems)
  assertEquals(0, totalPages)
  assertTrue(data.isEmpty())
  verify(rodajeRepository).findAllByProduccionId(prodId, pageable)
 }
}
