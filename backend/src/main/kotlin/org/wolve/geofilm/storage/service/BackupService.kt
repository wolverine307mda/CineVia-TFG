package org.wolve.geofilm.storage.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@Service
class BackupService(
    @Value("\${spring.datasource.username}") private val dbUser: String,
    @Value("\${spring.datasource.password}") private val dbPass: String,
    @Value("\${spring.datasource.url}") private val dbUrl: String
) {

    private val dataDir = File("data")
    private val dbName = dbUrl.substringAfterLast("/").substringBefore("?")

    fun createBackup(): String {
        if (!dataDir.exists()) dataDir.mkdirs()
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val backupFile = File(dataDir, "backup_$timestamp.sql")
        val cmd = listOf("pg_dump", "-U", dbUser, "-d", dbName, "-a", "-f", backupFile.absolutePath)

        val pb = ProcessBuilder(cmd).inheritIO().apply {
            environment()["PGPASSWORD"] = dbPass
        }

        val process = pb.start()
        val exitCode = process.waitFor()
        if (exitCode != 0) throw RuntimeException("Fallo al crear backup")
        return backupFile.name
    }

    fun listBackups(): List<String> {
        return dataDir.listFiles()?.filter { it.extension == "sql" }?.map { it.name }?.sortedDescending() ?: emptyList()
    }

    fun restoreBackup(fileName: String) {
        val file = File(dataDir, fileName)
        if (!file.exists()) throw RuntimeException("El backup no existe")

        val clearCmd = listOf("psql", "-U", dbUser, "-d", dbName, "-c", "TRUNCATE TABLE producciones, participaciones, rodajes, ubicaciones, sagas, profesionales CASCADE")
        val restoreCmd = listOf("psql", "-U", dbUser, "-d", dbName, "-a", "-f", file.absolutePath)

        val pbClear = ProcessBuilder(clearCmd).inheritIO().apply {
            environment()["PGPASSWORD"] = dbPass
        }
        val pbRestore = ProcessBuilder(restoreCmd).inheritIO().apply {
            environment()["PGPASSWORD"] = dbPass
        }

        if (pbClear.start().waitFor() != 0) throw RuntimeException("Fallo al limpiar la base de datos")
        if (pbRestore.start().waitFor() != 0) throw RuntimeException("Fallo al restaurar backup")
    }
}