package org.wolve.geofilm.producciones.produccion.service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.wolve.geofilm.producciones.produccion.exceptions.ProduccionNotFoundException
import org.wolve.geofilm.producciones.produccion.mapper.ProduccionMapper
import org.wolve.geofilm.producciones.produccion.repository.ProduccionRepository
import java.util.*

@ExtendWith(MockitoExtension::class)
class ProduccionServiceImplTest {

 @Mock
 private lateinit var repository: ProduccionRepository

 @Mock
 private lateinit var mapper: ProduccionMapper

 @InjectMocks
 private lateinit var service: ProduccionServiceImpl

 /*@Test
 fun `getProduccionById should return produccion when found`() {
  val id = "test-id"
  val produccion = Produccion(id = id)
  val response = ProduccionResponse(id = id)

  `when`(repository.findById(id)).thenReturn(Optional.of(produccion))
  `when`(mapper.toProduccionResponse(produccion)).thenReturn(response)

  val result = service.getProduccionById(id)

  assertNotNull(result)
  assertEquals(id, result?.id)
 }*/

 @Test
 fun `getProduccionById should return null when not found`() {
  val id = "non-existent"

  `when`(repository.findById(id)).thenReturn(Optional.empty())

  val result = service.getProduccionById(id)

  assertNull(result)
 }

 /*@Test
 fun `createProduccion should save and return response`() {
  val request = ProduccionRequest(titulo = "New Movie")
  val produccion = Produccion(titulo = "New Movie")
  val response = ProduccionResponse(id = "new-id", titulo = "New Movie")

  `when`(mapper.toProduccionEntity(request)).thenReturn(produccion)
  `when`(repository.save(produccion)).thenReturn(produccion)
  `when`(mapper.toProduccionResponse(produccion)).thenReturn(response)

  val result = service.createProduccion(request)

  assertEquals("New Movie", result.titulo)
  verify(repository).save(produccion)
 }

 @Test
 fun `updateProduccion should update existing produccion`() {
  val id = "test-id"
  val request = ProduccionRequest(titulo = "Updated Title")
  val existing = Produccion(id = id, titulo = "Original Title")
  val updated = Produccion(id = id, titulo = "Updated Title")
  val response = ProduccionResponse(id = id, titulo = "Updated Title")

  `when`(repository.findById(id)).thenReturn(Optional.of(existing))
  `when`(repository.save(any())).thenReturn(updated)
  `when`(mapper.toProduccionResponse(updated)).thenReturn(response)

  val result = service.updateProduccion(id, request)

  assertNotNull(result)
  assertEquals("Updated Title", result?.titulo)
 }

 @Test
 fun `updateProduccion should return null for non-existent produccion`() {
  val id = "non-existent"
  val request = ProduccionRequest(titulo = "Updated Title")

  `when`(repository.findById(id)).thenReturn(Optional.empty())

  val result = service.updateProduccion(id, request)

  assertNull(result)
 }*/

 @Test
 fun `deleteProduccion should delete existing produccion`() {
  val id = "test-id"

  service.deleteProduccion(id)

  verify(repository).deleteById(id)
 }

/* @Test
 fun `filtrarProducciones should apply filters correctly`() {
  val produccion1 = Produccion(
   titulo = "Action Movie",
   tipo = TipoProduccion.PELICULA,
   estreno = Date(),
   duracion = 120,
   categorias = mutableSetOf(Categoria.ACCION),
   clasificacionEdad = ClasificacionEdad.MAYORES_12
  )

  val produccion2 = Produccion(
   titulo = "Comedy Series",
   tipo = TipoProduccion.SERIE,
   estreno = Date(),
   duracion = 30,
   categorias = mutableSetOf(Categoria.COMEDIA),
   clasificacionEdad = ClasificacionEdad.TODOS_LOS_PUBLICOS
  )

  `when`(repository.findAll()).thenReturn(listOf(produccion1, produccion2))
  `when`(mapper.toProduccionResponse(produccion1)).thenReturn(
   ProduccionResponse(id = "1", titulo = "Action Movie")
  )

  val result = service.filtrarProducciones(
   titulo = "action",
   tipo = TipoProduccion.PELICULA,
   estrenoDesde = null,
   estrenoHasta = null,
   categorias = setOf(Categoria.ACCION),
   clasificacionEdad = ClasificacionEdad.MAYORES_12,
   duracionMin = 100,
   duracionMax = 150,
   page = 0,
   size = 10,
   sortBy = listOf("titulo"),
   sortDirection = "asc"
  )

  assertEquals(1, result.totalItems)
  assertEquals("Action Movie", result.data[0].titulo)
 }*/

 @Test
 fun `findEntityById should throw exception when not found`() {
  val id = "non-existent"

  `when`(repository.findById(id)).thenReturn(Optional.empty())

  assertThrows(ProduccionNotFoundException::class.java) {
   service.findEntityById(id)
  }
 }

 @Test
 fun `should return all enum values`() {
  assertTrue(service.getClasificacionesEdad().isNotEmpty())
  assertTrue(service.getCategoriasDisponibles().isNotEmpty())
  assertTrue(service.getTiposProduccion().isNotEmpty())
 }
}