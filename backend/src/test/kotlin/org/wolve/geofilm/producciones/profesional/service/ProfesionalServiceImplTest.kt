package org.wolve.geofilm.producciones.profesional.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalRequest
import org.wolve.geofilm.producciones.profesional.dto.ProfesionalResponse
import org.wolve.geofilm.producciones.profesional.mapper.ProfesionalMapper
import org.wolve.geofilm.producciones.profesional.model.Profesional
import org.wolve.geofilm.producciones.profesional.repository.IProfesionalRepository
import org.wolve.geofilm.utils.pagination.PaginationUtils
import java.text.SimpleDateFormat
import java.util.*

@ExtendWith(MockitoExtension::class)
class ProfesionalServiceTest {

    @Mock
    private lateinit var profesionalRepository: IProfesionalRepository

    @Mock
    private lateinit var profesionalMapper: ProfesionalMapper

    private lateinit var service: ProfesionalServiceImpl

    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd")

    @BeforeEach
    fun setUp() {
    service = ProfesionalServiceImpl(profesionalRepository, profesionalMapper)
    }

    @Test
    fun getProfesionalById() {
        val id = "prof1"
        val entity = Profesional(
            id = id,
            nombre = "Test",
            foto = "foto.png",
            fechaNacimiento = Date(1000000),
            fechaInicio = Date(2000000),
            lugarNacimiento = "Lugar",
            biografia = "Bio",
            participaciones = emptyList()
        )
        val response = ProfesionalResponse(
            id = id,
            nombre = "Test",
            foto = "foto.png",
            fechaNacimiento = entity.fechaNacimiento,
            fechaInicio = entity.fechaInicio,
            lugarNacimiento = "Lugar",
            biografia = "Bio",
            participacionesCount = 0
        )
        whenever(profesionalRepository.findById(id)).thenReturn(Optional.of(entity))
        whenever(profesionalMapper.toResponse(entity)).thenReturn(response)

        val result = service.getProfesionalById(id)

        assertNotNull(result)
        assertEquals(response, result)
        verify(profesionalRepository).findById(id)
        verify(profesionalMapper).toResponse(entity)
    }

    @Test
    fun getProfesionalByIdNotId() {
        val id = "missing"
        whenever(profesionalRepository.findById(id)).thenReturn(Optional.empty())

        val result = service.getProfesionalById(id)
        assertNull(result)
    }

    @Test
    fun createProfesional() {
        val fechaNacimientoStr = dateFormatter.format(Date(1000))
        val fechaInicioStr     = dateFormatter.format(Date(2000))

        val request = ProfesionalRequest(
            nombre          = "New",
            foto            = "new.png",
            fechaNacimiento = fechaNacimientoStr,
            fechaInicio     = fechaInicioStr,
            lugarNacimiento = "Lugar",
            biografia       = "Bio"
        )

        val entityToSave = Profesional(
            id              = "",
            nombre          = "New",
            foto            = "new.png",
            fechaNacimiento = dateFormatter.parse(request.fechaNacimiento),
            fechaInicio     = dateFormatter.parse(request.fechaInicio),
            lugarNacimiento = "Lugar",
            biografia       = "Bio",
            participaciones = emptyList()
        )

        val savedEntity = entityToSave.copy(id = "prof2")

        val response = ProfesionalResponse(
            id                  = "prof2",
            nombre              = "New",
            foto                = "new.png",
            fechaNacimiento     = dateFormatter.parse(request.fechaNacimiento),
            fechaInicio         = dateFormatter.parse(request.fechaInicio),
            lugarNacimiento     = "Lugar",
            biografia           = "Bio",
            participacionesCount = 0
        )

        whenever(profesionalMapper.toEntity(request)).thenReturn(entityToSave)
        whenever(profesionalRepository.save(entityToSave)).thenReturn(savedEntity)
        whenever(profesionalMapper.toResponse(savedEntity)).thenReturn(response)

        val result = service.createProfesional(request)

        assertEquals(response, result)
        verify(profesionalMapper).toEntity(request)
        verify(profesionalRepository).save(entityToSave)
        verify(profesionalMapper).toResponse(savedEntity)
    }

    @Test
    fun updateProfesional() {
        val id = "prof3"

        val fechaNacimientoStr = dateFormatter.format(Date(3000))
        val fechaInicioStr     = dateFormatter.format(Date(4000))

        val request = ProfesionalRequest(
            nombre          = "Upd",
            foto            = "upd.png",
            fechaNacimiento = fechaNacimientoStr,
            fechaInicio     = fechaInicioStr,
            lugarNacimiento = "Lugar2",
            biografia       = "Bio2"
        )

        val existing = Profesional(
            id               = id,
            nombre           = "Old",
            foto             = "old.png",
            fechaNacimiento  = Date(500),
            fechaInicio      = Date(1500),
            lugarNacimiento  = "LugarOld",
            biografia        = "BioOld",
            participaciones  = emptyList()
        )

        val requestEntity = Profesional(
            id               = "",
            nombre           = request.nombre,
            foto             = request.foto,
            fechaNacimiento  = dateFormatter.parse(request.fechaNacimiento),
            fechaInicio      = dateFormatter.parse(request.fechaInicio),
            lugarNacimiento  = request.lugarNacimiento,
            biografia        = request.biografia,
            participaciones  = emptyList()
        )
        whenever(profesionalMapper.toEntity(request)).thenReturn(requestEntity)

        val updated = existing.copy(
            nombre          = requestEntity.nombre,
            foto            = requestEntity.foto,
            fechaNacimiento = requestEntity.fechaNacimiento,
            fechaInicio     = requestEntity.fechaInicio,
            lugarNacimiento = requestEntity.lugarNacimiento,
            biografia       = requestEntity.biografia
        )

        val response = ProfesionalResponse(
            id                  = id,
            nombre              = request.nombre,
            foto                = request.foto,
            fechaNacimiento     = requestEntity.fechaNacimiento,
            fechaInicio         = requestEntity.fechaInicio,
            lugarNacimiento     = request.lugarNacimiento,
            biografia           = request.biografia,
            participacionesCount = 0
        )

        whenever(profesionalRepository.findById(id)).thenReturn(Optional.of(existing))
        whenever(profesionalRepository.save(eq(updated))).thenReturn(updated)
        whenever(profesionalMapper.toResponse(updated)).thenReturn(response)

        val result = service.updateProfesional(id, request)

        assertEquals(response, result)
        verify(profesionalRepository).findById(id)
        verify(profesionalMapper).toEntity(request)
        verify(profesionalRepository).save(eq(updated))
        verify(profesionalMapper).toResponse(updated)
    }


    @Test
    fun updateProfesionalIdNull() {
        val id = "none"
        val request = ProfesionalRequest(
            nombre = "X",
            foto = "x.png",
            fechaNacimiento = Date().toString(),
            fechaInicio = Date().toString(),
            lugarNacimiento = "",
            biografia = ""
        )
        whenever(profesionalRepository.findById(id)).thenReturn(Optional.empty())

        val result = service.updateProfesional(id, request)
        assertNull(result)
    }

    @Test
    fun deleteProfesional() {
        val id = "prof4"

        service.deleteProfesional(id)

        verify(profesionalRepository).deleteById(id)
    }

    @Test
    fun filtrarProfesionalesWithPagination() {
        val page = 0
        val size = 3
        val pageRequest: Pageable = PageRequest.of(page, size)

        val entity = Profesional(
            id = "p5",
            nombre = "Filt",
            foto = null,
            fechaNacimiento = Date(),
            fechaInicio = Date(),
            lugarNacimiento = "",
            biografia = "",
            participaciones = emptyList()
        )

        val pageImpl = PageImpl(listOf(entity), pageRequest, 1)

        val response = ProfesionalResponse(
            id = "p5",
            nombre = "Filt",
            foto = null,
            fechaNacimiento = entity.fechaNacimiento,
            fechaInicio = entity.fechaInicio,
            lugarNacimiento = "",
            biografia = "",
            participacionesCount = 0
        )

        whenever(profesionalRepository.findAll()).thenReturn(listOf(entity))
        whenever(profesionalMapper.toResponse(entity)).thenReturn(response)

        val result = service.filtrarProfesionales(
            nombre = "Filt",
            fechaNacimientoDesde = null,
            fechaNacimientoHasta = null,
            fechaInicioDesde = null,
            fechaInicioHasta = null,
            lugarNacimiento = null,
            page = page,
            size = size,
            sortBy = listOf(),
            sortDirection = ""
        )

        assertEquals(1, result.totalItems)
        assertEquals(response, result.data[0])
    }

    @Test
    fun getAllProfesionales() {
        val page = 1; val size = 10
        val pageable = PaginationUtils.createPageable(
            page = page,
            size = size,
            sortBy = emptyList(),
            sortDirection = "",
            allowedSortFields = setOf("id", "nombre", "fechaNacimiento", "fechaInicio")
        )
        val entity = Profesional(
            id = "a",
            nombre = "A",
            foto = null,
            fechaNacimiento = Date(),
            fechaInicio = Date(),
            lugarNacimiento = "",
            biografia = "",
            participaciones = emptyList()
        )
        val pageImpl = PageImpl(listOf(entity), pageable, 5)
        val response = ProfesionalResponse(
            id = "a",
            nombre = "A",
            foto = null,
            fechaNacimiento = entity.fechaNacimiento,
            fechaInicio = entity.fechaInicio,
            lugarNacimiento = "",
            biografia = "",
            participacionesCount = 0
        )

        whenever(profesionalRepository.findAll(eq(pageable))).thenReturn(pageImpl)
        whenever(profesionalMapper.toPaginatedResponse(pageImpl)).thenReturn(
        PaginationUtils.PaginatedResponse(
            data = listOf(response),
            totalItems = 5,
            totalPages = 1,
            currentPage = page,
            pageSize = size
        )
        )

        val result = service.getAllProfesionales(page, size, emptyList(), "")
        assertEquals(5, result.totalItems)
        assertEquals(response, result.data[0])
    }

    @Test
    fun actualizarImagen() {
        val id = "prof6"
        val nuevaUrl = "https://nueva.imagen.com/foto.jpg"
        val profesional = Profesional(
            id = id,
            nombre = "Nombre",
            foto = null,
            fechaNacimiento = Date(),
            fechaInicio = Date(),
            lugarNacimiento = "Lugar",
            biografia = "Bio",
            participaciones = emptyList()
        )
        val actualizado = profesional.copy(foto = nuevaUrl)
            val response = ProfesionalResponse(
            id = id,
            nombre = "Nombre",
            foto = nuevaUrl,
            fechaNacimiento = profesional.fechaNacimiento,
            fechaInicio = profesional.fechaInicio,
            lugarNacimiento = profesional.lugarNacimiento,
            biografia = profesional.biografia,
            participacionesCount = 0
        )

        whenever(profesionalRepository.findById(id)).thenReturn(Optional.of(profesional))
        whenever(profesionalRepository.save(profesional)).thenReturn(actualizado)
        whenever(profesionalMapper.toResponse(actualizado)).thenReturn(response)

        val result = service.actualizarImagen(id, nuevaUrl)

        assertEquals(response, result)
        verify(profesionalRepository).save(profesional)
    }

    @Test
    fun searchByNombre() {
        val page = 0
        val size = 2
        val pageable = PaginationUtils.createPageable(
            page = page,
            size = size,
            sortBy = emptyList(),
            sortDirection = "",
            allowedSortFields = setOf("id", "nombre", "fechaNacimiento", "fechaInicio")
        )
        val nombre = "Pro"
        val entity = Profesional(
            id = "p7",
            nombre = "Profe",
            foto = null,
            fechaNacimiento = Date(),
            fechaInicio = Date(),
            lugarNacimiento = "Lugar",
            biografia = "Bio",
            participaciones = emptyList()
        )
        val pageImpl = PageImpl(listOf(entity), pageable, 1)
        val response = ProfesionalResponse(
            id = "p7",
            nombre = "Profe",
            foto = null,
            fechaNacimiento = entity.fechaNacimiento,
            fechaInicio = entity.fechaInicio,
            lugarNacimiento = "Lugar",
            biografia = "Bio",
            participacionesCount = 0
        )

        whenever(profesionalRepository.findByNombreContainingIgnoreCase(nombre, pageable)).thenReturn(pageImpl)
        whenever(profesionalMapper.toPaginatedResponse(pageImpl)).thenReturn(
            PaginationUtils.PaginatedResponse(
                data = listOf(response),
                totalItems = 1,
                totalPages = 1,
                currentPage = 0,
                pageSize = 2
            )
        )

        val result = service.searchByNombre(nombre, page, size, emptyList(), "")

        assertEquals(1, result.totalItems)
        assertEquals(response, result.data[0])
    }

    @Test
    fun findEntityById() {
        val id = "prof8"
        val entity = Profesional(
            id = id,
            nombre = "Nombre",
            foto = null,
            fechaNacimiento = Date(),
            fechaInicio = Date(),
            lugarNacimiento = "Lugar",
            biografia = "Bio",
            participaciones = emptyList()
        )

        whenever(profesionalRepository.findById(id)).thenReturn(Optional.of(entity))

        val result = service.findEntityById(id)
        assertEquals(entity, result)
    }

    @Test
    fun findEntityByIdNotFound() {
        val id = "notfound"
        whenever(profesionalRepository.findById(id)).thenReturn(Optional.empty())

        val result = service.findEntityById(id)
        assertNull(result)
    }

    @Test
    fun filtrarProfesionales() {
        val exception = assertThrows(IllegalArgumentException::class.java) {
        service.filtrarProfesionales(
            nombre = null,
            fechaNacimientoDesde = "invalid-date",
            fechaNacimientoHasta = null,
            fechaInicioDesde = null,
            fechaInicioHasta = null,
            lugarNacimiento = null,
            page = 0,
            size = 10,
            sortBy = listOf(),
            sortDirection = ""
        )
        }

        assertTrue(exception.message!!.contains("Formato de fecha inválido"))
    }

    @Test
    fun filtrarProfesionalesPorTodosLosParametros() {
        val fmt = SimpleDateFormat("yyyy-MM-dd")
        val profA = Profesional(
            id = "pA",
            nombre = "Alfa",
            foto = null,
            fechaNacimiento = fmt.parse("1990-05-10"),
            fechaInicio = fmt.parse("2010-01-15"),
            lugarNacimiento = "CiudadX",
            biografia = "",
            participaciones = emptyList()
        )
        val profB = Profesional(
            id = "pB",
            nombre = "Beta",
            foto = null,
            // fechaNacimiento fuera del rango
            fechaNacimiento = fmt.parse("1980-03-20"),
            fechaInicio = fmt.parse("2015-06-01"),
            lugarNacimiento = "CiudadY",
            biografia = "",
            participaciones = emptyList()
        )
        val profC = Profesional(
            id = "pC",
            nombre = "Gamma",
            foto = null,
            fechaNacimiento = fmt.parse("1995-11-30"),
            // fechaInicio fuera del rango
            fechaInicio = fmt.parse("2005-12-31"),
            lugarNacimiento = "CiudadX",
            biografia = "",
            participaciones = emptyList()
        )
        val profD = Profesional(
            id = "pD",
            nombre = "Delta",
            foto = null,
            fechaNacimiento = fmt.parse("1992-07-25"),
            fechaInicio = fmt.parse("2012-08-20"),
            // lugarNacimiento no coincide
            lugarNacimiento = "OtraCiudad",
            biografia = "",
            participaciones = emptyList()
        )
        // Único que debería pasar todos los filtros
        val profE = Profesional(
            id = "pE",
            nombre = "Omega",
            foto = null,
            fechaNacimiento = fmt.parse("1991-09-05"),
            fechaInicio = fmt.parse("2011-03-10"),
            lugarNacimiento = "CiudadX",
            biografia = "",
            participaciones = emptyList()
        )

        whenever(profesionalRepository.findAll()).thenReturn(listOf(profA, profB, profC, profD, profE))
        // Mapeamos cada entidad filtrada a su respuesta
        val respE = ProfesionalResponse(
            id = "pE",
            nombre = "Omega",
            foto = null,
            fechaNacimiento = profE.fechaNacimiento,
            fechaInicio = profE.fechaInicio,
            lugarNacimiento = profE.lugarNacimiento,
            biografia = profE.biografia,
            participacionesCount = 0
        )
        whenever(profesionalMapper.toResponse(profE)).thenReturn(respE)

        // Ejecutamos con todos los parámetros
        val resultado = service.filtrarProfesionales(
            nombre = "meg",                      // coincide con "Omega"
            fechaNacimientoDesde = "1990-01-01", // rango que incluye 1991-09-05
            fechaNacimientoHasta = "1993-12-31",
            fechaInicioDesde = "2010-01-01",     // rango que incluye 2011-03-10
            fechaInicioHasta = "2012-12-31",
            lugarNacimiento = "ciudadx",         // case-insensitive match con "CiudadX"
            page = 0,
            size = 10,
            sortBy = listOf("nombre"),
            sortDirection = "asc"
        )

        // Solo profE debe permanecer tras filtrar
        assertEquals(1, resultado.totalItems)
        assertEquals(1, resultado.data.size)
        assertEquals(respE, resultado.data.first())

        verify(profesionalRepository).findAll()
        verify(profesionalMapper).toResponse(profE)
    }

}