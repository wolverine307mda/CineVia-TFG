/*package org.wolve.geofilm.producciones.produccion.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.wolve.geofilm.auth.services.jwt.JwtAuthenticationFilter
import org.wolve.geofilm.auth.services.jwt.JwtService
import org.wolve.geofilm.producciones.produccion.dto.ProduccionCompletaResponse
import org.wolve.geofilm.producciones.produccion.dto.ProduccionRequest
import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import org.wolve.geofilm.producciones.produccion.models.Categoria
import org.wolve.geofilm.producciones.produccion.models.ClasificacionEdad
import org.wolve.geofilm.producciones.produccion.models.TipoProduccion
import org.wolve.geofilm.producciones.produccion.service.IProduccionService
import org.wolve.geofilm.users.repositories.UsuarioRepository
import org.wolve.geofilm.utils.pagination.PaginatedResponse
import org.wolve.geofilm.utils.storage.images.FirebaseStorageService
import java.util.*

import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.test.context.ActiveProfiles
import javax.sql.DataSource

@ActiveProfiles("test")
@WebMvcTest(
    controllers = [ProduccionController::class],
    excludeAutoConfiguration = [
        SecurityFilterAutoConfiguration::class,
        DataSourceAutoConfiguration::class,
        HibernateJpaAutoConfiguration::class,
        FlywayAutoConfiguration::class
    ]
)
@AutoConfigureMockMvc(addFilters = false)
class ProduccionControllerTest {

    @Autowired private lateinit var mockMvc: MockMvc
    @Autowired private lateinit var objectMapper: ObjectMapper

    // mocks de seguridad (aunque no los uses en slice-tests permitAll)
    @MockkBean private lateinit var jwtService: JwtService
    @MockkBean private lateinit var usuarioRepository: UsuarioRepository
    @MockkBean private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter

    // mocks de flyway/datasource para que no arranque nada de DB
    @MockkBean private lateinit var dataSource: DataSource
    @MockkBean private lateinit var flyway: Flyway

    // mock de tu servicio y de Firebase
    @MockkBean private lateinit var produccionService: IProduccionService
    @MockkBean private lateinit var firebaseStorageService: FirebaseStorageService

    private lateinit var sampleResponse: ProduccionResponse
    private lateinit var sampleCompleta: ProduccionCompletaResponse
    private lateinit var paginated: PaginatedResponse<ProduccionResponse>

    @BeforeEach
    fun setup() {
        val now = Date()
        sampleResponse = ProduccionResponse(
            id = "id1",
            titulo = "Titulo1",
            tipo = TipoProduccion.PELICULA,
            estreno = now,
            duracion = 100,
            sinopsis = "sinopsis",
            imagen = null,
            informacion = null,
            puntuacion = 7.5,
            categorias = emptySet(),
            clasificacionEdad = ClasificacionEdad.MAYORES_12.valorNumerico
        )
        sampleCompleta = ProduccionCompletaResponse(
            id = "id1",
            titulo = "Titulo1",
            tipo = TipoProduccion.PELICULA.name,
            estreno = now.toString(),
            duracion = 100,
            sinopsis = "sinopsis",
            imagen = null,
            informacion = null,
            puntuacion = 7.5,
            clasificacionEdad = ClasificacionEdad.MAYORES_12.valorNumerico,
            categorias = emptySet(),
            saga = null,
            participaciones = emptyList(),
            rodajes = emptyList()
        )
        paginated = PaginatedResponse(
            data       = listOf(sampleResponse),
            totalItems = 1,
            totalPages = 1,
            currentPage= 0,
            pageSize   = 10
        )
    }

    @Test
    fun getProduccionesPaginated200Ok() {
        whenever(produccionService.filtrarProducciones(
            any(), any(), any(), any(), any(), any(), any(), any(),
            eq(0), eq(10), any<List<String>>(), any<String>()
        )).thenReturn(paginated)

        mockMvc.perform(get("/api/producciones")
            .param("page", "0").param("size", "10")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.length()" ).value(1))
            .andExpect(jsonPath("$.data[0].id").value("id1"))

        verify(produccionService).getAllProducciones(0, 10, listOf("titulo"), "asc")
    }

    @Test
    fun getProduccionById200Ok() {
        whenever(produccionService.getProduccionById("id1"))
            .thenReturn(sampleResponse)

        mockMvc.perform(get("/api/producciones/{id}", "id1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value("id1"))

        verify(produccionService).getProduccionById("id1")
    }

    @Test
    fun getProduccionById404NotFound() {
        whenever(produccionService.getProduccionById("missing"))
            .thenReturn(null)

        mockMvc.perform(get("/api/producciones/missing"))
            .andExpect(status().isNotFound)

        verify(produccionService).getProduccionById("missing")
    }

    @Test
    fun createProduccion201Created() {
        val req = ProduccionRequest(
            titulo = "Nueva",
            tipo = TipoProduccion.SERIE,
            estreno = Date(),
            duracion = 90,
            sinopsis = "desc",
            imagen = null,
            informacion = null,
            puntuacion = 6.0,
            categorias = emptySet(),
            clasificacionEdad = ClasificacionEdad.MAYORES_7.valorNumerico
        )
        whenever(produccionService.createProduccion(any()))
            .thenReturn(sampleResponse.copy(id = "idNew"))

        mockMvc.perform(post("/api/producciones")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "/api/producciones/idNew"))
            .andExpect(jsonPath("$.id").value("idNew"))

        verify(produccionService).createProduccion(any())
    }

    @Test
    fun createProduccion400BadRequest() {
        val invalidProduccion = """
        {
            "titulo": null,
            "tipo": "INVALIDO"
        }
    """.trimIndent()

        mockMvc.perform(
            post("/api/producciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidProduccion)
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun updateProduccion200Ok() {
        whenever(produccionService.updateProduccion("id1", any()))
            .thenReturn(sampleResponse)

        mockMvc.perform(put("/api/producciones/{id}", "id1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reqFromResponse(sampleResponse))))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value("id1"))

        verify(produccionService).updateProduccion("id1", any())
    }

    @Test
    fun updateProduccion404NotFound() {
        whenever(produccionService.updateProduccion("none", any()))
            .thenReturn(null)

        mockMvc.perform(put("/api/producciones/{id}", "none")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reqFromResponse(sampleResponse))))
            .andExpect(status().isNotFound)

        verify(produccionService).updateProduccion("none", any())
    }

    @Test
    fun deleteProduccion204NoContent() {
        mockMvc.perform(delete("/api/producciones/{id}", "id1"))
            .andExpect(status().isNoContent)

        verify(produccionService).deleteProduccion("id1")
    }

    @Test
    fun deleteProduccion400BadRequest() {
        whenever(produccionService.deleteProduccion("bad"))
            .thenThrow(IllegalArgumentException("Bad ID"))

        mockMvc.perform(delete("/api/producciones/{id}", "bad"))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun deleteProduccion500InternalError() {
        whenever(produccionService.deleteProduccion("err"))
            .thenThrow(RuntimeException("Error"))

        mockMvc.perform(delete("/api/producciones/{id}", "err"))
            .andExpect(status().isInternalServerError)
    }

    @Test
    fun filtrarProducciones200Ok() {
        whenever(produccionService.filtrarProducciones(
            any(), any(), any(), any(), any(), any(), any(), any(),
            eq(0), eq(10), any<List<String>>(), any<String>()
        )).thenReturn(paginated)

        mockMvc.perform(get("/api/producciones/filtrar")
            .param("page", "0").param("size", "10")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.length()").value(1))

        verify(produccionService).filtrarProducciones(
            any(), any(), any(), any(), any(), any(), any(), any(), 0, 10, listOf("titulo"), "asc"
        )
    }

    @Test
    fun getCategorias200Ok() {
        whenever(produccionService.getCategoriasDisponibles())
            .thenReturn(Categoria.values().toList())

        mockMvc.perform(get("/api/producciones/categorias"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(Categoria.values().size))

        verify(produccionService).getCategoriasDisponibles()
    }

    @Test
    fun getClasificaciones200Ok() {
        whenever(produccionService.getClasificacionesEdad())
            .thenReturn(ClasificacionEdad.values().toList())

        mockMvc.perform(get("/api/producciones/clasificaciones-edad"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(ClasificacionEdad.values().size))

        verify(produccionService).getClasificacionesEdad()
    }

    @Test
    fun getTipos200Ok() {
        whenever(produccionService.getTiposProduccion())
            .thenReturn(TipoProduccion.values().toList())

        mockMvc.perform(get("/api/producciones/tipos"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(TipoProduccion.values().size))

        verify(produccionService).getTiposProduccion()
    }

    @Test
    fun getProduccionCompleta200Ok() {
        whenever(produccionService.findProduccionCompletaById("id1"))
            .thenReturn(sampleCompleta)

        mockMvc.perform(get("/api/producciones/completa/{id}", "id1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value("id1"))

        verify(produccionService).findProduccionCompletaById("id1")
    }

    @Test
    fun getProduccionCompleta404NotFound() {
        whenever(produccionService.findProduccionCompletaById("none"))
            .thenReturn(null)

        mockMvc.perform(get("/api/producciones/completa/{id}", "none"))
            .andExpect(status().isNotFound)

        verify(produccionService).findProduccionCompletaById("none")
    }

    // Helper to map a ProduccionResponse to ProduccionRequest for update
    private fun reqFromResponse(resp: ProduccionResponse): ProduccionRequest = ProduccionRequest(
        titulo = resp.titulo,
        tipo = resp.tipo,
        estreno = resp.estreno,
        duracion = resp.duracion,
        sinopsis = resp.sinopsis,
        imagen = resp.imagen,
        informacion = resp.informacion,
        puntuacion = resp.puntuacion,
        categorias = resp.categorias,
        clasificacionEdad = resp.clasificacionEdad
    )
}*/
