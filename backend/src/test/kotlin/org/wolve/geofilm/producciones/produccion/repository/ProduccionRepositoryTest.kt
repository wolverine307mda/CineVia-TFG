package org.wolve.geofilm.producciones.produccion.repository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.wolve.geofilm.producciones.produccion.models.Categoria
import org.wolve.geofilm.producciones.produccion.models.ClasificacionEdad
import org.wolve.geofilm.producciones.produccion.models.Produccion
import org.wolve.geofilm.producciones.produccion.models.TipoProduccion
import java.util.*

@DataJpaTest
class ProduccionRepositoryTest {

    @Autowired
    private lateinit var repo: ProduccionRepository

    private fun createSample(id: String = UUID.randomUUID().toString()): Produccion {
        val p = Produccion(
            id = id,
            titulo = "Sample",
            tipo = TipoProduccion.PELICULA,
            estreno = Date(),
            duracion = 100,
            sinopsis = "Desc",
            imagen = null,
            informacion = null,
            puntuacion = 7.5,
            categorias = mutableSetOf(Categoria.ACCION),
            clasificacionEdad = ClasificacionEdad.TODOS_LOS_PUBLICOS
        )
        return repo.save(p)
    }

    @Test
    fun saveAndFindById() {
        val saved = createSample()
        val found = repo.findById(saved.id).orElse(null)
        assertNotNull(found)
        assertEquals(saved.id, found.id)
    }

    @Test
    fun deleteById() {
        val saved = createSample()
        repo.deleteById(saved.id)
        assertTrue(repo.findById(saved.id).isEmpty)
    }

    @Test
    fun countParticipacionesById_empty() {
        val saved = createSample()
        val count = repo.countParticipacionesById(saved.id)
        assertEquals(0L, count)
    }

    @Test
    fun filterByTitulo() {
        createSample(id = "1").apply { titulo = "First" }.let { repo.save(it) }
        createSample(id = "2").apply { titulo = "Second" }.let { repo.save(it) }
        val spec: Specification<Produccion> = Specification { root, _, cb -> cb.like(root.get("titulo"), "%First%") }
        val page = repo.findAll(spec, PageRequest.of(0, 10))
        assertEquals(1, page.totalElements)
        assertEquals("First", page.content[0].titulo)
    }

    @Test
    fun findCompletaById() {
        val saved = createSample()
        // Assuming fetch join does not break basic retrieval
        val complete = repo.findCompletaById(saved.id)
        assertNotNull(complete)
        assertEquals(saved.id, complete.id)
    }
}
