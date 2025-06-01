package org.wolve.geofilm.producciones.produccion.mapper

import org.springframework.data.domain.Page
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.wolve.geofilm.producciones.produccion.dto.ProduccionCompletaResponse
import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import org.wolve.geofilm.producciones.produccion.dto.ProduccionRequest
import org.wolve.geofilm.producciones.produccion.models.Categoria
import org.wolve.geofilm.producciones.produccion.models.ClasificacionEdad
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.produccion.models.TipoProduccion
import org.wolve.geofilm.utils.pagination.PaginationUtils
import java.util.*

class ProduccionMapperTest {

    private lateinit var mapper: ProduccionMapper

    @BeforeEach
    fun setUp() {
        mapper = ProduccionMapper()
    }

    @Test
    fun toProduccionEntity() {
        val request = ProduccionRequest(
            titulo = "Test",
            sinopsis = "Desc",
            estreno = Date(1000000),
            duracion = 120,
            imagen = "img.jpg",
            informacion = "Info",
            puntuacion = 8.5,
            categorias = setOf(
            Categoria.ACCION,
            Categoria.AVENTURA),
            tipo = TipoProduccion.SERIE,
            clasificacionEdad = ClasificacionEdad.MAYORES_12.valorNumerico
        )
        val entity = mapper.toProduccionEntity(request)

        assertEquals(request.titulo, entity.titulo)
        assertEquals(request.sinopsis, entity.sinopsis)
        assertEquals(request.estreno, entity.estreno)
        assertEquals(request.duracion, entity.duracion)
        assertEquals(request.imagen, entity.imagen)
        assertEquals(request.informacion, entity.informacion)
        assertEquals(request.puntuacion, entity.puntuacion)
        assertTrue(entity.categorias.contains(Categoria.ACCION))
        assertEquals(ClasificacionEdad.MAYORES_12, entity.clasificacionEdad)
    }

    @Test
    fun toProduccionResponse() {
        val entity = Produccion(
            id = "id1",
            titulo = "Test",
            tipo = TipoProduccion.SERIE,
            estreno = Date(2000000),
            duracion = 90,
            sinopsis = "Desc",
            imagen = "img.png",
            informacion = "Info",
            puntuacion = 7.0,
            categorias = mutableSetOf(Categoria.DRAMA, Categoria.ACCION),
            clasificacionEdad = ClasificacionEdad.TODOS_LOS_PUBLICOS
        )
        val response: ProduccionResponse = mapper.toProduccionResponse(entity)

        assertEquals(entity.id, response.id)
        assertEquals(entity.titulo, response.titulo)
        assertEquals(entity.sinopsis, response.sinopsis)
        assertEquals(entity.estreno, response.estreno)
        assertEquals(entity.duracion, response.duracion)
        assertEquals(entity.imagen, response.imagen)
        assertEquals(entity.informacion, response.informacion)
        assertEquals(entity.puntuacion, response.puntuacion)
        assertTrue(response.categorias.contains(Categoria.DRAMA))
        assertTrue(response.categorias.contains(Categoria.ACCION))
        assertEquals(entity.clasificacionEdad.valorNumerico, response.clasificacionEdad)
        assertEquals(entity.tipo, response.tipo)
    }

    @Test
    fun toCompletaResponse() {
        val entity = Produccion(
            id = "id2",
            titulo = "Full",
            tipo = TipoProduccion.PELICULA,
            estreno = Date(3000000),
            duracion = 100,
            sinopsis = "FullDesc",
            imagen = "img2.png",
            informacion = "Info2",
            puntuacion = 9.0,
            categorias = mutableSetOf(),
            clasificacionEdad = ClasificacionEdad.MAYORES_7
        )
        // Assume mapper fills participaciones, rodajes, ubicaciones as empty
        val completa: ProduccionCompletaResponse = mapper.toCompletaResponse(entity)

        assertEquals(entity.id, completa.id)
        assertEquals(entity.titulo, completa.titulo)
        assertEquals(entity.clasificacionEdad.valorNumerico, completa.clasificacionEdad)
        assertNotNull(completa.participaciones)
        assertNotNull(completa.rodajes)
    }

    @Test
    fun toPaginatedResponseConservaPaginationYMapeaContenido() {
        val entidad = Produccion(
            id = "idP",
            titulo = "Título de prueba",
            tipo = TipoProduccion.PELICULA,
            estreno = Date(),
            duracion = 120,
            sinopsis = "Sinopsis de prueba",
            imagen = "url-imagen",
            informacion = "Info adicional",
            puntuacion = 7.5,
            categorias = mutableSetOf(Categoria.ACCION, Categoria.AVENTURA),
            clasificacionEdad = ClasificacionEdad.MAYORES_12
        )

        val pageRequest = PageRequest.of(0, 1)
        val page: Page<Produccion> = PageImpl(listOf(entidad), pageRequest, 10)

        val paginated: PaginationUtils.PaginatedResponse<ProduccionResponse> =
            mapper.toPaginatedResponse(page)

        assertEquals(10, paginated.totalItems)
        assertEquals(10, paginated.totalPages)
        assertEquals(1, paginated.pageSize)

        assertEquals(1, paginated.data.size)
        val resp = paginated.data[0]
        assertEquals(entidad.id, resp.id)
        assertEquals(entidad.titulo, resp.titulo)
        assertEquals(entidad.tipo, resp.tipo)
        assertEquals(entidad.duracion, resp.duracion)
    }

    @Test
    fun mapEnumsFunciona() {
        // Test all TipoProduccion values
        TipoProduccion.values().forEach {
            assertDoesNotThrow { TipoProduccion.valueOf(it.name) }
        }
        // Test all ClasificacionEdad values
        ClasificacionEdad.values().forEach {
            assertDoesNotThrow { ClasificacionEdad.valueOf(it.name) }
        }
        // Test all Categoria values
        Categoria.values().forEach {
            assertDoesNotThrow { Categoria.valueOf(it.name) }
        }
    }

    @Test
    fun produccionToProduccionResponse() {
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
