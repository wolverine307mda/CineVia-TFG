package org.wolve.geofilm.producciones.produccion.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
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
import org.wolve.geofilm.producciones.saga.exception.SagaNotFoundException
import org.wolve.geofilm.utils.client.TmdbClient
import org.wolve.geofilm.utils.pagination.PaginatedResponse
import java.util.*

@ExtendWith(MockitoExtension::class)
class ProduccionServiceImplTest {

    @Mock
    private lateinit var produccionRepository: ProduccionRepository

    @Mock
    private lateinit var produccionMapper: ProduccionMapper

    @Mock
    private lateinit var tmdbClient: TmdbClient

    private lateinit var service: ProduccionServiceImpl

    @BeforeEach
    fun setUp() {
        service = ProduccionServiceImpl(produccionRepository, produccionMapper, tmdbClient)
    }

    @Test
    fun getProduccionByIdTest() {
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
    fun getProduccionByIdNotFound() {
        whenever(produccionRepository.findById("nope")).thenReturn(Optional.empty())

        val result = service.getProduccionById("nope")

        assertNull(result)
        verify(produccionRepository).findById("nope")
    }

    /*@Test
    fun createProduccion() {
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
    }*/

    @Test
    fun updateProduccionNotFoundReturnsNull() {
        val id = "none"
        val request = mock(ProduccionRequest::class.java)
        whenever(produccionRepository.findById(id)).thenReturn(Optional.empty())

        val result = service.updateProduccion(id, request)

        assertNull(result)
        verify(produccionRepository).findById(id)
    }

    @Test
    fun deleteProduccionTest() {
        service.deleteProduccion("whatever")

        verify(produccionRepository).deleteById("whatever")
    }

    @Test
    fun filtrarProduccionesAllparams() {
        val fecha = Date()
        val entity = Produccion(
            id = "1",
            titulo = "Test",
            tipo = TipoProduccion.SERIE,
            estreno = fecha,
            duracion = 100,
            sinopsis = "s",
            imagen = "i",
            informacion = "inf",
            puntuacion = 6.0,
            categorias = mutableSetOf(Categoria.DRAMA),
            clasificacionEdad = ClasificacionEdad.MAYORES_12
        )
        whenever(produccionRepository.findAll()).thenReturn(listOf(entity))
        val dto = ProduccionResponse(
            id = entity.id,
            titulo = entity.titulo,
            tipo = entity.tipo,
            estreno = entity.estreno,
            duracion = entity.duracion,
            sinopsis = entity.sinopsis,
            imagen = entity.imagen,
            informacion = entity.informacion,
            puntuacion = entity.puntuacion,
            categorias = entity.categorias,
            clasificacionEdad = entity.clasificacionEdad.valorNumerico
        )
        whenever(produccionMapper.toProduccionResponse(entity)).thenReturn(dto)

        val result = service.filtrarProducciones(
            titulo = "Te",
            tipo = TipoProduccion.SERIE,
            estrenoDesde = fecha.year + 1900,
            estrenoHasta = fecha.year + 1900,
            categorias = setOf(Categoria.DRAMA),
            clasificacionEdad = ClasificacionEdad.MAYORES_12,
            duracionMin = 50,
            duracionMax = 150,
            page = 0,
            size = 5,
            sortBy = listOf("titulo"),
            sortDirection = "asc"
        )

        assertEquals(1, result.data.size)
        assertEquals(dto, result.data[0])
        assertEquals(1L, result.totalItems)
        assertEquals(1, result.totalPages)
        assertEquals(0, result.currentPage)
        assertEquals(5, result.pageSize)
        verify(produccionRepository).findAll()
        verify(produccionMapper).toProduccionResponse(entity)
    }

    @Test
    fun getClasificacionesEdad() {
        val enums = ClasificacionEdad.values().toList()
        assertEquals(enums, service.getClasificacionesEdad())
    }

    @Test
    fun getCategoriasDisponibles() {
        val enums = Categoria.values().toList()
        assertEquals(enums, service.getCategoriasDisponibles())
    }

    @Test
    fun getTiposProduccion() {
        val enums = TipoProduccion.values().toList()
        assertEquals(enums, service.getTiposProduccion())
    }

    @Test
    fun findProduccionCompletaById() {
        val id = "full1"
        val fullEntity = mock(Produccion::class.java)
        val fullResponse = mock(ProduccionCompletaResponse::class.java)
        whenever(produccionRepository.findCompletaById(id)).thenReturn(fullEntity)
        whenever(produccionMapper.toCompletaResponse(fullEntity)).thenReturn(fullResponse)

        val result = service.findProduccionCompletaById(id)

        assertEquals(fullResponse, result)
    }

    @Test
    fun findProduccionCompletaByIdNotFoundException() {
        whenever(produccionRepository.findCompletaById("none")).thenReturn(null)

        val result: ProduccionCompletaResponse? = service.findProduccionCompletaById("none")

        assertNull(result)
        verify(produccionRepository).findCompletaById("none")
    }

    @Test
    fun countParticipacionesByProduccionId() {
        val id = "cnt1"
        whenever(produccionRepository.countParticipacionesById(id)).thenReturn(3L)
        assertEquals(3L, service.countParticipacionesByProduccionId(id))
    }

    @Test
    fun countParticipacionesByProduccionIdCero() {
        val id = "cnt0"
        whenever(produccionRepository.countParticipacionesById(id)).thenReturn(0L)
        assertEquals(0L, service.countParticipacionesByProduccionId(id))
    }

    @Test
    fun filtrarProduccionesEmptyList() {
        whenever(produccionRepository.findAll()).thenReturn(emptyList())

        val result = service.filtrarProducciones(
            titulo = "",
            tipo = null,
            estrenoDesde = null,
            estrenoHasta = null,
            categorias = null,
            clasificacionEdad = null,
            duracionMin = null,
            duracionMax = null,
            page = 2,
            size = 4,
            sortBy = listOf(),
            sortDirection = ""
        )

        assertTrue(result.data.isEmpty())
        assertEquals(0L, result.totalItems)
        assertEquals(0, result.totalPages)
        assertEquals(2, result.currentPage)
        assertEquals(4, result.pageSize)
        verify(produccionRepository).findAll()
    }

    @Test
    fun createProduccionNullLanzaException() {
        val exception = assertThrows(NullPointerException::class.java) {
            service.createProduccion(null as ProduccionRequest)
        }
        assertNotNull(exception)
    }

    @Test
    fun updateProduccionExiste() {
        val id = "upd-1"
        val existente = Produccion(
            id = id,
            titulo = "Viejo",
            tipo = TipoProduccion.PELICULA,
            estreno = Date(),
            duracion = 90,
            sinopsis = "Sinopsis",
            imagen = "vieja.png",
            informacion = "info",
            puntuacion = 5.0,
            categorias = mutableSetOf(Categoria.ACCION),
            clasificacionEdad = ClasificacionEdad.MAYORES_12
        )
        val peticion = ProduccionRequest(
            titulo = "Nuevo",
            tipo = TipoProduccion.SERIE,
            estreno = existente.estreno,
            duracion = 120,
            sinopsis = "Nueva sinopsis",
            imagen = "nueva.png",
            informacion = "nueva info",
            puntuacion = 8.0,
            categorias = setOf(Categoria.DRAMA),
            clasificacionEdad = ClasificacionEdad.MAYORES_16.valorNumerico
        )
        val entidadGuardada = existente.copy(
            titulo = peticion.titulo,
            tipo = peticion.tipo,
            duracion = peticion.duracion,
            sinopsis = peticion.sinopsis,
            imagen = peticion.imagen,
            informacion = peticion.informacion,
            puntuacion = peticion.puntuacion,
            categorias = peticion.categorias.toMutableSet(),
            clasificacionEdad = ClasificacionEdad.MAYORES_16
        )
        val respuestaEsperada = mock<ProduccionResponse>()

        whenever(produccionRepository.findById(id)).thenReturn(Optional.of(existente))
        whenever(produccionRepository.save(any())).thenReturn(entidadGuardada)
        whenever(produccionMapper.toProduccionResponse(entidadGuardada)).thenReturn(respuestaEsperada)

        val resultado = service.updateProduccion(id, peticion)

        assertEquals(respuestaEsperada, resultado)
        argumentCaptor<Produccion>().apply {
            verify(produccionRepository).save(capture())
            val capturada = firstValue
            assertEquals(peticion.titulo, capturada.titulo)
            assertEquals(peticion.imagen, capturada.imagen)
            assertEquals(peticion.duracion, capturada.duracion)
            assertEquals(ClasificacionEdad.MAYORES_16, capturada.clasificacionEdad)
        }
    }

    @Test
    fun actualizarImagen() {
        val id = "img1"
        val entidad = Produccion(
            id = id,
            titulo = "Img",
            tipo = TipoProduccion.PELICULA,
            estreno = Date(),
            duracion = 100,
            sinopsis = "s",
            imagen = "antigua.png",
            informacion = "inf",
            puntuacion = 7.0,
            categorias = mutableSetOf(Categoria.ACCION),
            clasificacionEdad = ClasificacionEdad.MAYORES_12
        )
        val respuesta = mock<ProduccionResponse>()
        val nuevaUrl = "nueva.png"

        whenever(produccionRepository.findById(id)).thenReturn(Optional.of(entidad))
        whenever(produccionRepository.save(entidad)).thenReturn(entidad)
        whenever(produccionMapper.toResponse(entidad)).thenReturn(respuesta)

        val resultado = service.actualizarImagen(id, nuevaUrl)

        assertEquals(respuesta, resultado)
        assertEquals(nuevaUrl, entidad.imagen)
        verify(produccionRepository).save(entidad)
        verify(produccionMapper).toResponse(entidad)
    }

    @Test
    fun getAllProduccionesMapeaCorrectamente() {
        val entidad = mock<Produccion>()
        val dto = mock<ProduccionResponse>()

        whenever(produccionRepository.findAll()).thenReturn(listOf(entidad))
        whenever(produccionMapper.toProduccionResponse(entidad)).thenReturn(dto)

        val resultado = service.getAllProducciones()

        assertEquals(listOf(dto), resultado)
        verify(produccionRepository).findAll()
        verify(produccionMapper).toProduccionResponse(entidad)
    }

    @Test
    fun existsByIdTrueYFalse() {
        whenever(produccionRepository.existsById("yes")).thenReturn(true)
        whenever(produccionRepository.existsById("no")).thenReturn(false)

        assertTrue(service.existsById("yes"))
        assertFalse(service.existsById("no"))
    }

    @Test
    fun findEntityByIdDevuelveEntidad() {
        val entidad = mock<Produccion>()
        whenever(produccionRepository.findById("ok")).thenReturn(Optional.of(entidad))

        assertEquals(entidad, service.findEntityById("ok"))
    }

    @Test
    fun findEntityByIdProduccionNotFoundException() {
        whenever(produccionRepository.findById("bad")).thenReturn(Optional.empty())

        assertThrows(ProduccionNotFoundException::class.java) {
            service.findEntityById("bad")
        }
    }

    @Test
    fun filtrarProduccionesEstrenoDescendente() {
        val hoy = Date()
        val ayer = Date(hoy.time - 86_400_000)
        val e1 = Produccion(
            id = "1", titulo = "A", tipo = TipoProduccion.PELICULA,
            estreno = ayer, duracion = 90, sinopsis = "", imagen = "",
            informacion = "", puntuacion = 5.0, categorias = mutableSetOf(),
            clasificacionEdad = ClasificacionEdad.MAYORES_12
        )
        val e2 = e1.copy(id = "2", estreno = hoy, titulo = "B")
        whenever(produccionRepository.findAll()).thenReturn(listOf(e1, e2))
        whenever(produccionMapper.toProduccionResponse(any())).thenAnswer {
            val p: Produccion = it.getArgument(0)
            ProduccionResponse(
                id = p.id, titulo = p.titulo, tipo = p.tipo, estreno = p.estreno,
                duracion = p.duracion, sinopsis = p.sinopsis, imagen = p.imagen,
                informacion = p.informacion, puntuacion = p.puntuacion,
                categorias = p.categorias, clasificacionEdad = p.clasificacionEdad.valorNumerico
            )
        }

        val result = service.filtrarProducciones(
            titulo = null, tipo = null,
            estrenoDesde = null, estrenoHasta = null,
            categorias = null, clasificacionEdad = null,
            duracionMin = null, duracionMax = null,
            page = 0, size = 10,
            sortBy = emptyList(), sortDirection = ""
        )

        assertEquals(2, result.data.size)
        // Al no haber sortBy el estreno más reciente debe ir primero
        assertEquals("2", result.data.first().id)
    }
}