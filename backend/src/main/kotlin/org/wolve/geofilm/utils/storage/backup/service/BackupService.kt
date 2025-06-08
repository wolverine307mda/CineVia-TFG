package org.wolve.geofilm.utils.storage.backup.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date

@Service
class BackupService {

    private val log = LoggerFactory.getLogger(javaClass)

    private val backupDir = File("data/backups")

    private val dbHost = "postgres"
    private val dbPort = "5432"
    private val dbUser = "wolverine307"
    private val dbName = "geofilm"
    private val dbPassword = "admin307204"

    init {
        if (!backupDir.exists()) backupDir.mkdirs()
    }

    /**
     * Genera un volcado completo (DDL + DML) con DROP previo de cada objeto,
     * y lo nombra usando formato dd-MM-yyyy_HH-mm-ss para que sea legible en el front.
     */
    fun exportBackup(): File {
        // Formato dd-MM-yyyy_HH-mm-ss -> ej. "08-06-2025_23-28-45"
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss")
        val timestamp = LocalDateTime.now().plusHours(2).format(formatter)
        val filename = "backup_$timestamp.sql"
        val file = File(backupDir, filename)

        val command = listOf(
            "/usr/bin/pg_dump",
            "-h", dbHost,
            "-p", dbPort,
            "-U", dbUser,
            "-d", dbName,
            "--clean",          // DROP previo de cada tabla, función, etc.
            "--no-owner",
            "--no-privileges"
        )

        log.debug("Ejecutando export: {}", command.joinToString(" "))
        val proc = ProcessBuilder(command)
            .redirectOutput(file)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .apply { environment()["PGPASSWORD"] = dbPassword }
            .start()

        val exit = proc.waitFor()
        if (exit != 0) {
            val err = proc.errorStream.bufferedReader().readText().trim()
            log.error("pg_dump falló: {}", err)
            throw RuntimeException("Error al exportar backup: $err")
        }

        log.info("Backup exportado a {}", file.absolutePath)
        return file
    }

    /**
     * Importa un fichero SQL completo (generado por exportBackup),
     * aplicando todos los DROP, CREATE e INSERT que contenga.
     */
    fun importBackup(file: File): String {
        if (!file.exists()) {
            throw IllegalArgumentException("No existe el archivo de backup: ${file.name}")
        }

        val command = listOf(
            "/usr/bin/psql",
            "-h", dbHost,
            "-p", dbPort,
            "-U", dbUser,
            "-d", dbName,
            "-f", file.absolutePath
        )

        log.debug("Ejecutando import: {}", command.joinToString(" "))
        val proc = ProcessBuilder(command)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .apply { environment()["PGPASSWORD"] = dbPassword }
            .start()

        val exit = proc.waitFor()
        if (exit != 0) {
            val err = proc.errorStream.bufferedReader().readText().trim()
            log.error("psql -f falló: {}", err)
            throw RuntimeException("Error al importar backup: $err")
        }

        log.info("Backup importado desde {}", file.absolutePath)
        return "Backup importado correctamente desde ${file.name}"
    }

    fun listBackups(): List<File> =
        backupDir.listFiles()?.sortedByDescending { it.lastModified() } ?: emptyList()

    fun getBackupFile(name: String): File {
        val file = File(backupDir, name)
        if (!file.exists()) throw IllegalArgumentException("No existe el archivo: $name")
        return file
    }

    fun saveUploadedFile(filename: String, bytes: ByteArray): File {
        val dest = File(backupDir, filename)
        dest.writeBytes(bytes)
        return dest
    }
}
