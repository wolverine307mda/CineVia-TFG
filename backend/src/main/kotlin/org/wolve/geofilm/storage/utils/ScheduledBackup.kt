package org.wolve.geofilm.storage.utils

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.wolve.geofilm.storage.service.BackupService

@Component
class ScheduledBackup(private val backupService: BackupService) {

    @Scheduled(cron = "0 0 2 * * *") // Cada día a las 2:00 AM
    fun dailyBackup() {
        try {
            backupService.createBackup()
        } catch (e: Exception) {
            println("Fallo al crear backup programado: ${'$'}{e.message}")
        }
    }
}