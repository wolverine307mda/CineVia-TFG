package org.wolve.geofilm.producciones.rodajes.mapper

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.wolve.geofilm.producciones.rodajes.dto.response.RodajeResponse
import org.wolve.geofilm.producciones.rodajes.models.Rodaje
import org.wolve.geofilm.producciones.ubicaciones.dto.UbicacionResponse
import org.wolve.geofilm.producciones.ubicaciones.mapper.UbicacionMapper
import org.wolve.geofilm.producciones.ubicaciones.models.Ubicacion
import java.time.LocalDateTime

class RodajeMapperTest {

 private lateinit var ubicacionMapper: UbicacionMapper
 private lateinit var mapper: RodajeMapper

 @BeforeEach
 fun setUp() {
  ubicacionMapper = mock()
  mapper = RodajeMapper(ubicacionMapper)
 }

 @Test
 fun `toResponse - asigna todos los campos correctamente con notas e imagenes`() {
  val now = LocalDateTime.of(2023, 6, 1, 10, 0)
  val ubicacionEntity = Ubicacion(
   id = "loc1",
   nombre = "Ubicación 1",
   latitud = 12.34,
   longitud = 56.78,
   createdAt = now,
   updatedAt = now
  )
  val rodajeEntity = Rodaje(
   id = "r1",
   produccion = mock(),
   ubicacion = ubicacionEntity,
   notas = "Notas de prueba",
   imagenes = mutableListOf("img1.jpg", "img2.png"),
   createdAt = now,
   updatedAt = now
  )
  val ubicacionDto = UbicacionResponse(
   id = "loc1",
   nombre = "Ubicación 1",
   latitud = 12.34,
   longitud = 56.78,
   createdAt = now,
   updatedAt = now
  )
  whenever(ubicacionMapper.toResponse(eq(ubicacionEntity))).thenReturn(ubicacionDto)

  val response: RodajeResponse = mapper.toResponse(rodajeEntity)

  assertEquals("r1", response.id)
  assertEquals("Notas de prueba", response.notas)
  assertEquals(listOf("img1.jpg", "img2.png"), response.imagenes)
  assertEquals(now, response.createdAt)
  assertEquals(now, response.updatedAt)
  assertEquals(ubicacionDto, response.ubicacion)
 }

 @Test
 fun `toResponse - maneja notas nulas e imagenes vacias`() {
  val now = LocalDateTime.of(2023, 7, 1, 12, 0)
  val ubicacionEntity = Ubicacion(
   id = "loc2",
   nombre = "Ubicación 2",
   latitud = 98.76,
   longitud = 54.32,
   createdAt = now,
   updatedAt = now
  )
  val rodajeEntity = Rodaje(
   id = "r2",
   produccion = mock(),
   ubicacion = ubicacionEntity,
   notas = null,
   imagenes = mutableListOf(),
   createdAt = now,
   updatedAt = now
  )
  val ubicacionDto = UbicacionResponse(
   id = "loc2",
   nombre = "Ubicación 2",
   latitud = 98.76,
   longitud = 54.32,
   createdAt = now,
   updatedAt = now
  )
  whenever(ubicacionMapper.toResponse(eq(ubicacionEntity))).thenReturn(ubicacionDto)

  val response: RodajeResponse = mapper.toResponse(rodajeEntity)

  assertEquals("r2", response.id)
  assertNull(response.notas)
  assertTrue(response.imagenes.isEmpty())
  assertEquals(now, response.createdAt)
  assertEquals(now, response.updatedAt)
  assertEquals(ubicacionDto, response.ubicacion)
 }

 @Test
 fun `toResponse - asegura que llama a ubicacionMapper una sola vez`() {
  val now = LocalDateTime.now()
  val ubicacionEntity = Ubicacion(
   id = "loc3",
   nombre = "Otra Ubicacion",
   latitud = 0.0,
   longitud = 0.0,
   createdAt = now,
   updatedAt = now
  )
  val rodajeEntity = Rodaje(
   id = "r3",
   produccion = mock(),
   ubicacion = ubicacionEntity,
   notas = "Algo",
   imagenes = mutableListOf("a.jpg"),
   createdAt = now,
   updatedAt = now
  )
  val ubicacionDto = UbicacionResponse(
   id = "loc3",
   nombre = "Otra Ubicacion",
   latitud = 0.0,
   longitud = 0.0,
   createdAt = now,
   updatedAt = now
  )
  whenever(ubicacionMapper.toResponse(eq(ubicacionEntity))).thenReturn(ubicacionDto)

  mapper.toResponse(rodajeEntity)
  mapper.toResponse(rodajeEntity)

  verify(ubicacionMapper, org.mockito.kotlin.times(2)).toResponse(eq(ubicacionEntity))
 }

 @Test
 fun `toResponse - valida varios tipos de imagenes`() {
  val now = LocalDateTime.of(2022, 1, 1, 0, 0)
  val ubicacionEntity = Ubicacion(
   id = "loc4",
   nombre = "Sitio X",
   latitud = -12.0,
   longitud = 34.0,
   createdAt = now,
   updatedAt = now
  )
  val imagenesList = mutableListOf("a.png", "b.bmp", "c.gif", "d.jpeg")
  val rodajeEntity = Rodaje(
   id = "r4",
   produccion = mock(),
   ubicacion = ubicacionEntity,
   notas = "Variedad de imágenes",
   imagenes = imagenesList,
   createdAt = now,
   updatedAt = now
  )
  val ubicacionDto = UbicacionResponse(
   id = "loc4",
   nombre = "Sitio X",
   latitud = -12.0,
   longitud = 34.0,
   createdAt = now,
   updatedAt = now
  )
  whenever(ubicacionMapper.toResponse(eq(ubicacionEntity))).thenReturn(ubicacionDto)

  val response = mapper.toResponse(rodajeEntity)

  assertEquals(imagenesList, response.imagenes)
  assertEquals("Variedad de imágenes", response.notas)
  assertEquals(ubicacionDto, response.ubicacion)
 }
}
