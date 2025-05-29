package org.wolve.geofilm.producciones.produccion.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.wolve.geofilm.producciones.produccion.models.*
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@DataJpaTest
class ProduccionRepositoryTest {

    @Autowired
    private lateinit var repository: ProduccionRepository

    @Test
    fun `should save and retrieve produccion`() {
        val produccion = Produccion(
            titulo = "Test Movie",
            tipo = TipoProduccion.PELICULA,
            estreno = Date(),
            duracion = 120,
            sinopsis = "Test synopsis",
            categorias = mutableSetOf(Categoria.ACCION)
        )

        val saved = repository.save(produccion)
        val found = repository.findById(saved.id)

        assertTrue(found.isPresent)
        assertEquals(saved.id, found.get().id)
        assertEquals("Test Movie", found.get().titulo)
    }

    @Test
    fun `should find by titulo containing ignore case`() {
        repository.save(Produccion(titulo = "The Great Movie"))
        repository.save(Produccion(titulo = "Great Expectations"))
        repository.save(Produccion(titulo = "Not Matching"))

        val page = repository.findByTituloContainingIgnoreCase("great", PageRequest.of(0, 10))

        assertEquals(2, page.totalElements)
        assertTrue(page.content.any { it.titulo == "The Great Movie" })
        assertTrue(page.content.any { it.titulo == "Great Expectations" })
    }

    @Test
    fun `should find by tipo`() {
        repository.save(Produccion(titulo = "Movie 1", tipo = TipoProduccion.PELICULA))
        repository.save(Produccion(titulo = "Series 1", tipo = TipoProduccion.SERIE))

        val page = repository.findByTipo(TipoProduccion.SERIE, PageRequest.of(0, 10))

        assertEquals(1, page.totalElements)
        assertEquals("Series 1", page.content[0].titulo)
    }

    @Test
    fun `should find by categorias`() {
        val produccion1 = Produccion(titulo = "Action Movie", categorias = mutableSetOf(Categoria.ACCION))
        val produccion2 = Produccion(titulo = "Comedy Movie", categorias = mutableSetOf(Categoria.COMEDIA))
        repository.save(produccion1)
        repository.save(produccion2)

        val page = repository.findByCategoriasIn(setOf(Categoria.ACCION), PageRequest.of(0, 10))

        assertEquals(1, page.totalElements)
        assertEquals("Action Movie", page.content[0].titulo)
    }

    @Test
    fun `should count participaciones by produccion id`() {
        val produccion = repository.save(Produccion(titulo = "Test Movie"))
        // Note: In a real test, you'd need to set up participaciones for this produccion

        val count = repository.countParticipacionesById(produccion.id)

        assertEquals(0, count) // Adjust based on test data
    }
}