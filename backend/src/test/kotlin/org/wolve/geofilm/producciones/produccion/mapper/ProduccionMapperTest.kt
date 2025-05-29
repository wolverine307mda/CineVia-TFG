package org.wolve.geofilm.producciones.produccion.mapper

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.wolve.geofilm.producciones.produccion.models.*
import java.util.*

class ProduccionMapperTest {

 private val mapper = ProduccionMapper()

 @Test
 fun `should map Produccion to ProduccionResponse`() {
  val produccion = Produccion(
   id = "test-id",
   titulo = "Test Movie",
   tipo = TipoProduccion.PELICULA,
   estreno = Date(),
   duracion = 120,
   sinopsis = "Test synopsis",
   imagen = "test.jpg",
   informacion = "Test info",
   puntuacion = 8.5,
   categorias = mutableSetOf(Categoria.ACCION, Categoria.AVENTURA),
   clasificacionEdad = ClasificacionEdad.MAYORES_12
  )

  val response = mapper.toProduccionResponse(produccion)

  assertEquals(produccion.id, response.id)
  assertEquals(produccion.titulo, response.titulo)
  assertEquals(produccion.tipo, response.tipo)
  assertEquals(produccion.estreno, response.estreno)
  assertEquals(produccion.duracion, response.duracion)
  assertEquals(produccion.sinopsis, response.sinopsis)
  assertEquals(produccion.imagen, response.imagen)
  assertEquals(produccion.informacion, response.informacion)
  assertEquals(produccion.puntuacion, response.puntuacion)
  assertEquals(produccion.categorias, response.categorias)
  assertEquals(produccion.clasificacionEdad.valorNumerico, response.clasificacionEdad)
 }
}