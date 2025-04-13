package org.wolve.geofilm.users.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.wolve.geofilm.users.models.Usuario
import org.wolve.geofilm.users.services.UsuarioService
import java.util.*

@RestController
@RequestMapping("/api/usuarios")
class UsuarioController(private val usuarioService: UsuarioService) {

}
