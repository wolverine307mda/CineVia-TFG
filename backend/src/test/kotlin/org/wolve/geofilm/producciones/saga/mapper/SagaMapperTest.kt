package org.wolve.geofilm.producciones.saga.mapper

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import org.wolve.geofilm.producciones.produccion.mapper.ProduccionMapper
import org.wolve.geofilm.producciones.produccion.models.ClasificacionEdad
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.produccion.models.TipoProduccion
import org.wolve.geofilm.producciones.saga.dto.SagaRequest
import org.wolve.geofilm.producciones.saga.dto.SagaResponse
import org.wolve.geofilm.producciones.saga.model.Saga
import java.util.*

class SagaMapperTest {

 private lateinit var produccionMapper: ProduccionMapper
 private lateinit var mapper: SagaMapper

 @BeforeEach
 fun setUp() {
  produccionMapper = mock()
  mapper = SagaMapper(produccionMapper)
 }

 @Test
 fun toSagaEntity() {
  val fechaIni = Date(1688205600000L)
  val fechaFi = Date(1690924200000L)

  val request = SagaRequest(
   nombre = "Saga de Prueba",
   descripcion = "Descripción detallada",
   isAcabada = true,
   fechaInicio = fechaIni,
   fechaFin = fechaFi,
   imagen = "http://url/imagen.jpg"
  )

  val entidad: Saga = mapper.toSagaEntity(request)

  assertNotNull(entidad.id)
  assertTrue(entidad.id.isNotEmpty())

  assertEquals(request.nombre, entidad.nombre)
  assertEquals(request.descripcion, entidad.descripcion)
  assertEquals(request.isAcabada, entidad.isAcabada)
  assertEquals(request.fechaInicio, entidad.fechaInicio)
  assertEquals(request.fechaFin, entidad.fechaFin)
  assertEquals(request.imagen, entidad.imagen)

  assertNotNull(entidad.producciones)
  assertTrue(entidad.producciones.isEmpty())
 }

 @Test
 fun toSagaResponse() {
  val fechaIni = Date(1688205600000L)
  val fechaFi = Date(1690924200000L)

  val sagaEntity = Saga(
   id = "saga001",
   nombre = "Saga Existente",
   descripcion = "Descripción existente",
   isAcabada = false,
   fechaInicio = fechaIni,
   fechaFin = fechaFi,
   imagen = "http://ruta/imagen.png",
   producciones = mutableListOf()
  )

  val response: SagaResponse = mapper.toSagaResponse(sagaEntity)

  assertEquals("saga001", response.id)
  assertEquals("Saga Existente", response.nombre)
  assertEquals("Descripción existente", response.descripcion)
  assertEquals(fechaIni.toString(), response.fechaInicio)
  assertEquals(fechaFi.toString(), response.fechaFin)
  assertEquals("http://ruta/imagen.png", response.imagen)
  assertFalse(response.isAcabada)

  assertNotNull(response.producciones)
  assertTrue(response.producciones.isEmpty())
 }

 @Test
 fun toSagaResponseConProducciones() {
  val produccion1 = Produccion(
   id = "p1",
   titulo = "Película 1",
   tipo = TipoProduccion.PELICULA,
   estreno = Date(1609459200000L),
   duracion = 100,
   sinopsis = "Sinopsis 1",
   imagen = null,
   informacion = null,
   puntuacion = 7.5,
   categorias = mutableSetOf(),
   clasificacionEdad = ClasificacionEdad.MAYORES_12
  )
  val produccion2 = Produccion(
   id = "p2",
   titulo = "Película 2",
   tipo = TipoProduccion.PELICULA,
   estreno = Date(1612137600000L),
   duracion = 120,
   sinopsis = "Sinopsis 2",
   imagen = null,
   informacion = null,
   puntuacion = 8.0,
   categorias = mutableSetOf(),
   clasificacionEdad = ClasificacionEdad.MAYORES_12
  )

  val fechaIni = Date(1688205600000L)
  val sagaEntity = Saga(
   id = "sagaABC",
   nombre = "Saga con Producciones",
   descripcion = "Descripción detallada de saga",
   isAcabada = true,
   fechaInicio = fechaIni,
   fechaFin = null,
   imagen = null,
   producciones = mutableListOf(produccion1, produccion2)
  )

  val dto1 = ProduccionResponse(
   id = "p1",
   titulo = "Película 1",
   sinopsis = "Sinopsis 1",
   estreno = produccion1.estreno,
   duracion = 100,
   imagen = null,
   informacion = null,
   puntuacion = 7.5,
   categorias = emptySet(),
   tipo = TipoProduccion.PELICULA,
   clasificacionEdad = ClasificacionEdad.MAYORES_12.valorNumerico
  )
  val dto2 = ProduccionResponse(
   id = "p2",
   titulo = "Película 2",
   sinopsis = "Sinopsis 2",
   estreno = produccion2.estreno,
   duracion = 120,
   imagen = null,
   informacion = null,
   puntuacion = 8.0,
   categorias = emptySet(),
   clasificacionEdad = ClasificacionEdad.MAYORES_12.valorNumerico,
   tipo = TipoProduccion.PELICULA
  )

  whenever(produccionMapper.toResponse(eq(produccion1))).thenReturn(dto1)
  whenever(produccionMapper.toResponse(eq(produccion2))).thenReturn(dto2)

  val response: SagaResponse = mapper.toSagaResponse(sagaEntity)

  assertEquals("sagaABC", response.id)
  assertEquals("Saga con Producciones", response.nombre)
  assertEquals("Descripción detallada de saga", response.descripcion)
  assertEquals(fechaIni.toString(), response.fechaInicio)
  assertNull(response.fechaFin)   // coincide con entidad que tenía fechaFin = null
  assertNull(response.imagen)
  assertTrue(response.isAcabada)

  assertEquals(2, response.producciones.size)
  assertEquals(dto1, response.producciones[0])
  assertEquals(dto2, response.producciones[1])
 }
}
