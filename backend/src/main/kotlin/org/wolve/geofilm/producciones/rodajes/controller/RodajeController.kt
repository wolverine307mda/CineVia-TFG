package org.wolve.geofilm.producciones.rodajes.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.wolve.geofilm.producciones.rodajes.dto.request.RodajeRequest
import org.wolve.geofilm.producciones.rodajes.dto.response.RodajeResponse
import org.wolve.geofilm.producciones.rodajes.service.IRodajeService
import org.wolve.geofilm.utils.storage.images.FirebaseStorageService
import java.util.UUID.randomUUID

@RestController
@RequestMapping("/api/rodajes")
class RodajeController(
    private val rodajeService: IRodajeService,
    private val firebaseStorageService: FirebaseStorageService
) {

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): ResponseEntity<RodajeResponse> {
        return ResponseEntity.ok(rodajeService.findById(id))
    }

    @PostMapping
    fun create(@RequestBody request: RodajeRequest): ResponseEntity<RodajeResponse> {
        val rodaje = rodajeService.create(request)
        return ResponseEntity.ok(rodaje)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: String, @RequestBody request: RodajeRequest): ResponseEntity<RodajeResponse> {
        val updated = rodajeService.update(id, request)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: String): ResponseEntity<Void> {
        rodajeService.delete(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/produccion/{produccionId}")
    fun getByProduccion(
        @PathVariable produccionId: String,
        @RequestParam page: Int,
        @RequestParam size: Int
    ): ResponseEntity<Map<String, Any>> {
        val paged = rodajeService.findByProduccion(produccionId, page, size)
        return ResponseEntity.ok(paged)
    }

    @PostMapping("/upload-image-temp")
    fun uploadTempRodajeImage(
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<String> {
        val filename = "rodaje_${randomUUID()}.jpg"
        val imageUrl = firebaseStorageService.uploadImage(file, "rodajes", filename)
        return ResponseEntity.ok(imageUrl)
    }

}