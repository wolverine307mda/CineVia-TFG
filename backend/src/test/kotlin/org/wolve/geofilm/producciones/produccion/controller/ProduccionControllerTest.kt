package org.wolve.geofilm.producciones.produccion.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.wolve.geofilm.producciones.produccion.dto.ProduccionRequest
import org.wolve.geofilm.producciones.produccion.dto.ProduccionResponse
import org.wolve.geofilm.producciones.produccion.service.ProduccionService
import java.util.*

@WebMvcTest(controllers = [ProduccionController::class])
class ProduccionControllerTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    private lateinit var produccionService: ProduccionService

    @Test
    fun getProducciones() {
        val resp = listOf(ProduccionResponse("id1", "T1", "desc1", Date(), 90, null, null, 7.0, emptyList(), null))
        every { produccionService.getAll(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns resp

        mockMvc.perform(get("/api/producciones")
            .param("page", "0").param("size", "10")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(resp.size))
            .andExpect(jsonPath("$[0].id").value("id1"))

        verify { produccionService.getAll( any(), any(), any(), any(), any(), any(), any(), any(), 0, 10, any() ) }
    }

    @Test
    fun getProduccionById() {
        val id = "id2"
        val response = ProduccionResponse(id, "T2", "desc2", Date(), 100, null, null, 8.5, emptyList(), null)
        every { produccionService.getById(id) } returns response

        mockMvc.perform(get("/api/producciones/{id}", id)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.titulo").value("T2"))

        verify { produccionService.getById(id) }
    }

    @Test
    fun getProduccionNotFound() {
        val id = "missing"
        every { produccionService.getById(id) } throws NoSuchElementException()

        mockMvc.perform(get("/api/producciones/{id}", id))
            .andExpect(status().isNotFound)
    }

    @Test
    fun createdProduccion() {
        val req = ProduccionRequest("T3", "desc3", Date(), 80, null, null, 6.5, emptyList(), null)
        val saved = ProduccionResponse("id3", "T3", "desc3", req.estreno, 80, null, null, 6.5, emptyList(), null)
        every { produccionService.create(req) } returns saved

        mockMvc.perform(post("/api/producciones")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"titulo\":\"T3\",\"sinopsis\":\"desc3\",\"estreno\":\"2025-01-01T00:00:00.000Z\",\"duracion\":80,\"puntuacion\":6.5 }")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "/api/producciones/id3"))
            .andExpect(jsonPath("$.id").value("id3"))

        verify { produccionService.create(any()) }
    }

    @Test
    fun updateProduccion() {
        val id = "id4"
        val req = ProduccionRequest("T4", "desc4", Date(), 110, null, null, 9.0, emptyList(), null)
        val resp = ProduccionResponse(id, "T4", "desc4", req.estreno, 110, null, null, 9.0, emptyList(), null)
        every { produccionService.update(id, req) } returns resp

        mockMvc.perform(put("/api/producciones/{id}", id)
            .contentType(MediaType.APPLICATION_JSON)
            .content("{ \"titulo\":\"T4\",\"sinopsis\":\"desc4\",\"estreno\":\"2025-01-02T00:00:00.000Z\",\"duracion\":110,\"puntuacion\":9.0 }")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(id))

        verify { produccionService.update(id, any()) }
    }

    @Test
    fun eliminarProduccion() {
        val id = "id5"
        every { produccionService.delete(id) } returns Unit

        mockMvc.perform(delete("/api/producciones/{id}", id))
            .andExpect(status().isNoContent)
        verify { produccionService.delete(id) }
    }

    @Test
    fun eliminarProduccionNotFound() {
        val id = "missing"
        every { produccionService.delete(id) } throws NoSuchElementException()

        mockMvc.perform(delete("/api/producciones/{id}", id))
            .andExpect(status().isNotFound)
    }

    @Test
    fun getPrduccionesFiltradas() {
        val resp = listOf(ProduccionResponse("f1", "Filtro", "desc", Date(), 95, null, null, 8.0, emptyList(), null))
        every {
            produccionService.getAll(
                titulo = "Filtro",
                tipo = any(),
                estrenoDesde = any(),
                estrenoHasta = any(),
                categorias = any(),
                clasificacionEdad = any(),
                duracionMin = any(),
                duracionMax = any(),
                page = 0,
                size = 10,
                sortBy = listOf("titulo")
            )
        } returns resp

        mockMvc.perform(
            get("/api/producciones")
                .param("titulo", "Filtro")
                .param("sortBy", "titulo")
                .param("page", "0").param("size", "10")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].id").value("f1"))
    }

    @Test
    fun crearProduccionInvalida() {
        mockMvc.perform(
            post("/api/producciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ invalid json }")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun ActualizarProduccionNotFound() {
        val id = "none"
        every { produccionService.update(id, any()) } throws NoSuchElementException()

        mockMvc.perform(
            put("/api/producciones/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"titulo\":\"X\" }")
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun getProduccionesListaVacia() {
        every { produccionService.getAll(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()) } returns emptyList()

        mockMvc.perform(
            get("/api/producciones")
                .param("page", "0").param("size", "10")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().json("[]"))
    }

    @Test
    fun deleteProduccionInvalida() {
        val id = "bad"
        every { produccionService.delete(id) } throws IllegalArgumentException("Bad ID")

        mockMvc.perform(delete("/api/producciones/{id}", id))
            .andExpect(status().isBadRequest)
    }

    @Test
    fun getTiposProducciones() {
        val tipos = TipoProduccion.values().map { it.name }
        every { produccionService.getTiposProduccion() } returns TipoProduccion.values().toList()

        mockMvc.perform(get("/api/producciones/tipos")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(tipos.size))
        verify { produccionService.getTiposProduccion() }
    }

    @Test
    fun getListaDeCategorias() {
        val cats = Categoria.values().map { it.name }
        every { produccionService.getCategoriasDisponibles() } returns Categoria.values().toList()

        mockMvc.perform(get("/api/producciones/categorias")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(cats.size))
        verify { produccionService.getCategoriasDisponibles() }
    }

    @Test
    fun getClasificacionesDeEdad() {
        val cls = ClasificacionEdad.values().map { it.name }
        every { produccionService.getClasificacionesEdad() } returns ClasificacionEdad.values().toList()

        mockMvc.perform(get("/api/producciones/clasificaciones")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(cls.size))
        verify { produccionService.getClasificacionesEdad() }
    }

    @Test
    fun getProduccionesById() {
        val id = "comp1"
        val full = mockk<ProduccionCompletaResponse>()
        every { produccionService.findProduccionCompletaById(id) } returns full

        mockMvc.perform(get("/api/producciones/{id}/completa", id)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
        verify { produccionService.findProduccionCompletaById(id) }
    }

    @Test
    fun getProduccionCompletaNotFound() {
        val id = "compX"
        every { produccionService.findProduccionCompletaById(id) } throws NoSuchElementException()

        mockMvc.perform(get("/api/producciones/{id}/completa", id))
            .andExpect(status().isNotFound)
    }


    @Test
    fun `getProduccionesIllegalArgumentException() {
        every { produccionService.getAll(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any()) } throws IllegalArgumentException("Invalid filter")

        mockMvc.perform(
            get("/api/producciones")
                .param("page", "0").param("size", "10")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun getTiposInternalErr() {
        every { produccionService.getTiposProduccion() } throws RuntimeException("DB down")

        mockMvc.perform(get("/api/producciones/tipos"))
            .andExpect(status().isInternalServerError)
    }

    @Test
    fun crearProduccionErr() {
        mockMvc.perform(
            post("/api/producciones")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun updateProduccionErr() {
        val id = "idInvalid"
        mockMvc.perform(
            put("/api/producciones/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("not a json")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun deleteProduccionInternalErr() {
        val id = "errorId"
        every { produccionService.delete(id) } throws RuntimeException("DB error")

        mockMvc.perform(delete("/api/producciones/{id}", id))
            .andExpect(status().isInternalServerError)
    }
}
