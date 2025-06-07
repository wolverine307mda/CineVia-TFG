package org.wolve.geofilm.producciones.produccion.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.*
import org.flywaydb.core.Flyway
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
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
import org.wolve.geofilm.utils.pagination.PaginationUtils
import org.wolve.geofilm.utils.storage.images.FirebaseStorageService
import java.util.*
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

    @MockkBean private lateinit var jwtService: JwtService
    @MockkBean private lateinit var usuarioRepository: UsuarioRepository
    @MockkBean private lateinit var jwtAuthenticationFilter: JwtAuthenticationFilter
    @MockkBean private lateinit var dataSource: DataSource
    @MockkBean private lateinit var flyway: Flyway
    @MockkBean private lateinit var produccionService: IProduccionService
    @MockkBean private lateinit var firebaseStorageService: FirebaseStorageService

    private lateinit var sampleResponse: ProduccionResponse
    private lateinit var sampleCompleta: ProduccionCompletaResponse
    private lateinit var paginated: PaginatedResponse<ProduccionResponse>

    @BeforeEach
    fun setup() {
        every { flyway.migrate() } returns mockk()
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
            data = listOf(sampleResponse),
            totalItems = 1,
            totalPages = 1,
            currentPage = 0,
            pageSize = 10
        )
    }

    @Test
    fun getAllProduccionesSinPaginacion() {
        every { produccionService.getAllProducciones() } returns listOf(sampleResponse)

        mockMvc.perform(get("/api/producciones/all").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value("id1"))

        verify { produccionService.getAllProducciones() }
    }

    @Test
    fun getProduccionesPaginado() {
        val sampleData = listOf(sampleResponse.copy(id = "id1"))
        // Crea la respuesta paginada usando la clase interna PaginationUtils.PaginatedResponse
        val paginated = PaginationUtils.PaginatedResponse(
            data = sampleData,
            totalItems = 1L,
            totalPages = 1,
            pageSize = 10,
            currentPage = 0
        )

        every {
            produccionService.getAllProducciones(
                page = 0, size = 10, sortBy = listOf("titulo"), sortDirection = "asc"
            )
        } returns paginated  // Ahora el tipo coincide exactamente

        mockMvc.perform(
            get("/api/producciones")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "titulo")
                .param("sortDirection", "asc")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.length()").value(1))
            .andExpect(jsonPath("$.data[0].id").value("id1"))

        verify { produccionService.getAllProducciones(0, 10, listOf("titulo"), "asc") }
    }


    @Test
    fun getProduccionById() {
        every{produccionService.getProduccionById("id1")}
            .returns(sampleResponse)

        mockMvc.perform(get("/api/producciones/{id}", "id1")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value("id1"))

        verify { produccionService.getProduccionById("id1") }
    }

    @Test
    fun getProduccionByIdNotFound() {
        every {produccionService.getProduccionById("missing")}
            .returns(null)

        mockMvc.perform(get("/api/producciones/missing"))
            .andExpect(status().isNotFound)

        verify {produccionService.getProduccionById("missing") }
    }

    @Test
    fun createProduccion() {
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
        val returned = sampleResponse.copy(id = "idNew")

        every { produccionService.createProduccion(any<ProduccionRequest>()) } returns returned

        mockMvc.perform(
            post("/api/producciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "/api/producciones/idNew"))
            .andExpect(jsonPath("$.id").value("idNew"))

        verify { produccionService.createProduccion(any<ProduccionRequest>()) }
    }


    @Test
    fun createProduccionBadRequest() {
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
    fun updateProduccion() {
        every {produccionService.updateProduccion("id1", any())}
            .returns(sampleResponse)

        mockMvc.perform(put("/api/producciones/{id}", "id1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reqFromResponse(sampleResponse))))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value("id1"))

        verify {produccionService.updateProduccion("id1", any()) }
    }

    @Test
    fun updateProduccionNotFound() {
        val req = ProduccionRequest(
            titulo = "Nada",
            tipo = TipoProduccion.PELICULA,
            estreno = Date(),
            duracion = 120,
            sinopsis = "nueva",
            imagen = null,
            informacion = null,
            puntuacion = 8.0,
            categorias = emptySet(),
            clasificacionEdad = ClasificacionEdad.MAYORES_12.valorNumerico
        )

        every { produccionService.updateProduccion("none", any<ProduccionRequest>()) } returns null

        mockMvc.perform(
            put("/api/producciones/none")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        )
            .andExpect(status().isNotFound)

        verify { produccionService.updateProduccion("none", any<ProduccionRequest>()) }
    }

    @Test
    fun deleteProduccionBadRequest() {
        every { produccionService.deleteProduccion("bad") } throws
                IllegalArgumentException("Bad ID")

        mockMvc.perform(delete("/api/producciones/bad"))
            .andExpect(status().isBadRequest)

        verify { produccionService.deleteProduccion("bad") }
    }

    @Test
    fun deleteProduccionNoContent() {
        every { produccionService.deleteProduccion("id1") } just Runs

        mockMvc.perform(delete("/api/producciones/id1"))
            .andExpect(status().isNoContent)

        verify { produccionService.deleteProduccion("id1") }
    }

    @Test
    fun deleteProduccionInternalServerError() {
        every { produccionService.deleteProduccion("id1") } throws RuntimeException("Error")

        mockMvc.perform(delete("/api/producciones/id1"))
            .andExpect(status().isInternalServerError)

        verify { produccionService.deleteProduccion("id1") }
    }

    @Test
    fun filtrarProducciones() {
        every {
            produccionService.filtrarProducciones(
                titulo = "Te",
                tipo = TipoProduccion.SERIE,
                estrenoDesde = 2025,
                estrenoHasta = 2025,
                categorias = setOf(Categoria.DRAMA),
                clasificacionEdad = ClasificacionEdad.MAYORES_12,
                duracionMin = 50,
                duracionMax = 150,
                page = 0,
                size = 5,
                sortBy = listOf("titulo"),
                sortDirection = "asc"
            )
        } returns paginated.copy(totalItems = 5)

        mockMvc.perform(
            get("/api/producciones/filtrar")
                .param("titulo", "Te")
                .param("tipo", "SERIE")
                .param("estrenoDesde", "2025")
                .param("estrenoHasta", "2025")
                .param("categorias", "DRAMA")
                .param("clasificacionEdad", "MAYORES_12")
                .param("duracionMin", "50")
                .param("duracionMax", "150")
                .param("page", "0")
                .param("size", "5")
                .param("sortBy", "titulo")
                .param("sortDirection", "asc")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.length()").value(1))
            .andExpect(jsonPath("$.data[0].id").value("id1"))

        verify {
            produccionService.filtrarProducciones(
                titulo = "Te",
                tipo = TipoProduccion.SERIE,
                estrenoDesde = 2025,
                estrenoHasta = 2025,
                categorias = setOf(Categoria.DRAMA),
                clasificacionEdad = ClasificacionEdad.MAYORES_12,
                duracionMin = 50,
                duracionMax = 150,
                page = 0,
                size = 5,
                sortBy = listOf("titulo"),
                sortDirection = "asc"
            )
        }
    }

    @Test
    fun getCategorias() {
        every{produccionService.getCategoriasDisponibles()}
            .returns(Categoria.values().toList())

        mockMvc.perform(get("/api/producciones/categorias"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(Categoria.values().size))

        verify {produccionService.getCategoriasDisponibles() }
    }

    @Test
    fun getClasificaciones() {
        every{produccionService.getClasificacionesEdad()}
            .returns(ClasificacionEdad.values().toList())

        mockMvc.perform(get("/api/producciones/clasificaciones-edad"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(ClasificacionEdad.values().size))

        verify { produccionService.getClasificacionesEdad() }
    }

    @Test
    fun getTipos() {
        every{produccionService.getTiposProduccion()}
            .returns(TipoProduccion.values().toList())

        mockMvc.perform(get("/api/producciones/tipos"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(TipoProduccion.entries.size))

        verify { produccionService.getTiposProduccion() }
    }

    @Test
    fun getProduccionCompleta() {
        every{produccionService.findProduccionCompletaById("id1")}
            .returns(sampleCompleta)

        mockMvc.perform(get("/api/producciones/completa/{id}", "id1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value("id1"))

        verify { produccionService.findProduccionCompletaById("id1") }
    }

    @Test
    fun getProduccionCompletaNotFound() {
        every{produccionService.findProduccionCompletaById("none")}
            .returns(null)

        mockMvc.perform(get("/api/producciones/completa/{id}", "none"))
            .andExpect(status().isNotFound)

        verify {produccionService.findProduccionCompletaById("none")}
    }

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
}
