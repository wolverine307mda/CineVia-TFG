package org.wolve.geofilm.storage.controller

import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.wolve.geofilm.storage.service.BackupService

@RestController
@RequestMapping("/api/admin/backup")
class BackupController(private val backupService: BackupService) {

    @GetMapping("/export")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    fun exportBackup(): ResponseEntity<String> {
        val filename = backupService.createBackup()
        return ResponseEntity.ok("Backup guardado como: $filename")
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    fun list(): ResponseEntity<List<String>> {
        return ResponseEntity.ok(backupService.listBackups())
    }

    @PostMapping("/restore/{fileName}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    fun restore(@PathVariable fileName: String): ResponseEntity<String> {
        backupService.restoreBackup(fileName)
        return ResponseEntity.ok("Restaurado desde $fileName")
    }
}