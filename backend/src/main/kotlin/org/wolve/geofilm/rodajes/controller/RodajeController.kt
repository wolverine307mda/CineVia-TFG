package org.wolve.geofilm.rodajes.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.wolve.geofilm.rodajes.dto.response.RodajeResponse
import org.wolve.geofilm.rodajes.service.RodajeService

@RestController
@RequestMapping("/api/rodajes")
class RodajeController(private val rodajeService: RodajeService) {

    @GetMapping("/{id}")
    fun getById(@PathVariable id: String): ResponseEntity<RodajeResponse> {
        return ResponseEntity.ok(rodajeService.findById(id))
    }
}