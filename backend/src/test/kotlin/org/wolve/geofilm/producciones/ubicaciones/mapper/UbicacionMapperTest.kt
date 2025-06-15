package org.wolve.geofilm.producciones.ubicaciones.mapper

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.wolve.geofilm.producciones.ubicaciones.dto.UbicacionRequest
import org.wolve.geofilm.producciones.ubicaciones.dto.UbicacionResponse
import org.wolve.geofilm.producciones.ubicaciones.models.Ubicacion
import org.wolve.geofilm.utils.pagination.PaginationUtils.PaginatedResponse
import java.time.LocalDateTime

class UbicacionMapperTest {

 private lateinit var mapper: UbicacionMapper

 @BeforeEach
 fun setUp() {
  mapper = UbicacionMapper()
 }

 @Test
 fun toEntity() {
  val request = UbicacionRequest(
   nombre = "Lugar X",
   latitud = 12.345,
   longitud = 67.890
  )
  val entidad: Ubicacion = mapper.toEntity(request)
  assertNotNull(entidad.id)
  assertTrue(entidad.id.isNotEmpty())
  assertEquals("Lugar X", entidad.nombre)
  assertEquals(12.345, entidad.latitud)
  assertEquals(67.890, entidad.longitud)
  assertNotNull(entidad.createdAt)
  assertNotNull(entidad.updatedAt)
 }

 @Test
 fun toResponse() {
  val now = LocalDateTime.of(2023, 6, 1, 10, 0)
  val entidad = Ubicacion(
   id = "u1",
   nombre = "Lugar Y",
   latitud = 1.234,
   longitud = 5.678,
   createdAt = now,
   updatedAt = now
  )
  val response: UbicacionResponse = mapper.toResponse(entidad)
  assertEquals("u1", response.id)
  assertEquals("Lugar Y", response.nombre)
  assertEquals(1.234, response.latitud)
  assertEquals(5.678, response.longitud)
  assertEquals(now, response.createdAt)
  assertEquals(now, response.updatedAt)
 }

 @Test
 fun toPaginatedResponse() {
  val now = LocalDateTime.of(2023, 7, 1, 12, 0)
  val e1 = Ubicacion(id = "uA", nombre = "A", latitud = 0.1, longitud = 0.2, createdAt = now, updatedAt = now)
  val e2 = Ubicacion(id = "uB", nombre = "B", latitud = 0.3, longitud = 0.4, createdAt = now, updatedAt = now)
  val pageRequest: Pageable = PageRequest.of(0, 2)
  val springPage = PageImpl(listOf(e1, e2), pageRequest, 5L)
  val paginated: PaginatedResponse<UbicacionResponse> = mapper.toPaginatedResponse(springPage)
  assertEquals(5L, paginated.totalItems)
  assertEquals(2, paginated.data.size)
  val r1 = paginated.data[0]
  val r2 = paginated.data[1]
  assertEquals("uA", r1.id)
  assertEquals("A", r1.nombre)
  assertEquals(0.1, r1.latitud)
  assertEquals(0.2, r1.longitud)
  assertEquals(now, r1.createdAt)
  assertEquals(now, r1.updatedAt)
  assertEquals("uB", r2.id)
  assertEquals("B", r2.nombre)
  assertEquals(0.3, r2.latitud)
  assertEquals(0.4, r2.longitud)
  assertEquals(now, r2.createdAt)
  assertEquals(now, r2.updatedAt)
 }
}
